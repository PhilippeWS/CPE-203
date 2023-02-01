import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctopusNotFull extends Octopus{
    public OctopusNotFull(String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod, int animationPeriod)
    {
        super(id, position, images, resourceCount, resourceLimit, actionPeriod, animationPeriod);
    }


    public static OctopusNotFull createOctoNotFull(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod)
    {
        return new OctopusNotFull(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
    }


    private boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit())
        {

            Octopus octo = OctopusFull.createOctoFull(this.getId(),
                    this.getPosition(), this.getImages(),
                    this.getResourceLimit(), this.getActionPeriod(), this.getAnimationPeriod());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);
            octo.scheduleAction(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(), Fish.class);

        if (!notFullTarget.isPresent() ||
                !this.moveTo(world, notFullTarget.get(), scheduler) ||
                !this.transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
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
            Point nextPos = nextPositionOcto(world, target.getPosition());

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
}
