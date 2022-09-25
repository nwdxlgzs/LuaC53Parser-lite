# LuaC53Parser-lite
轻量级的LuaC53序列化和反序列化解析工具
> 注意！本身工具做了解密API包装，你可以对BaseDecoder覆写，进行自动解密

* Java
```java
new BaseDecoder() {//修正某种防导入unluac的字节码
   @Override
   public int VirtualMemorySize() {
      return buff.length;//一倍拓容
   }
   @Override
   public boolean requireLuaCParse() {
      return true;
   }
});
```

* Lua:(luajava借助interface的基础包装)
```lua
BUFF=DecTool.decrypt(BUFF,DecInterface{
   VirtualMemorySize=function(...)
    return int(0);
  end;
  requireLuaCParse=function(...)
    return false;
  end;
  requireStrip=function(...)
    return false;
  end;
  LuaCPreProcess=function(...)
    return ...;
  end;
  LuaCPostProcess=function(...)
    return ...;
  end;
  StringDecrypt=function(...)
    return ...;
  end;
  BooleanDecrypt=function(...)
    return ...;
  end;
  IntegerDecrypt=function(...)
    return ...;
  end;
  NumberDecrypt=function(...)
    return ...;
  end;
  InstructionDecrypt=function(...)
    return ...;
  end;
  OPMapReplace=function()
    return nil;
  end;
  IntDecrypt=function(i)
    return int(i);
  end;
  NDecrypt=function(i)
    return int(i);
  end;
  ByteDecrypt=function(i)
    return byte(i);
  end;
  BlockDecrypt=function(...)
    return ...;
  end;
  ProtoScan=function(...)
    return ...;
  end;
})
```
