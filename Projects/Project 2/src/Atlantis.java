import processing.core.PImage;

import java.util.List;

public class Atlantis implements AnimationPeriod{
    public static final String ATLANTIS_KEY = "atlantis";
    public static final int ATLANTIS_NUM_PROPERTIES = 4;
    public static final int ATLANTIS_ID = 1;
    public static final int ATLANTIS_COL = 2;
    public static final int ATLANTIS_ROW = 3;
    public static final int ATLANTIS_ANIMATION_PERIOD = 70;
    public static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public Atlantis(String id, Point position,
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

    public static Atlantis createAtlantis(String id, Point position, List<PImage> images)
    {
        return new Atlantis(id, position, images, 0, 0, 0, 0);
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
                Animation.createAnimationAction(this, Atlantis.ATLANTIS_ANIMATION_REPEAT_COUNT),
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
