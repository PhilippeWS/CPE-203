final class Point
{
   public final int x;
   public final int y;

   public double gValue;
   public double hValue;
   public double fCost = gValue + hValue;

   public Point parentPoint;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

   public boolean adjacent(Point p)
   {
      return (x == p.x && Math.abs(y - p.y) == 1) ||
              (y == p.y && Math.abs(x - p.x) == 1);
   }

   //Euclidean Distance
   public double distance(Point secondPoint){

      return Math.sqrt(Math.pow(secondPoint.x - this.x, 2) + Math.pow(secondPoint.y - this.y, 2));
   }

   public void setUpPoint(double gValue, double hValue, Point parentPoint){
      this.gValue = gValue;
      this.hValue = hValue;
      this.fCost = gValue + hValue;
      this.parentPoint = parentPoint;
   }
}
