import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Backend {
    private String input = "0"; //Current input
    private String mem1 = ""; //Stored previous input or the running answer. This is the other input that the current input is computed against.
    private String mem2 = "";
    private String operation = "";
    private String history = "";
    private boolean isVolatile = false; //Is it an answer (which means if we input a number it will disappear)
    private boolean isOperable = true;
    private boolean divByZero = false;
    private boolean negSqrt = false;


    public String[] getDisplayInfo(){
        if (divByZero){
            history = "DIVIDE BY ZERO ERROR";
            String[] displayInfo = {"", history};
            divByZero = false;
            return displayInfo;
        }
        if (negSqrt){
            history = "NEGATIVE SQUARE ROOT ERROR";
            String[] displayInfo = {"", history};
            negSqrt = false;
            return displayInfo;
        }

        history = mem1 + " " + operation;
        if (mem2 != "")
            history = history + " " + mem2 + " =";
        String[] displayInfo = {input, history};
        return displayInfo;
    }

    private void handleBackSpace(){
        if (input.length() > 1){
            input = input.substring(0, input.length() - 1);
        }
        else{
            input = "0";
        }
    }

    private void handleInput(char button){
        if (isVolatile){
            input = "0";
            isVolatile = false;
            isOperable = true;
        }
        if (mem2 != "")
           isOperable = false;
        if (button == '.' && input.contains("."))
            return;

        if (input.length() == 1 && input.charAt(0) == '0')
            input = "" + button;
        else
            input = input + button;
    }

    public void handle(String button){
        System.out.println(button);
        if ((button.charAt(0) >= '0' && button.charAt(0) <= '9' && button.length() == 1) || button.charAt(0) == '.'){
            handleInput(button.charAt(0));
        }
        else if (button.equals("<-")){
            if (!isVolatile)
                handleBackSpace();
        }
        else if (button.equals("1/x") || button.equals("\u221A") || button.equals("x\u00B2") || button.equals("+-") || button.equals("%")){
            //Handle uniry operators.
            handleUniryOperation(button);
            isVolatile = true;
        }
        else if ("-+/x".contains(button)){
            mem2 = "";
            if (isOperable)
            handleBinaryOperation(button);
            else {
                mem1 = input;
                operation = button;
                isVolatile = true;
                isOperable = false;
            }
        }
        else if (button.equals("=")){
            //If no operator.
            if (operation == ""){
                mem1 = input;
                isVolatile = true;
                isOperable = false;
            }
            else{
                if (mem2 == "")
                    mem2 = input;
                else
                    mem1 = input;
                handleBinaryOperation("=");
            }
        }
        else if (button.equals("C")){
            mem1 = "";
            input = "0";
            operation = "";
            mem2 = "";
            isVolatile = false;
            isOperable = true;
        }
        else if (button.equals("CE")){
            if (mem2 != "")
                mem1 = "";
            input = "0";
            isVolatile = false;
            isOperable = true;
        }
    }

    private void handleUniryOperation(String button){
        if (button.equals("1/x")){
            BigDecimal temp = new BigDecimal("1");
            BigDecimal in = new BigDecimal(input);
            if (in.equals(new BigDecimal(0))){
                divByZero = true;
                return;
            }
            input = temp.divide(in, 2, RoundingMode.HALF_UP).toString();
        }
        else if (button.equals("+-")){
            if (input.equals("0"))
                return;
            if (input.charAt(0) == '-')
                input = input.substring(1);
            else
                input = "-" + input;
        }
        else if (button.equals("%")){
            BigDecimal toPercent;
            if (mem2 != "")
                toPercent = new BigDecimal(mem2).add(new BigDecimal(mem1));
            else if (mem1 != "")
                toPercent = new BigDecimal(mem1);
            else {
                input = "0";
                return;
            }
            input = ((toPercent.divide(new BigDecimal(100))).multiply(new BigDecimal(input))).toString();
        }
        else if (button.equals("\u221A")){
            if (input.charAt(0) == '-'){
                negSqrt = true;
                return;
            }
            input = new BigDecimal(input).sqrt(new MathContext(10)).toString();
        }
        else if (button.equals("x\u00B2"))
            input = new BigDecimal(input).pow(2).toString();
    }

    private void handleBinaryOperation(String button){
        String temp = "";
        if (operation == ""){
            mem1 = input;
        }
        else {
            //We have to do the operation.
            BigDecimal num1 = new BigDecimal(mem1);
            BigDecimal num2 = new BigDecimal(input);
            if (button == "="){
                num2 = new BigDecimal(mem2);
                temp = mem1;
            }
            System.out.println("Mem1 = " + mem1);
            System.out.println("Mem2 = " + mem2);
            if (operation.equals("+"))
                sum(num1, num2);
            else if (operation.equals("-"))
                subtract(num1, num2);
            else if (operation.equals("x"))
                multiply(num1, num2);
            else if (operation.equals("/"))
                divide(num1, num2);
            if (button == "=")
                mem1 = temp;
        }
        if (!button.equals("=") && button != ""){
            operation = button;
        }
        isOperable = false;
        isVolatile = true;
    }

    private void sum(BigDecimal num1, BigDecimal num2){
        System.out.println("Sum");
        input = mem1 = num1.add(num2).toString();
    }
    private void subtract(BigDecimal num1, BigDecimal num2){
        input = mem1 = num1.subtract(num2).toString();
    }
    private void multiply(BigDecimal num1, BigDecimal num2){
        input = mem1 = num1.multiply(num2).toString();
    }
    private void divide(BigDecimal num1, BigDecimal num2){
        if (num2.equals(new BigDecimal(0))){
            divByZero = true;
            return;
        }
        input = mem1 = num1.divide(num2, 2, RoundingMode.HALF_UP).toString();
    }
}
