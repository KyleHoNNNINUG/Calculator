import java.util.ArrayList;
import java.util.Scanner;

public class Test {
    private static final Scanner sc = new Scanner(System.in);
    private static String requestString(String firstQuestion, String followUpQuestion, String[] options) {
        String r = "";
        System.out.print(firstQuestion);
        boolean boolExistingBecuzJavaIsStupidAndCannotAcceptUnreachableCode = true;
        while (boolExistingBecuzJavaIsStupidAndCannotAcceptUnreachableCode) { 
            String input = sc.nextLine().toLowerCase();
            for (String o: options) {
                if (input.equals(o)) {
                    r = input;
                    boolExistingBecuzJavaIsStupidAndCannotAcceptUnreachableCode = false;
                    break;
                }
            }
            if (!boolExistingBecuzJavaIsStupidAndCannotAcceptUnreachableCode) break;
            System.out.print(followUpQuestion);
        }
        return r;
    }
    private static double requestDouble(String firstQuestion, String followUpQuestion) {
        double r = 0;
        System.out.print(firstQuestion);
        boolean boolExistingBecuzJavaIsStupidAndCannotAcceptUnreachableCode = true;
        while (boolExistingBecuzJavaIsStupidAndCannotAcceptUnreachableCode) { 
            try {
                String input = sc.nextLine();
                boolean isE = false;
                if (input.substring(input.length()-1).equals("e")) {
                    isE = true;
                    input = input.substring(0, input.length()-1);
                }
                if (input.equals("")) r = 1;
                else r = Double.parseDouble(input);
                if (isE) r *= Math.E;
                break;
            }
            catch(NumberFormatException e) {
                System.out.print(followUpQuestion);
            }
        }
        return r;
    }
    private static ArrayList<Function> requestFunctions(String firstQuestion, String followUpQuestion, String exitCmd) {
        ArrayList<Function> r = new ArrayList<>();
        System.out.println(firstQuestion);
        boolean boolExistingBecuzJavaIsStupidAndCannotAcceptUnreachableCode = true;
        while (boolExistingBecuzJavaIsStupidAndCannotAcceptUnreachableCode) {
            r.add(constructFunction());
            System.out.print(followUpQuestion);
            String input = sc.nextLine().toLowerCase();
            if (input.equals(exitCmd)) boolExistingBecuzJavaIsStupidAndCannotAcceptUnreachableCode = false;
        }
        return r;
    }
    public static Function constructFunction() {
        return constructFunction("Enter the function type: ");
    }
    public static Function constructFunction(String firstQuestion) {
        String funcType = requestString(firstQuestion,"Invalid function type; enter again: ",Function.FUNCTION_TYPES);
        switch(funcType) {
            case "zero" -> {
                return Function.ZERO;
            }
            case "identity" -> {
                return Function.IDENTITY;
            }
            case "constant" -> {
                double val = requestDouble("Enter the constant value: ", "Invalid value; enter again: ");
                return new ConstantFunction(val);
            }
            case "power" -> {
                double coeff = requestDouble("Enter the coefficient: ", "Invalid value; enter again: ");
                Function input = constructFunction("Constructing the input function; enter the function type: ");
                System.out.println("The input function is " + input);
                double power = requestDouble("Enter the power: ", "Invalid value; enter again: ");
                return new PowerFunction(coeff, input, power);
            }
            case "trigonometric" -> {
                double coeff = requestDouble("Enter the coefficient: ", "Invalid value; enter again: ");
                Function input = constructFunction("Constructing the input function; enter the function type: ");
                System.out.println("The input function is " + input);
                String trigType = requestString("Enter the trig type (e.g. sin): ", "Invalid trig type; enter again: ", TrigonometricFunction.TRIG_TYPES);
                return new TrigonometricFunction(coeff, input, trigType);
            }
            case "logarithmic" -> {
                double coeff = requestDouble("Enter the coefficient: ", "Invalid value; enter again: ");
                Function input = constructFunction("Constructing the input function; enter the function type: ");
                System.out.println("The input function is " + input);
                double base = requestDouble("Enter the log base: ", "Invalid value; enter again: ");
                return new LogarithmicFunction(coeff, input, base);
            }
            case "exponential" -> {
                double coeff = requestDouble("Enter the coefficient: ", "Invalid value; enter again: ");
                Function input = constructFunction("Constructing the input function; enter the function type: ");
                System.out.println("The input function is " + input);
                double base = requestDouble("Enter the exponential base: ", "Invalid value; enter again: ");
                return new ExponentialFunction(coeff, input, base);
            }
            case "compound" -> {
                String[] compoundTypeStrings = {"+", "plus", "sum", "*", "multiply", "product"};
                String compoundTypeString = requestString("Enter the compound type (+/*): ", "Invalid compound type; enter again: ", compoundTypeStrings);
                boolean compoundType = compoundTypeString.equals("*") || compoundTypeString.equals("multiply") || compoundTypeString.equals("product");
                double coeff = requestDouble("Enter the coefficient: ", "Invalid value; enter again: ");
                ArrayList<Function> functions = requestFunctions("Begin to enter functions", "Enter exit to finish appending functions; enter anything else to continue: ", "exit");
                return new CompoundFunction(compoundType, coeff, functions);
            }
        }
        System.out.println("Unknown function type");
        return null;
    }
    public static Function constructGuessDerivative() {
        System.out.println("Take the derivative of the function and enter the answer as a function.");
        Function guess = constructFunction();
        boolean boolExistingBecuzJavaIsStupidAndCannotAcceptUnreachableCode = true;
        while(boolExistingBecuzJavaIsStupidAndCannotAcceptUnreachableCode) {
            String[] options = {"y", "n"};
            String respond = requestString("The function you put was " + guess + "; confirm? (y/n) ", "Invalid response; enter again (y/n): ", options);
            if (respond.equals("y")) break;
            guess = constructFunction();
        }
        System.out.println("Your answer is " + guess);
        return guess;
    }
    
    public static void main(String[] args) {
        Function test = constructFunction();
        System.out.println();
        System.out.println("Function: " + test);
        Function guess = constructGuessDerivative();
        Function answer = Function.getDerivative(test);
        if (guess.equals(answer)) System.out.println("Correct!");
        else System.out.println("Incorrect; the answer is " + answer);
        System.out.println("Number of derivative rules used:");
        test.printRuleCount();
    }
}