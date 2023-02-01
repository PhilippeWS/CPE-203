import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


final class Entity
{
   public static final String OCTO_KEY = "octo";
   public static final int OCTO_NUM_PROPERTIES = 7;
   public static final int OCTO_ID = 1;
   public static final int OCTO_COL = 2;
   public static final int OCTO_ROW = 3;
   public static final int OCTO_LIMIT = 4;
   public static final int OCTO_ACTION_PERIOD = 5;
   public static final int OCTO_ANIMATION_PERIOD = 6;

   public static final String OBSTACLE_KEY = "obstacle";
   public static final int OBSTACLE_NUM_PROPERTIES = 4;
   public static final int OBSTACLE_ID = 1;
   public static final int OBSTACLE_COL = 2;
   public static final int OBSTACLE_ROW = 3;

   public static final String FISH_KEY = "fish";
   public static final int FISH_NUM_PROPERTIES = 5;
   public static final int FISH_ID = 1;
   public static final int FISH_COL = 2;
   public static final int FISH_ROW = 3;
   public static final int FISH_ACTION_PERIOD = 4;

   public static final String ATLANTIS_KEY = "atlantis";
   public static final int ATLANTIS_NUM_PROPERTIES = 4;
   public static final int ATLANTIS_ID = 1;
   public static final int ATLANTIS_COL = 2;
   public static final int ATLANTIS_ROW = 3;
   public static final int ATLANTIS_ANIMATION_PERIOD = 70;
   public static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

   public static final String SGRASS_KEY = "seaGrass";
   public static final int SGRASS_NUM_PROPERTIES = 5;
   public static final int SGRASS_ID = 1;
   public static final int SGRASS_COL = 2;
   public static final int SGRASS_ROW = 3;
   public static final int SGRASS_ACTION_PERIOD = 4;

   public static final String CRAB_KEY = "crab";
   public static final String CRAB_ID_SUFFIX = " -- crab";
   public static final int CRAB_PERIOD_SCALE = 4;
   public static final int CRAB_ANIMATION_MIN = 50;
   public static final int CRAB_ANIMATION_MAX = 150;

   public static final String QUAKE_KEY = "quake";
   public static final String QUAKE_ID = "quake";
   public static final int QUAKE_ACTION_PERIOD = 1100;
   public static final int QUAKE_ANIMATION_PERIOD = 100;
   public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;


   public static final String FISH_ID_PREFIX = "fish -- ";
   public static final int FISH_CORRUPT_MIN = 20000;
   public static final int FISH_CORRUPT_MAX = 30000;
   public static final int FISH_REACH = 1;



   private EntityKind kind;
   private String id;
   private Point position;
   private List<PImage> images;
   private int imageIndex;
   private int resourceLimit;
   private int resourceCount;
   private int actionPeriod;
   private int animationPeriod;

   public Entity(EntityKind kind, String id, Point position,
      List<PImage> images, int resourceLimit, int resourceCount,
      int actionPeriod, int animationPeriod)
   {
      this.kind = kind;
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }

   public int getActionPeriod() {return actionPeriod;}

   public EntityKind getKind() {return kind;}

   public Point getPosition() {return position;}

   public void setPosition(Point position) {this.position = position;}

   public int getAnimationPeriod()
   {
      switch (this.kind)
      {
         case OCTO_FULL:
         case OCTO_NOT_FULL:
         case CRAB:
         case QUAKE:
         case ATLANTIS:
            return this.animationPeriod;
         default:
            throw new UnsupportedOperationException(
                    String.format("getAnimationPeriod not supported for %s",
                            this.kind));
      }
   }

   private boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
   {
      if (this.resourceCount >= this.resourceLimit)
      {
         Entity octo = Entity.createOctoFull(this.id, this.resourceLimit,
                 this.position, this.actionPeriod, this.animationPeriod,
                 this.images);

         world.removeEntity(this);
         scheduler.unscheduleAllEvents(this);

         world.addEntity(octo);
         octo.scheduleActions(scheduler, world, imageStore);

         return true;
      }

      return false;
   }

   private void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
   {
      Entity octo = Entity.createOctoNotFull(this.id, this.resourceLimit,
              this.position, this.actionPeriod, this.animationPeriod,
              this.images);

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      world.addEntity(octo);
      octo.scheduleActions(scheduler, world, imageStore);
   }

