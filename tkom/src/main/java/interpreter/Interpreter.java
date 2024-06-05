package interpreter;

import inputHandle.Position;
import interpreter.dynamic.*;
import interpreter.embedded.LastValue;
import interpreter.embedded.Value;
import interpreter.embedded.ValueType;
import interpreter.embedded.functions.*;
import interpreter.exceptions.InterperterException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import parser.parsableObjects.FunctionDeclaration;
import parser.parsableObjects.Program;
import parser.parsableObjects.Statement;
import parser.parsableObjects.conditional.*;
import parser.parsableObjects.expression.*;
import parser.parsableObjects.blocks.arithmeticStandalone.Increment;
import parser.parsableObjects.blocks.arithmeticStandalone.PostIncrement;
import parser.parsableObjects.blocks.BreakBlock;
import parser.parsableObjects.blocks.FunctionCall;
import parser.parsableObjects.blocks.ReturnBlock;
import parser.parsableObjects.expression.arithmatic.results.*;
import parser.parsableObjects.match.Match;
import parser.parsableObjects.match.MatchCase;
import parser.parsableObjects.structures.Struct;
import parser.parsableObjects.expression.StructCall;
import parser.parsableObjects.structures.StructValueAssigment;
import parser.parsableObjects.structures.Structure;
import parser.parsableObjects.structures.TaggedUnion;
import parser.parsableObjects.variables.*;
import parser.visitators.IVisitor;

import java.util.*;

import static interpreter.embedded.Embedded.getEmbeddedFunctions;
import static interpreter.embedded.Embedded.getEmbeddedTypes;
import static lekser.tokenBuilderUtils.BuildersUtils.MAX_INT;

public class Interpreter implements IVisitor {
    private static final Integer MAX_RECURSION_DEPTH = 500;
    Map<String, CallableFunction> functions = new HashMap<>();
    Map<String, AllTypes> variablesTypes = new HashMap<>();
    LastValue lastValue = new LastValue();
    Vector<Context> contextStack = new Vector<>();
    Vector<Pair<String, Position>> callStack = new Vector<>();
    private int functionCallCounter = 0;

    public Interpreter() {
        functions.putAll(getEmbeddedFunctions());
        variablesTypes.putAll(getEmbeddedTypes());
        Context globalContext = new Context();
        contextStack.add(globalContext);
    }

    public void interpret(Program program) throws InterperterException {
        for (Statement statement : program.getStatements()) {
            statement.accept(this);
        }

    }

    @Override
    public void visit(TaggedUnion taggedUnion) throws InterperterException {
        String name = taggedUnion.getName();
        TaggedUnionType taggedUnionType = new TaggedUnionType(new LinkedHashMap<>());
        setDynamicTypesFields(taggedUnion, name, taggedUnionType);

    }

    @Override
    public void visit(Struct struct) throws InterperterException {
        String name = struct.getName();
        DynamicType structType = new DynamicType(new LinkedHashMap<>());
        setDynamicTypesFields(struct, name, structType);
        functions.put(name, new structInitFunction(struct.getPosition(), name));
    }

    @Override
    public void visit(TaggedUnionInit taggedUnionInit) throws InterperterException {
        String taggedUnionTypeName = taggedUnionInit.getStructName();
        AllTypes type = variablesTypes.get(taggedUnionTypeName);
        if (!(type instanceof TaggedUnionType taggedUnionType))
            throw new InterperterException("Type " + taggedUnionTypeName + " does not exist", taggedUnionInit.getPosition());

        // get field name
        String field = taggedUnionInit.getParameters().get(0);
        if (taggedUnionInit.getParameters().size() != 1)
            throw new InterperterException("Incorrect number of values for tagged union " + taggedUnionTypeName, taggedUnionInit.getPosition());
        if (!taggedUnionType.getFields().containsKey(field))
            throw new InterperterException("Field " + field + " does not exist in tagged union " + taggedUnionTypeName, taggedUnionInit.getPosition());


        // get value
        taggedUnionInit.getInitValue().accept(this);
        Value initValues = lastValue.getSingleLastValue(taggedUnionInit.getPosition());

        // check if value is correct
        if (!Objects.equals(initValues.getTypeName(), taggedUnionType.getFields().get(field).getType()))
            throw new InterperterException("Incompatible types for tagged union " + taggedUnionTypeName, taggedUnionInit.getPosition());

        Value resultValue = new Value(new TaggedUnionValue(field, initValues.getValue()), taggedUnionInit.getStructName(), false, false);

        lastValue.addLastValue(resultValue);

    }

    @Override
    public void visit(structInitFunction structInitFunction) throws InterperterException {
        String structType = structInitFunction.getName();

        // get type
        AllTypes type = variablesTypes.get(structType);
        if (!(type instanceof DynamicType dynamicType)) // also checks for null
            throw new InterperterException("Type " + structType + " does not exist", getCallContextPos());

        // get values
        List<Value> initValues = lastValue.getLastValueList(getCallContextPos());


        // check if number of values is correct
        if (initValues.size() != dynamicType.getFields().size())
            throw new InterperterException("Incorrect number of values for struct " + structType, getCallContextPos());


        LinkedHashMap<String, Value> values = new LinkedHashMap<>();
        int i = 0;
        for (Map.Entry<String, ValueType> entry : dynamicType.getFields().entrySet()) {
            if (!Objects.equals(initValues.get(i).getTypeName(), entry.getValue().getType()))
                throw new InterperterException("Incompatible types for struct " + structType, getCallContextPos());
            values.put(entry.getKey(), initValues.get(i));
            i++;
        }
        Value resultValue = new Value(values, structType, false, false);

        lastValue.addLastValue(resultValue);
    }

