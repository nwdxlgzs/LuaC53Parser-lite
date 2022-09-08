package com.nwdxlgzs.luac53support;

public class LocVar {
    public TValue varname;
    public int startpc;
    public int endpc;

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
