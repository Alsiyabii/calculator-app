package com.alsiyabii.calculatorapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Provides static methods for parsing, converting, and evaluating mathematical expressions.
 * <p>
 * This class supports:
 * <ul>
 *     <li>Tokenizing infix expressions with support for negative numbers and decimals.</li>
 *     <li>Converting infix expressions to postfix notation using operator precedence.</li>
 *     <li>Evaluating postfix expressions and handling arithmetic exceptions.</li>
 *     <li>Flipping the sign of the last number in an expression.</li>
 * </ul>
 */
public class ExpressionParser {

    /**
     * Returns the precedence of an arithmetic operator.
     *
     * @param operator the operator character ('+', '-', '*', '/')
     * @return an integer representing operator precedence (higher number = higher precedence)
     */
    private static int getPrecedence(char operator) {
        switch (operator) {
            case '*':
            case '/':
                return 2;
            case '+':
            case '-':
                return 1;
            default:
                return 0;
        }
    }

    /**
     * Tokenizes an infix expression into numbers and operators.
     * <p>
     * Supports multi-digit numbers, decimal numbers, and negative numbers.
     *
     * @param infix the infix expression string
     * @return a list of tokens as strings
     * @throws IllegalArgumentException if the expression contains consecutive operators,
     *                                  invalid number formats, or exceeds 70 characters
     */
    public static List<String> tokenize(String infix) {
        List<String> tokens = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        String trimmed = infix.trim();
        char prevChar = 0;

        if (trimmed.length() > 70)
            throw new IllegalArgumentException("Expression too long");

        for (int i = 0; i < trimmed.length(); i++) {
            char currentChar = trimmed.charAt(i);

            // Handle negative numbers
            if (currentChar == '-' && (i == 0 || "+-*/".indexOf(prevChar) != -1)) {
                buffer.append(currentChar);

            } else if ("+-*/".indexOf(currentChar) != -1) {
                // Check for consecutive operators
                if (prevChar != 0 && "+-*/".indexOf(prevChar) != -1) {
                    throw new IllegalArgumentException(
                            "Consecutive operators are not allowed: (" + prevChar + currentChar + ")"
                    );
                }

                if (!buffer.isEmpty()) {
                    tokens.add(buffer.toString());
                    buffer.setLength(0);
                }
                tokens.add(String.valueOf(currentChar));

            } else if (Character.isDigit(currentChar) || currentChar == '.') {
                if (currentChar == '.' && buffer.indexOf(".") != -1) {
                    throw new IllegalArgumentException(
                            "Invalid number format: multiple decimal points in one number"
                    );
                }
                buffer.append(currentChar);
            }

            prevChar = currentChar;
        }

        if (!buffer.isEmpty()) {
            String token = buffer.toString();
            if (token.endsWith(".")) {
                throw new IllegalArgumentException(
                        "Invalid number format: number cannot end with a decimal point"
                );
            }
            tokens.add(token);
        }

        return tokens;
    }

    /**
     * Converts an infix expression (list of tokens) to postfix notation.
     *
     * @param tokens a list of infix tokens
     * @return a list of tokens in postfix order
     */
    public static List<String> convert(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (String token : tokens) {
            switch (token) {
                case "+", "-", "*", "/" -> {
                    while (!stack.isEmpty() &&
                            getPrecedence(stack.peek().charAt(0)) >= getPrecedence(token.charAt(0))) {
                        output.add(stack.pop());
                    }
                    stack.push(token);
                }
                default -> output.add(token);
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }

        return output;
    }

    /**
     * Evaluates a postfix expression.
     *
     * @param tokens a list of tokens in postfix order
     * @return the calculated result as a double
     * @throws IllegalArgumentException if there are not enough operands for an operator
     * @throws ArithmeticException      if division by zero occurs or the result is invalid
     */
    public static double calculate(List<String> tokens) {
        Stack<Double> stack = new Stack<>();

        for (String token : tokens) {
            double result;
            switch (token) {
                case "+", "-", "*", "/" -> {
                    if (stack.size() < 2) {
                        throw new IllegalArgumentException(
                                "Invalid postfix expression, not enough operands for " + token
                        );
                    }
                    double operand1 = stack.pop();
                    double operand2 = stack.pop();

                    result = switch (token) {
                        case "+" -> operand2 + operand1;
                        case "-" -> operand2 - operand1;
                        case "*" -> operand2 * operand1;
                        default -> {
                            if (operand1 == 0) throw new ArithmeticException("Cannot divide by Zero");
                            yield operand2 / operand1;
                        }
                    };

                    if (Double.isInfinite(result)) throw new ArithmeticException("Overflow");
                    if (Double.isNaN(result)) throw new ArithmeticException("Invalid result");

                    stack.push(result);
                }
                default -> stack.push(Double.parseDouble(token));
            }
        }

        return stack.pop();
    }

    /**
     * Flips the sign of the last numeric token in a list of tokens.
     *
     * @param tokens a list of tokens
     * @return a string representing the expression after flipping the last number's sign
     * @throws IllegalArgumentException if the last token is an operator
     */
    public static String flipSign(List<String> tokens) {
        String last = tokens.get(tokens.size() - 1);

        if (last.equals("+") || last.equals("-") || last.equals("*") || last.equals("/")) {
            throw new IllegalArgumentException("Cannot flip the sign of an operator");
        }

        last = last.startsWith("-") ? last.substring(1) : "-" + last;
        tokens.set(tokens.size() - 1, last);

        StringBuilder result = new StringBuilder();
        for (String token : tokens) result.append(token);

        return result.toString();
    }
}
