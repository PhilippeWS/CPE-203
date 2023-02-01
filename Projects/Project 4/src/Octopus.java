import processing.core.PImage;

import java.util.List;

public abstract class Octopus extends MovableEntity{
    public static final String OCTO_KEY = "octo";
    public static final int OCTO_NUM_PROPERTIES = 7;
    public static final int OCTO_ID = 1;
    public static final int OCTO_COL = 2;
    public static final int OCTO_ROW = 3;
    public static final int OCTO_LIMIT = 4;
    public static final int OCTO_ACTION_PERIOD = 5;
    public static final int OCTO_ANIMATION_PERIOD = 6;

    private int resourceCount;
    private int resourceLimit;

    public Octopus(String id, Point position,
                   List<PImage> images, int resourceCount, int resourceLimit,
                   int actionPeriod, int animationPeriod){

        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public int getResourceCount(){ return this.resourceCount; }

    public void setResourceCount(int resourceCount){ this.resourceCount = resourceCount; }

    public int getResourceLimit() { return this.resourceLimit; }

    public Point nextPositionOcto(WorldModel world, Point destPos){
        Point newPos;

        List<Point> path = super.computePath(this.getPosition(), destPos,
                p -> world.withinBounds(p) && world.getOccupant(p).isEmpty(),
                Point::neighbors,
                MovableEntity.CARDINAL_NEIGHBORS);

        if(path.size() == 1){
            newPos = super.getPosition();
        }else{
            newPos = path.get(1);
        }

        //Return for single step
//        if(nextPos.size() == 0)
//            return this.getPosition();
//        else
//            return nextPos.get(0);

//        int horiz = Integer.signum(destPos.getX() - super.getPosition().getX());
//        Point newPos = new Point(super.getPosition().getX() + horiz,
//                super.getPosition().getY());
//
//        if (horiz == 0 || world.isOccupied(newPos))
//        {
//            int vert = Integer.signum(destPos.getY() - super.getPosition().getY());
//            newPos = new Point(super.getPosition().getX(),
//                    super.getPosition().getY() + vert);
//
//            if (vert == 0 || world.isOccupied(newPos))
//            {
//                newPos = super.getPosition();
//            }
//        }
//
//        return newPos;
        return newPos;
    }

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

}
