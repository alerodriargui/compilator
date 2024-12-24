package compilador.src.symbols;

public class SymbolArithOperator extends SymbolBase {

	private String operator;

	public SymbolArithOperator(String operator) {
		super("Symbol Arithmetical Operator", 0);
		this.operator = operator;
	}
	
	public String getOperator() {
		return this.operator;
	}
}