    @Override
    public void visit(StructCall structCall) throws InterperterException { // TODO is also tagged union call
        Value structValue = getStructFieldValue(structCall);
        lastValue.addLastValue(structValue);
    }



    @Override
    public void visit(StructValueAssigment structValueAssigment) throws InterperterException {
        Value structValue = getStructFieldValue(structValueAssigment.getStructCall());

        // check assigned value
        structValueAssigment.getAssignedValue().accept(this);
        Value assignedValue = lastValue.getSingleLastValue(structValueAssigment.getPosition());
        structValue.setValue(assignedValue.getValue());
    }

    @Override
    public void visit(VariableDeclaration variableDeclaration) throws InterperterException {
        addVariable(variableDeclaration.getName(), variableDeclaration.getType(),
                null, false, variableDeclaration.getPosition());
    }

    @Override
    public void visit(VariableInit variableInit) throws InterperterException {
        addVariable(variableInit.getName(), variableInit.getType(), variableInit.getValue(), variableInit.isConst(), variableInit.getPosition());
    }

    @Override
    public void visit(ConstVariableDeclaration constVariableDeclaration) throws InterperterException {
        addVariable(constVariableDeclaration.getName(), constVariableDeclaration.getType(),
                null, constVariableDeclaration.isConst(), constVariableDeclaration.getPosition());
    }

    @Override
    public void visit(ConstGlobalVariableDeclaration constGlobalVariableDeclaration) throws InterperterException {
        addVariable(constGlobalVariableDeclaration.getName(), constGlobalVariableDeclaration.getType(),
                null, constGlobalVariableDeclaration.isConst(), constGlobalVariableDeclaration.getPosition());
    }

    @Override
    public void visit(LiteralValue literalValue) {
        lastValue.addLastValue(new interpreter.embedded.Value(literalValue.getValue(), literalValue.getType(), false, false));
    }

    @Override
    public void visit(FunctionDeclaration functionDeclaration) throws InterperterException {
        if (functions.containsKey(functionDeclaration.getName()))
            throw new InterperterException("Function " + functionDeclaration.getName() + " already exists",
                    functionDeclaration.getPosition());
        if (functionDeclaration.getBlocks().isEmpty())
            throw new InterperterException("Function " + functionDeclaration.getName() + " should have a body",
                    functionDeclaration.getPosition());
        functions.put(functionDeclaration.getName(), new DynamicFunction(functionDeclaration.getBlocks(),
                functionDeclaration.getParameters(), functionDeclaration.getReturnType()));
    }

    @Override
    public void visit(DivideResult divideResult) throws InterperterException {
        divideResult.getLeft().accept(this);
        divideResult.getRight().accept(this);
        Value rightValue = lastValue.getSingleLastValue(divideResult.getPosition());
        Value leftValue = lastValue.getSingleLastValue(divideResult.getPosition());

        if (!(rightValue.isInteger() || rightValue.isFloat())
                ||  (!Objects.equals(rightValue.getType(), leftValue.getType())))
            throw new InterperterException("Incompatible types for division, expected int or float", divideResult.getPosition());

        // check for division by zero
        if (rightValue.isInteger() && (Integer) rightValue.getValue() == 0 ||
                (rightValue.getType().equals("flt") && (Float) rightValue.getValue() == 0.0f))
            throw new InterperterException("Division by zero", divideResult.getPosition());

        // return int division
        if (rightValue.isInteger()) {
            lastValue.addLastValue(new Value(((Integer) leftValue.getValue()) / ( (Integer) rightValue.getValue()),
                    leftValue.getType(),
                    false,
                    false));
            return; }

        // return float division
        else if ("flt".equals(rightValue.getType())) {
            lastValue.addLastValue(new Value(((Float) leftValue.getValue()) / ((Float) rightValue.getValue()),
                    leftValue.getType(),
                    false,
                    false));
            return; }
        throw new InterperterException("Incompatible types for division", divideResult.getPosition());
    }


    @Override
    public void visit(ModuloResult moduloResult) throws InterperterException {
        moduloResult.getLeft().accept(this);
        moduloResult.getRight().accept(this);
        Value rightValue = lastValue.getSingleLastValue(moduloResult.getPosition());
        Value leftValue = lastValue.getSingleLastValue(moduloResult.getPosition());

        if (!(rightValue.isInteger() || rightValue.isInteger()))
            throw new InterperterException("Incompatible types for modulo, expected int", moduloResult.getPosition());

        // check for division by zero
        if ((Integer) rightValue.getValue() == 0)
            throw new InterperterException("Cant run modulo by zero", moduloResult.getPosition());

        // return value
        lastValue.addLastValue(new Value(((Integer) leftValue.getValue()) % ( (Integer) rightValue.getValue()),
                leftValue.getType(),
                false,
                false));

    }

