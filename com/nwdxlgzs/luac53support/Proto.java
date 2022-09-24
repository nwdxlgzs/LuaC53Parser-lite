package com.nwdxlgzs.luac53support;

import java.util.ArrayList;

public class Proto {
    public byte numparams = 0;
    public byte is_vararg = 0;
    public byte maxstacksize = 0;
    public int sizeupvalues = 0;
    public int sizek = 0;
    public int sizecode = 0;
    public int sizelineinfo = 0;
    public int sizep = 0;
    public int sizelocvars = 0;
    public int linedefined = 0;
    public int lastlinedefined = 0;
    public ArrayList<TValue> k = new ArrayList<>();
    public ArrayList<Instruction> code = new ArrayList<>();
    public ArrayList<Proto> p = new ArrayList<>();
    public ArrayList<Integer> lineinfo = new ArrayList<>();
    public ArrayList<LocVar> locvars = new ArrayList<>();
    public ArrayList<Upvaldesc> upvalues = new ArrayList<>();
    public TValue source = TValue.NIL;

    public Proto() {
    }

    @Override
    public String toString() {
        return "Proto{" + "\r\n" +
                "numparams=" + numparams + "\r\n" +
                ", is_vararg=" + is_vararg + "\r\n" +
                ", maxstacksize=" + maxstacksize + "\r\n" +
                ", sizeupvalues=" + sizeupvalues + "\r\n" +
                ", sizek=" + sizek + "\r\n" +
                ", sizecode=" + sizecode + "\r\n" +
                ", sizelineinfo=" + sizelineinfo + "\r\n" +
                ", sizep=" + sizep + "\r\n" +
                ", sizelocvars=" + sizelocvars + "\r\n" +
                ", linedefined=" + linedefined + "\r\n" +
                ", lastlinedefined=" + lastlinedefined + "\r\n" +
                ", k=" + k + "\r\n" +
                ", code=" + code + "\r\n" +
                ", p=" + p + "\r\n" +
                ", lineinfo=" + lineinfo + "\r\n" +
                ", locvars=" + locvars + "\r\n" +
                ", upvalues=" + upvalues + "\r\n" +
                ", source=" + source + "\r\n" +
                '}';
    }
}
