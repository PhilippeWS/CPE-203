import processing.core.PImage;

import java.util.List;

public abstract class Octopus extends AnimationEntity{
    public static final String OCTO_KEY = "octo";
    public static final int OCTO_NUM_PROPERTIES = 7;
    public static final int OCTO_ID = 1;
    public static final int OCTO_COL = 2;
    public static final int OCTO_ROW = 3;
    public static final int OCTO_LIMIT = 4;
    public static final int OCTO_ACTION_PERIOD = 5;
    public static final int OCTO_ANIMATION_PERIOD = 6;

    private int resourceCount;
    private int resourceLimit;

    public Octopus(String id, Point position,
                   List<PImage> images, int resourceCount, int resourceLimit,
                   int actionPeriod, int animationPeriod){

        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public int getResourceCount(){ return this.resourceCount; }

    public void setResourceCount(int resourceCount){ this.resourceCount = resourceCount; }

    public int getResourceLimit() { return this.resourceLimit; }

    public abstract Point nextPositionOcto(WorldModel world, Point destPos);

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

}
