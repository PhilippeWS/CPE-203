import processing.core.PImage;

import java.util.List;

public class Atlantis extends AnimationEntity{
    public static final String ATLANTIS_KEY = "atlantis";
    public static final int ATLANTIS_NUM_PROPERTIES = 4;
    public static final int ATLANTIS_ID = 1;
    public static final int ATLANTIS_COL = 2;
    public static final int ATLANTIS_ROW = 3;
    public static final int ATLANTIS_ANIMATION_PERIOD = 70;
    public static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

    public Atlantis(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public static Atlantis createAtlantis(String id, Point position, List<PImage> images)
    {
        return new Atlantis(id, position, images, 0, 0);
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
                this.getAnimationPeriod());
    }

}
