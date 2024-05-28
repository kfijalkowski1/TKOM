package parser.subParsers;

import exceptions.AnalizerException;
import lekser.tokenBuilderUtils.ThrowingFunction;
import parser.Parser;
import parser.parsableObjects.Statement;
import parser.parsableObjects.blocks.Block;
import parser.subParsers.condition.conditionalParser;
import parser.subParsers.variableParsers.VariableInitParser;

import java.util.Arrays;
import java.util.List;

public class BlockParser {
    /**
     * EBNF: block   = conditional
     *               | variable_init
     *               | name_block
     *               | match_block
     *               | break_block
     *               | return_block;
     *
     */
    public static Statement parseBlock(Parser par) throws AnalizerException {
        List<ThrowingFunction<Parser, Statement, AnalizerException>> expressionBuilders = getBlockBuilders();
        Statement result;
        for (ThrowingFunction<Parser, Statement, AnalizerException> builder : expressionBuilders) {
            result = builder.apply(par);
            if (result != null) {
                return result;
            }
        }
        return null;
    }


    private static List<ThrowingFunction<Parser, Statement, AnalizerException>> getBlockBuilders() {
        return Arrays.asList(
                conditionalParser::parseCondition,
                VariableInitParser::parseVariableInit,
                NameBlockParser::parseNameBlock,
                matchParser::parseMatch,
                ExitBlock::parseExitBlock


        );
    }
}
