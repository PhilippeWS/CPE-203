import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy {
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        LinkedList<Point> path = new LinkedList<>();
        LinkedList<Point> closedList = new LinkedList<>();
        List<Point> openList = new LinkedList<>();

        openList.add(start);
        start.parentPoint = start;
        Point currentPoint;
        int count = 0;
        while(!openList.isEmpty()) {
            //Filter list of possible neighbors (They are a neighbor if they are not an OBSTACLE/ already on the CLOSED LIST/ already on the OPEN LIST
            Function<Point, List<Point>> neighborCalculator = point -> potentialNeighbors.apply(point)
                    .filter(canPassThrough.and(pN -> !closedList.contains(pN)))
                    .collect(Collectors.toList());

            //Choose the lowest F cost on open list as current point
            currentPoint = null;
            for(Point openPoint : openList){
                if(currentPoint == null || openPoint.fCost < currentPoint.fCost) {
                    currentPoint = openPoint;
                }else if(currentPoint.fCost == openPoint.fCost){
                    currentPoint = openPoint.hValue < currentPoint.hValue ? openPoint : currentPoint;
                }
            }

            closedList.add(currentPoint);

            //If we have found the end, calculate the path
            if(withinReach.test(currentPoint, end)){
                path.add(closedList.getLast());
                while (!path.contains(start)){
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
                    double newGValue = currentPoint.gValue + currentPoint.distance(neighbor) ;
                    double newHValue = neighbor.distance(end);
                    neighbor.setUpPoint(newGValue, newHValue, currentPoint);
                }

                double tentativeGValue = currentPoint.gValue + currentPoint.distance(neighbor);
                if(tentativeGValue < neighbor.gValue){
                    neighbor.gValue = tentativeGValue;
                    neighbor.parentPoint = currentPoint;
                }
            }


            openList.remove(currentPoint);
//            System.out.println("Neighbors: " + neighbors);
////
//            System.out.println("Open List Before Choice: " + openList);
//            System.out.println("G Values: ");
//            openList.forEach((Point p) -> System.out.print(p.gValue + " "));
//            System.out.println();
//            System.out.println("H Values:");
//            openList.forEach((Point p) -> System.out.print(p.hValue + " "));
//            System.out.println();
//            System.out.println("F Costs:");
//            openList.forEach((Point p) -> System.out.print(p.fCost + " "));
//            System.out.println();
//            System.out.println("Closed List:" + closedList);
        }


        System.out.println(path);
        return path;
    }
}
