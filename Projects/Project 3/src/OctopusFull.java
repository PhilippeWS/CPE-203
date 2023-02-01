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

        OctopusNotFull octoNotFull = OctopusNotFull.createOctoNotFull(super.getId(),
                super.getPosition(), super.getImages(),
                super.getResourceLimit(), super.getActionPeriod(), super.getAnimationPeriod());

        /*
        Entity octo = new OctopusNotFull(this.id, super.getPosition(), this.images,
                this.resourceLimit, 0,
                this.actionPeriod, this.animationPeriod);
         */

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octoNotFull);
        octoNotFull.scheduleAction(scheduler, world, imageStore);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(super.getPosition(), Atlantis.class);

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
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    super.getAnimationPeriod());
        }
    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), super.getActionPeriod());
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0), super.getAnimationPeriod());
    }

    @Override
    public Point nextPositionOcto(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.getX() - super.getPosition().getX());
        Point newPos = new Point(super.getPosition().getX() + horiz,
                super.getPosition().getY());

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.getY() - super.getPosition().getY());
            newPos = new Point(super.getPosition().getX(),
                    super.getPosition().getY() + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = super.getPosition();
            }
        }

        return newPos;
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (super.getPosition().adjacent(target.getPosition())) {
            return true;
        }
        else {
            Point nextPos = nextPositionOcto(world, target.getPosition());

            if (!super.getPosition().equals(nextPos)) {
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
