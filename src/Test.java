import java.util.Scanner;

public class Test {
    private static final Scanner sc = new Scanner(System.in);
    private static Function input() {
        System.out.print("Enter the function to take derivative of: ");
        String funcStr = sc.nextLine();
        return stringToFunction(funcStr);
    }
    private static Function stringToFunction(String str) {
        if (str == null || str.equals("")) return Function.ZERO;

        
        return null;
    }
    public static void main(String[] args) {
        Function.printDerivative(null);
        Function func1 = new PowerFunction(2, null, 2);
        Function func2 = new TrigonometricFunction(1, new PowerFunction(3, null, 1), "cos");
        Function func = CompoundFunction.multiply(func1, func2);
        Function.printDerivative(func);
        Function inner = CompoundFunction.sum(null, new ExponentialFunction(-1, new PowerFunction(-1, null, 1), Math.E));
        System.out.println(new ExponentialFunction(-1, new PowerFunction(-1, null, 1), Math.E));
        Function.printDerivative(new TrigonometricFunction(3, inner, "sin"), "g");
        Function expTest = new ExponentialFunction(2, new PowerFunction(2, null, 1), "x");
        Function.printDerivative(expTest);
        System.out.println(new ExponentialFunction(4, new LogarithmicFunction(3, func), 10));
    }
}