   private boolean moveToNotFull(WorldModel world, Entity target, EventScheduler scheduler)
   {
      if (this.position.adjacent(target.position))
      {
         this.resourceCount += 1;
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);

         return true;
      }
      else
      {
         Point nextPos = nextPositionOcto(world, target.position);

         if (!this.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }

   private boolean moveToFull(WorldModel world, Entity target, EventScheduler scheduler)
   {
      if (this.position.adjacent(target.position))
      {
         return true;
      }
      else
      {
         Point nextPos = nextPositionOcto(world, target.position);

         if (!this.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }

   private boolean moveToCrab(WorldModel world, Entity target, EventScheduler scheduler)
   {
      if (this.position.adjacent(target.position))
      {
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);
         return true;
      }
      else
      {
         Point nextPos = nextPositionCrab(world, target.position);

         if (!this.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }

   private Point nextPositionOcto(WorldModel world, Point destPos)
   {
      int horiz = Integer.signum(destPos.getX() - this.position.getX());
      Point newPos = new Point(this.position.getX() + horiz,
              this.position.getY());

      if (horiz == 0 || world.isOccupied(newPos))
      {
         int vert = Integer.signum(destPos.getY() - this.position.getY());
         newPos = new Point(this.position.getX(),
                 this.position.getY() + vert);

         if (vert == 0 || world.isOccupied(newPos))
         {
            newPos = this.position;
         }
      }

      return newPos;
   }

   private Point nextPositionCrab(WorldModel world, Point destPos)
   {
      int horiz = Integer.signum(destPos.getX() - this.position.getX());
      Point newPos = new Point(this.position.getX() + horiz,
              this.position.getY());

      Optional<Entity> occupant = world.getOccupant(newPos);

      if (horiz == 0 ||
              (occupant.isPresent() && !(occupant.get().kind == EntityKind.FISH)))
      {
         int vert = Integer.signum(destPos.getY() - this.position.getY());
         newPos = new Point(this.position.getX(), this.position.getY() + vert);
         occupant = world.getOccupant(newPos);

         if (vert == 0 ||
                 (occupant.isPresent() && !(occupant.get().kind == EntityKind.FISH)))
         {
            newPos = this.position;
         }
      }

      return newPos;
   }

   private static Entity createOctoFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod,  List<PImage> images)
   {
      return new Entity(EntityKind.OCTO_FULL, id, position, images, resourceLimit, resourceLimit, actionPeriod, animationPeriod);
   }

   private static Entity createCrab(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images)
   {
      return new Entity(EntityKind.CRAB, id, position, images,
              0, 0, actionPeriod, animationPeriod);
   }

   private static Entity createQuake(Point position, List<PImage> images)
   {
      return new Entity(EntityKind.QUAKE, Entity.QUAKE_ID, position, images, 0, 0, Entity.QUAKE_ACTION_PERIOD, Entity.QUAKE_ANIMATION_PERIOD);
   }

   public static Entity createOctoNotFull(String id, int resourceLimit,  Point position, int actionPeriod, int animationPeriod, List<PImage> images)
   {
      return new Entity(EntityKind.OCTO_NOT_FULL, id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
   }

   public static Entity createAtlantis(String id, Point position, List<PImage> images)
   {
      return new Entity(EntityKind.ATLANTIS, id, position, images, 0, 0, 0, 0);
   }

   public static Entity createObstacle(String id, Point position, List<PImage> images)
   {
      return new Entity(EntityKind.OBSTACLE, id, position, images, 0, 0, 0, 0);
   }

   public static Entity createFish(String id, Point position, int actionPeriod, List<PImage> images)
   {
      return new Entity(EntityKind.FISH, id, position, images, 0, 0, actionPeriod, 0);
   }


   public static Entity createSgrass(String id, Point position, int actionPeriod, List<PImage> images)
   {
      return new Entity(EntityKind.SGRASS, id, position, images, 0, 0, actionPeriod, 0);
   }

   //---------------IN interface----------------------------------------------------------------------------------------

   public PImage getCurrentImage()
   {
      return (this.images.get(this.imageIndex));
   }

   public void nextImage()
   {
      this.imageIndex = (this.imageIndex + 1) % this.images.size();
   }

   public void executeOctoFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> fullTarget = world.findNearest(this.position,
              EntityKind.ATLANTIS);

      if (fullTarget.isPresent() &&
              this.moveToFull(world, fullTarget.get(), scheduler))
      {
         //at atlantis trigger animation
         fullTarget.get().scheduleActions(scheduler, world, imageStore);

         //transform to unfull
         this.transformFull(world, scheduler, imageStore);
      }
      else
      {
         scheduler.scheduleEvent(this,
                 Action.createActivityAction(this, world, imageStore),
                 this.actionPeriod);
      }
   }

   public void executeOctoNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> notFullTarget = world.findNearest(this.position,
              EntityKind.FISH);

      if (!notFullTarget.isPresent() ||
              !this.moveToNotFull(world, notFullTarget.get(), scheduler) ||
              !this.transformNotFull(world, scheduler, imageStore))
      {
         scheduler.scheduleEvent(this,
                 Action.createActivityAction(this, world, imageStore),
                 this.actionPeriod);
      }
   }

   public void executeFishActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Point pos = this.position;  // store current position before removing

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      Entity crab = Entity.createCrab(this.id + Entity.CRAB_ID_SUFFIX,
              pos, this.actionPeriod / Entity.CRAB_PERIOD_SCALE,
              Entity.CRAB_ANIMATION_MIN +
                      Functions.rand.nextInt(Entity.CRAB_ANIMATION_MAX - Entity.CRAB_ANIMATION_MIN),
              imageStore.getImageList(Entity.CRAB_KEY));

      world.addEntity(crab);
      crab.scheduleActions(scheduler, world, imageStore);
   }


   public void executeCrabActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> crabTarget = world.findNearest(this.position, EntityKind.SGRASS);
      long nextPeriod = this.actionPeriod;

      if (crabTarget.isPresent())
      {
         Point tgtPos = crabTarget.get().position;

         if (this.moveToCrab(world, crabTarget.get(), scheduler))
         {
            Entity quake = Entity.createQuake(tgtPos,
                    imageStore.getImageList(Entity.QUAKE_KEY));

            world.addEntity(quake);
            nextPeriod += this.actionPeriod;
            quake.scheduleActions(scheduler, world, imageStore);
         }
      }

      scheduler.scheduleEvent(this,
              Action.createActivityAction(this, world, imageStore),
              nextPeriod);
   }

