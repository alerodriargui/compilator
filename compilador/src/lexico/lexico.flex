package compilador.src.lexico;

import compilador.src.errores.LexicalError;
import compilador.src.lexico.Token.Tokens;
import compilador.src.sintactico.ParserSym;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java_cup.runtime.Symbol;

import java.io.BufferedWriter;
import java.io.FileWriter;

%%                              //INICIO DE OPCIONES

%class Lexico
%cup
%public
%line
%column
%full

%init{
    try{
        out = new BufferedWriter(new FileWriter(TOKENS_PATH, true));
        out.write("*** Tokens ***\n");
    }catch(Exception e){
        System.out.println("Error writing Tokens : " + e);
        e.printStackTrace();
    }
%init}

%{
    public Lexico(java.io.Reader in, ComplexSymbolFactory sf) {
      this(in);
      this.symbolFactory = sf;
    }

    public static final String TOKENS_PATH = "output\\Tokens.txt";

    public static final String TOKENS_ERROR_PATH = "output\\Error_Tokens.txt";

    private static BufferedWriter out;

    private ComplexSymbolFactory symbolFactory;

    public void closeTokensFile(int line, int column){
        try{
            out.write("Stopped processing tokens due to a syntax or semantic error on line : " + line + " and column : "+ column);
            out.close();
        }catch(Exception e){
            System.out.println("Error closing Tokens file : " + e);
            e.printStackTrace();
        }
    }

    public int getLine(){
        return yyline + 1;
    }

    public int getColumn(){
        return yycolumn + 1;
    }

    public void writeToken(Token token){
        try{
            out.write(token.toString());
        }catch(Exception e){
            System.out.println("Error writing Tokens : " + e);
            e.printStackTrace();
        }
    }

    private Symbol symbol(String plainname,int type){
        return symbolFactory.newSymbol(plainname, type, new Location(getLine(), getColumn()), new Location(getColumn(), yycolumn+yylength()));
    }

    private Symbol symbol(String plainname, int type, String lexeme){
        return symbolFactory.newSymbol(plainname, type, new Location(getLine(), getColumn()), new Location(getLine(), yycolumn+yylength()), lexeme);
    }
%}

%eof{
    try {
        writeToken(new Token(Tokens.EOF,yyline,yycolumn, ""));
        out.write("\n\n*** All token data shown! ***");
        out.close();
    }catch(Exception e){
        System.out.println("Error writing Tokens : " + e);
        e.printStackTrace();
    }
%eof}

%eofval{
  return symbol(Tokens.EOF.name(),ParserSym.EOF);
%eofval}


DIGIT           = [0-9]     //ok
LETTER          = [a-zA-Z]  //ok

BLANK           = (" "|\t|\r|\n)
COMMENT         = ("/*".*"*/") //OK

ID              = (({LETTER})({LETTER}|{DIGIT})*)   // ok
NUMBER          = ("0" | [1-9]{DIGIT}*)                     // ok
STRING          = \" [^\"]* \"                              // ok
BOOLEAN         = ("true" | "false")                        // ok

// Operadores

OP_SUM      = ("+")
OP_SUB      = ("-")
OP_MULT     = ("*")
OP_DIV      = ("/")
OP_MOD      = ("%")
OP_EQ       = ("==")
OP_NEQ      = ("!=")
OP_LT       = ("<")
OP_GT       = (">")
OP_LE       = ("<=")
OP_GE       = (">=")
OP_AND      = ("&&")
OP_OR       = ("||")
OP_NOT      = ("!")
OP_ASSIG    = (":=")

CONSTANT        = ("constant")     //ok

// Operaciones

INST_IF         = ("if")    //ok
INST_ELSE       = ("else")  //ok
INST_ELIF       = ("elif")  //ok BORRAMOS??

INST_WHILE      = ("while") //ok
/*
INST_FOR        = ("for")   //ok
*/

INST_FUNCTION   = ("function") //ok
INST_RETURN     = ("return")    //ok
INST_MAIN       = ("main")      //ok    //*

// Entrada/salida

INSTR_READ        = ("read") //ok
INSTR_PRINT       = ("print") //ok

// Caracteres especiales

