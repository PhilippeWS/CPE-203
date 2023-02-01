import java.util.stream.Collectors;

final class Point
{
   private final int x;
   private final int y;

   private double gValue;
   private double hValue;
   private double fCost = gValue + hValue;

   public Point parentPoint;

   public Point(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public int getX() {return x;}

   public int getY() {return y;}

   public double getGValue() {return gValue;}

   public double getHValue() {return hValue;}

   public double getFCost() {return fCost;}

   public void setGValue(double gValue) {this.gValue = gValue;}

   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other) {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode() {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

   public boolean adjacent(Point p2) {
      return (this.x == p2.x && Math.abs(this.y - p2.y) == 1) ||
              (this.y == p2.y && Math.abs(this.x - p2.x) == 1);
   }

   public int distanceSquared(Point p2) {
      int deltaX = this.x - p2.x;
      int deltaY = this.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }

   public void setUpPointValues(double gValue, double hValue, Point parentPoint){
      this.gValue = gValue;
      this.hValue = hValue;
      this.fCost = gValue + hValue;
      this.parentPoint = parentPoint;
   }

   public static boolean neighbors(Point p1, Point p2)
   {
      for(Point point : MovableEntity.DIAGONAL_CARDINAL_NEIGHBORS.apply(p1).collect(Collectors.toList())){
         if(p2.equals(point)){
            return true;
         }
      }
      return false;

   }
}
