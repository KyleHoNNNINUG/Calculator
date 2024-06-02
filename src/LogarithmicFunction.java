

public class LogarithmicFunction extends Function{
    private final double base;
    public static final LogarithmicFunction LN_X = new LogarithmicFunction(1, null, Math.E);

    public LogarithmicFunction(double coefficient, Function input, double base) {
        super(coefficient, "logarithmic", input);
        this.base = base;
    }

    public LogarithmicFunction(double coefficient, Function input) {
        super(coefficient, "logarithmic", input);
        this.base = 10;
    }

    @Override
    public Function withoutCoeff() {
        return new LogarithmicFunction(1, this.getInput(), this.base);
    }

    public boolean equalsLogarithmic(LogarithmicFunction other) {
        return Function.approx1(this.base / other.base) == 1;
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
        String inner;
        if (getInput() == null) inner = "(x)";
        else if (getInput().getType().equals("constant")) inner = getInput().toString();
        else inner = "(" + getInput() + ")";
        String log;
        if (Function.approxE(this.base) == 1) {
            log = "ln";
        }
        else if (this.base == 10) {
            log = "log";
        }
        else {
            log = "log_";
            int checker = (int)this.base;
            if (checker == this.base)
                log += checker;
            else
                log += this.base;
            if (getInput().getType().equals("constant")) inner = "(" + inner + ")";
        }
        return coeff + log + inner;
    }
    
    public double getBase() {
        return this.base;
    }

    public static Function getDerivative(LogarithmicFunction func) {
        Function denominator = func.getInput();
        if (Function.approxE(func.base) == 0) 
            denominator = CompoundFunction.multiply(denominator, new LogarithmicFunction(1, new ConstantFunction(func.base), Math.E));
        else if (Function.approxE(func.base) != 1) {
            Function toMultiply = CompoundFunction.sum(new LogarithmicFunction(1, new ConstantFunction(Function.approxE(func.base)), Math.E), 1);
            denominator = CompoundFunction.multiply(denominator, toMultiply);
        }
        Function chain1 = new PowerFunction(func.getCoefficient(), denominator, -1);
        Function chain2 = func.getInput() == null ? new ConstantFunction(1) : Function.getDerivative(func.getInput());
        Function result = CompoundFunction.multiply(chain1, chain2);
        return result.equals(Function.ZERO) ? Function.ZERO : result;
    }
}