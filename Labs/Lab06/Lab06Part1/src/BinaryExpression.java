public abstract class BinaryExpression implements Expression{
    private final Expression lft;
    private final Expression rht;
    private final String operator;

    public BinaryExpression(Expression lft, Expression rht, String operator){
        this.lft = lft;
        this.rht = rht;
        this.operator = operator;
    }

    public String getOperator(){return this.operator;}

    public String toString(){
        return "(" + this.lft + this.operator + this.rht + ")";
    }

    @Override
    public double evaluate(Bindings bindings) {
        return this._applyOperator(this.lft.evaluate(bindings), this.rht.evaluate(bindings));
    }

    protected abstract double _applyOperator(double lftEvaulated, double rhtEvaluated);
}
