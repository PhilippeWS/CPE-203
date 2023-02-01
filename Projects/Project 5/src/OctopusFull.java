import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctopusFull extends Octopus{

    public OctopusFull(String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod, int animationPeriod)
    {
        super(id, position, images, resourceCount, resourceLimit, actionPeriod, animationPeriod);
    }

    public static OctopusFull createOctoFull(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod)
    {
        return new OctopusFull(id, position, images, resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }

    private void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {

        OctopusNotFull octoNotFull = OctopusNotFull.createOctoNotFull(this.getId(),
                this.getPosition(), this.getImages(),
                this.getResourceLimit(), this.getActionPeriod(), this.getAnimationPeriod());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octoNotFull);
        octoNotFull.scheduleAction(scheduler, world, imageStore);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(), Atlantis.class);

        if (fullTarget.isPresent() &&
                this.moveTo(world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            ((ActiveEntity) fullTarget.get()).scheduleAction(scheduler, world, imageStore);

            //transform to unfull
            this.transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        }
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Point.neighbors(this.getPosition(), target.getPosition())) {
            return true;
        }
        else {
            Point nextPos = nextPositionOcto(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }


}
