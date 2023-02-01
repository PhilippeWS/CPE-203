import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class SeaGrass extends ActiveEntity{
    public static final String SGRASS_KEY = "seaGrass";
    public static final int SGRASS_NUM_PROPERTIES = 5;
    public static final int SGRASS_ID = 1;
    public static final int SGRASS_COL = 2;
    public static final int SGRASS_ROW = 3;
    public static final int SGRASS_ACTION_PERIOD = 4;

    public SeaGrass(String id, Point position, List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }

    public static SeaGrass createSgrass(String id, Point position,  List<PImage> images,int actionPeriod)
    {
        return new SeaGrass(id, position, images, actionPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Point> openPt = world.findOpenAround(this.getPosition());

        if (openPt.isPresent())
        {

            Fish fish = Fish.createFish(Fish.FISH_ID_PREFIX + this.getId(),
                    openPt.get(),
                    imageStore.getImageList(Fish.FISH_KEY),
                    Fish.FISH_CORRUPT_MIN + VirtualWorld.rand.nextInt(Fish.FISH_CORRUPT_MAX - Fish.FISH_CORRUPT_MIN));


            world.addEntity(fish);
            fish.scheduleAction(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                super.getActionPeriod());
    }

}
