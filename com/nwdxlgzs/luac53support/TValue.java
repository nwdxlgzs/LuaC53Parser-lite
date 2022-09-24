package com.nwdxlgzs.luac53support;

public class TValue {
    public static final int LUA_TNIL = 0;
    public static final int LUA_TBOOLEAN = 1;
    public static final int LUA_TNUMBER = 3;
    public static final int LUA_TNUMFLT = LUA_TNUMBER | (0 << 4);
    public static final int LUA_TNUMINT = LUA_TNUMBER | (1 << 4);
    public static final int LUA_TSTRING = 4;
    public static final int LUA_TSHRSTR = LUA_TSTRING | (0 << 4);
    public static final int LUA_TLNGSTR = LUA_TSTRING | (1 << 4);
    public static final int LUAI_MAXSHORTLEN = 40;

    public static final TValue NIL = TValue.createNil();
    public static final TValue TRUE = TValue.createBoolean(true);
    public static final TValue FALSE = TValue.createBoolean(false);
    public static final TValue EMPTY_STRING = TValue.createString("");

    public Object value_;
    public int tt_;

    public TValue() {
    }

    public void getNil() {
        if (tt_ != LUA_TNIL) {
            throw new RuntimeException("TValue不是LUA_TNIL");
        }
    }

    public boolean getBoolean() {
        if (tt_ != LUA_TBOOLEAN) {
            throw new RuntimeException("TValue不是LUA_TBOOLEAN");
        }
        return (boolean) value_;
    }

    public String getString() {
        if (tt_ != LUA_TSHRSTR && tt_ != LUA_TLNGSTR) {
            throw new RuntimeException("TValue不是LUA_TSHRSTR或LUA_TLNGSTR");
        }
        return (String) new String((byte[]) value_);
    }

    public byte[] getStringBytes() {
        if (tt_ != LUA_TSHRSTR && tt_ != LUA_TLNGSTR) {
            throw new RuntimeException("TValue不是LUA_TSHRSTR或LUA_TLNGSTR");
        }
        return (byte[]) value_;
    }

    public double getFLT() {
        if (tt_ != LUA_TNUMFLT) {
            throw new RuntimeException("TValue不是LUA_TNUMFLT");
        }
        return (double) value_;
    }

    public long getINT() {
        if (tt_ != LUA_TNUMINT) {
            throw new RuntimeException("TValue不是LUA_TNUMINT");
        }
        return (long) value_;
    }

    public static TValue createNil() {
        if (NIL == null) {
            TValue tv = new TValue();
            tv.tt_ = LUA_TNIL;
            return tv;
        }
        return NIL;
    }

    public static TValue createBoolean(boolean b) {
        if(b){
            if(TRUE == null){
                TValue tv = new TValue();
                tv.tt_ = LUA_TBOOLEAN;
                tv.value_ = true;
                return tv;
            }
            return TRUE;
        }else{
            if(FALSE == null){
                TValue tv = new TValue();
                tv.tt_ = LUA_TBOOLEAN;
                tv.value_ = false;
                return tv;
            }
            return FALSE;
        }
    }

    public static TValue createNumber(double d) {
        TValue tv = new TValue();
        tv.tt_ = LUA_TNUMFLT;
        tv.value_ = d;
        return tv;
    }

    public static TValue createInteger(long l) {
        TValue tv = new TValue();
        tv.tt_ = LUA_TNUMINT;
        tv.value_ = l;
        return tv;
    }

    public static TValue createString(String str) {
        return createString(str.getBytes());
    }

    public static TValue createString(byte[] data) {
        TValue ts = new TValue();
        ts.value_ = data;
        ts.tt_ = data.length <= LUAI_MAXSHORTLEN ? LUA_TSHRSTR : LUA_TLNGSTR;
        return ts;
    }

    @Override
    public String toString() {
        switch (tt_) {
            case LUA_TNIL: {
                return "nil";
            }
            case LUA_TBOOLEAN: {
                return (boolean) value_ ? "true" : "false";
            }
            case LUA_TNUMFLT: {
                return String.valueOf((double) value_);
            }
            case LUA_TNUMINT: {
                return String.valueOf((long) value_);
            }
            case LUA_TSHRSTR:
            case LUA_TLNGSTR: {
                if (value_ == null) {
                    return "null";
                }
                return new String((byte[]) value_);
            }
            default: {
                return "unknown type";
            }
        }
    }
}
