import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class RayFull extends Ray{
    public RayFull(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int resourceCount, int resourceLimit) {
        super(id, position, images, actionPeriod, animationPeriod, resourceCount, resourceLimit);
    }

    public static RayFull createRayFull(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod)
    {
        return new RayFull(id, position, images,actionPeriod, animationPeriod , resourceLimit, resourceLimit);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> rayFullTarget = world.findNearest(this.getPosition(), SeaGrass.class);

        if (rayFullTarget.isPresent() &&
                this.moveTo(world, rayFullTarget.get(), scheduler))
        {
            world.setBackground(rayFullTarget.get().getPosition(), new Background("sand", imageStore.getImageList("sand")));
            Ray.RayPopulation--;
        }
        else
        {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        }
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Point.neighbors(this.getPosition(), target.getPosition())) {
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            return true;
        }
        else {
            Point nextPos = nextPositionRay(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }
}
