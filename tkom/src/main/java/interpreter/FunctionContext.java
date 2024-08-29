package interpreter;

public class FunctionContext extends Context {
    private final String returnType;




    public FunctionContext(String returnType) {
        super();
        this.returnType = returnType;
    }

    public String getReturnType() {
        return returnType;
    }

}
