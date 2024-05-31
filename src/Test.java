import java.util.ArrayList;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        Function test1 = new TrigonometricFunction(1, null, "cos");
        Function test2 = new PowerFunction(5, null, 1);
        Function test3 = new ExponentialFunction(2, test1, Math.E);
        Function test4 = new CompoundFunction(false, 1, new ArrayList<>(Arrays.asList(test1,test2)));
        Function test5 = new PowerFunction(2, null, 1);
        Function test6 = new ExponentialFunction(1, test1, 2);
        Function test7 = new ExponentialFunction(1, test1, 3);
        Function test8 = new TrigonometricFunction(-1, null, "csc");
        Function test9 = new TrigonometricFunction(1, test2, "cot");
        Function test10 = new TrigonometricFunction(1, null, "sin");
        Function test11 = new CompoundFunction(true, 1, new ArrayList<>(Arrays.asList(test9,test10)));
        Function test12 = new CompoundFunction(false, 1, new ArrayList<>(Arrays.asList(test9,test10)));
        Function test13 = new CompoundFunction(true, 3, new ArrayList<>(Arrays.asList(test9)));
        Function test14 = new CompoundFunction(true, 1, new ArrayList<>(Arrays.asList(test13)));
        Function test15 = new CompoundFunction(false, 2, new ArrayList<>(Arrays.asList(test14)));
        Function test16 = CompoundFunction.sum(test9, test10);
        Function test17 = CompoundFunction.sum(test9, test9);
        Function test18 = CompoundFunction.sum(test9, test17);
        Function test19 = new PowerFunction(5, test10, 2);
        Function test20 = new PowerFunction(2, test9, 2);
        Function test21 = new PowerFunction(2, test1, 2);
        Function test22 = new PowerFunction(3, test1, 0);

        Function Sean1 = new PowerFunction(2, null, 1);
        Function Sean2 = new TrigonometricFunction(1, Sean1, "sin");
        Function Sean3 = new PowerFunction(2, Sean2, 3);

        Function Tim1 = new PowerFunction(3, null, 4);

        CompoundFunction.printEquation(test20, null, "+");
        System.out.println(CompoundFunction.changes(null, test20, false));
        CompoundFunction.printEquation(test19, test21, "+");
        System.out.println(CompoundFunction.changes(test19, test21, false));
        CompoundFunction.printEquation(test20, 0, "+");
        System.out.println(CompoundFunction.changes(test20, 0, false));
        //System.out.println(test8);
        //System.out.println(test9);
        //System.out.println(Function.getDerivative(test9));
        //System.out.println(test10);
        //System.out.println(test11);
        //System.out.println(Function.getDerivative(test11));
        //System.out.println(CompoundFunction.multiply(test11, test10));
        //System.out.println(test12);
        //System.out.println(Function.getDerivative(test12));
        //System.out.println(CompoundFunction.multiply(test12, test10));
        //System.out.println(CompoundFunction.multiply(test10, test12));
        
    }
}
