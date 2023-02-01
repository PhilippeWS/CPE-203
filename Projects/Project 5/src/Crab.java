import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Crab extends MovableEntity{
    public static final String CRAB_KEY = "crab";
    public static final String CRAB_ID_SUFFIX = " -- crab";
    public static final int CRAB_PERIOD_SCALE = 4;
    public static final int CRAB_ANIMATION_MIN = 50;
    public static final int CRAB_ANIMATION_MAX = 150;

    public Crab(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public static Crab createCrab(String id, Point position,List<PImage> images, int actionPeriod, int animationPeriod)
    {
        return new Crab(id, position, images,actionPeriod, animationPeriod);
    }

    private Point nextPositionCrab(WorldModel world, Point destPos)
    {
        Point newPos;

        List<Point> path = super.computePath(this.getPosition(), destPos,
                p -> world.withinBounds(p) && (world.getOccupant(p).isEmpty() || world.getOccupant(p).get().getClass() == Fish.class),
                Point::neighbors,
                MovableEntity.DIAGONAL_CARDINAL_NEIGHBORS);


        if(path.size() < 1){
            newPos = this.getPosition();
        }else{
            newPos = path.get(0);
        }

        return newPos ;
    }

    private boolean moveToCrab(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (Point.neighbors(this.getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = nextPositionCrab(world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> crabTarget = world.findNearest(super.getPosition(), SeaGrass.class);
        long nextPeriod = super.getActionPeriod();

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().getPosition();

            if (this.moveToCrab(world, crabTarget.get(), scheduler))
            {

                Quake quake = Quake.createQuake(tgtPos, imageStore.getImageList(Quake.QUAKE_KEY));

                world.addEntity((quake));
                nextPeriod += super.getActionPeriod();
                quake.scheduleAction(scheduler, world, imageStore);

                if(world.getBackgroundImage(tgtPos).get().equals(imageStore.getImageList("sand").get(0))){
                    if (Ray.RayPopulation <= Ray.RAY_POPULATION_LIMIT){
                        List<Point> unoccupiedNeighbors = new ArrayList<>();
                        MovableEntity.DIAGONAL_CARDINAL_NEIGHBORS.apply(tgtPos).forEach(point -> {
                            if(!world.isOccupied(point)){
                                unoccupiedNeighbors.add(point);
                            }
                        });

                        RayNotFull newRay = RayNotFull.createRayNotFull("", unoccupiedNeighbors.get(new Random().nextInt(unoccupiedNeighbors.size())) , imageStore.getImageList(Ray.RAY_KEY), 1, Ray.RAY_ACTION_PERIOD, Ray.RAY_ANIMATION_PERIOD);
                        world.addEntity(newRay);
                        newRay.scheduleAction(scheduler, world, imageStore);
                    }
                }
            }
        }

        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), nextPeriod);
    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), super.getActionPeriod());
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0), super.getAnimationPeriod());
    }

}