LPAREN          = ("(")     //ok
RPAREN          = (")")     //ok
LBRACKET        = ("{")     //ok
RBRACKET        = ("}")     //ok
SEMICOLON       = (";")     //ok
COMMA           = (",")     //ok
TWO_POINTS      = (":")     //ok

%%
{BLANK}              {/*Ignore*/}
{COMMENT}            {/*Ignore*/}

{INST_IF}            {
                        Token token = new Token(Tokens.INST_IF,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.INST_IF.name(), ParserSym.inst_if);
                     }
{INST_ELSE}          {
                        Token token = new Token(Tokens.INST_ELSE,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.INST_ELSE.name(), ParserSym.inst_else);
                     }
{INST_ELIF}          {
                        Token token = new Token(Tokens.INST_ELSE,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.INST_ELSE.name(),ParserSym.inst_elif);
                     } // BORRAMOS??

{INST_WHILE}         {
                        Token token = new Token(Tokens.INST_WHILE,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.INST_WHILE.name(),ParserSym.inst_while);
                     }

/*
{INST_FOR}           {
                        Token token = new Token(Tokens.INST_FOR,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(ParserSym.inst_for, yytext());
                    }
*/

{INST_FUNCTION}      {
                        Token token = new Token(Tokens.INST_FUNCTION,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.INST_FUNCTION.name(),ParserSym.inst_function);
                     }
{INST_RETURN}        {
                        Token token = new Token(Tokens.INST_RETURN,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.INST_RETURN.name(),ParserSym.inst_return);
                     }
{INST_MAIN}          {
                        Token token = new Token(Tokens.INST_MAIN,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.INST_MAIN.name(),ParserSym.inst_main);
                     }

{INSTR_READ}         {
                        Token token = new Token(Tokens.INSTR_READ,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.INSTR_READ.name(),ParserSym.instr_read);
                     }
{INSTR_PRINT}        {
                        Token token = new Token(Tokens.INSTR_PRINT,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.INSTR_PRINT.name(),ParserSym.instr_print);
                     }

{LPAREN}             {
                        Token token = new Token(Tokens.LPAREN,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.LPAREN.name(),ParserSym.lparen);
                     }
{RPAREN}             {
                        Token token = new Token(Tokens.RPAREN,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.RPAREN.name(),ParserSym.rparen);
                     }
{LBRACKET}           {
                        Token token = new Token(Tokens.LBRACKET,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.LBRACKET.name(),ParserSym.lbracket);
                     }
{RBRACKET}           {
                        Token token = new Token(Tokens.RBRACKET,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.RBRACKET.name(),ParserSym.rbracket);
                     }
{SEMICOLON}          {
                        Token token = new Token(Tokens.SEMICOLON,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.SEMICOLON.name(),ParserSym.semicolon);
                     }
{COMMA}              {
                        Token token = new Token(Tokens.COMMA,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.COMMA.name(),ParserSym.comma);
                     }
{TWO_POINTS}         {
                        Token token = new Token(Tokens.TWO_POINTS,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.TWO_POINTS.name(),ParserSym.two_points);
                     }

              //OPERADORES

