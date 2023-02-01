import processing.core.PImage;

import java.util.List;

public abstract class AnimationEntity extends ActiveEntity{
    private int animationPeriod;

    public AnimationEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public void nextImage(){ super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size()); }
}
