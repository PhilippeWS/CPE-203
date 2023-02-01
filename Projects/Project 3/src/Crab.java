import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Crab extends AnimationEntity{
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
        int horiz = Integer.signum(destPos.getX() - super.getPosition().getX());
        Point newPos = new Point(super.getPosition().getX() + horiz,
                super.getPosition().getY());

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !((occupant.get()).getClass() == Fish.class)))
        {
            int vert = Integer.signum(destPos.getY() - super.getPosition().getY());
            newPos = new Point(this.getPosition().getX(), super.getPosition().getY() + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !((occupant.get()).getClass() == Fish.class)))
            {
                newPos = super.getPosition();
            }
        }

        return newPos;
    }

    private boolean moveToCrab(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (super.getPosition().adjacent(target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = nextPositionCrab(world, target.getPosition());

            if (!super.getPosition().equals(nextPos))
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

                Quake quake = (Quake) Quake.createQuake(tgtPos, imageStore.getImageList(Quake.QUAKE_KEY));

                /*
                Quake quake = new Quake(Quake.QUAKE_ID, tgtPos, imageStore.getImageList(Quake.QUAKE_KEY),
                        0, 0,
                        Quake.QUAKE_ACTION_PERIOD, Quake.QUAKE_ANIMATION_PERIOD);
                 */

                world.addEntity((quake));
                nextPeriod += super.getActionPeriod();
                quake.scheduleAction(scheduler, world, imageStore);
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
