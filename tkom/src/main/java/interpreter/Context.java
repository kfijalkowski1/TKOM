package interpreter;

import interpreter.embedded.Value;
import interpreter.embedded.ValueType;
import parser.parsableObjects.variables.Type;
import java.util.Vector;

public class Context {
    private int whileCounter = 0;
    private int breakCounter = 0;
    private boolean returnFlag = false;
    Vector<Scope> scopes = new Vector<>();

    public Context() {
        scopes.add(new Scope());
    }

    public int getWhileCounter() {
        return whileCounter;
    }

    public void incrementWhileCounter() {
        whileCounter++;
    }

    public void decrementWhileCounter() {
        whileCounter--;
    }

    public void decrementBreakCounter() {
        breakCounter--;
    }

    public void incrementBreakCounter() {
        whileCounter++;
    }

    public int getBreakCounter() {
        return breakCounter;
    }

    public void setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
    }

    public void addVariable(String name, Value value) {
        scopes.get(scopes.size() - 1).addVariable(name, value);
    }

    public void addVariable(String name, Type type, Object value, Boolean isConst) {
        scopes.get(scopes.size() - 1).addVariable(name, type, value, isConst);
    }

    public void addRefVariable(String name, Value value) {
        scopes.get(scopes.size() - 1).addRefVariable(name, value);
    }

    public void addVariable(String name, Object value, ValueType type) {
        scopes.get(scopes.size() - 1).addVariable(name, value, type);
    }

    public void addScope() {
        scopes.add(new Scope());
    }

    public void removeScope() {
        scopes.remove(scopes.size() - 1);
    }

    public boolean getReturnFlag() {
        return returnFlag;
    }
}
