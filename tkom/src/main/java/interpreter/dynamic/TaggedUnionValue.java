package interpreter.dynamic;

import org.apache.commons.lang3.tuple.Pair;

public class TaggedUnionValue {
        Pair<String, Object> value;

        public TaggedUnionValue(String currentField, Object value) {
            this.value = Pair.of(currentField, value);
        }

        public String getCurrentField() {
            return value.getLeft();
        }

        public Object getValue() {
            return value.getRight();
        }

}
