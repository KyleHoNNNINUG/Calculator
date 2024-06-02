public class Function {
    private final double coefficient;
    private final String type;
    private final Function input;
    
    public static final Function IDENTITY = new PowerFunction(1, null, 1); // x
    public static final Function ZERO = new Function(0, "zero", null); // 0

    public Function(double coefficient, String type, Function input) {
        this.coefficient = coefficient;
        this.type = type;
        this.input = input;
    }

    public double getCoefficient() {
        return this.coefficient;
    }
    public String getType() {
        return this.type;
    }
    public Function getInput() {
        return this.input;
    }

    protected static int approxE(double num) {
        return approxE(num, 0.000001);
    }

    // Precondition: num != 0
    protected static int approxE(double num, double tolerance) {
        double qDirect = num / Math.E;
        if (qDirect >= 0) qDirect += 0.5;
        else qDirect -= 0.5;
        int qCompare = (int)qDirect;
        if (Math.abs(qCompare * Math.E - num) <= tolerance) return qCompare;
        return 0;
    }

    protected static int approx1(double num) {
        return approx1(num, 0.000001);
    }

    protected static int approx1(double num, double tolerance) {
        return Math.abs(num - 1) <= tolerance ? 1 : 0;
    }

    public static void printEquation(Function func1, Function func2) {
        if (func1 == null) func1 = Function.IDENTITY;
        if (func2 == null) func2 = Function.IDENTITY;
        String str1 = func1.toString(), str2 = func2.toString();
        String equality = func1.equals(func2) ? " = " : " ≠ ";
        System.out.println(str1 + equality + str2);
    }

    public boolean equals(double num) {
        return this.equals(new ConstantFunction(num));
    }

    public boolean equals(Function other) {
        // null
        if (other == null) return this.equals(new PowerFunction(1, null, 1));
        // zero
        if (this.coefficient == 0 && other.coefficient == 0) return true;
        if (this.type.equals("exponential") && ((ExponentialFunction)this).getBase() == 0) return other.equals(Function.ZERO);
        if (other.type.equals("exponential") && ((ExponentialFunction)other).getBase() == 0) return this.equals(Function.ZERO);
        // constant in form of power function
        if (this.type.equals("power"))
            if (((PowerFunction)this).getPower() == 0)
                return other.equals(this.coefficient);
            else if (this.input != null && this.input.getType().equals("constant"))
                return other.equals(Math.pow(this.input.coefficient, ((PowerFunction)this).getPower()) * this.coefficient);
        if (other.type.equals("power"))
            if (((PowerFunction)other).getPower() == 0)
                return this.equals(other.coefficient);
            else if (other.input != null && other.input.getType().equals("constant"))
                return this.equals(Math.pow(other.input.coefficient, ((PowerFunction)other).getPower()) * other.coefficient);
        // one-element compound function
        if (this.type.equals("compound") && ((CompoundFunction)this).getFunctions().size() == 1)
            return CompoundFunction.multiply(((CompoundFunction)this).getFunctions().get(0), this.coefficient).equals(other);
        if (other.type.equals("compound") && ((CompoundFunction)other).getFunctions().size() == 1)
            return CompoundFunction.multiply(((CompoundFunction)other).getFunctions().get(0), other.coefficient).equals(this);
        // unequal function types
        if (!this.type.equals(other.getType())) return false;
        // respective criteria for different function types
        switch(this.type) {
            case "power" -> {
                if (this.coefficient != other.coefficient) return false;
                if (!((PowerFunction)this).equalsPower((PowerFunction)other)) return false;
            }
            case "trigonometric" -> {
                if (this.coefficient != other.coefficient) return false;
                if (!((TrigonometricFunction)this).equalsTrigonometric((TrigonometricFunction)other)) return false;
            }
            case "logarithmic" -> {
                if (this.coefficient != 1) {
                    PowerFunction newInput = new PowerFunction(1, this.input, this.coefficient);
                    LogarithmicFunction newThis = new LogarithmicFunction(1, newInput, ((LogarithmicFunction)this).getBase());
                    return newThis.equals(other);
                }
                if (other.coefficient != 1) {
                    PowerFunction newInput = new PowerFunction(1, other.input, other.coefficient);
                    LogarithmicFunction newOther = new LogarithmicFunction(1, newInput, ((LogarithmicFunction)other).getBase());
                    return this.equals(newOther);
                }
                if (!((LogarithmicFunction)this).equalsLogarithmic((LogarithmicFunction)other)) return false;
            }
            case "exponential" -> {
                if (this.coefficient != other.coefficient) return false;
                if (this.input != null && this.input.coefficient != 1) {
                    double newBase = Math.pow(((ExponentialFunction)this).getBase(), this.input.coefficient);
                    ExponentialFunction newThis = new ExponentialFunction(this.coefficient, this.input.withoutCoeff(), newBase);
                    return newThis.equals(other);
                }
                if (other.input != null && other.input.coefficient != 1) {
                    double newBase = Math.pow(((ExponentialFunction)other).getBase(), other.input.coefficient);
                    ExponentialFunction newOther = new ExponentialFunction(other.coefficient, other.input.withoutCoeff(), newBase);
                    return newOther.equals(other);
                }
                if (!((ExponentialFunction)this).equalsExponential((ExponentialFunction)other)) return false;
            }
            case "compound" -> {
                if (!((CompoundFunction)this).equalsCompound((CompoundFunction)other)) return false;
            }
            case "constant" -> {
                return this.coefficient == other.coefficient;
            }
        }
        // compare input
        if ((this.input == null || this.input.equals((Function)null)) ^ (other.input == null || other.input.equals((Function)null))) return false;
        if ((this.input == null || this.input.equals((Function)null)) && (other.input == null || other.input.equals((Function)null))) return true;
        return this.input.equals(other.input);
    }

    @Override
    public String toString() {
        if (this.coefficient == 0) return "0";
        switch(this.type) {
            case "power" -> {
                return ((PowerFunction)this).toString();
            }
            case "trigonometric" -> {
                return ((TrigonometricFunction)this).toString();
            }
            case "logarithmic" -> {
                return ((LogarithmicFunction)this).toString();
            }
            case "exponential" -> {
                return ((ExponentialFunction)this).toString();
            }
            case "compound" -> {
                return ((CompoundFunction)this).toString();
            }
            case "zero" -> {                
                return "0";
            }
            case "constant" -> {
                if (approxE(this.coefficient) != 0) {
                    if (approxE(this.coefficient) == 1) return "e";
                    else if (approxE(this.coefficient) == -1) return "-e";
                    return approxE(this.coefficient) + "e";
                }
                int checker = (int)this.coefficient;
                if (checker == this.coefficient) return checker + "";
                return this.coefficient + "";
            }
            default -> {
                System.out.println(this.type);
                return "Unknown function type";
            }
        }
    }

    public Function withoutCoeff() {
        switch (this.type) {
            case "power" -> {
                return ((PowerFunction)this).withoutCoeff();
            }
            case "trigonometric" -> {
                return ((TrigonometricFunction)this).withoutCoeff();
            }
            case "logarithmic" -> {
                return ((LogarithmicFunction)this).withoutCoeff();
            }
            case "exponential" -> {
                return ((ExponentialFunction)this).withoutCoeff();
            }
            case "compound" -> {
                return ((CompoundFunction)this).withoutCoeff();
            }
            case "zero" -> {
                return Function.ZERO;
            }
            case "constant" -> {
                return new ConstantFunction(1);
            }
            default -> {
                System.out.println(this.type + "\nUnknown function type");
                return new Function(1, this.type, this.input);
            }
        }
    }

    public static Function getDerivative(Function func) {
        if (func == null) return new ConstantFunction(1);
        if (func.coefficient != 1) return CompoundFunction.multiply(getDerivative(func.withoutCoeff()), func.coefficient);
        switch(func.type) {
            case "power" -> {
                return PowerFunction.getDerivative((PowerFunction)func);
            }
            case "trigonometric" -> {
                return TrigonometricFunction.getDerivative((TrigonometricFunction)func);
            }
            case "logarithmic" -> {
                return LogarithmicFunction.getDerivative((LogarithmicFunction)func);
            }
            case "exponential" -> {
                return ExponentialFunction.getDerivative((ExponentialFunction)func);
            }
            case "compound" -> {
                return CompoundFunction.getDerivative((CompoundFunction)func);
            }
            case "zero" -> {
                return Function.ZERO;
            }
            case "constant" -> {
                return Function.ZERO;
            }
        }
        return new Function(0, "default", null);
    }

    public static Function getDerivative(Function func, int degree) {
        for (int i = 1; i <= degree; i++)
            func = getDerivative(func);
        return func;
    }

    public static void printDerivative(Function func) {
        printDerivative(func, "f");
    }

    public static void printDerivative(Function func, String funcName) {
        System.out.println(funcName + "(x) = " + (func == null ? "x" : func));
        System.out.println(funcName + "'(x) = " + getDerivative(func));
    }

    public static void printDerivative(Function func, int degree) {
        printDerivative(func, "f", degree);
    }

    public static void printDerivative(Function func, String funcName, int degree) {
        String d = "";
        for (int i = 0; i <= degree; i++) {
            if (i >= 1 && i <= 3) d += "'";
            else if (i > 3) d = "^(" + i + ")";
            System.out.println(funcName + d + "(x) = " + func);
            func = getDerivative(func);
        }
    }
}