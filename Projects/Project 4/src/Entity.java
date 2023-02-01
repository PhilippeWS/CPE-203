import processing.core.PImage;

import java.util.List;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


public abstract class Entity {
   private String id;
   private Point position;
   private List<PImage> images;
   private int imageIndex;

   public Entity(String id, Point position, List<PImage> images)
   {
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
   }

   public PImage getCurrentImage(){ return this.images.get(this.imageIndex); }

   public String getId(){ return this.id; }

   public Point getPosition(){ return this.position; }

   public void setPosition(Point newPosition){
      this.position = newPosition;
   }

   public List<PImage> getImages() { return images; }

   public int getImageIndex() { return imageIndex; }

   public void setImageIndex(int imageIndex){ this.imageIndex = imageIndex; }


}
