package compilador.src.symbols;

public class SymbolMEnd extends SymbolBase {

	private String label;

	public SymbolMEnd(String label) {
		super("Symbol M_End", 0);
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}
}
