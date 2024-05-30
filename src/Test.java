import java.util.ArrayList;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        Function test1 = new TrigonometricFunction(3, null, "cot");
        Function test2 = new PowerFunction(5, null, 1);
        Function test3 = new ExponentialFunction(2, test1, Math.E);
        Function test4 = new CompoundFunction(false, 1, new ArrayList<>(Arrays.asList(test1,test2)));
        Function test5 = new PowerFunction(2, null, 1);
        Function test6 = new ExponentialFunction(1, test1, 2);
        Function test7 = new ExponentialFunction(1, test1, 3);

        Function Sean1 = new PowerFunction(2, null, 1);
        Function Sean2 = new TrigonometricFunction(1, Sean1, "sin");
        Function Sean3 = new PowerFunction(2, Sean2, 3);

        Function Tim1 = new PowerFunction(3, null, 4);

        System.out.println(Tim1);
        System.out.println(Function.getDerivative(Tim1));

        System.out.println(Sean1);
        System.out.println(Function.getDerivative(Sean1));
        System.out.println(Sean2);
        System.out.println(Function.getDerivative(Sean2));
        System.out.println(Sean3);
        System.out.println(Function.getDerivative(Sean3));
    }
}