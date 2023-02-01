import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileParser {
    public static void fileCreator(String fileName) throws FileNotFoundException {
        Scanner fileReader = new Scanner(new File(fileName));
        PrintStream fileWriter = new PrintStream("drawMe.txt");
        List<Point> pointList = new ArrayList<>();

        while(fileReader.hasNext()){
            String[] fileLine = fileReader.nextLine().split(",");
            pointList.add(new Point(Double.parseDouble(fileLine[0]), Double.parseDouble(fileLine[1]), Double.parseDouble(fileLine[2])));
        }

        List<Point> transformedPoints = pointList.stream().filter(point -> point.getZ() <= 2.0).
                map(point -> point.scale(.5)).
                map(point -> point.translate(new Point(-150, -37))).
                collect(Collectors.toList());

        transformedPoints.forEach(point -> fileWriter.printf("%f,%f,%f\n%n", point.getX(), point.getY(), point.getZ()));

//        for(Point point : transformedPoints){
//            String inputString = String.format("%f,%f,%f\n", point.getX(), point.getY(), point.getZ());
//            fileWriter.printf(inputString);
//        }
        fileReader.close();
        fileWriter.close();

    }
}
