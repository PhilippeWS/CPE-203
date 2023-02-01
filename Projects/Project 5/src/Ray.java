import processing.core.PImage;

import java.util.List;

public abstract class Ray extends MovableEntity{
    public static final String RAY_KEY = "ray";
    public static final int RAY_POPULATION_LIMIT = 3;
    public static final int RAY_ACTION_PERIOD = 2000;
    public static final int RAY_ANIMATION_PERIOD = 1500;

    public static int RayPopulation = 0;

    private int resourceCount;
    private int resourceLimit;


    public Ray(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int resourceCount, int resourceLimit) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public int getResourceCount(){ return this.resourceCount; }

    public void setResourceCount(int resourceCount) { this.resourceCount = resourceCount; }

    public int getResourceLimit() { return this.resourceLimit; }


    public Point nextPositionRay(WorldModel world, Point destPos){
        Point newPos;

        List<Point> path = super.computePath(this.getPosition(), destPos,
                p -> world.withinBounds(p) && world.getOccupant(p).isEmpty(),
                Point::neighbors,
                MovableEntity.DIAGONAL_CARDINAL_NEIGHBORS);

        if(path.size() < 1){
            newPos = this.getPosition();
        }else{
            newPos = path.get(0);
        }

        return newPos;
    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), super.getActionPeriod());
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0), super.getAnimationPeriod());
    }

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

}
