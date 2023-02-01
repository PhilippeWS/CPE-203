public interface EntityInteraction extends NextImage {
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

    public void setPosition(Point position);
}