    @Override
    public void visit(MultiplyResult multiplyResult) throws InterperterException {
        multiplyResult.getLeft().accept(this);
        multiplyResult.getRight().accept(this);
        Value rightValue = lastValue.getSingleLastValue(multiplyResult.getPosition());
        Value leftValue = lastValue.getSingleLastValue(multiplyResult.getPosition());

        if (leftValue.isString() && rightValue.isInteger()) { // create string multiplication
            lastValue.addLastValue(
                    new Value(
                            String.valueOf(leftValue.getValue())
                                    .repeat(Math.max(0, (Integer) rightValue.getValue())),
                            leftValue.getType(), false, false));
            return;
        }

        if (!(rightValue.isInteger() || rightValue.isFloat())
                ||  (!Objects.equals(rightValue.getType(), leftValue.getType())))
            throw new InterperterException("Incompatible types for multiplication, expected int or float", multiplyResult.getPosition());


        // return int multiplication
        if (rightValue.isInteger()) {
            lastValue.addLastValue(new Value(((Integer) leftValue.getValue()) * ( (Integer) rightValue.getValue()),
                    leftValue.getType(),
                    false,
                    false));
            return; }

        // return float multiplication
        else if (rightValue.isFloat()) {
            Float resVal = (Float) leftValue.getValue() * (Float) rightValue.getValue();
            checkResValue(resVal, multiplyResult.getPosition());
            lastValue.addLastValue(new Value(((Float) leftValue.getValue()) * ((Float) rightValue.getValue()),
                    leftValue.getType(),
                    false,
                    false));
            return; }
        throw new InterperterException("Incompatible types for multiplication", multiplyResult.getPosition());
    }



    @Override
    public void visit(PowResult powResult) throws InterperterException {
        powResult.getLeft().accept(this);
        powResult.getRight().accept(this);
        Value rightValue = lastValue.getSingleLastValue(powResult.getPosition());
        Value leftValue = lastValue.getSingleLastValue(powResult.getPosition());

        if (!(leftValue.isInteger() || leftValue.isFloat()) || !(rightValue.isInteger()))
            throw new InterperterException("Incompatible types for multiplication, " +
                    "expected int or float for base and int for pow", powResult.getPosition());

        // return int pow
        if (leftValue.isInteger()) {
            lastValue.addLastValue(new Value((int) (Math.pow((Integer) leftValue.getValue(), (Integer) rightValue.getValue())),
                    leftValue.getType(),
                    false,
                    false));}

        // return float pow
        else if (leftValue.isFloat()) {
            Float res = (float) Math.pow((Float) leftValue.getValue(), (Integer) rightValue.getValue());
            checkResValue(res, powResult.getPosition());
            lastValue.addLastValue(new Value(res,
                    leftValue.getType(),
                    false,
                    false));}

    }

    @Override
    public void visit(SubtractResult subtractResult) throws InterperterException {
        subtractResult.getLeft().accept(this);
        subtractResult.getRight().accept(this);
        Value rightValue = lastValue.getSingleLastValue(subtractResult.getPosition());
        Value leftValue = lastValue.getSingleLastValue(subtractResult.getPosition());

        if (!(rightValue.isInteger() || rightValue.isFloat())
                ||  (!Objects.equals(rightValue.getType(), leftValue.getType())))
            throw new InterperterException("Incompatible types for subtraction, expected int or float", subtractResult.getPosition());

        // return int subtraction
        if (rightValue.isInteger()) {
            lastValue.addLastValue(new Value(((Integer) leftValue.getValue()) - ( (Integer) rightValue.getValue()),
                    leftValue.getType(),
                    false,
                    false));}

        // return float subtraction
        else if (rightValue.isFloat()) {
            Float res = (Float) leftValue.getValue() - (Float) rightValue.getValue();
            checkResValue(res, subtractResult.getPosition());
            lastValue.addLastValue(new Value(res,
                    leftValue.getType(),
                    false,
                    false));}
    }

    @Override
    public void visit(AddResult addResult) throws InterperterException {
        addResult.getLeft().accept(this);
        addResult.getRight().accept(this);
        Value rightValue = lastValue.getSingleLastValue(addResult.getPosition());
        Value leftValue = lastValue.getSingleLastValue(addResult.getPosition());

        if (!(rightValue.isInteger() || rightValue.isFloat() || rightValue.isString())
                ||  (!Objects.equals(rightValue.getType(), leftValue.getType())))
            throw new InterperterException("Incompatible types for subtraction, expected int or float", addResult.getPosition());

        // return int sum
        if (rightValue.isInteger()) {
            lastValue.addLastValue(new Value(((Integer) leftValue.getValue()) + ( (Integer) rightValue.getValue()),
                    leftValue.getType(),
                    false,
                    false));}

        // return float sum
        else if (rightValue.isFloat()) {
            Float res = (Float) leftValue.getValue() + (Float) rightValue.getValue();
            checkResValue(res, addResult.getPosition());
            lastValue.addLastValue(new Value(res,
                    leftValue.getType(),
                    false,
                    false));}
        else if (rightValue.isString()) {
            lastValue.addLastValue(new Value((String) leftValue.getValue() + (String) rightValue.getValue(),
                    leftValue.getType(),
                    false,
                    false));}
    }



