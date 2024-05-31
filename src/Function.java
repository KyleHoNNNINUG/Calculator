public class Function {
    private final double coefficient;
    private final String type;
    private final Function input;
    
    public static final Function IDENTITY = new Function(1, "identity", null); // x
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

    protected static int approxE(double num, double tolerance) {
        double qDirect = num / Math.E;
        if (qDirect >= 0) qDirect += 0.5;
        else qDirect -= 0.5;
        int qCompare = (int)qDirect;
        if (Math.abs(qCompare * Math.E - num) <= tolerance) return qCompare;
        return 0;
    }
    protected static int approx1(double num, double tolerance) {
        return Math.abs(num - 1) <= tolerance ? 1 : 0;
    }

    public boolean equals(double num) {
        return this.equals(new ConstantFunction(num));
    }

    public boolean equals(Function other) {
        // identity/null
        if (other == null) return this.type.equals("identity");
        // zero
        if (this.coefficient == 0 && other.getCoefficient() == 0) return true;
        // identity/constant equalling power function
        if (this.type.equals("power"))
            if (((PowerFunction)this).getPower() == 0)
                return other.equals(this.coefficient);
            else if (this.input != null && this.input.getType().equals("constant"))
                return other.equals(Math.pow(this.input.getCoefficient(), ((PowerFunction)this).getPower()) * this.getCoefficient());
            else if (this.input == null && this.coefficient == 1 && ((PowerFunction)this).getPower() == 1 && other.type.equals("identity"))
                return true;
        if (other.type.equals("power"))
            if (((PowerFunction)other).getPower() == 0)
                return this.equals(other.coefficient);
            else if (other.input != null && other.input.getType().equals("constant"))
                return this.equals(Math.pow(other.input.getCoefficient(), ((PowerFunction)other).getPower()) * other.getCoefficient());
            else if (other.input == null && other.coefficient == 1 && ((PowerFunction)other).getPower() == 1 && this.type.equals("identity"))
                return true;
        // one-element compound function
        if (this.type.equals("compound") && ((CompoundFunction)this).getFunctions().size() == 1)
            return CompoundFunction.multiply(((CompoundFunction)this).getFunctions().get(0), this.getCoefficient()).equals(other);
        if (other.type.equals("compound") && ((CompoundFunction)other).getFunctions().size() == 1)
            return CompoundFunction.multiply(((CompoundFunction)other).getFunctions().get(0), other.getCoefficient()).equals(this);
        // unequal function types
        if (!this.type.equals(other.getType())) return false;
        // respective criteria for different function types
        switch(this.type) {
            case "power" -> {
                if (this.getCoefficient() != other.getCoefficient()) return false;
                if (!((PowerFunction)this).equalsPower((PowerFunction)other)) return false;
            }
            case "trigonometric" -> {
                if (this.getCoefficient() != other.getCoefficient()) return false;
                if (!((TrigonometricFunction)this).equalsTrigonometric((TrigonometricFunction)other)) return false;
            }
            case "compound" -> {
                if (!((CompoundFunction)this).equalsCompound((CompoundFunction)other)) return false;
            }
            case "identity" -> {
                return true;
            }
            case "constant" -> {
                return this.getCoefficient() == other.getCoefficient();
            }
        }
        // compare input
        if (this.input == null ^ other.getInput() == null) return false;
        if (this.input == null && other.getInput() == null) return true;
        return this.input.equals(other.getInput());
    }

    @Override
    public String toString() {
        if (this.getCoefficient() == 0) return "0";
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
            case "identity" -> {
                return "x";
            }
            case "zero" -> {                
                return "0";
            }
            case "constant" -> {
                int checker = (int)getCoefficient();
                if (checker == getCoefficient()) return checker + "";
                return getCoefficient() + "";
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
            case "identity" -> {
                return Function.IDENTITY;
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
        switch(func.type) {
            case "power" -> {
                return PowerFunction.getDerivative((PowerFunction)func);
            }
            case "trigonometric" -> {
                return TrigonometricFunction.getDerivative((TrigonometricFunction)func);
            }
            case "compound" -> {
                return CompoundFunction.getDerivative((CompoundFunction)func);
            }
            case "identity" -> {
                return new ConstantFunction(1);
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
        for (int i = 0; i < degree; i++)
            func = getDerivative(func);
        return func;
    }
}