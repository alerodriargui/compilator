package compilador.src.symbolsTable;

public class Expansion {

    private Type type;
    private String id;
    private int scope;

    public Expansion(Type type, String id, int scope) {
        this.type = type;
        this.id = id;
        this.scope = scope;
    }

    public Expansion(Description description) {
        this.type = description.getType();
        this.id = description.getId();
        this.scope = description.getScope();
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

    @Override
    public String toString() {
        return "ID : " + this.id + " TIPO: " + this.type.getTipo() + " TIPO SUBYACENTE: " + this.type.getTipoSubyacente() + " SCOPE: " + this.scope;
    }
}
