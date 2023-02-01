import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class MovableEntity extends AnimationEntity implements PathingStrategy{
    public MovableEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }




    @Override
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors){
        LinkedList<Point> path = new LinkedList<>();
        LinkedList<Point> closedList = new LinkedList<>();
        List<Point> openList = new LinkedList<>();

        openList.add(start);
        start.parentPoint = start;
        Point currentPoint;
        while(!openList.isEmpty()) {
            //Filter list of possible neighbors (They are a neighbor if they are not an OBSTACLE/ already on the CLOSED LIST/ already on the OPEN LIST
            Function<Point, List<Point>> neighborCalculator = point -> potentialNeighbors.apply(point)
                    .filter(canPassThrough.and(pN -> !closedList.contains(pN)))
                    .collect(Collectors.toList());

            //Choose the lowest F cost on open list as current point
            currentPoint = null;
            for(Point openPoint : openList){
                if(currentPoint == null || openPoint.getFCost() < currentPoint.getFCost()) {
                    currentPoint = openPoint;
                }else if(currentPoint.getFCost() == openPoint.getFCost()){
                    currentPoint = openPoint.getHValue() < currentPoint.getHValue() ? openPoint : currentPoint;
                }
            }
            closedList.add(currentPoint);

            //If we have found the end, calculate the path
            if(withinReach.test(currentPoint, end)){
                path.add(closedList.getLast());
                while (!path.getLast().parentPoint.equals(start)){
                    path.add(path.getLast().parentPoint);
                }
                break;
            }

            //Calculate the neighbors of the current point
            List<Point> neighbors = new ArrayList<>();
            //Since the neighbors generated off my function do not hold the values of my actual points, instead grab them of the list of open points or add them to neighbors where they wil be added.
            for (Point neighbor : (neighborCalculator.apply(currentPoint))){
                if(openList.contains(neighbor)){
                    neighbors.add(openList.get(openList.indexOf(neighbor)));
                }else{
                    neighbors.add(neighbor);
                }

            }

            //FOr each neighbor, add to open list with all values, otherwise if its g value at current point is less than its actual g value, overwrite its value.
            for (Point neighbor : neighbors){
                if(!openList.contains(neighbor)){
                    openList.add(neighbor);
                    double newGValue = currentPoint.getGValue() + currentPoint.distanceSquared(neighbor) ;
                    double newHValue = neighbor.distanceSquared(end);
                    neighbor.setUpPointValues(newGValue, newHValue, currentPoint);
                }

                double tentativeGValue = currentPoint.getGValue() + currentPoint.distanceSquared(neighbor);
                if(tentativeGValue < neighbor.getGValue()){
                    neighbor.setGValue(tentativeGValue);
                    neighbor.parentPoint = currentPoint;
                }
            }
            openList.remove(currentPoint);

        }
        Collections.reverse(path);
        return path;
    }

    static final Function<Point, Stream<Point>> CARDINAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.getX(), point.getY() - 1))
                            .add(new Point(point.getX(), point.getY() + 1))
                            .add(new Point(point.getX() - 1, point.getY()))
                            .add(new Point(point.getX() + 1, point.getY()))
                            .build();

    static final Function<Point, Stream<Point>> DIAGONAL_CARDINAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.getX() - 1, point.getY() - 1))
                            .add(new Point(point.getX() + 1, point.getY() + 1))
                            .add(new Point(point.getX() - 1, point.getY() + 1))
                            .add(new Point(point.getX() + 1, point.getY() - 1))
                            .add(new Point(point.getX(), point.getY() - 1))
                            .add(new Point(point.getX(), point.getY() + 1))
                            .add(new Point(point.getX() - 1, point.getY()))
                            .add(new Point(point.getX() + 1, point.getY()))
                            .build();
}
