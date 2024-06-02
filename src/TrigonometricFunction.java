public class TrigonometricFunction extends Function{
    private final String trigType;

    public TrigonometricFunction(double coefficient, Function input, String trigType) {
        super(coefficient, "trigonometric", input);
        this.trigType = trigType;
    }
    
    public String getTrigType() {
        return this.trigType;
    }

    @Override
    public TrigonometricFunction withoutCoeff() {
        return new TrigonometricFunction(1, this.getInput(), this.trigType);
    }

    public boolean equalsTrigonometric(TrigonometricFunction other) {
        return this.trigType.equals(other.trigType);
    }

    @Override
    public String toString() {
        if (this.getCoefficient() == 0) return "0";
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
        String inner = getInput() == null ? "x" : "(" + getInput().toString() + ")";
        return coeff + this.trigType + inner;
    }

    public static Function getDerivative(TrigonometricFunction func) {
        Function chain1;
        switch (func.trigType) {
            case "sin" -> chain1 = new TrigonometricFunction(func.getCoefficient(), func.getInput(), "cos");
            case "cos" -> chain1 = new TrigonometricFunction(-func.getCoefficient(), func.getInput(), "sin");
            case "tan" -> {
                Function tanChainInner = new TrigonometricFunction(1, func.getInput(), "sec");
                chain1 = new PowerFunction(func.getCoefficient(), tanChainInner, 2);
            }
            case "cot" -> {
                Function cotChainInner = new TrigonometricFunction(1, func.getInput(), "csc");
                chain1 = new PowerFunction(-func.getCoefficient(), cotChainInner, 2);
            }
            case "sec" -> {
                Function secChainAppendix = new TrigonometricFunction(1, func.getInput(), "tan");
                chain1 = CompoundFunction.multiply(func, secChainAppendix);
            }
            case "csc" -> {
                Function cscChainAppendix = new TrigonometricFunction(-1, func.getInput(), "cot");
                chain1 = CompoundFunction.multiply(func, cscChainAppendix);
            }
            default -> {
                System.out.println("Invalid trig type entered");
                chain1 = new Function(0, "default", null);
            }
        }
        Function chain2 = func.getInput() == null ? new ConstantFunction(1) : Function.getDerivative(func.getInput());
        return CompoundFunction.multiply(chain1, chain2);
    }
}