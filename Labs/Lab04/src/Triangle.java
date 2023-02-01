import java.awt.*;

public class Triangle implements Shape {
    //Verticie given in counter-clockwise
    private Point vertexA;
    private Point vertexB;
    private Point vertexC;
    private Color color;

    public Triangle(Point vertexA, Point vertexB, Point vertexC, Color color){
        this.vertexA = vertexA;
        this.vertexB = vertexB;
        this.vertexC = vertexC;
        this.color = color;
    }

    public Point getVertexA() {return vertexA;}

    public Point getVertexB() {return vertexB;}

    public Point getVertexC() {return vertexC;}

    @Override
    public Color getColor() { return this.color; }

    @Override
    public void setColor(Color color) { this.color = color; }

    @Override
    public double getArea() {
        return .5*Math.abs(this.vertexA.getX()*(this.vertexB.getY()-this.vertexC.getY())
                + this.vertexB.getX()*(this.vertexC.getY()-this.vertexA.getY())
                + this.vertexC.getX()*(this.vertexA.getY()-this.vertexB.getY()));
    }

    @Override
    public double getPerimeter() {
        return this.vertexA.distance(this.vertexB)
                + this.vertexB.distance(this.vertexC)
                + this.vertexC.distance(this.vertexA);
    }

    @Override
    public void translate(Point destination) {
        this.vertexA.translate((int) destination.getX(), (int) destination.getY());
        this.vertexB.translate((int) destination.getX(), (int) destination.getY());
        this.vertexC.translate((int) destination.getX(), (int) destination.getY());
    }
}