    @Override
    public void visit(PostIncrement postIncrement) throws InterperterException {
        Value variable = getVariable(postIncrement.getVariableName());
        if (variable == null)
            throw new InterperterException("Variable " + postIncrement.getVariableName() + " does not exist",
                    postIncrement.getPosition());
        if (variable.isConst())
            throw new InterperterException("Variable " + postIncrement.getVariableName() + " is const so it cannot be incremented",
                    postIncrement.getPosition());
        if (!variable.isInteger())
            throw new InterperterException("Variable " + postIncrement.getVariableName() + " is not an integer so it cannot be incremented",
                    postIncrement.getPosition());
        variable.setValue((Integer) variable.getValue() + 1);
    }

    @Override
    public void visit(Increment increment) throws InterperterException {
        String varName = increment.getVariable().getName();
        Value variable = getVariable(varName);
        if (variable == null)
            throw new InterperterException("Variable " + varName + " does not exist",
                    increment.getPosition());
        if (variable.isConst())
            throw new InterperterException("Variable " + varName + " is const so it cannot be incremented",
                    increment.getPosition());

        // get value of post increment
        increment.getValue().accept(this);
        Value postIncrementValue = lastValue.getSingleLastValue(increment.getPosition());

        if (!(postIncrementValue.isInteger() || postIncrementValue.isFloat()))
            throw new InterperterException("Post increment value is not an integer or float", increment.getPosition());

        // check if same value types
        if (!Objects.equals(variable.getType(), postIncrementValue.getType()))
            throw new InterperterException("Incompatible types for increment, should be the same", increment.getPosition());

        if (postIncrementValue.isInteger())
            variable.setValue((Integer) variable.getValue() + (Integer) postIncrementValue.getValue());
        else if (postIncrementValue.isFloat())
            variable.setValue((Float) variable.getValue() + (Float) postIncrementValue.getValue());
    }

    @Override
    public void visit(FunctionCall functionCall) throws InterperterException {
        if (!functions.containsKey(functionCall.getName())) {
            throw new InterperterException("Function " + functionCall.getName() + " does not exist", functionCall.getPosition());
        }
        List<Value> funcCallValues = new Vector<>();
        for (Statement arg : functionCall.getArgs()) {
            arg.accept(this);
            funcCallValues.add(lastValue.getSingleLastValue(functionCall.getPosition()));
        }

        Context functionContext = new Context();
        contextStack.add(functionContext);
        callStack.add(new ImmutablePair<>(functionCall.getName(), functionCall.getPosition()));

        lastValue.addLastValue(funcCallValues);
        functionCallCounter++;

        try {
            functions.get(functionCall.getName()).accept(this);
        } finally {
            functionCallCounter--;
            contextStack.remove(functionContext);
            callStack.remove(callStack.size() - 1);
        }
    }


    @Override
    public void visit(VariableAssigment variableAssigment) throws InterperterException {
        Value variable = getVariable(variableAssigment.getVariableName());
        // check if variable exists
        if (variable == null)
            throw new InterperterException("Variable " + variableAssigment.getVariableName() + " does not exist",
                    variableAssigment.getPosition());
        // check if variable is const
        if (variable.isConst() && !Objects.isNull(variable.getValue()))
            throw new InterperterException("Variable " + variableAssigment.getVariableName() + " is const",
                    variableAssigment.getPosition());

        // evalute variable value
        variableAssigment.getAssignedValue().accept(this);

        Value potencialValue = lastValue.getSingleLastValue(variableAssigment.getPosition());
        // check if types are compatible
        if (!Objects.equals(variable.getType(), potencialValue.getType()))
            throw new InterperterException("Incompatible types for assignment", variableAssigment.getPosition());

        // set value
        variable.setValue(potencialValue.getValue());

    }


    @Override
    public void visit(Match match) throws InterperterException {
        // example match statement
        // match s {
        //         Shape.cir(value) {
        //            return circleArea(value)
        //          }
        //          Shape.rec(value) {
        //             return rectangleArea(value)
        //           }
        //}

        Value variable = getVariable(match.getVariableName());
        if (variable == null)
            throw new InterperterException("Variable " + match.getVariableName() + " does not exist", match.getPosition());
        AllTypes type = variablesTypes.get(variable.getType());
        if (!(type instanceof TaggedUnionType taggedUnionType))
            throw new InterperterException("Type " + variable.getType() + " is not a tagged union", match.getPosition());
        TaggedUnionValue curValue = (TaggedUnionValue) variable.getValue();
        if (curValue == null)
            throw new InterperterException("Variable " + match.getVariableName() + " is null", match.getPosition());
        String taggedUnionName = variable.getType();
        String currentValueName = curValue.getCurrentField();
        for (MatchCase matchCase : match.getCases()) {
            if (!matchCase.getTaggedUnionName().equals(taggedUnionName)) {
                throw new InterperterException("Incompatible types for match, expected " + taggedUnionName, match.getPosition());
            }
            if (matchCase.getTaggedUnionCase().equals(currentValueName)) {
                lastValue.addLastValue(new Value(curValue.getValue(),
                        getTaggedUnionVariableType(taggedUnionName, currentValueName), false, false));
                matchCase.accept(this);
                return;
            }
        }

    }

