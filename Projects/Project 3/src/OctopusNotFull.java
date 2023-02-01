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
        if (super.getResourceCount() >= super.getResourceLimit())
        {

            Octopus octo = OctopusFull.createOctoFull(super.getId(),
                    super.getPosition(), super.getImages(),
                    super.getResourceLimit(), super.getActionPeriod(), super.getAnimationPeriod());
            /*
            Entity octo = new OctopusFull(, super.getPosition(), this.images,
                    super.getResourceLimit(), super.getResourceLimit(),
                    this.actionPeriod, this.animationPeriod);
             */
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity((Entity) octo);
            ((ActiveEntity) octo).scheduleAction(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget = world.findNearest(super.getPosition(), Fish.class);

        if (!notFullTarget.isPresent() ||
                !this.moveTo(world, notFullTarget.get(), scheduler) ||
                !this.transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    super.getActionPeriod());
        }
    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                super.getActionPeriod());
        scheduler.scheduleEvent(this,
                Animation.createAnimationAction(this, 0), this.getAnimationPeriod());
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
        if (super.getPosition().adjacent(target.getPosition()))
        {
            super.setResourceCount(super.getResourceCount() + 1);
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = nextPositionOcto(world, target.getPosition());

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
