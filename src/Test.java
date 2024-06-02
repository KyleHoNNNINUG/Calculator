import java.util.ArrayList;
import java.util.Arrays;
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
        Function test1 = CompoundFunction.multiply(new ExponentialFunction(1, new TrigonometricFunction(1, null, "tan"), Math.E), 2);
        Function test2 = new LogarithmicFunction(1, test1, Math.E);
        System.out.println(test1);
        System.out.println(test2);
        Function.printDerivative(test2, 5);
        Function one = new ConstantFunction(1);
        for (int i = 0; i < 6; i++) {
            one = CompoundFunction.multiply(one, new CompoundFunction(false, 1, new ArrayList<>(Arrays.asList(Function.IDENTITY, new ConstantFunction(1)))));
            System.out.println(one);
        }
    }
}


