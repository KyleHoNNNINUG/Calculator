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
        Function test = new ExponentialFunction(1, null, "x");
        Function.printDerivative(test, 2);
    }
}


