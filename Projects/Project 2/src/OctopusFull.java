import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctopusFull implements Octopus{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public OctopusFull(String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }


    public static OctopusFull createOctoFull(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod)
    {
        return new OctopusFull(id, position, images, resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }

    private void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {

        OctopusNotFull octoNotFull = OctopusNotFull.createOctoNotFull(this.id,
                this.position, this.images,
                this.resourceLimit, this.actionPeriod, this.animationPeriod);

        /*
        Entity octo = new OctopusNotFull(this.id, this.position, this.images,
                this.resourceLimit, 0,
                this.actionPeriod, this.animationPeriod);
         */

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octoNotFull);
        octoNotFull.scheduleAction(scheduler, world, imageStore);
    }

    @Override
    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    @Override
    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    @Override
    public int getActionPeriod() {
        return this.actionPeriod;
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(this.position, Atlantis.class);

        if (fullTarget.isPresent() &&
                this.moveTo(world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            ((EntityInteraction) fullTarget.get()).scheduleAction(scheduler, world, imageStore);

            //transform to unfull
            this.transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0),
                this.animationPeriod);
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    @Override
    public Point nextPositionOcto(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz,
                this.position.getY());

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(),
                    this.position.getY() + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = nextPositionOcto(world, target.getPosition());

            if (!this.position.equals(nextPos))
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
