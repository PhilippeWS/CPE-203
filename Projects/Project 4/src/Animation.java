public class Animation implements Action {
    private Entity entity;
    private int repeatCount;

    private Animation(Entity entity, int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    private void executeAnimationAction(EventScheduler scheduler)
    {
        ((AnimationEntity) this.entity).nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.entity,
                    createAnimationAction(this.entity,
                            Math.max(this.repeatCount - 1, 0)),
                    ((AnimationEntity) this.entity).getAnimationPeriod());
        }
    }


    public static Animation createAnimationAction(Entity entity, int repeatCount)
    {
        return new Animation(entity, repeatCount);
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
       executeAnimationAction(scheduler);
    }
}
