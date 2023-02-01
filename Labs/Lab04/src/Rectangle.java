import java.awt.*;

public class Rectangle implements Shape{
    private double width;
    private double height;
    private Point topLeft;
    private Color color;

    public Rectangle(double width, double height, Point topLeft, Color color){
        this.width = width;
        this.height = height;
        this.topLeft = topLeft;
        this.color = color;
    }

    public double getWidth() {return this.width;}

    public double getHeight() {return this.height;}

    public void setWidth(double width){this.width = width;}

    public void setHeight(double height){this.height = height;}

    public Point getTopLeft() {return topLeft;}

    @Override
    public Color getColor() {return this.color;}

    @Override
    public void setColor(Color color) { this.color = color; }

    @Override
    public double getArea() { return this.height*this.width; }

    @Override
    public double getPerimeter() { return this.height*2 + this.width*2; }

    @Override
    public void translate(Point destination) {
        this.topLeft.translate((int) destination.getX(), (int) destination.getY());
    }
}
