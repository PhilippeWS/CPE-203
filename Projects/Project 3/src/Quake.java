import processing.core.PImage;

import java.util.List;

public class Quake extends AnimationEntity{
    public static final String QUAKE_KEY = "quake";
    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;
    public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(String id, Point position, List<PImage> images,int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }


    public static Quake createQuake(Point position, List<PImage> images)
    {
        return new Quake(Quake.QUAKE_ID, position, images, Quake.QUAKE_ACTION_PERIOD, Quake.QUAKE_ANIMATION_PERIOD);
    }


    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);

    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), super.getActionPeriod());
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, Quake.QUAKE_ANIMATION_REPEAT_COUNT), super.getAnimationPeriod());
    }

}
