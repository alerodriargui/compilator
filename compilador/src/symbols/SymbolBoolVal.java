package compilador.src.symbols;

public class SymbolBoolVal extends SymbolBase {

	private Boolean value;

	public SymbolBoolVal(Boolean value) {
		super("Symbol Boolean Value", 0);
		this.value = value;
	}
	
	public Boolean getValue() {
		return this.value;
	}
}
