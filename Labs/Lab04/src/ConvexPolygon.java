import java.awt.*;

public class ConvexPolygon implements Shape {
    //Always given counter clock wise
    private Point[] points;
    private Color color;

    public ConvexPolygon(Point[] points, Color color){
        this.points = points;
        this.color = color;
    }

    @Override
    public Color getColor() {return this.color;}

    @Override
    public void setColor(Color color) {this.color = color;}

    @Override
    public double getArea() {
        double area = 0;
        for(int index = 0; index <= this.points.length-1; index++){
            area += this.points[index].getX()*this.points[index+1 < this.points.length ? index+1 : 0].getY()
                    - this.points[index+1 < this.points.length ? index+1 : 0].getX()*this.points[index].getY();
        }
        area *= .5;
        return area;
}

    @Override
    public double getPerimeter() {
        double perimeter = 0;
        for(int index = 0; index < this.points.length; index++){
            perimeter += this.points[index].distance(this.points[index+1 < this.points.length ? index+1 : 0]);
        }

        return perimeter;
    }

    @Override
    public void translate(Point destination) {
        for(Point point : this.points){
            point.translate((int) destination.getX(), (int) destination.getY());

        }
    }

    public Point getVertex(int index){
        return points[index];
    }
}
