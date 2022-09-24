package com.nwdxlgzs.luac53support;

public class Upvaldesc {
    public TValue name = TValue.NIL;
    public byte instack = 0;
    public byte idx = 0;

    public Upvaldesc() {
    }

    @Override
    public String toString() {
        return "Upvaldesc{" + "\r\n" +
                "name=" + name + "\r\n" +
                ", instack=" + instack + "\r\n" +
                ", idx=" + idx + "\r\n" +
                '}';
    }
}
