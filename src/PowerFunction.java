public class PowerFunction extends Function{
    private final double power;

    public PowerFunction(double coefficient, Function input, double power) {
        super(coefficient * (input == null? 1 : Math.pow(input.getCoefficient(), power)), "power", (input == null? null : input.withoutCoeff()));
        this.power = power;
    }
    
    public double getPower() {
        return this.power;
    }

    public boolean equalsPower(PowerFunction other) {
        return this.power == other.power;
    }

    @Override
    public String toString() {
        if (this.getCoefficient() == 0) return "0";
        if (this.equals(1)) return "1";
        String coeff = "";
        if (this.getCoefficient() == -1)
            coeff = "-";
        else if (this.getCoefficient() != 1) {
            int checker = (int)getCoefficient();
            if (checker == getCoefficient())
                coeff += checker;
            else
                coeff += getCoefficient();
        }
        String pow = "";
        if (this.power != 1 && this.power != 0) {
            pow = "^";
            int checker = (int)this.power;
            if (checker == this.power)
                pow += checker;
            else
                pow += this.power;
        }
        if (this.power != 0)
            if (getInput() == null) {
                String inner = "x" + pow;
                if (this.power < 1) inner = "(" + inner + ")";
                return coeff + inner;
            }
            else if (getInput().equals(1))
                return coeff;
            else if (this.power > 0 && getInput().getType().equals("trigonometric")) {
                TrigonometricFunction inner = (TrigonometricFunction)(getInput());
                String innerInner = "(" + (inner.getInput() == null ? "x" : inner.getInput().toString()) + ")";
                return coeff + inner.getTrigType() + pow + innerInner;
            }
            else 
                return coeff + "(" + getInput().toString() + ")" + pow;
        else
            return coeff;
    }

    @Override
    public PowerFunction withoutCoeff() {
        return new PowerFunction(1, this.getInput(), this.power);
    }

    public static Function getDerivative(PowerFunction func) {
        Function chain1;
        if (func.power == 1) chain1 = new ConstantFunction(func.getCoefficient());
        else chain1 = new PowerFunction(func.getCoefficient() * func.power, func.getInput(), func.power-1);
        if (chain1.getCoefficient() == 0) chain1 = Function.ZERO;
        Function chain2 = func.getInput() == null ? new ConstantFunction(1) : Function.getDerivative(func.getInput());
        Function result = CompoundFunction.multiply(chain1, chain2);
        return result.equals(Function.ZERO) ? Function.ZERO : result;
    }
}
