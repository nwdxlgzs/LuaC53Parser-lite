package com.nwdxlgzs.luac53support;

import static com.nwdxlgzs.luac53support.OPCode.OpArgMask.*;
import static com.nwdxlgzs.luac53support.OPCode.OpMode.*;

public enum OPCode {
    UNKNOWN(-1, -1, -1, OpArgMask.UNKNOWN, OpArgMask.UNKNOWN, OpMode.UNKNOWN),
    OP_MOVE(0, 0, 1, OpArgR, OpArgN, iABC),
    OP_LOADK(1, 0, 1, OpArgK, OpArgN, iABx),
    OP_LOADKX(2, 0, 1, OpArgN, OpArgN, iABx),
    OP_LOADBOOL(3, 0, 1, OpArgU, OpArgU, iABC),
    OP_LOADNIL(4, 0, 1, OpArgU, OpArgN, iABC),
    OP_GETUPVAL(5, 0, 1, OpArgU, OpArgN, iABC),
    OP_GETTABUP(6, 0, 1, OpArgU, OpArgK, iABC),
    OP_GETTABLE(7, 0, 1, OpArgR, OpArgK, iABC),
    OP_SETTABUP(8, 0, 0, OpArgK, OpArgK, iABC),
    OP_SETUPVAL(9, 0, 0, OpArgU, OpArgN, iABC),
    OP_SETTABLE(10, 0, 0, OpArgK, OpArgK, iABC),
    OP_NEWTABLE(11, 0, 1, OpArgU, OpArgU, iABC),
    OP_SELF(12, 0, 1, OpArgR, OpArgK, iABC),
    OP_ADD(13, 0, 1, OpArgK, OpArgK, iABC),
    OP_SUB(14, 0, 1, OpArgK, OpArgK, iABC),
    OP_MUL(15, 0, 1, OpArgK, OpArgK, iABC),
    OP_MOD(16, 0, 1, OpArgK, OpArgK, iABC),
    OP_POW(17, 0, 1, OpArgK, OpArgK, iABC),
    OP_DIV(18, 0, 1, OpArgK, OpArgK, iABC),
    OP_IDIV(19, 0, 1, OpArgK, OpArgK, iABC),
    OP_BAND(20, 0, 1, OpArgK, OpArgK, iABC),
    OP_BOR(21, 0, 1, OpArgK, OpArgK, iABC),
    OP_BXOR(22, 0, 1, OpArgK, OpArgK, iABC),
    OP_SHL(23, 0, 1, OpArgK, OpArgK, iABC),
    OP_SHR(24, 0, 1, OpArgK, OpArgK, iABC),
    OP_UNM(25, 0, 1, OpArgR, OpArgN, iABC),
    OP_BNOT(26, 0, 1, OpArgR, OpArgN, iABC),
    OP_NOT(27, 0, 1, OpArgR, OpArgN, iABC),
    OP_LEN(28, 0, 1, OpArgR, OpArgN, iABC),
    OP_CONCAT(29, 0, 1, OpArgR, OpArgR, iABC),
    OP_JMP(30, 0, 0, OpArgR, OpArgN, iAsBx),
    OP_EQ(31, 1, 0, OpArgK, OpArgK, iABC),
    OP_LT(32, 1, 0, OpArgK, OpArgK, iABC),
    OP_LE(33, 1, 0, OpArgK, OpArgK, iABC),
    OP_TEST(34, 1, 0, OpArgR, OpArgU, iABC),
    OP_TESTSET(35, 1, 1, OpArgR, OpArgU, iABC),
    OP_CALL(36, 0, 1, OpArgU, OpArgU, iABC),
    OP_TAILCALL(37, 0, 0, OpArgU, OpArgU, iABC),
    OP_RETURN(38, 0, 0, OpArgU, OpArgN, iABC),
    OP_FORLOOP(39, 1, 0, OpArgR, OpArgN, iAsBx),
    OP_FORPREP(40, 1, 0, OpArgR, OpArgN, iAsBx),
    OP_TFORCALL(41, 0, 0, OpArgU, OpArgU, iABC),
    OP_TFORLOOP(42, 1, 0, OpArgR, OpArgN, iAsBx),
    OP_SETLIST(43, 0, 0, OpArgU, OpArgU, iABC),
    OP_CLOSURE(44, 0, 1, OpArgU, OpArgN, iABx),
    OP_VARARG(45, 0, 1, OpArgU, OpArgN, iABC),
    OP_EXTRAARG(46, 0, 0, OpArgU, OpArgU, iAx),
    //下头三个是nirenr的Androlua+自定义指令，其中TFOREACH只是保留指令，还未实现
    OP_TBC(47, 0, 0, OpArgN, OpArgN, iABC),
    OP_NEWARRAY(48, 0, 1, OpArgU, OpArgN, iABC),
    OP_TFOREACH(49, 0, 0, OpArgN, OpArgU, iABC);

    private int op;
    private int T;
    private int A;
    private OpArgMask B;
    private OpArgMask C;
    private OpMode Mode;

    OPCode(int op, int T, int A, OpArgMask B, OpArgMask C, OpMode Mode) {
        this.op = op;
        this.T = T;
        this.A = A;
        this.B = B;
        this.C = C;
        this.Mode = Mode;
    }

    public int getOP() {
        return op;
    }

    public int getT() {
        return T;
    }

    public int getA() {
        return A;
    }

    public OpArgMask getB() {
        return B;
    }

    public OpArgMask getC() {
        return C;
    }

    public OpMode getMode() {
        return Mode;
    }

    public static OPCode fromValue(int value) {
        for (OPCode opCode : OPCode.values()) {
            if (opCode.getOP() == value) {
                return opCode;
            }
        }
        return UNKNOWN;
    }

    public enum OpArgMask {
        OpArgN(0),
        OpArgU(1),
        OpArgR(2),
        OpArgK(3),
        UNKNOWN(-1);
        public int value;

        OpArgMask(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static OpArgMask fromValue(int value) {
            for (OpArgMask opArgMask : OpArgMask.values()) {
                if (opArgMask.getValue() == value) {
                    return opArgMask;
                }
            }
            return UNKNOWN;
        }
    }

    public enum OpMode {
        iABC(0),
        iABx(1),
        iAsBx(2),
        iAx(3),
        UNKNOWN(-1);
        public int value;

        OpMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static OpMode fromValue(int value) {
            for (OpMode opMode : OpMode.values()) {
                if (opMode.getValue() == value) {
                    return opMode;
                }
            }
            return UNKNOWN;
        }
    }
}
