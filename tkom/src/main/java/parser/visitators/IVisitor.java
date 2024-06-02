package parser.visitators;

import interpreter.dynamic.DynamicFunction;
import interpreter.embedded.functions.*;
import interpreter.exceptions.InterperterException;
import parser.parsableObjects.*;
import parser.parsableObjects.expression.*;
import parser.parsableObjects.blocks.arithmeticStandalone.Increment;
import parser.parsableObjects.blocks.arithmeticStandalone.PostIncrement;
import parser.parsableObjects.conditional.ElIfConditional;
import parser.parsableObjects.conditional.ElseConditional;
import parser.parsableObjects.conditional.IfConditional;
import parser.parsableObjects.conditional.WhileConditional;
import parser.parsableObjects.blocks.BreakBlock;
import parser.parsableObjects.blocks.ReturnBlock;
import parser.parsableObjects.expression.arithmatic.results.*;
import parser.parsableObjects.match.Match;
import parser.parsableObjects.match.MatchCase;
import parser.parsableObjects.expression.StructCall;
import parser.parsableObjects.structures.StructValueAssigment;
import parser.parsableObjects.variables.*;
import parser.parsableObjects.blocks.FunctionCall;
import parser.parsableObjects.structures.Struct;
import parser.parsableObjects.structures.TaggedUnion;
import parser.parsableObjects.expression.TaggedUnionInit;

public interface IVisitor {

    void visit(TaggedUnion taggedUnion) throws InterperterException;

    void visit(Struct struct) throws InterperterException;

    void visit(VariableDeclaration variableDeclaration) throws InterperterException;

    void visit(VariableInit variableDeclaration) throws InterperterException;

    void visit(ConstVariableDeclaration constVariableDeclaration) throws InterperterException;

    void visit(FunctionDeclaration functionDeclaration) throws InterperterException;

    void visit(LiteralValue literalValue);

    void visit(DivideResult divideResult) throws InterperterException;
    void visit(ModuloResult moduloResult) throws InterperterException;
    void visit(MultiplyResult multiplyResult) throws InterperterException;
    void visit(PowResult powResult) throws InterperterException;
    void visit(SubtractResult powResult) throws InterperterException;
    void visit(ConstGlobalVariableDeclaration constGlobalVariableDeclaration) throws InterperterException;

    void visit(FunctionCall functionCall) throws InterperterException;

    void visit(VariableAssigment variableAssigment) throws InterperterException;
    

    void visit(StructCall structCall) throws InterperterException;

    void visit(TaggedUnionInit taggedUnionInit) throws InterperterException;

    void visit(PostIncrement postIncrement) throws InterperterException;

    void visit(Increment increment) throws InterperterException;

    void visit(Match match) throws InterperterException;

    void visit(MatchCase matchCases) throws InterperterException;

    void visit(ReturnBlock returnBlock) throws InterperterException;

    void visit(BreakBlock breakBlock) throws InterperterException;

    void visit(StructValueAssigment structValueAssigment) throws InterperterException;

    void visit(IfConditional ifCondition) throws InterperterException;

    void visit(LogicalExpression logicalExpression) throws InterperterException;

    void visit(OrExpression orExpression) throws InterperterException;

    void visit(AndExpression andExpression) throws InterperterException;

    void visit(WhileConditional whileConditional) throws InterperterException;

    void visit(ElIfConditional elIfConditional) throws InterperterException;

    void visit(ElseConditional elseConditional) throws InterperterException;

    void visit(printFunction printFunction) throws InterperterException;

    void visit(fltFunction fltFunction) throws InterperterException;

    void visit(intFunction intFunction) throws InterperterException;

    void visit(strFunction strFunction) throws InterperterException;

    void visit(VariableCall variableCall) throws InterperterException;

    void visit(NegatedExpression negatedExpression) throws InterperterException;

    void visit(NegativeExpression negativeExpression) throws InterperterException;

    void visit(DynamicFunction dynamicFunction) throws InterperterException;

    void visit(AddResult addResult) throws InterperterException;

    void visit(structInitFunction structInitFunction) throws InterperterException;
}
