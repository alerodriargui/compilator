package compilador.src.backend;

import compilador.src.symbolsTable.Type.TipoSubyacente;

public class Procedure {
    private static int NV = 0;

    private String name;    // its name
    private int nv;         // variable number
    private int nparams;
    private int size;       // memory used
    private int offset;     // params offset
    private TipoSubyacente type;      // type

    public Procedure(String name, int nparams, int size, TipoSubyacente type) {
        this.name = name;
        this.nv = NV++;
        this.nparams = nparams;
        this.size = size;
        this.type = type;
        this.offset = 0;
    }

    @Override
    public String toString() {
        return this.name.replace("PROC_", "") + "\tnparams=" + this.nparams + "\ttype=" + this.type;
    }

    public String getName() {
        return name;
    }


    public int getNv() {
        return nv;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public TipoSubyacente getType() {
        return this.type;
    }
}
