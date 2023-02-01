public class Activity implements Action {
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;

    private Activity(Entity entity, WorldModel world, ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    private void executeActivityAction(EventScheduler scheduler)
    {
        ((ActiveEntity) this.entity).executeActivity(this.world, this.imageStore, scheduler);
    }

    public static Activity createActivityAction(Entity entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore);
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        executeActivityAction(scheduler);
    }
}
