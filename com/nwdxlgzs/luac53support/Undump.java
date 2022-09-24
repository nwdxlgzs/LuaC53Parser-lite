package com.nwdxlgzs.luac53support;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

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

    private byte[] readBlock(int size) {
        if (pos + size > data.length) {
            if (pos + size > data.length + decInterface.VirtualMemorySize()) {//VirtualMemorySize虚拟扩容，包装读取正常（C的Lua会内存溢出去读，通常在malloc块没有报错）
                throw new RuntimeException("读取数据溢出！企图从" + pos + "读取长度为" + size +
                        "的数据（已经溢出），但是实际数据长度为" + data.length + "，虚拟扩容为" + decInterface.VirtualMemorySize());
            } else {
                byte[] block = new byte[size];
                System.arraycopy(data, pos, block, 0, data.length - pos);
                pos = data.length;
                return block;
            }
        } else {
            byte[] block = new byte[size];
            System.arraycopy(data, pos, block, 0, size);
            pos += size;
            block = decInterface.BlockDecrypt(block);
            return block;
        }
    }

    private void readHeader() {
        byte[] buff_temp;
        buff_temp = readBlock(LUA_SIGNATURE.length);
        int i;
        for (i = 0; i < LUA_SIGNATURE.length; i++) {
            if (buff_temp[i] != LUA_SIGNATURE[i]) {
                throw new RuntimeException("不是一个LuaC文件！LuaC头部错误！头部应为" + Collections.singletonList(LUA_SIGNATURE) + "，但是读取到的头部为" + Collections.singletonList(buff_temp));
            }
        }
        if (decInterface.ByteDecrypt(readByte()) != LUAC_VERSION[0]) {
            throw new RuntimeException("在LuaC文件中丢失版本匹配");
        }
        if (decInterface.ByteDecrypt(readByte()) != LUAC_FORMAT[0]) {
            throw new RuntimeException("在LuaC文件中丢失格式匹配");
        }
        buff_temp = readBlock(LUAC_DATA.length);
        for (i = 0; i < LUAC_DATA.length; i++) {
            if (buff_temp[i] != LUAC_DATA[i]) {
                throw new RuntimeException("不是一个LuaC文件！LuaC数据错误！数据应为" + Collections.singletonList(LUAC_DATA) + "，但是读取到的数据为" + Collections.singletonList(buff_temp));
            }
        }
        sizeof_int = decInterface.ByteDecrypt(readByte());
        sizeof_size_t = decInterface.ByteDecrypt(readByte());
        sizeof_Instruction = decInterface.ByteDecrypt(readByte());
        sizeof_lua_Integer = decInterface.ByteDecrypt(readByte());
        sizeof_lua_Number = decInterface.ByteDecrypt(readByte());
        buff_temp = readBlock(sizeof_lua_Integer);
        for (i = 0; i < sizeof_lua_Integer; i++) {
            if (buff_temp[i] != LUAC_INT[i]) {
                throw new RuntimeException("不是一个LuaC文件！LuaC整数错误！整数数据应为" + Collections.singletonList(LUAC_INT) + "，但是读取到的整数数据为" + Collections.singletonList(buff_temp));
            }
        }
        buff_temp = readBlock(sizeof_lua_Number);
        for (i = 0; i < sizeof_lua_Number; i++) {
            if (buff_temp[i] != LUAC_NUM[i]) {
                throw new RuntimeException("不是一个LuaC文件！LuaC数字错误！浮点数据应为" + Collections.singletonList(LUAC_NUM) + "，但是读取到的浮点数据为" + Collections.singletonList(buff_temp));
            }
        }
    }

    private void readCode(Proto f) {
        HashMap<Integer, Integer> replace_op = decInterface.OPMapReplace();
        int n = decInterface.IntDecrypt((int) readInt());
        n = decInterface.NDecrypt(n, "sizecode");
        f.sizecode = n;
        for (int i = 0; i < n; i++) {
            Instruction I = decInterface.InstructionDecrypt(new Instruction(readInstruction()));
            if (replace_op != null) {
                Integer newopcode = replace_op.get(I.opcode);
                if (newopcode != null) {
                    I.opcode = newopcode;
                    I.value = SET_OPCODE(I.value, newopcode);
                }
            }
            f.code.add(I);
        }
    }

    private void readUpvalues(Proto f) {
        int n = decInterface.IntDecrypt((int) readInt());
        n = decInterface.NDecrypt(n, "sizeupvalues");
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
        n = decInterface.NDecrypt(n, "sizep");
        f.sizep = n;
        for (int i = 0; i < n; i++) {
            Proto p = new Proto();
            readFunction(p);
            f.p.add(p);
        }
    }

    private void readDebug(Proto f) {
        int n = decInterface.IntDecrypt((int) readInt());
        n = decInterface.NDecrypt(n, "sizelineinfo");
        for (int i = 0; i < n; i++) {
            f.lineinfo.add((int) readInt());
        }
        f.sizelineinfo = n;
        n = decInterface.IntDecrypt((int) readInt());
        n = decInterface.NDecrypt(n, "sizelocvars");
        for (int i = 0; i < n; i++) {
            LocVar locVar = new LocVar();
            locVar.varname = readTString();
            locVar.startpc = decInterface.IntDecrypt((int) readInt());
            locVar.endpc = decInterface.IntDecrypt((int) readInt());
            f.locvars.add(locVar);
        }
        f.sizelocvars = n;
        n = decInterface.IntDecrypt((int) readInt());
        n = decInterface.NDecrypt(n, "sizeupvalues");
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
        n = decInterface.NDecrypt(n, "sizek");
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
        return readBlock(1)[0];
    }

    private long readInt() {
        return bytes2int(readBlock(sizeof_int));
    }

    private int readInstruction() {
        byte[] bytes = readBlock(sizeof_Instruction);
        long result = 0;
        for (int i = 0; i < sizeof_Instruction; i++) {
//            result |= ((long) (readByte() & 0xFF) << (i * 8));
            result |= ((long) (bytes[i] & 0xFF) << (i * 8));
        }
        return (int) result;
    }

    private long readSizeT() {
        byte[] bytes = readBlock(sizeof_size_t);
        long result = 0;
        for (int i = 0; i < sizeof_size_t; i++) {
//            result |= ((long) (readByte() & 0xFF) << (i * 8));
            result |= ((long) (bytes[i] & 0xFF) << (i * 8));
        }
        return result;
    }

    private double readNumber() {
        return decInterface.NumberDecrypt(bytes2FLT(readBlock(sizeof_lua_Number)));
    }

    private long readInteger() {
        return decInterface.IntegerDecrypt(bytes2INT(readBlock(sizeof_lua_Integer)));
    }

    private TValue readTString() {
        TValue ret = null;
        int size = decInterface.ByteDecrypt(readByte()) & 0xff;
        if (size == 0xFF) {//长字符
            size = decInterface.IntDecrypt((int) readSizeT());
            size--;
            byte[] bytes = readBlock(size);
            bytes = decInterface.StringDecrypt(bytes);
            ret = TValue.createString(bytes);
        } else if (size == 0) {//""是1，所以0表示nil
            ret = TValue.NIL;
        } else if (size == 1) {//""是1
            ret = TValue.EMPTY_STRING;
        } else {//短字符
            size--;
            byte[] bytes = readBlock(size);
            bytes = decInterface.StringDecrypt(bytes);
            ret = TValue.createString(bytes);
        }
        return ret;
    }

}
