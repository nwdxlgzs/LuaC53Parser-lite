# LuaC53Parser-lite
轻量级的LuaC53序列化和反序列化解析工具
> 注意！本身工具做了解密API包装，你可以对BaseDecoder覆写，进行自动解密


* 本身javaAPI应该没有人不会吧？我就不给demo了，有人应该有Lua需求，我这里贴一份luajava借助interface的基础包装：
```lua
BUFF=DecTool.decrypt(BUFF,DecInterface{
  requireLuaCParse=function(...)
    return false;
  end;
  requireStrip=function()
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
