package com.nwdxlgzs.luac53support;

public class LocVar {
    public TValue varname = TValue.NIL;
    public int startpc = 0;
    public int endpc = 0;

    public LocVar() {
    }

    @Override
    public String toString() {
        return "LocVar{" + "\r\n" +
                "varname=" + varname + "\r\n" +
                ", startpc=" + startpc + "\r\n" +
                ", endpc=" + endpc + "\r\n" +
                '}';
    }
}
