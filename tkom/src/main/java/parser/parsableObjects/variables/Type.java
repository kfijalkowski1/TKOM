package parser.parsableObjects.variables;

public class Type {
    private String type;
    private Boolean isReference;

    public Type(String name) {
        this.type = name;
        this.isReference = false;
    }

    public Type(String name, Boolean isReference) {
        this.type = name;
        this.isReference = isReference;
    }

    public Boolean isReference() {
        return isReference;
    }

    public String getName() {
        return type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Type)) return false;
        Type that = (Type) o;
        return type.equals(that.type) && isReference.equals(that.isReference);
    }

    @Override
    public int hashCode() {
        return type.hashCode() + isReference.hashCode();
    }
}
