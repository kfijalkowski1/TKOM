package lekser;

public enum TokenType {
    INT_NUMBER("int"),
    FLT_NUMBER("flt"),
    STRING_VALUE("str"),
    UNKNOWN_TOKEN,
    END_OF_TEXT,
    INT_KEYWORD("int"),
    VOID_KEYWORD("void"),
    FLT_KEYWORD("flt"),
    FUN_KEYWORD("fun"),
    MATCH_KEYWORD("match"),
    STR_KEYWORD("str"),
    BOOL_KEYWORD("bool"),
    STRUCT_KEYWORD("struct"),
    TaggedUnion_KEYWORD("TaggedUnion"),
    CONST_KEYWORD("const"),
    GSCOPE_KEYWORD("gscope"),
    WHILE_KEYWORD("while"),
    IF_KEYWORD("if"),
    ELIF_KEYWORD("elif"),
    ELSE_KEYWORD("else"),
    AND_KEYWORD("and"),
    OR_KEYWORD("or"),
    NOT_KEYWORD("not"),
    RETURN_KEYWORD("return"),
    TRUE_KEYWORD("true"),
    FALSE_KEYWORD("false"),
    BREAK_KEYWORD("break"),
        NAME,
    PLUS_OP,
    MINUS_OP,
    MULTIPLE_OP,
    DIVIDE_OP,
    MODULO_OP,
    INCREMENT_OP,
    POST_INCREMENT_OP,
    POW_OP,
    LESS_OP,
    LESS_EQ_OP,
    MORE_OP,
    MORE_EQ_OP,
    EQ_OP,
    NOT_EQ_OP,
    ASSIGN_OP,
    RETURN_TYPE_OP,
    OPEN_SOFT_BRACKETS_OP,
    CLOSE_SOFT_BRACKETS_OP,
    OPEN_SHARP_BRACKETS_OP,
    CLOSE_SHARP_BRACKETS_OP,
    REFERENCE_OP,
    DOT_OP,
    COMA_OP,
    NEW_LINE,
    COMMENT;

    public final String label;

    private TokenType() {
        this.label = null;
    }

    private TokenType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }

    public String getType() {
        return switch (this) {
            case INT_NUMBER, INT_KEYWORD -> "int";
            case FLT_NUMBER, FLT_KEYWORD -> "flt";
            case STRING_VALUE, STR_KEYWORD -> "str";
            case VOID_KEYWORD -> "void";
            case TRUE_KEYWORD, FALSE_KEYWORD, BOOL_KEYWORD -> "bool";
            default -> null;
        };
    }

}
