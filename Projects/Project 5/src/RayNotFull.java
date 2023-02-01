import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class RayNotFull extends Ray{
    public RayNotFull(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int resourceCount, int resourceLimit) {
        super(id, position, images, actionPeriod, animationPeriod, resourceCount, resourceLimit);
    }

    public static RayNotFull createRayNotFull(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod)
    {
        Ray.RayPopulation ++;
        return new RayNotFull(id, position, images,  actionPeriod, animationPeriod,0, resourceLimit);
    }

    private boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit())
        {
            Ray rayFull = RayFull.createRayFull(this.getId(),
                    this.getPosition(), this.getImages(),
                    this.getResourceLimit(), this.getActionPeriod(), this.getAnimationPeriod());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            world.addEntity(rayFull);
            rayFull.scheduleAction(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(), Crab.class);
        if (!notFullTarget.isPresent() ||
                !this.moveTo(world, notFullTarget.get(), scheduler) ||
                !this.transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }


    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Point.neighbors(this.getPosition(), target.getPosition()))
        {
            this.setResourceCount(this.getResourceCount() + 1);
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = nextPositionRay(world, target.getPosition());

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
}
