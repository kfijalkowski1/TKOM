package parser.visitators;

import parser.parsableObjects.*;
import parser.parsableObjects.arithmatic.Increment;
import parser.parsableObjects.arithmatic.PostIncrement;
import parser.parsableObjects.conditional.ElIfConditional;
import parser.parsableObjects.conditional.ElseConditional;
import parser.parsableObjects.conditional.IfConditional;
import parser.parsableObjects.conditional.WhileConditional;
import parser.parsableObjects.conditional.condition.*;
import parser.parsableObjects.arithmatic.results.ArithmeticResult;
import parser.parsableObjects.expresions.BreakExpression;
import parser.parsableObjects.expresions.ReturnExpression;
import parser.parsableObjects.match.Match;
import parser.parsableObjects.match.MatchCase;
import parser.parsableObjects.structures.StructCall;
import parser.parsableObjects.structures.StructValueAssigment;
import parser.parsableObjects.variables.*;
import parser.parsableObjects.expresions.FunctionCall;
import parser.parsableObjects.structures.Struct;
import parser.parsableObjects.structures.TaggedUnion;
import parser.parsableObjects.variables.StructInit;

public interface IVisitator {

    void visit(TaggedUnion taggedUnion);
    void visit(Struct struct);

    void visit(VariableDeclaration variableDeclaration);
    void visit(ConstVariableDeclaration constVariableDeclaration);

    void visit(FunctionDeclaration functionDeclararion);

    void visit(Value value);

    void visit(FunctionCall functionCall);

    void visit(ArithmeticResult arithmeticResult);

    void visit(VariableAssigment variableAssigment);

    void visit(Variable variable);

    void visit(StructCall structCall);

    void visit(StructInit structInit);

    void visit(PostIncrement postIncrement);

    void visit(Increment increment);

    void visit(Match match);

    void visit(MatchCase matchCases);

    void visit(ReturnExpression returnExpression);

    void visit(BreakExpression breakExpression);

    void visit(StructValueAssigment structValueAssigment);

    void visit(IfConditional ifCondition);

    void visit(TestCondition testCondition);

    void visit(ValueCondition valueCondition);

    void visit(OrCondition orCondition);

    void visit(AndCondition andCondition);

    void visit(WhileConditional whileConditional);

    void visit(ElIfConditional elIfConditional);

    void visit(ElseConditional elseConditional);
}
