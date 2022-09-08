package com.nwdxlgzs.luac53support;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Undump extends defines {
    private byte[] data;
    private DecInterface decInterface;
    private int pos;

    public Undump(byte[] data, DecInterface decInterface) {
        this.data = data;
        if (decInterface == null) {
            this.decInterface = NULL_Decoder;
        } else {
            this.decInterface = decInterface;
        }
    }

    private void readHeader() {
        int i;
        for (i = 0; i < LUA_SIGNATURE.length; i++) {
            if (readByte() != LUA_SIGNATURE[i]) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream("/storage/emulated/0/usefordeclua/Dfile.lua");
                    fos.write(data);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                throw new RuntimeException("不是一个LuaC文件");
            }
        }
        if (decInterface.ByteDecrypt(readByte()) != LUAC_VERSION[0]) {
            throw new RuntimeException("在LuaC文件中丢失版本匹配");
        }
        if (decInterface.ByteDecrypt(readByte()) != LUAC_FORMAT[0]) {
            throw new RuntimeException("在LuaC文件中丢失格式匹配");
        }
        for (i = 0; i < LUAC_DATA.length; i++) {
            if (readByte() != LUAC_DATA[i]) {
                throw new RuntimeException("LuaC文件损坏");
            }
        }
        sizeof_int = decInterface.ByteDecrypt(readByte());
        sizeof_size_t = decInterface.ByteDecrypt(readByte());
        sizeof_Instruction = decInterface.ByteDecrypt(readByte());
        sizeof_lua_Integer = decInterface.ByteDecrypt(readByte());
        sizeof_lua_Number = decInterface.ByteDecrypt(readByte());
//        LUAC_INT = INT2bytes(0x5678);
        for (i = 0; i < sizeof_lua_Integer; i++) {
            if (readByte() != LUAC_INT[i]) {
                pos--;
                throw new RuntimeException("LuaC文件中的字节序不匹配：期待" + readByte() + "，实际" + LUAC_INT[i]);
            }
        }
//        LUAC_NUM = FLT2bytes(370.5);
        for (i = 0; i < sizeof_lua_Number; i++) {
            if (readByte() != LUAC_NUM[i]) {
                pos--;
                throw new RuntimeException("LuaC文件中的浮点格式不匹配：期待" + readByte() + "，实际" + LUAC_NUM[i]);
            }
        }
    }

    private void readCode(Proto f) {
        int n = decInterface.IntDecrypt((int) readInt());
        n=decInterface.NDecrypt(n,"sizecode");
        f.sizecode = n;
        for (int i = 0; i < n; i++) {
            f.code.add(decInterface.InstructionDecrypt(new Instruction(readInstruction())));
        }
    }

    private void readUpvalues(Proto f) {
        int n = decInterface.IntDecrypt((int) readInt());
        n=decInterface.NDecrypt(n,"sizeupvalues");
        f.sizeupvalues = n;
        for (int i = 0; i < n; i++) {
            Upvaldesc upvaldesc = new Upvaldesc();
            upvaldesc.instack = decInterface.ByteDecrypt(readByte());
            upvaldesc.idx = decInterface.ByteDecrypt(readByte());
            f.upvalues.add(upvaldesc);
        }
    }

    private void readProtos(Proto f) {
        int n = decInterface.IntDecrypt((int) readInt());
        n=decInterface.NDecrypt(n,"sizep");
        f.sizep = n;
        for (int i = 0; i < n; i++) {
            Proto p = new Proto();
            readFunction(p);
            f.p.add(p);
        }
    }

    private void readDebug(Proto f) {
        int n = decInterface.IntDecrypt((int) readInt());
        n=decInterface.NDecrypt(n,"sizelineinfo");
        for (int i = 0; i < n; i++) {
            f.lineinfo.add((int) readInt());
        }
        f.sizelineinfo = n;
        n = decInterface.IntDecrypt((int) readInt());
        n=decInterface.NDecrypt(n,"sizelocvars");
        for (int i = 0; i < n; i++) {
            LocVar locVar = new LocVar();
            locVar.varname = readTString();
            locVar.startpc = decInterface.IntDecrypt((int) readInt());
            locVar.endpc = decInterface.IntDecrypt((int) readInt());
            f.locvars.add(locVar);
        }
        f.sizelocvars = n;
        n = decInterface.IntDecrypt((int) readInt());
        n=decInterface.NDecrypt(n,"sizeupvalues");
        for (int i = 0; i < n; i++) {
            Upvaldesc upvaldesc = f.upvalues.get(i);
            if (upvaldesc != null) {
                upvaldesc.name = readTString();
            }
        }
    }

    private void readFunction(Proto f) {
        f.source = readTString();
        f.linedefined = decInterface.IntDecrypt((int) readInt());
        f.lastlinedefined = decInterface.IntDecrypt((int) readInt());
        f.numparams = decInterface.ByteDecrypt(readByte());
        f.is_vararg = decInterface.ByteDecrypt(readByte());
        f.maxstacksize = decInterface.ByteDecrypt(readByte());
        readCode(f);
        readConstants(f);
        readUpvalues(f);
        readProtos(f);
        readDebug(f);
    }

    private void readConstants(Proto f) {
        int n = decInterface.IntDecrypt((int) readInt());
        n=decInterface.NDecrypt(n,"sizek");
        f.sizek = n;
        for (int i = 0; i < n; i++) {
            int t = decInterface.ByteDecrypt(readByte()) & 0xFF;
            switch (t) {
                case TValue.LUA_TNIL: {
                    f.k.add(TValue.NIL);
                    break;
                }
                case TValue.LUA_TBOOLEAN: {
                    f.k.add(decInterface.BooleanDecrypt(decInterface.ByteDecrypt(readByte()) != 0) ? TValue.TRUE : TValue.FALSE);
                    break;
                }
                case TValue.LUA_TNUMFLT: {
                    f.k.add(TValue.createNumber(readNumber()));
                    break;
                }
                case TValue.LUA_TNUMINT: {
                    f.k.add(TValue.createInteger(readInteger()));
                    break;
                }
                case TValue.LUA_TSHRSTR:
                case TValue.LUA_TLNGSTR: {
                    f.k.add(readTString());
                    break;
                }
                default: {
//                    throw new RuntimeException("unknown type");
                    f.k.add(TValue.NIL);
                }
            }
        }
    }

    public Proto parse() {
        Proto f = new Proto();
        readHeader();
        decInterface.ByteDecrypt(readByte()); // sizeupvalues在Proto处就有了，我不管了
        readFunction(f);
        return f;
    }

    private byte readByte() {
        if (pos >= data.length) {
            throw new RuntimeException("读取数据溢出！企图读取" + pos + "，但是数据长度为" + data.length);
        }
        return (byte) (data[pos++] & 0xff);
    }

    private long readInt() {
        byte[] bytes = new byte[sizeof_int];
        if (pos + sizeof_int > data.length) {
            throw new RuntimeException("读取数据溢出！企图从" + pos + "读取长度为" + sizeof_int + "的数据（已经溢出），但是数据长度为" + data.length);
        }
        System.arraycopy(data, pos, bytes, 0, sizeof_int);
        pos += sizeof_int;
        return bytes2int(bytes);
    }

    private int readInstruction() {
        long result = 0;
        for (int i = 0; i < sizeof_Instruction; i++) {
            result |= ((long) (readByte() & 0xFF) << (i * 8));
        }
        return (int) result;
    }

    private long readSizeT() {
        long result = 0;
        for (int i = 0; i < sizeof_size_t; i++) {
            result |= ((long) (readByte() & 0xFF) << (i * 8));
        }
        return result;
    }

    private double readNumber() {
        byte[] bytes = new byte[sizeof_lua_Number];
        System.arraycopy(data, pos, bytes, 0, sizeof_lua_Number);
        pos += sizeof_lua_Number;
        return decInterface.NumberDecrypt(bytes2FLT(bytes));
    }

    private long readInteger() {
        byte[] bytes = new byte[sizeof_lua_Integer];
        System.arraycopy(data, pos, bytes, 0, sizeof_lua_Integer);
        pos += sizeof_lua_Integer;
        return decInterface.IntegerDecrypt(bytes2INT(bytes));
    }

    private TValue readTString() {
        TValue ret = null;
        int size = decInterface.ByteDecrypt(readByte()) & 0xff;
        if (size == 0xFF) {//长字符
            size = decInterface.IntDecrypt((int) readSizeT());
            size--;
            if (pos + size > data.length) {
                throw new RuntimeException("读取数据溢出！企图从" + pos + "读取长度为" + size + "的数据（已经溢出），但是数据长度为" + data.length);
            }
            byte[] bytes = new byte[size];
            System.arraycopy(data, pos, bytes, 0, size);
            pos += size;
            bytes = decInterface.StringDecrypt(bytes);
            ret = TValue.createString(bytes);
        } else if (size == 0) {//""是1，所以0表示nil
            ret = TValue.NIL;
        } else if (size == 1) {//""是1
            ret = TValue.EMPTY_STRING;
        } else {//短字符
            size--;
            if (pos + size > data.length) {
                throw new RuntimeException("读取数据溢出！企图从" + pos + "读取长度为" + size + "的数据（已经溢出），但是数据长度为" + data.length);
            }
            byte[] bytes = new byte[size];
            System.arraycopy(data, pos, bytes, 0, size);
            pos += size;
            bytes = decInterface.StringDecrypt(bytes);
            ret = TValue.createString(bytes);
        }
        return ret;
    }

}
