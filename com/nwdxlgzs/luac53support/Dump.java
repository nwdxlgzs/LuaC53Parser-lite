package com.nwdxlgzs.luac53support;

import static com.nwdxlgzs.luac53support.TValue.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Dump extends defines {
    private Proto proto;
    private ByteArrayOutputStream stream;
    private DecInterface decInterface;

    public Dump(Proto proto, DecInterface decInterface) {
        this.proto = proto;
        if (decInterface == null) {
            this.decInterface = NULL_Decoder;
        } else {
            this.decInterface = decInterface;
        }
    }

    private void writeHeader() throws IOException {
        writeLiteral(LUA_SIGNATURE);
        writeByte(LUAC_VERSION[0]);
        writeByte(LUAC_FORMAT[0]);
        writeLiteral(LUAC_DATA);
        writeByte((byte) sizeof_int);
        writeByte((byte) sizeof_size_t);
        writeByte((byte) sizeof_Instruction);
        writeByte((byte) sizeof_lua_Integer);
        writeByte((byte) sizeof_lua_Number);
        writeLiteral(LUAC_INT);// 0x5678
        writeLiteral(LUAC_NUM);// 370.5
    }

    private void writeCode(Proto f) throws IOException {
        writeInt(f.sizecode);
        for (int i = 0; i < f.sizecode; i++) {
            writeInstruction(f.code.get(i));
        }
    }

    private void writeConstants(Proto f) throws IOException {
        writeInt(f.sizek);
        for (int i = 0; i < f.sizek; i++) {
            TValue o = f.k.get(i);
            writeByte((byte) o.tt_);
            switch (f.k.get(i).tt_) {
                case LUA_TNIL:
                    break;
                case LUA_TBOOLEAN:
                    writeByte((byte) (o.getBoolean() ? 1 : 0));
                    break;
                case LUA_TNUMFLT:
                    writeNumber(o.getFLT());
                    break;
                case LUA_TNUMINT:
                    writeInteger(o.getINT());
                    break;
                case LUA_TSHRSTR:
                case LUA_TLNGSTR:
                    writeString(o);
                    break;
                default:
//                    throw new RuntimeException("无效的常量类型");
                    break;
            }
        }
    }

    private void writeProtos(Proto f) throws IOException {
        writeInt(f.sizep);
        for (int i = 0; i < f.sizep; i++) {
            writeFunction(f.p.get(i));
        }
    }

    private void writeUpvalues(Proto f) throws IOException {
        writeInt(f.sizeupvalues);
        for (int i = 0; i < f.sizeupvalues; i++) {
            Upvaldesc uv = f.upvalues.get(i);
            writeByte(uv.instack);
            writeByte(uv.idx);
        }
    }

    private void writeDebug(Proto f) throws IOException {
        if (decInterface.requireStrip() || f.sizelineinfo == 0 || f.sizelocvars == 0) {
            writeInt(0);
            writeInt(0);
            writeInt(0);
        } else {
            writeInt(f.sizelineinfo);
            for (int i = 0; i < f.sizelineinfo; i++) {
                writeInt(f.lineinfo.get(i));
            }
            writeInt(f.sizelocvars);
            for (int i = 0; i < f.sizelocvars; i++) {
                LocVar lv = f.locvars.get(i);
                writeString(lv.varname);
                writeInt(lv.startpc);
                writeInt(lv.endpc);
            }
            writeInt(f.sizeupvalues);
            for (int i = 0; i < f.sizeupvalues; i++) {
                Upvaldesc uv = f.upvalues.get(i);
                writeString(uv.name);
            }
        }
    }

    private void writeFunction(Proto f) throws IOException {
        if (decInterface.requireStrip() || f.sizelineinfo == 0 || f.sizelocvars == 0) {
            writeString(NIL);
        } else {
            writeString(f.source);
        }
        writeInt(f.linedefined);
        writeInt(f.lastlinedefined);
        writeByte(f.numparams);
        writeByte(f.is_vararg);
        writeByte(f.maxstacksize);
        writeCode(f);
        writeConstants(f);
        writeUpvalues(f);
        writeProtos(f);
        writeDebug(f);
    }

    public void dump() throws IOException {
        stream = new ByteArrayOutputStream();
        writeHeader();
        writeByte((byte) (proto.sizeupvalues & 0xFF));
        writeFunction(proto);
    }

    private void writeByte(byte b) {
        stream.write(b);
    }

    private void writeLiteral(String s) throws IOException {
        writeLiteral(s.getBytes());
    }

    private void writeLiteral(byte[] s) throws IOException {
        stream.write(s);
    }

    private void writeInt(int x) throws IOException {
        byte[] b = int2bytes(x);
        stream.write(b);
    }

    private void writeNumber(double x) throws IOException {
        byte[] b = FLT2bytes(x);
        stream.write(b);
    }

    private void writeInteger(long x) throws IOException {
        byte[] b = INT2bytes(x);
        stream.write(b);
    }

    private void writeString(TValue o) throws IOException {
        if (o == TValue.NIL || o.tt_ == LUA_TNIL) {
            writeByte((byte) 0);
            return;
        }
        byte[] bytes = o.getStringBytes();
        int size = bytes.length + 1;//加上尾0
        if (size < 0xFF) {
            writeByte((byte) size);
        } else {
            writeByte((byte) 0xFF);
            writeInt(size);
        }
        writeLiteral(bytes);
    }

    private void writeInstruction(Instruction i) throws IOException {
        int x = i.value;
        byte[] b = Instruction2bytes(x);
        stream.write(b);
    }

    public byte[] getBytes() {
        if (stream == null) {
            try {
                dump();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stream.toByteArray();
    }

}
