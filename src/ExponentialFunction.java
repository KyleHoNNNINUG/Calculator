public class ExponentialFunction extends Function{
    private final double base;

    public ExponentialFunction(double coefficient, Function input, double base) {
        super(coefficient, "exponential", input);
        this.base = base;
    }
    
    @Override
    public Function withoutCoeff() {
        return new ExponentialFunction(1, getInput(), this.base);
    }

    @Override
    public String toString() {
        if (this.base == 1) {
            return (new ConstantFunction(getCoefficient())).toString();
        }
        String b = "";
        int eQuotient = Function.approxE(this.base, 0.000001);
        if (eQuotient != 0) {
            if (eQuotient != 1) b += eQuotient;
            b += "e";
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
            int checker = (int)getCoefficient();
            if (checker == getCoefficient())
                coeff += checker;
            else
                coeff += getCoefficient();
            b = "(" + b + ")";
        }
        return coeff + b;
    }

    public double getBase() {
        return this.base;
    }
}
