import processing.core.PImage;

import java.util.List;

public class Fish implements EntityInteraction{
    public static final String FISH_KEY = "fish";
    public static final int FISH_NUM_PROPERTIES = 5;
    public static final int FISH_ID = 1;
    public static final int FISH_COL = 2;
    public static final int FISH_ROW = 3;
    public static final int FISH_ACTION_PERIOD = 4;
    public static final String FISH_ID_PREFIX = "fish -- ";
    public static final int FISH_CORRUPT_MIN = 20000;
    public static final int FISH_CORRUPT_MAX = 30000;
    public static final int FISH_REACH = 1;

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;

    public Fish(String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
    }


    public static Fish createFish(String id, Point position, List<PImage> images, int actionPeriod)
    {
        return new Fish(id, position, images, 0, 0, actionPeriod);
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
        Point pos = this.position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Crab crab = Crab.createCrab(this.id + Crab.CRAB_ID_SUFFIX, pos, imageStore.getImageList(Crab.CRAB_KEY),
                this.actionPeriod / Crab.CRAB_PERIOD_SCALE,
                Crab.CRAB_ANIMATION_MIN + VirtualWorld.rand.nextInt(Crab.CRAB_ANIMATION_MAX - Crab.CRAB_ANIMATION_MIN));

        world.addEntity(crab);
        crab.scheduleAction(scheduler, world, imageStore);
    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.actionPeriod);
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
