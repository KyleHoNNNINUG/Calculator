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

    // Precondition: ArrayList<Function> functions of both CompoundFunction objects have more than 1 element
    public boolean equalsCompound(Function other) {
        if (other.getType().equals("compound")) {
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
            return false;
        }
        else {
            
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public boolean equals(CompoundFunction other) {
        if (this.getCoefficient() != other.getCoefficient()) return false;
        ArrayList<Function> functionsThis = (ArrayList<Function>)this.getFunctions().clone(), functionsOther = (ArrayList<Function>)other.getFunctions().clone();
        if (functionsThis.size() != functionsOther.size()) return false;
        for (int i = 0; i < functionsThis.size(); i++) {
            Function curr1 = functionsThis.get(i);
            int index2 = -1;
            for (int j = 0; j < functionsThis.size(); j++) {
                if (curr1.equals(functionsOther.get(j))) {
                    index2 = j;
                    break;
                }
            }
            if (index2 == -1) return false;
            functionsThis.remove(i);
            functionsOther.remove(index2);
            i--;
        }
        return true;
    }




    @Override
    public String toString() {
        String r = "";
        if (this.compoundType) {
            double coeff = this.getCoefficient();
            for (Function func: this.functions) {
                coeff *= func.getCoefficient();
                r += "(" + func.withoutCoeff().toString() + ")";
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
                    r += checker + "(";
                else
                    r += Math.abs(this.getCoefficient()) + "(";
            }
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
            if (Math.abs(this.getCoefficient()) != 1) r += ")";
        }
        return r;
    }


    public static Function multiply(Function func, double coeff) {
        if (coeff == 1) return func;
        if (coeff == 0) return Function.ZERO;
        switch (func.getType()) {
            case "power" -> {
                return new PowerFunction(func.getCoefficient() * coeff, func.getInput(), ((PowerFunction)func).getPower());
            }
            case "trigonometric" -> {
                return new TrigonometricFunction(func.getCoefficient() * coeff, func.getInput(), ((TrigonometricFunction)func).getTrigType());
            }
            case "compound" -> {
                return new CompoundFunction(((CompoundFunction)func).getCompoundType().equals("multiply"), func.getCoefficient() * coeff, ((CompoundFunction)func).getFunctions());
            }
            case "identity" -> {
                return new ConstantFunction(coeff);
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
        // null/identity function case (turn back to power function)
        if (func1 == null || func1.getType().equals("identity")) return CompoundFunction.multiply(new PowerFunction(1, null, 1), func2);
        if (func2 == null || func2.getType().equals("identity")) return CompoundFunction.multiply(func1, new PowerFunction(1, null, 1));
        // zero function case
        if (func1.equals(Function.ZERO) || func2.equals(Function.ZERO)) return Function.ZERO;
        // constant function case
        if (func1.getType().equals("constant")) return CompoundFunction.multiply(func2, func1.getCoefficient());
        if (func2.getType().equals("constant"))
            return CompoundFunction.multiply(func1, func2.getCoefficient());
        // different type functions case (except compound)
        if (!func1.getType().equals("compound") && 
            !func2.getType().equals("compound") &&
            (!func1.getType().equals(func2.getType()) ||
            ((func1.getInput() == null) ^ (func2.getInput() == null)) || 
            (func1.getInput() != null && !func1.getInput().equals(func2.getInput())))) 
        {
            // power of the other
            if (func1.getInput() != null && func1.getType().equals("power"))
                if (func1.getInput().equals(func2.withoutCoeff()))
                    return new PowerFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput(), ((PowerFunction)func1).getPower() + 1);
            if (func2.getInput() != null && func2.getType().equals("power"))
                if (func2.getInput().equals(func1.withoutCoeff()))
                    return new PowerFunction(func1.getCoefficient() * func2.getCoefficient(), func2.getInput(), ((PowerFunction)func2).getPower() + 1);
            // exponential with identical input if without coefficient
            if (func1.getType().equals("exponential") && func2.getType().equals("exponential") && ((func1.getInput() == null && func2.getInput() == null) ||
                func1.getInput().withoutCoeff().equals(func2.getInput().withoutCoeff()))) {
                    double base1 = Math.pow(((ExponentialFunction)func1).getBase(), func1.getInput() == null ? 0 : func1.getInput().getCoefficient());
                    double base2 = Math.pow(((ExponentialFunction)func2).getBase(), func2.getInput() == null ? 0 : func2.getInput().getCoefficient());
                    if (base1 * base2 == 1) return new ConstantFunction(func1.getCoefficient() * func2.getCoefficient());
                    return new ExponentialFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput().withoutCoeff(), base1 * base2);
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
                                    return CompoundFunction.multiply(func1.getInput(), func1.getCoefficient() * func2.getCoefficient());
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
                                    return CompoundFunction.multiply(func1.getInput(), func1.getCoefficient() * func2.getCoefficient());
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
                                    return CompoundFunction.multiply(func1.getInput(), func1.getCoefficient() * func2.getCoefficient());
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
                                    return CompoundFunction.multiply(func1.getInput(), func1.getCoefficient() * func2.getCoefficient());
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
                                    return CompoundFunction.multiply(func1.getInput(), func1.getCoefficient() * func2.getCoefficient());
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
                                    return CompoundFunction.multiply(func1.getInput(), func1.getCoefficient() * func2.getCoefficient());
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
                    double base;
                    base = Math.pow(((ExponentialFunction)func1).getBase(), func1.getInput() == null ? 1 : func1.getInput().getCoefficient()) *
                            Math.pow(((ExponentialFunction)func2).getBase(), func2.getInput() == null ? 1 : func2.getInput().getCoefficient());
                    if (base == 1) return new ConstantFunction(func1.getCoefficient() * func2.getCoefficient());
                    return new ExponentialFunction(func1.getCoefficient() * func2.getCoefficient(), func1.getInput() == null ? null : func1.getInput().withoutCoeff(), base);
                }
            }
        }
        // compound
        if (func1.getType().equals("compound")) {
            if (((CompoundFunction)func1).compoundType) {
                switch(func2.getType()) {

                }
            }
        }
        return func1;
    }

    public static Function getDerivative(CompoundFunction func) {
        if (func.compoundType) {
            ArrayList<Function> toAdd = new ArrayList<>();
            for (int i = 0; i < func.getFunctions().size(); i++) {
                @SuppressWarnings("unchecked")
                ArrayList<Function> temp = (ArrayList<Function>)func.getFunctions().clone();
                Function derivative = Function.getDerivative(temp.get(i));
                double coeffI = derivative.getCoefficient();
                temp.set(i, derivative.withoutCoeff());
                toAdd.add(new CompoundFunction(true, func.getCoefficient() * coeffI, temp));
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
