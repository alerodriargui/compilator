package compilador.src.symbolsTable;

public class Type {
    private Tipo tipo;

    private TipoSubyacente tipoSubyacente;

    private String backendId = "";

    private int size;
    private int lowLimit;
    private int highLimit;
    private Object value;
    private String typeName;

    public Type(Tipo tipo, TipoSubyacente tipoSubyacente, int size, int lowLimit, int highLimit) {
        this.tipo = tipo;
        this.tipoSubyacente = tipoSubyacente;
        this.size = size;
        this.lowLimit = lowLimit;
        this.highLimit = highLimit;
    }

    public Type(Tipo tipo, String typeName) {
        this.tipo = tipo;
        this.typeName = typeName;
    }

    public Type(String backendId, Tipo tipo, String typeName) {
        this.backendId = backendId;
        this.tipo = tipo;
        this.typeName = typeName;
    }

    public Type(Tipo tipo, String typeName, Object value) {
        this.tipo = tipo;
        this.typeName = typeName;
        this.value = value;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public TipoSubyacente getTipoSubyacente() {
        return tipoSubyacente;
    }

    public void setTipoSubyacente(TipoSubyacente tipoSubyacente) {
        this.tipoSubyacente = tipoSubyacente;
    }

    public String getBackendId() {
        return backendId;
    }

    public void setBackendId(String backendId) {
        this.backendId = backendId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getLowLimit() {
        return lowLimit;
    }

    public void setLowLimit(int lowLimit) {
        this.lowLimit = lowLimit;
    }

    public int getHighLimit() {
        return highLimit;
    }

    public void setHighLimit(int highLimit) {
        this.highLimit = highLimit;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public enum Tipo {
        dnull,
        dtype,
        dconst,
        dvar,
        dfun,
        darg
    }

    public enum TipoSubyacente {
        TS_BOOLEAN,
        TS_NUMBER,
        TS_STRING,
        TS_NULL;

        public String toString() {
            return switch (this) {
                case TS_BOOLEAN -> "boolean";
                case TS_NULL -> "null";
                case TS_NUMBER -> "number";
                case TS_STRING -> "string";
            };
        }
    }
}