   public void executeQuakeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(this);
      world.removeEntity(this);
   }

   public void executeAtlantisActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(this);
      world.removeEntity(this);
   }

   public void executeSgrassActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Point> openPt = world.findOpenAround(this.position);

      if (openPt.isPresent())
      {
         Entity fish = Entity.createFish(Entity.FISH_ID_PREFIX + this.id,
                 openPt.get(), Entity.FISH_CORRUPT_MIN +
                         Functions.rand.nextInt(Entity.FISH_CORRUPT_MAX - Entity.FISH_CORRUPT_MIN),
                 imageStore.getImageList(Entity.FISH_KEY));
         world.addEntity(fish);
         fish.scheduleActions(scheduler, world, imageStore);
      }

      scheduler.scheduleEvent(this,
              Action.createActivityAction(this, world, imageStore),
              this.actionPeriod);
   }

   public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
   {
      switch (this.kind)
      {
         case OCTO_FULL:
            scheduler.scheduleEvent(this,
                    Action.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
            scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0),
                    this.getAnimationPeriod());
            break;

         case OCTO_NOT_FULL:
            scheduler.scheduleEvent(this,
                    Action.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
            scheduler.scheduleEvent(this,
                    Action.createAnimationAction(this, 0), this.getAnimationPeriod());
            break;

         case FISH:
            scheduler.scheduleEvent(this,
                    Action.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
            break;

         case CRAB:
            scheduler.scheduleEvent(this,
                    Action.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
            scheduler.scheduleEvent(this,
                    Action.createAnimationAction(this, 0), this.getAnimationPeriod());
            break;

         case QUAKE:
            scheduler.scheduleEvent(this,
                    Action.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
            scheduler.scheduleEvent(this,
                    Action.createAnimationAction(this, Entity.QUAKE_ANIMATION_REPEAT_COUNT),
                    this.getAnimationPeriod());
            break;

         case SGRASS:
            scheduler.scheduleEvent(this,
                    Action.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
            break;
         case ATLANTIS:
            scheduler.scheduleEvent(this,
                    Action.createAnimationAction(this, Entity.ATLANTIS_ANIMATION_REPEAT_COUNT),
                    this.getAnimationPeriod());
            break;

         default:
      }
   }

}