{OP_SUM}             {
                        Token token = new Token(Tokens.OP_SUM,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_SUM.name(),ParserSym.op_arithmetical_b, yytext());
                     }
{OP_SUB}             {
                        Token token = new Token(Tokens.OP_SUB,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_SUB.name(),ParserSym.op_arithmetical_b, yytext());
                     }
{OP_MULT}            {
                        Token token = new Token(Tokens.OP_MULT,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_MULT.name(),ParserSym.op_arithmetical_c, yytext());
                     }
{OP_DIV}             {
                        Token token = new Token(Tokens.OP_DIV,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_DIV.name(),ParserSym.op_arithmetical_c, yytext());
                     }
{OP_MOD}             {
                        Token token = new Token(Tokens.OP_MOD,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_MOD.name(),ParserSym.op_arithmetical_c, yytext());
                     }
{OP_EQ}              {
                        Token token = new Token(Tokens.OP_EQ,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_EQ.name(),ParserSym.op_relational, yytext());
                     }
{OP_NEQ}             {
                        Token token = new Token(Tokens.OP_NEQ,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_NEQ.name(),ParserSym.op_relational, yytext());
                     }
{OP_LT}              {
                        Token token = new Token(Tokens.OP_LT,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_LT.name(),ParserSym.op_relational, yytext());
                     }
{OP_GT}              {
                        Token token = new Token(Tokens.OP_GT,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_GT.name(),ParserSym.op_relational, yytext());
                     }
{OP_LE}              {
                        Token token = new Token(Tokens.OP_LE,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_LE.name(),ParserSym.op_relational, yytext());
                     }
{OP_GE}              {
                        Token token = new Token(Tokens.OP_GE,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_GE.name(),ParserSym.op_relational, yytext());
                     }
{OP_AND}             {
                        Token token = new Token(Tokens.OP_AND,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_AND.name(),ParserSym.op_logical, yytext());
                     }
{OP_OR}              {
                        Token token = new Token(Tokens.OP_OR,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_OR.name(),ParserSym.op_logical, yytext());
                     }
{OP_NOT}             {
                        Token token = new Token(Tokens.OP_NOT,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_NOT.name(),ParserSym.op_logical_not, yytext());
                     }
{OP_ASSIG}           {
                        Token token = new Token(Tokens.OP_ASSIG,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.OP_ASSIG.name(),ParserSym.op_assig, yytext());
                     }

{BOOLEAN}            {
                     Token token = new Token(Tokens.BOOLEAN,yyline,yycolumn, yytext());
                     writeToken(token);
                     return symbol(Tokens.BOOLEAN.name(),ParserSym.bool, yytext());
                     }
{CONSTANT}           {
                     Token token = new Token(Tokens.CONSTANT,yyline,yycolumn, yytext());
                     writeToken(token);
                     return symbol(Tokens.CONSTANT.name(),ParserSym.constant, yytext());
                     }

{ID}                 {
                     Token token = new Token(Tokens.ID,yyline,yycolumn, yytext());
                     writeToken(token);
                     return symbol(Tokens.ID.name(),ParserSym.id, yytext());
                     }
{NUMBER}             {
                     Token token = new Token(Tokens.NUMBER,yyline,yycolumn, yytext());
                     writeToken(token);
                     return symbol(Tokens.NUMBER.name(),ParserSym.number, yytext());
                     }
{STRING}             {
                        Token token = new Token(Tokens.STRING,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(Tokens.STRING.name(),ParserSym.string, yytext());
                     }

/*
{SPC_INC}            {
                        Token token = new Token(Tokens.SPC_INC,yyline,yycolumn, yytext());
                        writeToken(token);
                        return symbol(ParserSym.spc_inc);
                     }
{SPC_DEC}            {
                     Token token = new Token(Tokens.SPC_DEC,yyline,yycolumn, yytext());
                     writeToken(token);
                     return symbol(ParserSym.spc_dec);
                     }


{SPC_ASGINC}         {
                     Token token = new Token(Tokens.SPC_ASGINC,yyline,yycolumn, yytext());
                     writeToken(token);
                     return symbol(ParserSym.spc_asginc);
                     }
{SPC_ASGDEC}         {
                     Token token = new Token(Tokens.SPC_ASGDEC,yyline,yycolumn, yytext());
                     writeToken(token);
                     return symbol(ParserSym.spc_asgdec);
                     }
{SPC_ASGDIV}         {
                     Token token = new Token(Tokens.SPC_ASGDIV,yyline,yycolumn, yytext());
                     writeToken(token);
                     return symbol(ParserSym.spc_asgdiv);
                     }
{SPC_ASGMUL}         {
                     Token token = new Token(Tokens.SPC_ASGMUL,yyline,yycolumn, yytext());
                     writeToken(token);
                     return symbol(ParserSym.spc_asgmul);
                     }
*/
//==============================================================================
[^]                  {
                        Token token = new Token(Tokens.ERROR,yyline,yycolumn, yytext());
                        writeToken(token);
                        try{
                            throw new LexicalError("[Lexical error]:" + "[" + getLine() + ":" + getColumn() + "]" + " Unkown symbol: "+"'"+this.yytext()+"'");
                        } catch (LexicalError ex) {
                            System.err.println("ERROR: " + ex.getMessage());
                        }

                        return symbol(Tokens.ERROR.name(), ParserSym.error);
                     }
//==============================================================================