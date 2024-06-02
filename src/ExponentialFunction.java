public class ExponentialFunction extends Function{
    private final double base;
    public static final ExponentialFunction E_TO_X = new ExponentialFunction(1, null, Math.E);

    public ExponentialFunction(double coefficient, Function input, double base) {
        super(coefficient, "exponential", input);
        this.base = base;
    }

    public ExponentialFunction(double coefficient, Function input, String base) {
        super(base.equals("x") ? coefficient : 0, base.equals("x") ? "exponential" : "default",
            base.equals("x") ? CompoundFunction.multiply(input, new LogarithmicFunction(1, null, Math.E)) : null);
        if (base.equals("x")) this.base = Math.E;
        else {
            System.out.println("Unknown base");
            this.base = 0;
        }
    }
    
    @Override
    public Function withoutCoeff() {
        return new ExponentialFunction(1, getInput(), this.base);
    }

    public boolean equalsExponential(ExponentialFunction other) {
        return Function.approx1(this.base / other.base) == 1;
    }

    @Override
    public String toString() {
        if (this.getCoefficient() == 0) return "0";
        if (this.base == 1) {
            return (new ConstantFunction(getCoefficient())).toString();
        }
        
        if (getInput() != null && getInput().getType().equals("compound") && ((CompoundFunction)getInput()).getCompoundType().equals("multiply")) {
            CompoundFunction inputClone = ((CompoundFunction)getInput()).clone();
            if (inputClone.pickOut(new LogarithmicFunction(1, null, this.base))) {
                String power = "";
                if (!inputClone.getFunctions().isEmpty()) power = inputClone.toString();
                if (!power.equals("x")) power = "(" + power + ")";
                if (!power.equals("")) power = "^" + power;
                String b = "x" + power;
                String coeff = "";
                if (this.getCoefficient() != 1) {
                    if (this.getCoefficient() == -1)
                        coeff += "-";
                    else {
                        int checker = (int)getCoefficient();
                        if (checker == getCoefficient())
                            coeff += checker;
                        else
                            coeff += getCoefficient();
                        b = "(" + b + ")";
                    }
                }
                return coeff + b;
            }
        }
        if (getInput() != null && getInput().getType().equals("logarithmic") && this.base == ((LogarithmicFunction)getInput()).getBase()) {
            Function newWithoutCoeff = getInput().getInput();
            if (getInput().getCoefficient() != 1) newWithoutCoeff = new PowerFunction(1, newWithoutCoeff, getInput().getCoefficient());
            return (CompoundFunction.multiply(newWithoutCoeff, this.getCoefficient())).toString();
        }

        String b = "";
        int eQuotient = Function.approxE(this.base);
        if (eQuotient != 0) {
            if (eQuotient != 1) b += eQuotient;
            b += "e";
            if (eQuotient != 1) b = "(" + b + ")";
        }
        else if (this.base != 1) {
            int checker = (int)this.base;
            if (checker == this.base)
                b += checker;
            else
                b += this.base;
        }
        String power = getInput() == null || getInput().toString().equals("x") ? "x" : "(" + getInput().toString() + ")";
        b = b + "^" + power;
        String coeff = "";
        if (this.getCoefficient() != 1) {
            if (this.getCoefficient() == -1)
                coeff += "-";
            else {
                int checker = (int)getCoefficient();
                if (checker == getCoefficient())
                    coeff += checker;
                else
                    coeff += getCoefficient();
                b = "(" + b + ")";
            }
        }
        return coeff + b;
    }

    public double getBase() {
        return this.base;
    }

    public static Function getDerivative(ExponentialFunction func) {
        Function chain1 = CompoundFunction.multiply(func, new LogarithmicFunction(1, new ConstantFunction(func.base), Math.E));
        Function chain2 = func.getInput() == null ? new ConstantFunction(1) : Function.getDerivative(func.getInput());
        Function result = CompoundFunction.multiply(chain1, chain2);
        return result.equals(Function.ZERO) ? Function.ZERO : result;
    }
}

