package compilador.src.backend;

import compilador.src.symbolsTable.Type;

public class StrVariable extends Variable {

    String value;

    public StrVariable(String name, int idParent, int size, String value) {
        super(name, idParent, 0, size, Type.TipoSubyacente.TS_STRING, false);
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String getAssemblerDir() {
        return this.getName();
    }
}