    @Override
    public void visit(MatchCase matchCases) throws InterperterException {
        //match case example
        // Shape.cir(value) {
        //   return circleArea(value)
        // }
        // create new scope var with value of type Shape.cir -> execute blocks
        AllTypes varType = variablesTypes.get(matchCases.getTaggedUnionName());
        if(!(varType instanceof TaggedUnionType taggedUnionType))
            throw  new InterperterException("Type " + matchCases.getTaggedUnionName() + " does not exist", matchCases.getPosition());
        ValueType caseType = taggedUnionType.getFields().get(matchCases.getTaggedUnionCase());
        if (caseType == null)
            throw  new InterperterException("Type " + matchCases.getTaggedUnionName() + " does not exist", matchCases.getPosition());
        getCurrentContext().addScope();
        // check if variable exists
        if (variableExistsCurrentScope(matchCases.getVariableName()))
            throw new InterperterException("Variable " + matchCases.getVariableName() + " already exists", matchCases.getPosition());
        getCurrentContext().addVariable(matchCases.getVariableName(), lastValue.getSingleLastValue(matchCases.getPosition()));

        // execute blocks
        for (Statement block : matchCases.getBlocks()) {
            if (getCurrentContext().getReturnFlag())
                break;
            block.accept(this);
        }
        getCurrentContext().removeScope();

    }

    @Override
    public void visit(ReturnBlock returnBlock) throws InterperterException {
        returnBlock.getValue().accept(this);
        if (functionCallCounter < 1)
            throw new InterperterException("Return outside of function", returnBlock.getPosition());
        getCurrentContext().setReturnFlag(true);
    }

    @Override
    public void visit(BreakBlock breakBlock) throws InterperterException {
        getCurrentContext().incrementBreakCounter();
        if (!(getCurrentContext().getWhileCounter() > 0))
            throw new InterperterException("Break outside of loop", getCallContextPos());
    }


