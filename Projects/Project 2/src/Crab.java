import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Crab implements AnimationPeriod{
    public static final String CRAB_KEY = "crab";
    public static final String CRAB_ID_SUFFIX = " -- crab";
    public static final int CRAB_PERIOD_SCALE = 4;
    public static final int CRAB_ANIMATION_MIN = 50;
    public static final int CRAB_ANIMATION_MAX = 150;

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public Crab(String id, Point position,
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

    public static Crab createCrab(String id, Point position,List<PImage> images, int actionPeriod, int animationPeriod)
    {
        return new Crab(id, position, images,0, 0, actionPeriod, animationPeriod);
    }

    private Point nextPositionCrab(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz,
                this.position.getY());

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !((occupant.get()).getClass() == Fish.class)))
        {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(), this.position.getY() + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !((occupant.get()).getClass() == Fish.class)))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }

    private boolean moveToCrab(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.position.adjacent(target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = nextPositionCrab(world, target.getPosition());

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
        Optional<Entity> crabTarget = world.findNearest(this.position, SeaGrass.class);
        long nextPeriod = this.actionPeriod;

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

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                quake.scheduleAction(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), nextPeriod);
    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0), this.animationPeriod);
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }
}
