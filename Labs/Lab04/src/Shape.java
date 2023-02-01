import java.awt.*;

public interface Shape {
    public Color getColor();

    public void setColor(Color color);

    public double getArea();

    public double getPerimeter();

    public void translate(Point destination);
}
