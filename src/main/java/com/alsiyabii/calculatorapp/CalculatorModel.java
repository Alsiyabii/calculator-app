package com.alsiyabii.calculatorapp;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the model and UI logic of a simple calculator.
 * <p>
 * This class handles:
 * <ul>
 *     <li>Creating and arranging the calculator buttons and display.</li>
 *     <li>Setting up event handlers for number and operator buttons.</li>
 *     <li>Interfacing with ExpressionParser to calculate results.</li>
 * </ul>
 */
public class CalculatorModel {
    // Root nodes
    private StackPane root;
    private GridPane grid;

    // Buttons
    private Button[] numberButtons = new Button[10];
    private Button[] operatorButtons = {
            new Button("+"), new Button("*"), new Button("-"),
            new Button("/"), new Button("="), new Button("C"),
            new Button("."), new Button("⌫"), new Button("±")
    };

    // Constants to improve operatorButtons array readability
    int PLUS = 0, MULTIPLY = 1, MINUS = 2, DIVIDE = 3, EQUALS = 4,
            CLEAR = 5, DECIMAL_POINT = 6, BACKSPACE = 7, FLIP_SIGN = 8;

    // Calculator display
    private TextField textField = new TextField("0");

    /**
     * Constructs a CalculatorModel and initializes the UI and logic.
     *
     * @param stringBuilder a StringBuilder used to keep track of the current input/expression
     */
    public CalculatorModel(StringBuilder stringBuilder) {
        setupUI();
        setupCalculations(stringBuilder);
    }

    /**
     * Initializes the UI components, including buttons and display,
     * and arranges them in a GridPane wrapped in a StackPane.
     */
    private void setupUI() {
        grid = new GridPane();
        grid.setVgap(6.5);
        grid.setHgap(6.5);
        grid.setPadding(new Insets(8));

        // Create number buttons
        for (int i = 0; i < numberButtons.length; i++) {
            numberButtons[i] = new Button(String.valueOf(i));
            numberButtons[i].setPrefSize(40, 40);
        }

        // Create operator buttons
        for (Button button : operatorButtons) {
            button.setPrefSize(40, 40);
        }

        // Add number buttons to grid
        int number = 1;
        for (int row = 4; row > 1; row--) {
            for (int col = 0; col < 3; col++) {
                grid.add(numberButtons[number], col, row);
                number++;
            }
        }

        // Add operator buttons manually
        grid.add(operatorButtons[PLUS], 3, 4);
        grid.add(operatorButtons[MULTIPLY], 3, 3);
        grid.add(operatorButtons[MINUS], 3, 2);
        grid.add(operatorButtons[DIVIDE], 3, 1);

        grid.add(operatorButtons[CLEAR], 0, 1);
        grid.add(operatorButtons[FLIP_SIGN], 2, 1);
        grid.add(operatorButtons[BACKSPACE], 1, 1);

        grid.add(operatorButtons[DECIMAL_POINT], 0, 5);
        grid.add(numberButtons[0], 1, 5);
        operatorButtons[EQUALS].setPrefSize(86.5, 40);
        grid.add(operatorButtons[EQUALS], 2, 5);
        GridPane.setColumnSpan(operatorButtons[EQUALS], 2);

        // Setup display
        textField.setPrefSize(180, 40);
        textField.setAlignment(Pos.CENTER_RIGHT);
        textField.setEditable(false);
        grid.add(textField, 0, 0);
        GridPane.setColumnSpan(textField, 4);

        // Wrap in StackPane
        root = new StackPane(grid);
        root.setAlignment(Pos.CENTER);
    }

    /**
     * Returns the root node containing the entire calculator UI.
     *
     * @return the root StackPane
     */
    public StackPane getRoot() {
        return root;
    }

    /**
     * Sets up the event handlers for all calculator buttons, including numbers,
     * operators, equals, clear, flip-sign, and backspace.
     *
     * @param stringBuilder a StringBuilder tracking the current input expression
     */
    public void setupCalculations(StringBuilder stringBuilder) {
        PauseTransition pause = new PauseTransition(Duration.seconds(2));

        // Number buttons
        for (int i = 0; i < numberButtons.length; i++) {
            int index = i;
            numberButtons[i].setOnMouseClicked(e -> {
                stringBuilder.append(numberButtons[index].getText());
                textField.setText(stringBuilder.toString());
            });
        }

        // Operator buttons (+, -, *, /, .)
        for (int i = 0; i < 7; i++) {
            int index = i;
            operatorButtons[i].setOnMouseClicked(e -> {
                stringBuilder.append(operatorButtons[index].getText());
                textField.setText(stringBuilder.toString());
            });
        }

        // Equals button
        operatorButtons[EQUALS].setOnMouseClicked(e -> {
            if (!textField.getText().equals("0")) {
                String infix = textField.getText();
                List<String> tokens = new ArrayList<>();
                try {
                    tokens = ExpressionParser.tokenize(infix);
                } catch (IllegalArgumentException iae) {
                    textField.setText(iae.getMessage());
                    pause.setOnFinished(event -> {
                        textField.setText("0");
                        stringBuilder.setLength(0);
                    });
                    pause.play();
                }

                List<String> postfix = ExpressionParser.convert(tokens);

                try {
                    double result = ExpressionParser.calculate(postfix);
                    textField.setText(result == Math.floor(result) ? String.valueOf((int) result) : Double.toString(result));
                    stringBuilder.setLength(0);
                    stringBuilder.append(result);
                } catch (ArithmeticException ae) {
                    textField.setText(ae.getMessage());
                    pause.setOnFinished(event -> {
                        textField.setText("0");
                        stringBuilder.setLength(0);
                    });
                    pause.play();
                }
            }
        });

        // Clear button
        operatorButtons[CLEAR].setOnMouseClicked(e -> {
            stringBuilder.setLength(0);
            textField.setText("0");
        });

        // Flip-sign button (±)
        operatorButtons[FLIP_SIGN].setOnMouseClicked(e -> {
            if (!textField.getText().equals("0")) {
                String infix = textField.getText();
                List<String> tokens = new ArrayList<>();
                String flippedTokens = "";
                try {
                    tokens = ExpressionParser.tokenize(infix);
                    flippedTokens = ExpressionParser.flipSign(tokens);
                } catch (IllegalArgumentException iae) {
                    textField.setText(iae.getMessage());
                    pause.setOnFinished(event -> {
                        textField.setText("0");
                        stringBuilder.setLength(0);
                    });
                    pause.play();
                }
                stringBuilder.setLength(0);
                stringBuilder.append(flippedTokens);
                textField.setText(stringBuilder.toString());
            }
        });

        // Backspace button (⌫)
        operatorButtons[BACKSPACE].setOnMouseClicked(e -> {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.setLength(stringBuilder.length() - 1);
                textField.setText(stringBuilder.isEmpty() ? "0" : stringBuilder.toString());
            }
        });
    }
}
