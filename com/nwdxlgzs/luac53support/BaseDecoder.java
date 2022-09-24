package com.nwdxlgzs.luac53support;

import java.util.HashMap;

public class BaseDecoder implements DecInterface {
    @Override
    public int VirtualMemorySize() {
        return 0;
    }

    @Override
    public boolean requireLuaCParse() {
        return false;
    }

    @Override
    public boolean requireStrip() {
        return false;
    }

    @Override
    public byte[] LuaCPreProcess(byte[] data) {
        return data;
    }

    @Override
    public byte[] LuaCPostProcess(byte[] data) {
        return data;
    }

    @Override
    public byte[] StringDecrypt(byte[] data) {
        return data;
    }

    @Override
    public boolean BooleanDecrypt(boolean data) {
        return data;
    }

    @Override
    public long IntegerDecrypt(long data) {
        return data;
    }

    @Override
    public double NumberDecrypt(double data) {
        return data;
    }

    @Override
    public Instruction InstructionDecrypt(Instruction data) {
        return data;
    }

    @Override
    public HashMap<Integer, Integer> OPMapReplace() {
        return null;
    }

    @Override
    public int IntDecrypt(int data) {
        return data;
    }

    @Override
    public int NDecrypt(int data, String name) {
        return data;
    }

    @Override
    public byte ByteDecrypt(byte data) {
        return data;
    }

    @Override
    public byte[] BlockDecrypt(byte[] data) {
        return data;
    }

    @Override
    public Proto ProtoScan(Proto data) {
        return data;
    }
}