    @Override
    public void visit(IfConditional ifCondition) throws InterperterException {
        ifCondition.getCondition().accept(this);
        Value conditionValue = lastValue.getSingleLastValue(ifCondition.getPosition());
        if (!conditionValue.isBoolean()) {
            throw new InterperterException("Expected boolean in if condition", ifCondition.getPosition());
        }
        if ((Boolean) conditionValue.getValue()) {
            for (Statement block : ifCondition.getBlock()) {
                if (getCurrentContext().getReturnFlag()) {
                    return;
                }
                block.accept(this);
            }
        } else {
            for (Conditional ifElseOrElseBlock : ifCondition.getSubConditions()) {
                if (ifElseOrElseBlock instanceof ElseConditional elseConditional) {
                    elseConditional.accept(this);
                    return;
                } else if (ifElseOrElseBlock instanceof ElIfConditional elIfConditional) {
                    elIfConditional.getCondition().accept(this);
                    Value conditionVal = lastValue.getSingleLastValue(ifCondition.getPosition());
                    if (!conditionVal.isBoolean()) {
                        throw new InterperterException("Expected boolean in if condition", ifCondition.getPosition());
                    }
                    if ((Boolean) conditionVal.getValue()) {
                        elIfConditional.accept(this);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void visit(ElIfConditional elIfConditional) throws InterperterException {
        for (Statement block : elIfConditional.getBlock()) {
            if (getCurrentContext().getReturnFlag()) {
                return;
            }
            block.accept(this);
        }
    }

    @Override
    public void visit(ElseConditional elseConditional) throws InterperterException {
        for (Statement block : elseConditional.getBlock()) {
            if (getCurrentContext().getReturnFlag()) {
                return;
            }
            block.accept(this);
        }
    }

    @Override
    public void visit(LogicalExpression logicalExpression) throws InterperterException {
        logicalExpression.getLeft().accept(this);
        logicalExpression.getRight().accept(this);
        Value rightValue = lastValue.getSingleLastValue(logicalExpression.getPosition());
        Value leftValue = lastValue.getSingleLastValue(logicalExpression.getPosition());

        if ((!(leftValue.isFloat() || leftValue.isInteger())) || !Objects.equals(leftValue.getType(), rightValue.getType()))
            throw new InterperterException("Incompatible types for comparison", logicalExpression.getPosition());
        switch (logicalExpression.getTester()){
            case MORE_OP -> {
                if (leftValue.isInteger()) {
                    lastValue.addLastValue(new Value((Integer) leftValue.getValue() > (Integer) rightValue.getValue(),
                            "bool", false, false));
                } else {
                    lastValue.addLastValue(new Value((Float) leftValue.getValue() > (Float) rightValue.getValue(),
                            "bool", false, false));
                }
            }
            case LESS_OP -> {
                if (leftValue.isInteger()) {
                    lastValue.addLastValue(new Value((Integer) leftValue.getValue() < (Integer) rightValue.getValue(),
                            "bool", false, false));
                } else {
                    lastValue.addLastValue(new Value((Float) leftValue.getValue() < (Float) rightValue.getValue(),
                            "bool", false, false));
                }
            }
            case MORE_EQ_OP -> {
                if (leftValue.isInteger()) {
                    lastValue.addLastValue(new Value((Integer) leftValue.getValue() >= (Integer) rightValue.getValue(),
                            "bool", false, false));
                } else {
                    lastValue.addLastValue(new Value((Float) leftValue.getValue() >= (Float) rightValue.getValue(),
                            "bool", false, false));
                }
            }
            case LESS_EQ_OP -> {
                if (leftValue.isInteger()) {
                    lastValue.addLastValue(new Value((Integer) leftValue.getValue() <= (Integer) rightValue.getValue(),
                            "bool", false, false));
                } else {
                    lastValue.addLastValue(new Value((Float) leftValue.getValue() <= (Float) rightValue.getValue(),
                            "bool", false, false));
                }
            }
            case EQ_OP -> {
                if (leftValue.isInteger()) {
                    lastValue.addLastValue(new Value(Objects.equals((Integer) leftValue.getValue(), (Integer) rightValue.getValue()),
                            "bool", false, false));
                } else {
                    lastValue.addLastValue(new Value(Objects.equals((Float) leftValue.getValue(), (Float) rightValue.getValue()),
                            "bool", false, false));
                }
            }
            case NOT_EQ_OP -> {
                if (leftValue.isInteger()) {
                    lastValue.addLastValue(new Value(!Objects.equals((Integer) leftValue.getValue(), (Integer) rightValue.getValue()),
                            "bool", false, false));
                } else {
                    lastValue.addLastValue(new Value(!Objects.equals((Float) leftValue.getValue(), (Float) rightValue.getValue()),
                            "bool", false, false));
                }
            }
        }

    }


    @Override
    public void visit(OrExpression orExpression) throws InterperterException {
        orExpression.getLeft().accept(this);
        orExpression.getRight().accept(this);
        Value rightValue = lastValue.getSingleLastValue(orExpression.getPosition());
        Value leftValue = lastValue.getSingleLastValue(orExpression.getPosition());
        if (!leftValue.isBoolean() || !rightValue.isBoolean())
            throw new InterperterException("Expected boolean in or expression", getCallContextPos());
        lastValue.addLastValue(new Value((Boolean) leftValue.getValue() || (Boolean) rightValue.getValue(),
                "bool", false, false));
    }

    @Override
    public void visit(AndExpression andExpression) throws InterperterException {
        andExpression.getLeft().accept(this);
        andExpression.getRight().accept(this);
        Value rightValue = lastValue.getSingleLastValue(andExpression.getPosition());
        Value leftValue = lastValue.getSingleLastValue(andExpression.getPosition());
        if (!leftValue.isBoolean() || !rightValue.isBoolean())
            throw new InterperterException("Expected boolean in or expression", getCallContextPos());
        lastValue.addLastValue(new Value((Boolean) leftValue.getValue() && (Boolean) rightValue.getValue(),
                "bool", false, false));
    }

    @Override
    public void visit(WhileConditional whileConditional) throws InterperterException {
        getCurrentContext().incrementWhileCounter();
        whileConditional.getCondition().accept(this);
        Value conditionValue = lastValue.getSingleLastValue(whileConditional.getPosition());
        if (!conditionValue.isBoolean())
            throw new InterperterException("Expected boolean in while condition", getCallContextPos());
        while ((Boolean) conditionValue.getValue()) {
            for (Statement block : whileConditional.getBlock()) {
                if (getCurrentContext().getReturnFlag() || getCurrentContext().getBreakCounter() > 0) {
                    getCurrentContext().decrementWhileCounter();
                    getCurrentContext().decrementBreakCounter();
                    return;
                }
                block.accept(this);
            }
            whileConditional.getCondition().accept(this);
            conditionValue = lastValue.getSingleLastValue(whileConditional.getPosition());
            if (!conditionValue.isBoolean())
                throw new InterperterException("Expected boolean in while condition", getCallContextPos());
        }
        getCurrentContext().decrementWhileCounter();
    }

    @Override
    public void visit(printFunction printFunction) throws InterperterException {
        List<Value> lastValue = this.lastValue.getLastValueList(getCallContextPos());
        if (lastValue.size() != 1) {
            throw new InterperterException("Expected a single value in print call", getCallContextPos());
        }
        System.out.println(lastValue.get(0).getValue());
    }


    @Override
    public void visit(fltFunction fltFunction) throws InterperterException {
        List<Value> localLastValue = this.lastValue.getLastValueList(getCallContextPos());
        if (localLastValue.size() != 1) {
            throw new InterperterException("Expected a single value in float cast call", getCallContextPos());
        }
        if (!localLastValue.get(0).isInteger())
            throw new InterperterException("Expected integer in float cast call", getCallContextPos());
        lastValue.addLastValue(new Value((float) (Integer) localLastValue.get(0).getValue(), "flt", false, false));
    }

    @Override
    public void visit(intFunction intFunction) throws InterperterException {
        List<Value> localLastValue = this.lastValue.getLastValueList(getCallContextPos());
        if (localLastValue.size() != 1) {
            throw new InterperterException("Expected a single value in int cast call", getCallContextPos());
        }

        if (localLastValue.get(0).isFloat()) {
            lastValue.addLastValue(new Value((int) (float) localLastValue.get(0).getValue(), "int", false, false));
            return;
        }
        else if (localLastValue.get(0).isString()) {
            try {
                lastValue.addLastValue(new Value(Integer.parseInt(String.valueOf(localLastValue.get(0).getValue())), "int", false, false));
                return;
            } catch (NumberFormatException e) {
                throw new InterperterException("Expected integer in string to int cast call", getCallContextPos());
            }
        }
        throw new InterperterException("Expected float or string in int cast call", getCallContextPos());
    }

    @Override
    public void visit(strFunction strFunction) throws InterperterException {
        List<Value> localLastValue = this.lastValue.getLastValueList(getCallContextPos());
        if (localLastValue.size() != 1) {
            throw new InterperterException("Expected a single value in string cast call", getCallContextPos());
        }
        if (!(localLastValue.get(0).isInteger() || localLastValue.get(0).isFloat() || localLastValue.get(0).isBoolean()))
            throw new InterperterException("Expected integer, float or bool in string cast call", getCallContextPos());
        lastValue.addLastValue(new Value(String.valueOf(localLastValue.get(0).getValue()), "str", false, false));
    }

    @Override
    public void visit(VariableCall variableCall) throws InterperterException {
        // check if tagged union
        Value variable = getVariable(variableCall.getName());
        if (variable == null)
            throw new InterperterException("Variable " + variableCall.getName() + " does not exist", variableCall.getPosition());
//        if (variable.getValue() instanceof TaggedUnionValue taggedUnionValue) {
//            lastValue.addLastValue(new Value(taggedUnionValue.getValue(),
//                    getTaggedUnionVariableType(variable.getTypeName(), taggedUnionValue.getCurrentField()), variableCall.getReference(), false));
//            return;
//        }
        variable.setReference(variableCall.getReference());
        lastValue.addLastValue(variable);

    }

    private String getTaggedUnionVariableType(String taggedUnionTypeName, String tuParameter) {
        AllTypes type = variablesTypes.get(taggedUnionTypeName);
        if (!(type instanceof TaggedUnionType taggedUnionType))
            return null;
        return taggedUnionType.getFields().get(tuParameter).getType();
    }

    @Override
    public void visit(NegatedExpression negatedExpression) throws InterperterException {
        negatedExpression.getExpression().accept(this);
        Value value = lastValue.getSingleLastValue(negatedExpression.getPosition());
        if (!value.isBoolean())
            throw new InterperterException("Expected boolean in negated expression", getCallContextPos());
        lastValue.addLastValue(new Value(!(Boolean) value.getValue(), "bool", false, false));
    }

    @Override
    public void visit(NegativeExpression negativeExpression) throws InterperterException {
        negativeExpression.getExpression().accept(this);
        Value value = lastValue.getSingleLastValue(negativeExpression.getPosition());
        if (value.isInteger()) {
            lastValue.addLastValue(new Value(-(Integer) value.getValue(), "int", false, false));
            return;
        } else if (value.isFloat()) {
            lastValue.addLastValue(new Value(-(Float) value.getValue(), "flt", false, false));
            return;
        }
        throw new InterperterException("Expected integer or float in negative expression", getCallContextPos());
    }

    @Override
    public void visit(DynamicFunction dynamicFunction) throws InterperterException {

        if (countStringOccurrences(callStack.get(callStack.size() - 1).getLeft()) > MAX_RECURSION_DEPTH)
            throw new InterperterException("Max recursion of 500 reached", getCallContextPos());

        String returnType = dynamicFunction.getReturnType();
        List<Value> parameters = lastValue.getLastValueList(getCallContextPos());

        // check if number of parameters is correct
        if (parameters.size() != dynamicFunction.getParameters().size())
            throw new InterperterException("Incorrect number of parameters", getCallContextPos());

        // add parameters to context
        for (int i = 0; i < parameters.size(); i++) {
            // check type
            if (!Objects.equals(parameters.get(i).getTypeName(), dynamicFunction.getParameters().get(i).getType().getName())
                || !Objects.equals(parameters.get(i).isReference(), dynamicFunction.getParameters().get(i).getType().isReference()))
                throw new InterperterException("Incompatible types for parameter " + dynamicFunction.getParameters().get(i).getName(),
                        getCallContextPos());
            // check if reference then do not copy value
            if (parameters.get(i).isReference())
                getCurrentContext().addRefVariable(dynamicFunction.getParameters().get(i).getName(), parameters.get(i));
            else
                getCurrentContext().addVariable(dynamicFunction.getParameters().get(i).getName(),
                    dynamicFunction.getParameters().get(i).getType(), parameters.get(i).getValue(), false);
        }

        for (Statement block : dynamicFunction.getBlocks()) {
            if (getCurrentContext().getReturnFlag()) {
                checkReturnValue(returnType);
                return;
            }
            block.accept(this);
        }
    }

    private void checkReturnValue(String returnType) throws InterperterException {
        Value returnValue;
        if (!Objects.equals(returnType, "void")) {
            returnValue = lastValue.getSingleLastValue(getCallContextPos());
            if (returnValue == null)
                throw new InterperterException("Function does not return a value", getCallContextPos());
            if (!Objects.equals(returnValue.getTypeName(), returnType))
                throw new InterperterException("Incompatible return type", getCallContextPos());
            lastValue.addLastValue(returnValue);
        } else {
            throw new InterperterException("Function should not return a value", getCallContextPos());
        }
    }


    // TODO move to context-utils

    public Integer countStringOccurrences(String name) {
        int counter = 0;

        for (Pair<String, Position> pair : callStack) {
            if (pair.getLeft().equals(name))
                counter++;
        }

        return counter;
    }

    private Value getStructFieldValue(StructCall structCall) throws InterperterException {
        List<String> fields = structCall.getParameters();
        String structName = structCall.getStructName();


        Value var = getVariable(structName);
        if (var == null)
            throw new InterperterException("Variable " + structName + " does not exist", structCall.getPosition());
        if (!(var.getValue() instanceof LinkedHashMap<?, ?> structValues))
            throw new InterperterException("Variable " + structName + " is not a struct", structCall.getPosition());

        if (fields.size() > 1) {
            for (String field : fields.subList(0, fields.size() - 1)) {
                if (!structValues.containsKey(field))
                    throw new InterperterException("Struct " + structName + " does not have field " + field, structCall.getPosition());
                var = (Value) structValues.get(field);
                if (!(var.getValue() instanceof LinkedHashMap<?, ?>))
                    throw new InterperterException("Field " + field + " is not a struct", structCall.getPosition());
                structValues = (LinkedHashMap<String, Value>) var.getValue();
            } }

        Value resultValue = (Value) structValues.get(fields.get(fields.size() - 1));
        if (Objects.isNull(resultValue))
            throw new InterperterException("Field " + fields.get(fields.size() - 1) + " does not exist", structCall.getPosition());

        return resultValue;
    }


    private Context getCurrentContext() {
        return contextStack.get(contextStack.size() - 1);
    }

    private boolean variableExists(String name) {
        List<Context> localGlobalContext = List.of(contextStack.get(contextStack.size() - 1), contextStack.get(0)); // current context and global context
        for (Context currentContext : localGlobalContext) {
            for (Scope scope : currentContext.scopes) {
                if (scope.variables.containsKey(name))
                    return true;
            }
        }

        return false;
    }

    private boolean variableExistsCurrentScope(String name) {
        List<Scope> currentScopes =  contextStack.get(contextStack.size() - 1).scopes;
        return currentScopes.get(currentScopes.size() - 1).variables.containsKey(name);
    }

    Value getVariable(String name) {
        List<Context> localGlobalContext = List.of(contextStack.get(contextStack.size() - 1), contextStack.get(0)); // current context and global context
        for (Context currentContext : localGlobalContext) {
            ListIterator<Scope> iterator = currentContext.scopes.listIterator(currentContext.scopes.size());
            while (iterator.hasPrevious()) {
                Scope scope = iterator.previous();
                if (scope.variables.containsKey(name))
                    return scope.variables.get(name);
            }
        }
        return null;
    }


    private void addVariable(String name, Type type, Expression value, Boolean isConst, Position pos) throws InterperterException {
        // check type
        if (!variablesTypes.containsKey(type.getName()))
            throw new InterperterException("Type " + type.getName() + " does not exist", pos);
        // check name in current scope
        if (variableExistsCurrentScope(name))
            throw new InterperterException("Variable " + name + " already exists", pos);

        // assign value
        Value lastValue = null;
        Context currentContext = contextStack.get(contextStack.size() - 1);

        if (value != null) {
            // get value
            value.accept(this);
            lastValue = this.lastValue.getSingleLastValue(pos);

            // check if value is correct
            if (!type.getName().equals(lastValue.getTypeName())) {
                throw new InterperterException("Incompatible types for variable " + name + ", expected " + type.getName(), pos);
            }
            currentContext.addVariable(name, type, lastValue.getValue(), isConst);
        } else {
            currentContext.addVariable(name, type, null, isConst);
        }

    }

    private Position getCallContextPos() {
        return callStack.get(callStack.size() - 1).getRight();
    }


    private boolean typeExists(String name) {
        return variablesTypes.containsKey(name);
    }

    private void setDynamicTypesFields(Structure structOrUnion, String name,
                                       DynamicType dynamicType) throws InterperterException {
        if (typeExists(name))
            throw new InterperterException("Type " + name + " already exists", structOrUnion.getPosition());
        if (functions.containsKey(name))
            throw new InterperterException("Type " + name + " cannot have the same name as a function", structOrUnion.getPosition());
        for (ConstVariableDeclaration varDec : structOrUnion.getVariablesDecl()) {
            if (varDec.getType().getName().equals(name))
                throw new InterperterException("Type " + name + " cannot contain itself", structOrUnion.getPosition());
            if (!typeExists(varDec.getType().getName()))
                throw new InterperterException("Type " + varDec.getType() +
                        " does not exist", structOrUnion.getPosition());
            dynamicType.addField(varDec.getName(),
                    new ValueType(varDec.getType().getName(), false, varDec.isConst()));
        }
        variablesTypes.put(name, dynamicType);
    }

    private void checkResValue(Float resVal, Position position) throws InterperterException {
        if (Math.abs(resVal) > MAX_INT)
            throw new InterperterException("Integer overflow", position);
    }
}
