package com.nwdxlgzs.luac53support;

public class DecTool extends defines {
    public static byte[] decrypt(byte[] luac, DecInterface decInterface) {
        if (decInterface == null) {
            decInterface = NULL_Decoder;
        }
        //预处理
        luac = decInterface.LuaCPreProcess(luac);
        //判断是否需要解析
        if (decInterface.requireLuaCParse()) {
            //走文件解析解密结构
            Undump undump = new Undump(luac, decInterface);//反序列化
            Proto proto = undump.parse();//解析
            proto = decInterface.ProtoScan(proto);//扫描Proto并修改
            Dump dump = new Dump(proto, decInterface);//序列化
            return dump.getBytes();//输出
        } else {
            //不需要解析结构
            return luac;
        }
    }
}
