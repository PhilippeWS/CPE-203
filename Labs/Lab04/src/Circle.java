import java.awt.*;

public class Circle implements Shape {
    private double radius;
    private Point center;
    private Color color;


    public Circle(double radius, Point center, Color color){
        this.radius = radius;
        this.center = center;
        this.color = color;
    }

    public double getRadius() {return this.radius;}

    public Point getCenter() {return this.center;}

    public void setRadius(double radius) {this.radius = radius;}

    @Override
    public Color getColor() {return this.color;}

    @Override
    public void setColor(Color color) {this.color = color;}

    @Override
    public double getArea() {
        return Math.PI*Math.pow(this.radius, 2);
    }

    @Override
    public double getPerimeter() {
        return 2*Math.PI*this.radius;
    }

    @Override
    public void translate(Point destination) {
        this.center.translate((int) (destination.getX() - center.getX()), (int) (destination.getY() - center.getY()));
    }
}
