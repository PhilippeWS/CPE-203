import processing.core.PImage;

import java.util.List;

public class Quake implements AnimationPeriod{
    public static final String QUAKE_KEY = "quake";
    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;
    public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;


    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public Quake(String id, Point position,
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


    public static Quake createQuake(Point position, List<PImage> images)
    {
        return new Quake(Quake.QUAKE_ID, position, images, 0, 0, Quake.QUAKE_ACTION_PERIOD, Quake.QUAKE_ANIMATION_PERIOD);
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
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);

    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                Animation.createAnimationAction(this, Quake.QUAKE_ANIMATION_REPEAT_COUNT),
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
}
