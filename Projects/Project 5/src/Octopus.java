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

        if(path.size() < 1){
            newPos = this.getPosition();
        }else{
            newPos = path.get(0);
        }

        return newPos;
    }

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0), this.getAnimationPeriod());
    }

}
