package parser.visitators;

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
import parser.parsableObjects.expression.StructInit;

public interface IVisitor {

    void visit(TaggedUnion taggedUnion);

    void visit(Struct struct);

    void visit(VariableDeclaration variableDeclaration) throws InterperterException;

    void visit(VariableInit variableDeclaration) throws InterperterException;

    void visit(ConstVariableDeclaration constVariableDeclaration) throws InterperterException;

    void visit(FunctionDeclaration functionDeclaration);

    void visit(LiteralValue literalValue);

    void visit(DivideResult divideResult);
    void visit(ModuloResult moduloResult);
    void visit(MultiplyResult multiplyResult);
    void visit(PowResult powResult);
    void visit(SubtractResult powResult);
    void visit(ConstGlobalVariableDeclaration constGlobalVariableDeclaration);

    void visit(FunctionCall functionCall) throws InterperterException;

    void visit(ArithmeticResult arithmeticResult);

    void visit(VariableAssigment variableAssigment);
    

    void visit(StructCall structCall);

    void visit(StructInit structInit);

    void visit(PostIncrement postIncrement);

    void visit(Increment increment);

    void visit(Match match);

    void visit(MatchCase matchCases);

    void visit(ReturnBlock returnBlock);

    void visit(BreakBlock breakBlock);

    void visit(StructValueAssigment structValueAssigment);

    void visit(IfConditional ifCondition);

    void visit(LogicalExpression logicalExpression);

    void visit(OrExpression orExpression);

    void visit(AndExpression andExpression);

    void visit(WhileConditional whileConditional);

    void visit(ElIfConditional elIfConditional);

    void visit(ElseConditional elseConditional);

    void visit(printFunction printFunction) throws InterperterException;

    void visit(fltFunction fltFunction);

    void visit(intFunction intFunction);

    void visit(strFunction strFunction);

    void visit(VariableCall variableCall);
}
