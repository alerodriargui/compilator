package compilador.src.symbolsTable;

import compilador.src.symbolsTable.Type.Tipo;


/*
 * La tabla de descripción usará un ArrayList
 */
public class Description {
    private String id;
    private Type type;
    private int scope;
    private int first = -1;

    public Description(String id, Type type, int scope) {
        this.id = id;
        this.type = type;
        this.scope = scope;
    }

    public Description(Expansion exp) {
        this.id = exp.getId();
        this.type = exp.getType();
        this.scope = exp.getScope();
    }

    public String getId() {
        return this.id;
    }

    public Type getType() {
        return this.type;
    }

    public int getScope() {
        return this.scope;
    }

    public int getFirst() {
        return this.first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    @Override
    public String toString() {
        String result = "ID : " + this.id + " TIPO: " + this.type.getTipo() + " TIPO SUBYACENTE: " + this.type.getTipoSubyacente() + " SCOPE: " + this.scope;
        if (type.getTipo() == Tipo.dfun) {
            result += " FIRST: " + this.first;
        }
        return result;
    }
}