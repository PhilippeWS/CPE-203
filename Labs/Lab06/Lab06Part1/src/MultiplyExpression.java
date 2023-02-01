class MultiplyExpression extends BinaryExpression {
   public MultiplyExpression(final Expression lft, final Expression rht, String operator)
   {
      super(lft, rht, operator);
   }

   @Override
   protected double _applyOperator(double lftEvaulated, double rhtEvaluated) {
      return lftEvaulated * rhtEvaluated;
   }


}

