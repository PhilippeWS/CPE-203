import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParsePosition;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import java.awt.Color;
import java.awt.Point;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestCases
{
   public static final double DELTA = 0.00001;

   /* some sample tests but you must write more! see lab write up */

   // https://rechneronline.de/pi/simple-polygon.php Website used to check area/perimeter calculations

   @Test
   public void testGetters(){
      WorkSpace ws = new WorkSpace();

      Circle c = new Circle(1.11, new Point(-5, -3), Color.RED);
      ConvexPolygon cp = new ConvexPolygon(new Point[]{new Point(0,4), new Point(-3,2), new Point(-1,-2), new Point(2,-3), new Point(3,1)},
              Color.ORANGE);
      Rectangle r = new Rectangle(1.234, 5.678, new Point(2, 3), Color.GREEN);
      Triangle t = new Triangle(new Point(0,4), new Point(-2,-1), new Point(3, 0),
              Color.BLUE);

      ws.add(c);
      ws.add(cp);
      ws.add(r);
      ws.add(t);

      assertSame(ws.getShape(0), c);
      assertSame(ws.getShape(1), cp);
      assertSame(ws.getShape(2), r);
      assertSame(ws.getShape(3), t);
   }

   //-----Circle Tests-----//
   @Test
   public void testCircleGetArea()
   {
      Circle circle = new Circle(5.678, new Point(2, 3), Color.BLACK);

      assertEquals(101.2839543, circle.getArea(), DELTA);
   }

   @Test
   public void testCircleGetPerimeter()
   {
      Circle circle = new Circle(5.678, new Point(2, 3), Color.BLACK);

      assertEquals(35.6759261, circle.getPerimeter(), DELTA);
   }

   @Test
   public void testTranslateCircle(){
      Point translateTarget = new Point(1,2);
      Circle circle = new Circle(5.678, new Point(2, 3), Color.BLACK);
      circle.translate(translateTarget);

      assertEquals(translateTarget, circle.getCenter());
   }

   //-----Convex Polygon Tests-----//
   @Test
   public void testConvexPolygonGetPerimeter_size1(){
      Point[] vertices = {new Point(0,4), new Point(-3,2), new Point(-1,-2), new Point(2,-3), new Point(3,1)};
      ConvexPolygon convexPolygon = new ConvexPolygon(vertices,Color.MAGENTA);

      assertEquals(19.605711203368895, convexPolygon.getPerimeter(), DELTA);
   }

   @Test
   public void testConvexPolygonGetPerimeter_size2(){
      Point[] vertices = {new Point(-3,1), new Point(-3,-1),
              new Point(-1,-3), new Point(1,-3),
              new Point(3,-1), new Point(3,1),
              new Point(1,3), new Point(-1, 3)};
      ConvexPolygon convexPolygon = new ConvexPolygon(vertices,Color.MAGENTA);

      assertEquals(19.31370849898476, convexPolygon.getPerimeter(), DELTA);
   }

   @Test
   public void testConvexPolygonGetArea_size1(){
      Point[] vertices = {new Point(0,4), new Point(-3,2), new Point(-1,-2), new Point(2,-3), new Point(3,1)};
      ConvexPolygon convexPolygon = new ConvexPolygon(vertices,Color.MAGENTA);

      assertEquals(25, convexPolygon.getArea(), DELTA);
   }

   @Test
   public void testConvexPolygonGetArea_size2(){
      Point[] vertices = {new Point(-3,1), new Point(-3,-1),
              new Point(-1,-3), new Point(1,-3),
              new Point(3,-1), new Point(3,1),
              new Point(1,3), new Point(-1, 3)};
      ConvexPolygon convexPolygon = new ConvexPolygon(vertices,Color.MAGENTA);

      assertEquals(28, convexPolygon.getArea(), DELTA);
   }

   @Test
   public void testTranslateConvexPolygon(){
      Point translateVector = new Point(1,2);
      Point[] vertices = {new Point(0,4), new Point(-3,2), new Point(-1,-2), new Point(2,-3), new Point(3,1)};

      ConvexPolygon convexPolygon = new ConvexPolygon(vertices,Color.MAGENTA);
      convexPolygon.translate(translateVector);

      for (Point point : vertices) {
         point.translate((int) translateVector.getX(),(int) translateVector.getY());
      }

      Point[] currentShapePoints = new Point[vertices.length];
      for (int index = 0; index < vertices.length; index++){
         currentShapePoints[index] = convexPolygon.getVertex(index);
      }

      for(int index = 0; index < vertices.length; index++){
         assertEquals(vertices[index], currentShapePoints[index]);
      }
   }

   //-----Rectangle Tests-----//
   @Test
   public void testRectanglePerimeter(){
      Rectangle rectangle = new Rectangle(3.5, 4.5, new Point(1, 2), Color.CYAN);

      assertEquals(16, rectangle.getPerimeter(), DELTA);
   }

   @Test
   public void testRectangleArea(){
      Rectangle rectangle = new Rectangle(3.5, 4.5, new Point(1, 2), Color.CYAN);

      assertEquals(15.75, rectangle.getArea(), DELTA);
   }

   @Test
   public void testRectangleTranslate(){
      Point translateVector = new Point(3,4);
      Rectangle rectangle = new Rectangle(3.5, 4.5, new Point(1, 2), Color.CYAN);
      rectangle.translate(translateVector);
      assertEquals(new Point(4,6), rectangle.getTopLeft());
   }

   //-----Triangle Tests-----//
   @Test
   public void testTrianglePerimeter(){
      Triangle triangle = new Triangle(new Point(0, 3), new Point(-2, -3), new Point(3, -1), Color.ORANGE);

      assertEquals(16.709720127471265, triangle.getPerimeter(), DELTA);
   }

   @Test
   public void testTriangleArea(){
      Triangle triangle = new Triangle(new Point(0, 3), new Point(-2, -3), new Point(3, -1), Color.ORANGE);

      assertEquals(13, triangle.getArea(), DELTA);
   }

   @Test
   public void testTriangleTranslate(){
      Point translateVector = new Point(1,2);
      Triangle triangle = new Triangle(new Point(0, 3), new Point(-2, -3), new Point(3, -1), Color.ORANGE);
      triangle.translate(translateVector);

      assertEquals(new Point(1,5), triangle.getVertexA());
      assertEquals(new Point(-1,-1), triangle.getVertexB());
      assertEquals(new Point(4,1), triangle.getVertexC());

   }

   //-----Workspace Tests-----//
   @Test
   public void testWorkSpacePerimeterOfAllShapes()
   {
      WorkSpace ws = new WorkSpace();

      ws.add(new Rectangle(1.234, 5.678, new Point(2, 3), Color.BLACK));
      ws.add(new Circle(5.678, new Point(2, 3), Color.BLACK));
      ws.add(new Triangle(new Point(0,0), new Point(2,-4), new Point(3, 0),
              Color.BLACK));
      ws.add(new ConvexPolygon(new Point[]{new Point(0,4), new Point(-3,2), new Point(-1,-2), new Point(2,-3), new Point(3,1)},
              Color.BLACK));

      assertEquals(80.70087895815182, ws.getPerimeterOfAllShapes(), DELTA);
   }

   @Test
   public void testWorkSpaceAreaOfAllShapes()
   {
      WorkSpace ws = new WorkSpace();

      ws.add(new Rectangle(1.234, 5.678, new Point(2, 3), Color.BLACK));
      ws.add(new Circle(5.678, new Point(2, 3), Color.BLACK));
      ws.add(new Triangle(new Point(0,0), new Point(2,-4), new Point(3, 0), 
              Color.BLACK));
      ws.add(new ConvexPolygon(new Point[]{new Point(0,4), new Point(-3,2), new Point(-1,-2), new Point(2,-3), new Point(3,1)},
              Color.BLACK));

      assertEquals(139.2906063, ws.getAreaOfAllShapes(), DELTA);
   }

   @Test
   public void testWorkSpaceGetShapesByColor()
   {
      WorkSpace ws = new WorkSpace();
      List<Shape> expected = new LinkedList<>();

      Circle c1 = new Circle(1.11, new Point(-5, -3), Color.GREEN);
      ConvexPolygon cp1 = new ConvexPolygon(new Point[]{new Point(0,4), new Point(-3,2), new Point(-1,-2), new Point(2,-3), new Point(3,1)},
              Color.GREEN);
      Rectangle r1 = new Rectangle(1.234, 5.678, new Point(2, 3), Color.GREEN);
      Triangle t1 = new Triangle(new Point(0,4), new Point(-2,-1), new Point(3, 0),
              Color.GREEN);

      ws.add(new Circle(5.678, new Point(2, 3), Color.ORANGE));
      ws.add(c1);
      ws.add(new Rectangle(1.234, 5.678, new Point(2, 3), Color.MAGENTA));
      ws.add(cp1);
      ws.add(new Triangle(new Point(0,0), new Point(2,-4), new Point(3, 0),
              Color.YELLOW));
      ws.add(r1);
      ws.add(new ConvexPolygon(new Point[]{new Point(0,4), new Point(-3,2), new Point(-1,-2), new Point(2,-3), new Point(3,1)},
              Color.BLUE));
      ws.add(t1);

      expected.add(c1);
      expected.add(cp1);
      expected.add(r1);
      expected.add(t1);

      assertEquals(expected, ws.getShapesByColor(Color.GREEN));
   }

   @Test
   public void testWorkSpaceGetCircles()
   {
      WorkSpace ws = new WorkSpace();
      List<Circle> expected = new LinkedList<>();

      // Have to make sure the same objects go into the WorkSpace as
      // into the expected List since we haven't overriden equals in Circle.
      Circle c1 = new Circle(5.678, new Point(2, 3), Color.BLACK);
      Circle c2 = new Circle(1.11, new Point(-5, -3), Color.RED);

      ws.add(new Rectangle(1.234, 5.678, new Point(2, 3), Color.BLACK));
      ws.add(c1);
      ws.add(new Triangle(new Point(0,0), new Point(2,-4), new Point(3, 0),
                 Color.BLACK));
      ws.add(c2);
      ws.add(new ConvexPolygon(new Point[]{new Point(0,4), new Point(-3,2), new Point(-1,-2), new Point(2,-3), new Point(3,1)},
              Color.BLACK));

      expected.add(c1);
      expected.add(c2);

      // Doesn't matter if the "type" of lists are different (e.g Linked vs
      // Array).  List equals only looks at the objects in the List.
      assertEquals(expected, ws.getCircles());
   }

   @Test
   public void testWorkSpaceGetConvexPolygons()
   {
      WorkSpace ws = new WorkSpace();
      List<ConvexPolygon> expected = new LinkedList<>();

      ConvexPolygon cp1 = new ConvexPolygon(new Point[]{new Point(1,3), new Point(-4,2), new Point(-1,-2), new Point(3,-1), new Point(4,2)},
              Color.BLACK);
      ConvexPolygon cp2 = new ConvexPolygon(new Point[]{new Point(0,4), new Point(-3,2), new Point(-1,-2), new Point(2,-3), new Point(3,1)},
              Color.RED);

      ws.add(new Rectangle(1.234, 5.678, new Point(2, 3), Color.BLACK));
      ws.add(cp1);
      ws.add(new Triangle(new Point(0,0), new Point(2,-4), new Point(3, 0),
              Color.BLACK));
      ws.add(cp2);
      ws.add(new Circle(5.678, new Point(2, 3), Color.BLACK));

      expected.add(cp1);
      expected.add(cp2);

      assertEquals(expected, ws.getConvexPolygons());
   }

   @Test
   public void testWorkSpaceGetRectangles()
   {
   WorkSpace ws = new WorkSpace();
   List<Rectangle> expected = new LinkedList<>();

   Rectangle r1 = new Rectangle(1.234, 5.678, new Point(2, 3), Color.BLACK);
   Rectangle r2 = new Rectangle(1.234, 5.678, new Point(2, 3), Color.RED);

   ws.add(new Circle(5.678, new Point(2, 3), Color.BLACK));
   ws.add(r1);
   ws.add(new Triangle(new Point(0,0), new Point(2,-4), new Point(3, 0),
           Color.BLACK));
   ws.add(r2);
   ws.add(new ConvexPolygon(new Point[]{new Point(0,4), new Point(-3,2), new Point(-1,-2), new Point(2,-3), new Point(3,1)},
           Color.BLACK));

   expected.add(r1);
   expected.add(r2);

   assertEquals(expected, ws.getRectangles());
}

   @Test
   public void testWorkSpaceGetTriangles()
   {
      WorkSpace ws = new WorkSpace();
      List<Triangle> expected = new LinkedList<>();

      Triangle t1 = new Triangle(new Point(0,0), new Point(2,-4), new Point(3, 0),
              Color.BLACK);
      Triangle t2 = new Triangle(new Point(0,4), new Point(-2,-1), new Point(3, 0),
              Color.RED);

      ws.add(new Circle(5.678, new Point(2, 3), Color.BLACK));
      ws.add(t1);
      ws.add(new Rectangle(1.234, 5.678, new Point(2, 3), Color.BLACK));
      ws.add(t2);
      ws.add(new ConvexPolygon(new Point[]{new Point(0,4), new Point(-3,2), new Point(-1,-2), new Point(2,-3), new Point(3,1)},
              Color.BLACK));

      expected.add(t1);
      expected.add(t2);

      assertEquals(expected, ws.getTriangles());
   }


   /* HINT - comment out implementation tests for the classes that you have not 
    * yet implemented */
   @Test
   public void testCircleImplSpecifics()
      throws NoSuchMethodException
   {
      final List<String> expectedMethodNames = Arrays.asList(
         "getColor", "setColor", "getArea", "getPerimeter", "translate",
         "getRadius", "setRadius", "getCenter");

      final List<Class> expectedMethodReturns = Arrays.asList(
         Color.class, void.class, double.class, double.class, void.class,
         double.class, void.class, Point.class);

      final List<Class[]> expectedMethodParameters = Arrays.asList(
         new Class[0], new Class[] {Color.class}, new Class[0], new Class[0], new Class[] {Point.class},
         new Class[0], new Class[] {double.class}, new Class[0]);

      verifyImplSpecifics(Circle.class, expectedMethodNames,
         expectedMethodReturns, expectedMethodParameters);
   }

   @Test
   public void testRectangleImplSpecifics()
      throws NoSuchMethodException
   {
      final List<String> expectedMethodNames = Arrays.asList(
         "getColor", "setColor", "getArea", "getPerimeter", "translate",
         "getWidth", "setWidth", "getHeight", "setHeight", "getTopLeft");

      final List<Class> expectedMethodReturns = Arrays.asList(
         Color.class, void.class, double.class, double.class, void.class,
         double.class, void.class, double.class, void.class, Point.class);

      final List<Class[]> expectedMethodParameters = Arrays.asList(
         new Class[0], new Class[] {Color.class}, new Class[0], new Class[0], new Class[] {Point.class},
         new Class[0], new Class[] {double.class}, new Class[0], new Class[] {double.class}, new Class[0]);

      verifyImplSpecifics(Rectangle.class, expectedMethodNames,
         expectedMethodReturns, expectedMethodParameters);
   }

   @Test
   public void testTriangleImplSpecifics()
      throws NoSuchMethodException
   {
      final List<String> expectedMethodNames = Arrays.asList(
         "getColor", "setColor", "getArea", "getPerimeter", "translate",
         "getVertexA", "getVertexB", "getVertexC");

      final List<Class> expectedMethodReturns = Arrays.asList(
         Color.class, void.class, double.class, double.class, void.class,
         Point.class, Point.class, Point.class);

      final List<Class[]> expectedMethodParameters = Arrays.asList(
         new Class[0], new Class[] {Color.class}, new Class[0], new Class[0], new Class[] {Point.class},
         new Class[0], new Class[0], new Class[0]);

      verifyImplSpecifics(Triangle.class, expectedMethodNames,
         expectedMethodReturns, expectedMethodParameters);
   }

   @Test
   public void testConvexPolygonImplSpecifics()
      throws NoSuchMethodException
   {
      final List<String> expectedMethodNames = Arrays.asList(
         "getColor", "setColor", "getArea", "getPerimeter", "translate",
         "getVertex");

      final List<Class> expectedMethodReturns = Arrays.asList(
         Color.class, void.class, double.class, double.class, void.class,
         Point.class);

      final List<Class[]> expectedMethodParameters = Arrays.asList(
         new Class[0], new Class[] {Color.class}, new Class[0], new Class[0], new Class[] {Point.class},
         new Class[] {int.class});

      verifyImplSpecifics(ConvexPolygon.class, expectedMethodNames,
         expectedMethodReturns, expectedMethodParameters);
   }

   private static void verifyImplSpecifics(
      final Class<?> clazz,
      final List<String> expectedMethodNames,
      final List<Class> expectedMethodReturns,
      final List<Class[]> expectedMethodParameters)
      throws NoSuchMethodException
   {
      assertEquals("Unexpected number of public fields",
         0, clazz.getFields().length);

      final List<Method> publicMethods = Arrays.stream(
         clazz.getDeclaredMethods())
            .filter(m -> Modifier.isPublic(m.getModifiers()))
            .collect(Collectors.toList());

      assertEquals("Unexpected number of public methods",
         expectedMethodNames.size(), publicMethods.size());

      assertTrue("Invalid test configuration",
         expectedMethodNames.size() == expectedMethodReturns.size());
      assertTrue("Invalid test configuration",
         expectedMethodNames.size() == expectedMethodParameters.size());

      for (int i = 0; i < expectedMethodNames.size(); i++)
      {
         Method method = clazz.getDeclaredMethod(expectedMethodNames.get(i),
            expectedMethodParameters.get(i));
         assertEquals(expectedMethodReturns.get(i), method.getReturnType());
      }
   }
}
