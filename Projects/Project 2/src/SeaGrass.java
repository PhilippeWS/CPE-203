import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class SeaGrass implements EntityInteraction{
    public static final String SGRASS_KEY = "seaGrass";
    public static final int SGRASS_NUM_PROPERTIES = 5;
    public static final int SGRASS_ID = 1;
    public static final int SGRASS_COL = 2;
    public static final int SGRASS_ROW = 3;
    public static final int SGRASS_ACTION_PERIOD = 4;


    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;

    public SeaGrass(String id, Point position,
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


    public static SeaGrass createSgrass(String id, Point position,  List<PImage> images,int actionPeriod)
    {
        return new SeaGrass(id, position, images, 0, 0, actionPeriod);
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
        Optional<Point> openPt = world.findOpenAround(this.position);

        if (openPt.isPresent())
        {

            Fish fish = Fish.createFish(Fish.FISH_ID_PREFIX + this.id,
                    openPt.get(),
                    imageStore.getImageList(Fish.FISH_KEY),
                    Fish.FISH_CORRUPT_MIN + VirtualWorld.rand.nextInt(Fish.FISH_CORRUPT_MAX - Fish.FISH_CORRUPT_MIN));

            /*
            Fish fish = new Fish(Fish.FISH_ID_PREFIX + this.id, openPt.get(),imageStore.getImageList(Fish.FISH_KEY),
                    0, 0,Fish.FISH_CORRUPT_MIN +
                    Functions.rand.nextInt(Fish.FISH_CORRUPT_MAX - Fish.FISH_CORRUPT_MIN), 0);
             */

            world.addEntity(fish);
            fish.scheduleAction(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.actionPeriod);
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
