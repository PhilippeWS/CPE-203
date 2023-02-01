import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkSpace {
    private ArrayList<Shape> shapes;

    public WorkSpace(){
        this.shapes = new ArrayList<>();
    }

    public void add(Shape shape){
        this.shapes.add(shape);
    }

    public Shape getShape(int index){
        return this.shapes.get(index);
    }

    public int size(){
        return this.shapes.size();
    }

    public List<Circle> getCircles(){
        List<Circle> circles = new ArrayList<Circle>();
        for (Shape shape : this.shapes) {
            if(shape instanceof Circle){
                circles.add((Circle) shape);
            }
        }
        return circles;
    }

    public List<Rectangle> getRectangles(){
        List<Rectangle> rectangles = new ArrayList<Rectangle>();
        for (Shape shape : this.shapes) {
            if(shape instanceof Rectangle){
                rectangles.add((Rectangle) shape);
            }
        }
        return rectangles;
    }

    public List<Triangle> getTriangles(){
        List<Triangle> triangles = new ArrayList<Triangle>();
        for (Shape shape : this.shapes) {
            if(shape instanceof Triangle){
                triangles.add((Triangle) shape);
            }
        }
        return triangles;
    }

    public List<ConvexPolygon> getConvexPolygons(){
        List<ConvexPolygon> convexPolygons = new ArrayList<ConvexPolygon>();
        for (Shape shape : this.shapes) {
            if(shape instanceof ConvexPolygon){
                convexPolygons.add((ConvexPolygon) shape);
            }
        }
        return convexPolygons;
    }

    public List<Shape> getShapesByColor(Color color){
        List<Shape> coloredShapes = new ArrayList<Shape>();
        for (Shape shape : this.shapes) {
            if(shape.getColor().equals(color)){
                coloredShapes.add(shape);
            }
        }
        return coloredShapes;
    }

    public double getAreaOfAllShapes(){
        double sumOfAreas = 0;
        for(Shape shape : this.shapes){
            sumOfAreas += shape.getArea();
        }
        return sumOfAreas;
    }

    public double getPerimeterOfAllShapes(){
        double sumOfPerimeters = 0;
        for(Shape shape : this.shapes){
            sumOfPerimeters += shape.getPerimeter();
        }
        return sumOfPerimeters;

    }
}
