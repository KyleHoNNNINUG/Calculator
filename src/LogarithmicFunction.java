public class LogarithmicFunction extends Function{
    private final double base;

    public LogarithmicFunction(double coefficient, Function input, double base) {
        super(coefficient, "logarithmic", input);
        this.base = base;
    }
    
    public double getBase() {
        return this.base;
    }
}