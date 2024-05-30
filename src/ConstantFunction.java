public class ConstantFunction extends Function{
    public ConstantFunction(double coefficient) {
        super(coefficient, "constant", null);
    }
    public static Function getDerivative(ConstantFunction func) {
        return Function.ZERO;
    }
}
