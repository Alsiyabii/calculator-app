# Calculator App (JavaFX) 🖥️
A simple **JavaFX-based Calculator** that supports basic arithmetic operations. <br>
<br>
This is my **second Java project exploring JavaFX UI design, CSS styling, and expression parsing**, mainly to practice structuring Java applications and handling events to ensure a smooth user experience.

## Features ⚡️
- ➕➖✖️➗ Perform basic arithmetic operations (addition, subtraction, multiplication, division)
- 🔁 Flip sign (±) of the current number
- ⌫ Backspace and Clear expression functionalities support
- (·) Decimal point support
- 🧮 Expression parsing (infix -> postfix -> evaluation)
- 🎨 Custom **CSS styling** for a modern UI

## Tech Stack 🛠️
- **Java 17+** (or version compatible with your setup)
- **JavaFX** (UI framework)
- **CSS** (styling)

## Project Structure 📂
```
CalculatorApp/
├── src/
│   └── com/alsiyabii/calculatorapp/
│       ├── MainApplication.java      # Entry point, sets up stage and scene
│       ├── CalculatorModel.java      # UI layout and event handling
│       ├── ExpressionParser.java     # Expression parsing & evaluation logic
│       └── styles.css                # CSS styling for UI
├── resources/
│   └── com/alsiyabii/calculatorapp/
│       └── logo.jpg                  # App icon
└── README.md
```

## Run the Application
### On Windows / macOS / Linux

1. Clone the repository
```bash
git clone https://github.com/Alsiyabii/calculator-app.git
cd calculator-app
```

2. Compile and run with JavaFX
```bash
javac --module-path "PATH_TO_FX/lib" --add-modules javafx.controls,javafx.fxml src/com/alsiyabii/calculatorapp/*.java

java --module-path "PATH_TO_FX/lib" --add-modules javafx.controls,javafx.fxml com.alsiyabii.calculatorapp.MainApplication
```
Replace ```PATH_TO_FX``` with your local javaFX SDK path


## Screenshots 📸
<img src=Images/Demo1.png alt="Demo Image 1" width="275"> 
<img src=Images/Demo2.png alt="Demo Image 1" width="272"> 

## License 📜
This project is licensed under the [MIT License](LICENSE)
