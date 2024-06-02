import java.util.ArrayList;
import java.util.Arrays;

public class CompoundFunction extends Function{
    private final boolean compoundType; // true -> multiply; false -> sum
    private final ArrayList<Function> functions;
    
    public CompoundFunction(boolean compoundType, double coefficient, ArrayList<Function> functions) {
        super(coefficient, "compound", null);
        this.compoundType = compoundType;
        this.functions = functions;
    }

    public String getCompoundType() {
        if (this.compoundType) return "multiply";
        return "sum";
    }

    public ArrayList<Function> getFunctions() {
        return this.functions;
    }

    @Override
    public Function withoutCoeff() {
        return new CompoundFunction(this.compoundType, 1, this.functions);
    }

    @SuppressWarnings("all")
    @Override
    public CompoundFunction clone() {
        boolean newCompoundType = this.compoundType;
        double newCoeff = this.getCoefficient();
        @SuppressWarnings("unchecked")
        ArrayList<Function> newFunctions = (ArrayList<Function>)this.functions.clone();
        return new CompoundFunction(newCompoundType, newCoeff, newFunctions);
    }

    // Precondition: ArrayList<Function> functions of both CompoundFunction objects have more than 1 element
    public boolean equalsCompound(Function other) {
        if (other.getType().equals("compound")) {
            if (this.compoundType != ((CompoundFunction)other).compoundType) return false;
            if (this.compoundType) {
                double coeff1 = this.getCoefficient(), coeff2 = other.getCoefficient();
                @SuppressWarnings("unchecked")
                ArrayList<Function> func1 = (ArrayList<Function>)this.getFunctions().clone(), func2 = (ArrayList<Function>)((CompoundFunction)other).getFunctions().clone();
                if (func1.size() != func2.size()) return false;
                for (int i = 0; i < func1.size(); i++) {
                    coeff1 *= func1.get(i).getCoefficient();
                    coeff2 *= func2.get(i).getCoefficient();
                    func1.set(i, func1.get(i).withoutCoeff());
                    func2.set(i, func2.get(i).withoutCoeff());
                }
                if (coeff1 != coeff2) return false;
                for (int i = 0; i < func1.size(); i++) {
                    Function curr1 = func1.get(i);
                    int index2 = -1;
                    for (int j = 0; j < func1.size(); j++) {
                        if (curr1.equals(func2.get(j))) {
                            index2 = j;
                            break;
                        }
                    }
                    if (index2 == -1) return false;
                    func1.remove(i);
                    func2.remove(index2);
                    i--;
                }
                return true;
            }
            else {
                double coeff1 = this.getCoefficient(), coeff2 = other.getCoefficient();
                @SuppressWarnings("unchecked")
                ArrayList<Function> func1 = (ArrayList<Function>)this.getFunctions().clone(), func2 = (ArrayList<Function>)((CompoundFunction)other).getFunctions().clone();
                if (func1.size() != func2.size()) return false;
                for (int i = 0; i < func1.size(); i++) {
                    if (func1.get(i) == null) func1.set(i, new PowerFunction(1, null, 1));
                    if (func2.get(i) == null) func2.set(i, new PowerFunction(1, null, 1));
                    func1.set(i, multiply(func1.get(i).withoutCoeff(), coeff1));
                    func2.set(i, multiply(func2.get(i).withoutCoeff(), coeff2));
                }
                for (int i = 0; i < func1.size(); i++) {
                    Function curr1 = func1.get(i);
                    int index2 = -1;
                    for (int j = 0; j < func1.size(); j++) {
                        if (curr1.equals(func2.get(j))) {
                            index2 = j;
                            break;
                        }
                    }
                    if (index2 == -1) return false;
                    func1.remove(i);
                    func2.remove(index2);
                    i--;
                }
                return true;
            }
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (this.functions.size() == 1) return multiply(this.functions.get(0), this.getCoefficient()).toString();
        String r = "";
        if (this.compoundType) {
            double coeff = this.getCoefficient();
            for (Function func: this.functions) {
                coeff *= func.getCoefficient();
                String inner = func.withoutCoeff().toString();
                if (!inner.equals("x")) inner = "(" + inner + ")";
                r += inner;
            }
            if (coeff == -1) r = "-" + r;
            else if (coeff != 1) {
                int checker = (int)coeff;
                if (checker == coeff)
                    r = checker + r;
                else
                    r = coeff + r;
            }
        }
        else {
            if (Math.abs(this.getCoefficient()) != 1) {
                int checker = (int)Math.abs(this.getCoefficient());
                if (checker == Math.abs(this.getCoefficient()))
                    r += checker;
                else
                    r += Math.abs(this.getCoefficient());
            }
            if (this.getCoefficient() != 1) r += "(";
            for (int i = 0; i < this.functions.size(); i++) {
                String next = this.functions.get(i).toString();
                if (!next.substring(0,1).equals("-")) {
                    if (i > 0) r += " + " + next;
                    else r += next;
                }
                else {
                    if (i > 0) r += " - " + next.substring(1);
                    else r += "-" + next.substring(1);
                }
            }
            if (this.getCoefficient() != 1) r += ")";
        }
        return r;
    }

    protected static boolean changes(double num, Function func, boolean compoundType) {
        return changes(new ConstantFunction(num), func, compoundType);
    }

    protected static boolean changes(Function func, double num, boolean compoundType) {
        return changes(func, new ConstantFunction(num), compoundType);
    }

    protected static boolean changes(Function func1, Function func2, boolean compoundType) {
        if (compoundType)
            return !multiply(func1, func2).equals(new CompoundFunction(true, 1, new ArrayList<>(Arrays.asList(func1, func2))));
        return !sum(func1, func2).equals(new CompoundFunction(false, 1, new ArrayList<>(Arrays.asList(func1, func2))));
    }

    public static void printEquation(double num, Function func, String operation) {
        printEquation(new ConstantFunction(num), func, operation);
    }

    public static void printEquation(Function func, double num, String operation) {
        printEquation(func, new ConstantFunction(num), operation);
    }

    public static void printEquation(Function func1, Function func2, String operation) {
        if (func1 == null) func1 = Function.IDENTITY;
        if (func2 == null) func2 = Function.IDENTITY;
        String str1 = func1.toString(), str2 = func2.toString();
        if (func1.getType().equals("compound") && ((CompoundFunction)func1).getCompoundType().equals("sum") && func1.getCoefficient() == 1) 
            str1 = "(" + str1 + ")";
        if (func2.getType().equals("compound") && ((CompoundFunction)func2).getCompoundType().equals("sum") && func2.getCoefficient() == 1) 
            str2 = "(" + str2 + ")";
        switch (operation) {
            case "+" -> System.out.println(str1 + " + " + str2 + " = " + sum(func1, func2));
            case "*" -> System.out.println(str1 + " * " + str2 + " = " + multiply(func1, func2));
            default -> System.out.println("Unknown operation");
        }
    }

    public boolean pickOut(Function func) {
        for (int i = 0; i < this.functions.size(); i++) {
            if (this.functions.get(i).equals(func)) {
                this.functions.remove(i);
                return true;
            }
        }
        return false;
    }

    public static Function sum(Function func, double num) {
        return sum(func, new ConstantFunction(num));
    }

    public static Function sum(double num, Function func) {
        return sum(new ConstantFunction(num), func);
    }

    public static Function sum(Function func1, Function func2) {
        if (func1 == null) return sum(new PowerFunction(1, null, 1), func2);
        if (func2 == null) return sum(func1, new PowerFunction(1, null, 1));
        if (func1.equals(Function.ZERO)) return func2;
        if (func2.equals(Function.ZERO)) return func1;
        // logarithmic(constant) == 1 case
        if (func1.getType().equals("logarithmic") && func1.getInput() != null && func1.getInput().equals(((LogarithmicFunction)func1).getBase())) 
            return sum(new ConstantFunction(func1.getCoefficient()), func2);
        if (func2.getType().equals("logarithmic") && func2.getInput() != null && func2.getInput().equals(((LogarithmicFunction)func2).getBase()))
            return sum(func1, new ConstantFunction(func2.getCoefficient()));
        if (!(func1.getType().equals("compound") && ((CompoundFunction)func1).getCompoundType().equals("sum")) && 
            !(func2.getType().equals("compound") && ((CompoundFunction)func2).getCompoundType().equals("sum"))) {
            // sum of trig square cases
            if (func1.getType().equals("power") && ((PowerFunction)func1).getPower() == 2 && 
                func1.getInput() != null && func1.getInput().getType().equals("trigonometric")) {
                TrigonometricFunction trig1 = (TrigonometricFunction)func1.getInput();
                if (func2.getType().equals("power") && ((PowerFunction)func2).getPower() == 2 && 
                    func2.getInput() != null && func2.getInput().getType().equals("trigonometric")) {
                    TrigonometricFunction trig2 = (TrigonometricFunction)func2.getInput();
                    if (((trig1.getInput() == null && trig2.getInput() == null) || 
                        (trig1.getInput() != null && trig2.getInput() != null && trig1.getInput().equals(trig2.getInput()))) &&
                        ((trig1.getTrigType().equals("sin") && trig2.getTrigType().equals("cos")) ||
                        (trig1.getTrigType().equals("cos") && trig2.getTrigType().equals("sin")))) {
                        boolean excess = func1.getCoefficient() >= func2.getCoefficient(); // true -> trig1 is excessive; false -> trig2 is excessive
                        double combined = excess ? func2.getCoefficient() : func1.getCoefficient();
                        double remain = (excess ? func1.getCoefficient() : func2.getCoefficient()) - combined;
                        Function remainFunc = multiply((excess ? func1.withoutCoeff() : func2.withoutCoeff()), remain);
                        if (remainFunc.equals(0)) return new ConstantFunction(combined);
                        return new CompoundFunction(false, 1, new ArrayList<>(Arrays.asList(remainFunc, new ConstantFunction(combined))));
                    }
                }
                else if (func2.equals(func2.getCoefficient())) {
                    String resultTrigType = "";
                    if (trig1.getTrigType().equals("tan")) resultTrigType = "sec";
                    else if (trig1.getTrigType().equals("cot")) resultTrigType = "csc";
                    if (!resultTrigType.equals("")) {
                        double remain = func2.getCoefficient() - func1.getCoefficient();
                        TrigonometricFunction innerTrig = new TrigonometricFunction(1, trig1.getInput(), resultTrigType);
                        PowerFunction resultFunction = new PowerFunction(func1.getCoefficient(), innerTrig, 2);
                        if (remain == 0) return resultFunction;
                        return new CompoundFunction(false, 1, new ArrayList<>(Arrays.asList(resultFunction, new ConstantFunction(remain))));
                    }
                }
            }
            else if (func1.equals(func1.getCoefficient())) {
                if (func2.getType().equals("power") && ((PowerFunction)func2).getPower() == 2 && 
                    func2.getInput() != null && func2.getInput().getType().equals("trigonometric")) {
                    TrigonometricFunction trig2 = (TrigonometricFunction)func2.getInput();
                    String resultTrigType = "";
                    if (trig2.getTrigType().equals("tan")) resultTrigType = "sec";
                    else if (trig2.getTrigType().equals("cot")) resultTrigType = "csc";
                    if (!resultTrigType.equals("")) {
                        double remain = func1.getCoefficient() - func2.getCoefficient();
                        TrigonometricFunction innerTrig = new TrigonometricFunction(1, trig2.getInput(), resultTrigType);
                        PowerFunction resultFunction = new PowerFunction(func2.getCoefficient(), innerTrig, 2);
                        if (remain == 0) return resultFunction;
                        return new CompoundFunction(false, 1, new ArrayList<>(Arrays.asList(resultFunction, new ConstantFunction(remain))));
                    }
                }
                else if (func2.equals(func2.getCoefficient())) return new ConstantFunction(func1.getCoefficient() + func2.getCoefficient());
            }
            // functions identical if without coefficient
            if (func1.withoutCoeff().equals(func2.withoutCoeff())) {
                return multiply(func1.withoutCoeff(), func1.getCoefficient() + func2.getCoefficient());
            }
            // same-base logarithmic functions
            if (func1.getType().equals("logarithmic") && func2.getType().equals("logarithmic") && 
                (((LogarithmicFunction)func1).getBase() == ((LogarithmicFunction)func2).getBase() ||
                (Function.approxE(((LogarithmicFunction)func1).getBase()) != 0 &&
                (Function.approxE(((LogarithmicFunction)func1).getBase()) == Function.approxE(((LogarithmicFunction)func2).getBase())))))
                return new LogarithmicFunction(func1.getCoefficient() * func2.getCoefficient(), multiply(func1.getInput(), func2.getInput()), ((LogarithmicFunction)func1).getBase());
            // different functions
            else
                return new CompoundFunction(false, 1, new ArrayList<>(Arrays.asList(func1,func2)));
        }
        else if (func1.getType().equals("compound") && ((CompoundFunction)func1).getCompoundType().equals("sum") &&
            !(func2.getType().equals("compound") && ((CompoundFunction)func2).getCompoundType().equals("sum"))) {
            @SuppressWarnings("unchecked")
            ArrayList<Function> newFunctions = (ArrayList<Function>)((CompoundFunction)func1).functions.clone();
            int indexChange = -1;
            for (int i = 0; i < newFunctions.size(); i++) {
                if (changes(newFunctions.get(i), func2, false)) {
                    indexChange = i;
                    break;
                }
            }
            func2 = multiply(func2, 1 / func1.getCoefficient());
            if (indexChange != -1) newFunctions.set(indexChange, sum(newFunctions.get(indexChange), func2));
            else newFunctions.add(func2);
            return new CompoundFunction(false, func1.getCoefficient(), newFunctions);
        }
        else if (!(func1.getType().equals("compound") && ((CompoundFunction)func1).getCompoundType().equals("sum")) &&
            func2.getType().equals("compound") && ((CompoundFunction)func2).getCompoundType().equals("sum")) {
                return sum(func2, func1);
            }
        else {
            Function r = func1;
            for (Function func: ((CompoundFunction)func2).functions) r = sum(r, multiply(func, func2.getCoefficient()));
            return r;
        }
    }

    public static Function multiply(Function func, double coeff) {
        if (coeff == 0) return Function.ZERO;
        if (func == null) return multiply(Function.IDENTITY, coeff);
        if (coeff == 1) return func;
        switch (func.getType()) {
            case "power" -> {
                return new PowerFunction(func.getCoefficient() * coeff, func.getInput(), ((PowerFunction)func).getPower());
            }
            case "trigonometric" -> {
                return new TrigonometricFunction(func.getCoefficient() * coeff, func.getInput(), ((TrigonometricFunction)func).getTrigType());
            }
            case "logarithmic" -> {
                return new LogarithmicFunction(func.getCoefficient() * coeff, func.getInput(), ((LogarithmicFunction)func).getBase());
            }
            case "exponential" -> {
                return new ExponentialFunction(func.getCoefficient() * coeff, func.getInput(), ((ExponentialFunction)func).getBase());
            }
            case "compound" -> {
                return new CompoundFunction(((CompoundFunction)func).getCompoundType().equals("multiply"), func.getCoefficient() * coeff, ((CompoundFunction)func).getFunctions());
            }
            case "zero" -> {                
                return Function.ZERO;
            }
            case "constant" -> {
                return new ConstantFunction(func.getCoefficient() * coeff);
            }
            default -> {
                System.out.println("Unknown function type");
                return new Function(0, "default", null);
            }
        }
    }

    public static Function multiply(Function func1, Function func2) {
        // null function case (turn back to power function)
        if (func1 == null) return multiply(new PowerFunction(1, null, 1), func2);
        if (func2 == null) return multiply(func1, new PowerFunction(1, null, 1));
        // multiply by 1 case
        if (func1.equals(1)) return multiply(func2, 1);
        if (func2.equals(1)) return multiply(func1, 1);
        // zero function case
        if (func1.equals(Function.ZERO) || func2.equals(Function.ZERO)) return Function.ZERO;
        // logarithmic(constant) == 1 case
        if (func1.getType().equals("logarithmic") && func1.getInput() != null && func1.getInput().equals(((LogarithmicFunction)func1).getBase())) 
            return multiply(new ConstantFunction(func1.getCoefficient()), func2);
        if (func2.getType().equals("logarithmic") && func2.getInput() != null && func2.getInput().equals(((LogarithmicFunction)func2).getBase()))
            return multiply(func1, new ConstantFunction(func2.getCoefficient()));
        // constant function case
        if (func1.getType().equals("constant")) return multiply(func2, func1.getCoefficient());
        if (func2.getType().equals("constant")) return multiply(func1, func2.getCoefficient());
        // base-1 exponential case
        if (func1.getType().equals("exponential") && ((ExponentialFunction)func1).getBase() == 1) return func2;
        if (func2.getType().equals("exponential") && ((ExponentialFunction)func2).getBase() == 1) return func1;
        // one-element compound function case
        if (func1.getType().equals("compound") && ((CompoundFunction)func1).functions.size() == 1) 
            func1 = multiply(((CompoundFunction)func1).functions.get(0), func1.getCoefficient());
        if (func2.getType().equals("compound") && ((CompoundFunction)func2).functions.size() == 1) 
            func2 = multiply(((CompoundFunction)func2).functions.get(0), func2.getCoefficient());
        // different type/input functions case (except compound)
        if (!func1.getType().equals("compound") && !func2.getType().equals("compound") &&
            (!func1.getType().equals(func2.getType()) || ((func1.getInput() == null) ^ (func2.getInput() == null)) || 
            (func1.getInput() != null && !func1.getInput().equals(func2.getInput())))) 
        {
            // power of the other
            if (func1.getInput() != null && func1.getType().equals("power") && func1.getInput().equals(func2.withoutCoeff()))
                return new PowerFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput(), ((PowerFunction)func1).getPower() + 1);
            if (func2.getInput() != null && func2.getType().equals("power") && func2.getInput().equals(func1.withoutCoeff()))
                return new PowerFunction(func1.getCoefficient() * func2.getCoefficient(), func2.getInput(), ((PowerFunction)func2).getPower() + 1);
            // exponential
            if (func1.getType().equals("exponential") && func2.getType().equals("exponential")) {
                ExponentialFunction exp1 = (ExponentialFunction)func1, exp2 = (ExponentialFunction)func2;
                // identical input if input without coefficient
                if (((func1.getInput() == null && func2.getInput() == null) ||
                    (func1.getInput() != null && func2.getInput() != null && func1.getInput().withoutCoeff().equals(func2.getInput().withoutCoeff())) && 
                    (((Function.approxE(exp1.getBase())) != 0) == ((Function.approxE(exp2.getBase())) != 0)))) {
                        double base1 = Math.pow(exp1.getBase(), func1.getInput() == null ? 0 : func1.getInput().getCoefficient());
                        double base2 = Math.pow(exp2.getBase(), func2.getInput() == null ? 0 : func2.getInput().getCoefficient());
                        if (base1 * base2 == 1) return new ConstantFunction(func1.getCoefficient() * func2.getCoefficient());
                        return new ExponentialFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput().withoutCoeff(), base1 * base2);
                    }
                // identical base
                if (exp1.getBase() == exp2.getBase() || (Function.approxE(exp1.getBase()) != 0 && Function.approxE(exp1.getBase()) == Function.approxE(exp2.getBase())))
                    return new ExponentialFunction(func1.getCoefficient() * func2.getCoefficient(), sum(func1.getInput(), func2.getInput()), ((ExponentialFunction)func1).getBase());
                // e with different coefficient as base
                if (Function.approxE(exp1.getBase()) != 0 && Function.approxE(exp2.getBase()) != 0) {
                    ExponentialFunction coeff1 = new ExponentialFunction(func1.getCoefficient(), func1.getInput(), Function.approxE(exp1.getBase())),
                                        e1 = new ExponentialFunction(func1.getCoefficient(), func1.getInput(), Math.E),
                                        coeff2 = new ExponentialFunction(func2.getCoefficient(), func2.getInput(), Function.approxE(exp2.getBase())),
                                        e2 = new ExponentialFunction(func2.getCoefficient(), func2.getInput(), Math.E);
                    Function r = e1;
                    r = multiply(r, e2);
                    r = multiply(r, coeff1);
                    r = multiply(r, coeff2);
                    return r;
                }
            }
            // else
            return new CompoundFunction(true, func1.getCoefficient() * func2.getCoefficient(), new ArrayList<>(Arrays.asList(func1.withoutCoeff(), func2.withoutCoeff())));
        }
        // same type functions case (except compound)
        if (!func1.getType().equals("compound") && !func2.getType().equals("compound")) {
            switch (func1.getType()) {
                case "power" -> {
                    double coeff = func1.getCoefficient() * func2.getCoefficient();
                    Function input = func1.getInput();
                    double power = ((PowerFunction)func1).getPower() + ((PowerFunction)func2).getPower();
                    return new PowerFunction(coeff, input, power);
                }
                case "trigonometric" -> {
                    switch (((TrigonometricFunction)func1).getTrigType()) {
                        case "sin" -> {
                            switch (((TrigonometricFunction)func2).getTrigType()) {
                                case "sin" -> {
                                    return new PowerFunction(func1.getCoefficient() * func2.getCoefficient(), func1.withoutCoeff(), 2);
                                }
                                case "cot" -> {
                                    return new TrigonometricFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput(), "cos");
                                }
                                case "sec" -> {
                                    return new TrigonometricFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput(), "tan");
                                }
                                case "csc" -> {
                                    if (func1.getInput() == null) return new ConstantFunction(func1.getCoefficient() * func2.getCoefficient());
                                    return multiply(func1.getInput(), func1.getCoefficient() * func2.getCoefficient());
                                }
                            }
                        }
                        case "cos" -> {
                            switch (((TrigonometricFunction)func2).getTrigType()) {
                                case "cos" -> {
                                    return new PowerFunction(func1.getCoefficient() * func2.getCoefficient(), func1.withoutCoeff(), 2);
                                }
                                case "tan" -> {
                                    return new TrigonometricFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput(), "sin");
                                }
                                case "sec" -> {
                                    if (func1.getInput() == null) return new ConstantFunction(func1.getCoefficient() * func2.getCoefficient());
                                    return multiply(func1.getInput(), func1.getCoefficient() * func2.getCoefficient());
                                }
                                case "csc" -> {
                                    return new TrigonometricFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput(), "cot");
                                }
                            }
                        }
                        case "tan" -> {
                            switch (((TrigonometricFunction)func2).getTrigType()) {
                                case "cos" -> {
                                    return new TrigonometricFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput(), "sin");
                                }
                                case "tan" -> {
                                    return new PowerFunction(func1.getCoefficient() * func2.getCoefficient(), func1.withoutCoeff(), 2);
                                }
                                case "cot" -> {
                                    if (func1.getInput() == null) return new ConstantFunction(func1.getCoefficient() * func2.getCoefficient());
                                    return multiply(func1.getInput(), func1.getCoefficient() * func2.getCoefficient());
                                }
                                case "csc" -> {
                                    return new TrigonometricFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput(), "sec");
                                }
                            }
                        }
                        case "cot" -> {
                            switch (((TrigonometricFunction)func2).getTrigType()) {
                                case "sin" -> {
                                    return new TrigonometricFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput(), "cos");
                                }
                                case "tan" -> {
                                    if (func1.getInput() == null) return new ConstantFunction(func1.getCoefficient() * func2.getCoefficient());
                                    return multiply(func1.getInput(), func1.getCoefficient() * func2.getCoefficient());
                                }
                                case "cot" -> {
                                    return new PowerFunction(func1.getCoefficient() * func2.getCoefficient(), func1.withoutCoeff(), 2);
                                }
                                case "sec" -> {
                                    return new TrigonometricFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput(), "csc");
                                }
                            }
                        }
                        case "sec" -> {
                            switch (((TrigonometricFunction)func2).getTrigType()) {
                                case "sin" -> {
                                    return new TrigonometricFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput(), "tan");
                                }
                                case "cos" -> {
                                    if (func1.getInput() == null) return new ConstantFunction(func1.getCoefficient() * func2.getCoefficient());
                                    return multiply(func1.getInput(), func1.getCoefficient() * func2.getCoefficient());
                                }
                                case "cot" -> {
                                    return new TrigonometricFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput(), "csc");
                                }
                                case "sec" -> {
                                    return new PowerFunction(func1.getCoefficient() * func2.getCoefficient(), func1.withoutCoeff(), 2);
                                }
                            }
                        }
                        case "csc" -> {
                            switch (((TrigonometricFunction)func2).getTrigType()) {
                                case "sin" -> {
                                    if (func1.getInput() == null) return new ConstantFunction(func1.getCoefficient() * func2.getCoefficient());
                                    return multiply(func1.getInput(), func1.getCoefficient() * func2.getCoefficient());
                                }
                                case "cos" -> {
                                    return new TrigonometricFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput(), "cot");
                                }
                                case "tan" -> {
                                    return new TrigonometricFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput(), "sec");
                                }
                                case "csc" -> {
                                    return new PowerFunction(func1.getCoefficient() * func2.getCoefficient(), func1.withoutCoeff(), 2);
                                }
                            }
                        }
                    }
                    return new CompoundFunction(true, func1.getCoefficient() * func2.getCoefficient(), new ArrayList<>(Arrays.asList(func1.withoutCoeff(), func2.withoutCoeff())));
                }
                case "exponential" -> {
                    Function inputFunction = func1.getInput();
                    ExponentialFunction exp1 = (ExponentialFunction)func1, exp2 = (ExponentialFunction)func2;
                    int eCoeff1 = approxE(exp1.getBase()), eCoeff2 = approxE(exp2.getBase());
                    // same-coefficient e base
                    if (eCoeff1 != 0 && eCoeff2 != 0) {
                        ExponentialFunction coeff1 = new ExponentialFunction(func1.getCoefficient(), inputFunction, Function.approxE(exp1.getBase())),
                                            coeff2 = new ExponentialFunction(func2.getCoefficient(), inputFunction, Function.approxE(exp2.getBase())),
                                            e = new ExponentialFunction(func2.getCoefficient(), multiply(inputFunction, 2), Math.E);
                        Function r = e;
                        r = multiply(r, coeff1);
                        r = multiply(r, coeff2);
                        return r;
                    }
                    double base;
                    base = Math.pow(exp1.getBase(), func1.getInput() == null ? 1 : func1.getInput().getCoefficient()) *
                            Math.pow(exp2.getBase(), func2.getInput() == null ? 1 : func2.getInput().getCoefficient());
                    if (base == 1) return new ConstantFunction(func1.getCoefficient() * func2.getCoefficient());
                    return new ExponentialFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput() == null ? null : func1.getInput().withoutCoeff(), base);
                }
            }
        }
        // one compound
        if (func1.getType().equals("compound") && !func2.getType().equals("compound")) {
            if (((CompoundFunction)func1).compoundType) {
                double coeff = func1.getCoefficient();
                @SuppressWarnings("unchecked")
                ArrayList<Function> newFunctions = (ArrayList<Function>)((CompoundFunction)func1).getFunctions().clone();
                int indexChange = -1;
                for (int i = 0; i < newFunctions.size(); i++) {
                    if (changes(newFunctions.get(i), func2, true)) {
                        indexChange = i;
                        break;
                    }
                }
                if (indexChange == -1) newFunctions.add(func2);
                else {
                    Function changedFunction = multiply(newFunctions.get(indexChange), func2);
                    coeff *= changedFunction.getCoefficient();
                    changedFunction = changedFunction.withoutCoeff();
                    if (changedFunction.equals(1)) newFunctions.remove(indexChange);
                    else newFunctions.set(indexChange, changedFunction);
                }
                return new CompoundFunction(true, coeff, newFunctions);
            }
            else {
                double coeff = func1.getCoefficient();
                @SuppressWarnings("unchecked")
                ArrayList<Function> newFunctions = (ArrayList<Function>)((CompoundFunction)func1).getFunctions().clone();
                for (int i = 0; i < newFunctions.size(); i++)
                    newFunctions.set(i, multiply(newFunctions.get(i), func2));
                return new CompoundFunction(false, coeff, newFunctions);
            }
        }
        if (!func1.getType().equals("compound") && func2.getType().equals("compound")) return multiply(func2, func1);
        // both compound
        if (func1.getType().equals("compound") && func2.getType().equals("compound")) {
            if (((CompoundFunction)func1).compoundType) {
                if (((CompoundFunction)func2).compoundType) {
                    Function r = func1;
                    for (Function func: ((CompoundFunction)func2).functions) r = multiply(r, func);
                    return r;
                }
                else {
                    return multiply(func2, func1);
                }
            }
            else {
                if (((CompoundFunction)func2).compoundType) {
                    @SuppressWarnings("unchecked")
                    ArrayList<Function> newFunctions = (ArrayList<Function>)((CompoundFunction)func1).functions.clone();
                    for (int i = 0; i < newFunctions.size(); i++) {
                        newFunctions.set(i, multiply(newFunctions.get(i), func2.withoutCoeff()));
                    }
                    return new CompoundFunction(false, func1.getCoefficient() * func2.getCoefficient(), newFunctions);
                }
                else {
                    ArrayList<Function> multiplyTemps = new ArrayList<>();
                    for (Function func: ((CompoundFunction)func2).functions) multiplyTemps.add(multiply(func1, func));
                    Function r = Function.ZERO;
                    for (Function func: multiplyTemps)r = sum(r, func);
                    return r;
                }
            }
        }
        System.out.println("Unknown function types");
        return null;
    }

    public static Function getDerivative(CompoundFunction func) {
        if (func.compoundType) {
            ArrayList<Function> toAdd = new ArrayList<>();
            for (int i = 0; i < func.getFunctions().size(); i++) {
                @SuppressWarnings("unchecked")
                ArrayList<Function> tempFunctions = (ArrayList<Function>)func.getFunctions().clone();
                Function derivative = Function.getDerivative(tempFunctions.get(i));
                int indexChange = -1;
                for (int j = 0; j < tempFunctions.size(); j++) {
                    if (i != j && changes(tempFunctions.get(j), derivative, true)) {
                        indexChange = j;
                        break;
                    }
                }
                if (indexChange == -1) {
                    tempFunctions.set(i, derivative.withoutCoeff());
                    double coeffI = derivative.getCoefficient();
                    toAdd.add(new CompoundFunction(true, func.getCoefficient() * coeffI, tempFunctions));
                }
                else {
                    Function changedFunction = multiply(tempFunctions.get(indexChange), derivative);
                    tempFunctions.set(indexChange, changedFunction.withoutCoeff());
                    tempFunctions.remove(i);
                    double coeffChange = changedFunction.getCoefficient();
                    toAdd.add(new CompoundFunction(true, func.getCoefficient() * coeffChange, tempFunctions));
                }
            }
            if (toAdd.size() == 1) {
                return toAdd.get(0);
            }
            return new CompoundFunction(false, 1, toAdd);
        }
        else {
            ArrayList<Function> toAdd = new ArrayList<>();
            for (Function component: func.getFunctions())
                toAdd.add((Function)Function.getDerivative(component));
            return new CompoundFunction(false, func.getCoefficient(), toAdd);
        }
    }
}
