package compilador.src.backend;

import compilador.src.symbolsTable.Type.TipoSubyacente;

// Information that Variable table will be using (TV)
public class Variable {

    private String name;    // variable name
    private int idParent;   // parent id
    private int offset;     // offset
    private int size;       // space occupation
    private TipoSubyacente type;      // type
    private boolean isParam;

    public Variable(String name, int idParent, int offset, int tam, TipoSubyacente type, boolean isParam) {
        this.name = name;
        this.idParent = idParent;
        this.offset = offset;
        this.size = tam;
        this.type = type;
        this.isParam = isParam;
    }

    public TipoSubyacente getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getAssemblerDir() {
        if (isParam) {
            return this.offset + "(%rbp)";
        }
        // - offset
        return (-this.offset) + "(%rbp)";
    }

    @Override
    public String toString() {
        return this.name + "\ttam : " + this.size + "\ttipo subyacente :" + this.type + "\tidFun : " + this.idParent + "\toffset : " + getAssemblerDir();
    }
}
