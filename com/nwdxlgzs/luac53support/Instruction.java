package com.nwdxlgzs.luac53support;

import static com.nwdxlgzs.luac53support.defines.*;

public class Instruction {
    public int value = 0;
    public int opcode = 0;
    public OPCode opName;
    public int A = 0;
    public int B = 0;
    public int C = 0;
    public int Bx = 0;
    public int sBx = 0;
    public int Ax = 0;

    public Instruction(int instruction) {
        value = instruction;
        opcode = GET_OPCODE(instruction);
        opName = OPCode.fromValue(opcode);
        switch (opName.getMode()) {
            case iABC: {
                A = GETARG_A(instruction);
                B = GETARG_B(instruction);
                C = GETARG_C(instruction);
                break;
            }
            case iABx: {
                A = GETARG_A(instruction);
                Bx = GETARG_Bx(instruction);
                break;
            }
            case iAsBx: {
                A = GETARG_A(instruction);
                sBx = GETARG_sBx(instruction);
                break;
            }
            case iAx: {
                Ax = GETARG_Ax(instruction);
                break;
            }
            default: {
                A = GETARG_A(instruction);
                B = GETARG_B(instruction);
                C = GETARG_C(instruction);
                Bx = GETARG_Bx(instruction);
                sBx = GETARG_sBx(instruction);
                Ax = GETARG_Ax(instruction);
                break;
            }
        }
    }

    @Override
    public String toString() {
        switch (opName.getMode()) {
            case iABC: {
                return "Instruction{" + "\r\n" +
                        "value=" + value + "\r\n" +
                        ", opcode=" + opcode + "\r\n" +
                        ", opName=" + opName + "\r\n" +
                        ", A=" + A + "\r\n" +
                        ", B=" + B + "\r\n" +
                        ", C=" + C + "\r\n" +
                        '}';
            }
            case iABx: {
                return "Instruction{" + "\r\n" +
                        "value=" + value + "\r\n" +
                        ", opcode=" + opcode + "\r\n" +
                        ", opName=" + opName + "\r\n" +
                        ", A=" + A + "\r\n" +
                        ", Bx=" + Bx + "\r\n" +
                        '}';
            }
            case iAsBx: {
                return "Instruction{" + "\r\n" +
                        "value=" + value + "\r\n" +
                        ", opcode=" + opcode + "\r\n" +
                        ", opName=" + opName + "\r\n" +
                        ", A=" + A + "\r\n" +
                        ", sBx=" + sBx + "\r\n" +
                        '}';
            }
            case iAx: {
                return "Instruction{" + "\r\n" +
                        "value=" + value + "\r\n" +
                        ", opcode=" + opcode + "\r\n" +
                        ", opName=" + opName + "\r\n" +
                        ", Ax=" + Ax + "\r\n" +
                        '}';
            }
            default: {
                return "Instruction{" + "\r\n" +
                        "value=" + value + "\r\n" +
                        ", opcode=" + opcode + "\r\n" +
                        ", opName=" + opName + "\r\n" +
                        ", A=" + A + "\r\n" +
                        ", B=" + B + "\r\n" +
                        ", C=" + C + "\r\n" +
                        ", Bx=" + Bx + "\r\n" +
                        ", sBx=" + sBx + "\r\n" +
                        ", Ax=" + Ax + "\r\n" +
                        '}';
            }
        }
    }
}
