package compilador.src.symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

public class SymbolBase extends ComplexSymbol {

    public SymbolBase(String variable, Integer valor) {
        super(variable, valor);
    }

}