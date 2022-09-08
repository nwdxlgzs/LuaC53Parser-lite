package com.nwdxlgzs.luac53support;

import java.util.HashMap;

public interface DecInterface {
    //是否进入LuaC结构解析（如果只是LuaC整体加解密不用搞解析，返回false将不进行下方一些API执行）
    boolean requireLuaCParse();

    //strip模式(注意，如果sizelineinfo或者sizelocvars为0，那么强制true，这两个是调试信息，如果没有说明是strip模式，防呆保护)
    boolean requireStrip();

    ///LuaC的预处理
    byte[] LuaCPreProcess(byte[] data);

    //LuaC的后处理
    byte[] LuaCPostProcess(byte[] data);

    ///字符串解密
    byte[] StringDecrypt(byte[] data);

    //布尔值解密
    boolean BooleanDecrypt(boolean data);

    //整数（lua_Integer）解密
    long IntegerDecrypt(long data);

    //浮点数（lua_Number）解密
    double NumberDecrypt(double data);

    //指令解密（在这里可以进行OPCode替换，注意，只有value才是最终序列化的对象，其他的只是提供分析的数据）
    Instruction InstructionDecrypt(Instruction data);

    //OPCode替换（null放弃替换），返回一个OPMAP，映射到原来的OPCode
    HashMap<Integer, Integer> OPMapReplace();

    //int解密(基本是n值的unsigned int处理，Int和lua_Integer是两码事)
    int IntDecrypt(int data);

    //n解密(sizeXXX的n值)
    int NDecrypt(int data, String name);

    //byte解密
    byte ByteDecrypt(byte data);

    //Block解密
    byte[] BlockDecrypt(byte[] data);

    //Proto扫描修改
    Proto ProtoScan(Proto proto);
}
