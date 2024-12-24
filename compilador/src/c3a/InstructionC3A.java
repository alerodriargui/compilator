package compilador.src.c3a;

public class InstructionC3A {

    // All posible operations that our c3@ will be able to use
    public static enum Code {
        copy,
        neg,
        add,
        sub,
        prod,
        div,
        mod,
        and,
        or,
        not,
        skip,
        go_to, // UN-conditional jump
        jump_cond,
        LT, // <    jl
        LE, // <=   jle
        EQ, // =    je
        NE, // !=   jne
        GE, // >=   jge
        GT, // >    jg
        pmb,
        call,
        param,
        rtn,
        input, // System in
        output, // System print
    }

    public Code opCode;
    public String op1, op2, dest;

    public InstructionC3A(Code opCode, String op1, String op2, String dest) {
        this.opCode = opCode;
        this.op1 = op1;
        this.op2 = op2;
        this.dest = dest;
    }

    @Override
    public String toString() {
        String result = "";
        switch (opCode) {
            /* UNARY OP */
            case not:
            case neg:
                result += this.dest + " = " + this.opCode + " " + this.op1;
                break;
            /* ARITHMETICAL OP*/
            case add:
            case sub:
            case div:
            case prod:
            case mod:
                /* BOOLEAN OP */
            case and:
            case or:
                result += this.dest + " = " + this.op1 + " " + this.opCode + " " + this.op2;
                break;
            case call:
                result += this.opCode + " " + this.dest;
                break;
            case copy:
                result += this.dest + " = " + this.op1;
                if (this.op1.equals("return")) {
                    result += " " + this.op2;
                }
                break;
            /* JUMPS */
            case go_to:
                result += this.opCode + " " + this.dest;
                break;
            case EQ:
            case GE:
            case GT:
            case LE:
            case LT:
            case NE:
                result += this.dest + "= " + this.op1 + " " + this.opCode + " " + this.op2;
                break;
            case jump_cond:
                result += "if " + this.op1 + "=" + this.op2 + " goto " + this.dest;
                break;
            /* OTHER OPERATIONS*/
            case input:
                result += this.dest + " = " + this.opCode;
                break;
            case output:
                if (this.op2 != null) {
                    result += this.dest + " = \"" + this.op2 + "\"\n";
                }
                result += this.opCode + " " + this.dest;
                break;
            case param:
                result += this.opCode + " " + this.dest + "(" + this.op1 + ")";
                break;
            case pmb:
                result += this.opCode + " " + this.dest;
                break;
            case rtn:
                result += this.opCode;
                if (this.op1 != null) {
                    result += " " + this.op1;
                }
                break;
            case skip:
                result += this.dest + ":" + this.opCode;
                break;
            default:
                break;
        }

        return result;
    }

    public Code getOpCode() {
        return opCode;
    }

    public void setOpCode(Code opCode) {
        this.opCode = opCode;
    }

    public String signeOperador() {
        switch (this.opCode) {
            case add:
                return "+";
            case div:
                return "%";
            case sub:
                return "-";
            case mod:
                return "%";
            case prod:
                return "*";
            case LT:
                return "<";
            case LE:
                return "<=";
            case EQ:
                return "=";
            case NE:
                return "!=";
            case GE:
                return ">=";
            case GT:
                return ">";
        }
        return null;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public static boolean opIsLiteral(String n) {
        return opIsInt(n) || opIsBoolean(n) || opIsString(n);
    }

    public static boolean opIsInt(String n) {
        return n.matches("[0-9]+") || n.matches("^-[0-9]+");
    }

    public static boolean opIsBoolean(String n) {
        return n.equals("true") || n.equals("false");
    }

    public static boolean opIsString(String n) {
        return n.charAt(0) == '"' && n.charAt(n.length() - 1) == '"';
    }

}
