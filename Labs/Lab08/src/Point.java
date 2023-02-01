final class Point
{
   private double x;
   private double y;
   private double z;

   public Point(double x, double y)
   {
      this.x = x;
      this.y = y;
      this.z = 0;
   }

   public Point(double x, double y, double z)
   {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public double getX() {return x;}

   public double getY() {return y;}

   public double getZ() {return z;}

   public String toString()
   {
      return "(" + x + "," + y + ", " + z + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y &&
         ((Point)other).z == this.z;
   }

   public int hashCode()
   {
      double result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      result = result * 31 + z;
      return (int)result;
   }

   public Point scale(double scalar){
      this.x *= scalar;
      this.y *= scalar;
      this.z *= scalar;
      return this;
   }



   public Point translate(Point vector){
      this.x += vector.x;
      this.y += vector.y;
      this.z += vector.z;
      return this;
   }

}
