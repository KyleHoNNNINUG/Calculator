import java.util.ArrayList;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        Function test1 = new TrigonometricFunction(3, null, "cos");
        Function test2 = new PowerFunction(5, null, 4);
        Function test3 = new ExponentialFunction(2, test1, Math.E);
        Function test4 = new CompoundFunction(false, 1, new ArrayList<>(Arrays.asList(test1,test2)));
        Function test5 = new PowerFunction(2, null, 1);
        Function test6 = new ExponentialFunction(1, test1, 2);
        Function test7 = new ExponentialFunction(1, test1, 3);
        Function test8 = new TrigonometricFunction(-1, null, "csc");
        Function test9 = new TrigonometricFunction(3, test2, "cot");
        Function test10 = new TrigonometricFunction(3, null, "sin");
        Function test11 = new CompoundFunction(true, 1, new ArrayList<>(Arrays.asList(test9,test10)));
        Function test12 = new CompoundFunction(false, 1, new ArrayList<>(Arrays.asList(test9,test10)));
        Function test13 = new CompoundFunction(true, 3, new ArrayList<>(Arrays.asList(test9)));
        Function test14 = new CompoundFunction(true, 1, new ArrayList<>(Arrays.asList(test13)));
        Function test15 = new CompoundFunction(false, 2, new ArrayList<>(Arrays.asList(test14)));
        Function test16 = CompoundFunction.multiply(test9, test10);
        Function test17 = CompoundFunction.sum(test9, test9);
        Function test18 = CompoundFunction.sum(test9, test17);
        Function test19 = new PowerFunction(2, test10, 2);
        Function test20 = new PowerFunction(2, test9, 2);
        Function test21 = new PowerFunction(2, test1, 2);
        Function test22 = new PowerFunction(2, test1, 0);
        Function test23 = CompoundFunction.sum(test20, null);
        Function test24 = CompoundFunction.sum(null, test1);

        System.out.println(test23);
        System.out.println(CompoundFunction.sum(null, test23));
        System.out.println(CompoundFunction.sum(CompoundFunction.sum(test23, null), test16));
        System.out.println(CompoundFunction.sum(CompoundFunction.sum(CompoundFunction.sum(test23, null), test16), CompoundFunction.multiply(test10, test9)));
        System.out.println(CompoundFunction.sum(test23, test23));
        System.out.println(test24);
        CompoundFunction.printEquation(test23, test24, "+");
    }
}
