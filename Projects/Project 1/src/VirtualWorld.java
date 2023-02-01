import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import processing.core.*;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
   extends PApplet
{
   public static final int TIMER_ACTION_PERIOD = 100;

   public static final int VIEW_WIDTH = 640;
   public static final int VIEW_HEIGHT = 480;
   public static final int TILE_WIDTH = 32;
   public static final int TILE_HEIGHT = 32;
   public static final int WORLD_WIDTH_SCALE = 2;
   public static final int WORLD_HEIGHT_SCALE = 2;

   public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   public static final String IMAGE_LIST_FILE_NAME = "imagelist";
   public static final String DEFAULT_IMAGE_NAME = "background_default";
   public static final int DEFAULT_IMAGE_COLOR = 0x808080;

   public static final String LOAD_FILE_NAME = "world.sav";

   public static final String FAST_FLAG = "-fast";
   public static final String FASTER_FLAG = "-faster";
   public static final String FASTEST_FLAG = "-fastest";
   public static final double FAST_SCALE = 0.5;
   public static final double FASTER_SCALE = 0.25;
   public static final double FASTEST_SCALE = 0.10;

   public static final int PROPERTY_KEY = 0;

   public static double timeScale = 1.0;

   private ImageStore imageStore;
   private WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;

   private long next_time;

   private Background createDefaultBackground()
   {
      return new Background(DEFAULT_IMAGE_NAME,
              this.imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   private PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private void loadImages(String filename, PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         this.imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private void loadWorld(String filename)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         load(in);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private void scheduleActions()
   {
      for (Entity entity : this.world.getEntities())
      {
         //Only start actions for entities that include action (not those with just animations)
         if (entity.getActionPeriod() > 0)
            entity.scheduleActions(this.scheduler, this.world, this.imageStore);
      }
   }

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground());
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, this);
      loadWorld(LOAD_FILE_NAME);

      scheduleActions();

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      this.view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         this.view.shiftView(dx, dy);
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }





   private void load(Scanner in)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine()))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   private boolean processLine(String line)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case Background.BGND_KEY:
               return parseBackground(properties);
            case Entity.OCTO_KEY:
               return parseOcto(properties);
            case Entity.OBSTACLE_KEY:
               return parseObstacle(properties);
            case Entity.FISH_KEY:
               return parseFish(properties);
            case Entity.ATLANTIS_KEY:
               return parseAtlantis(properties);
            case Entity.SGRASS_KEY:
               return parseSgrass(properties);
         }
      }

      return false;
   }

   private boolean parseBackground(String [] properties)
   {
      if (properties.length == Background.BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Background.BGND_COL]),
                 Integer.parseInt(properties[Background.BGND_ROW]));
         String id = properties[Background.BGND_ID];
         this.world.setBackground(pt, new Background(id, this.imageStore.getImageList(id)));
      }

      return properties.length == Background.BGND_NUM_PROPERTIES;
   }

   private boolean parseOcto(String [] properties)
   {
      if (properties.length == Entity.OCTO_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Entity.OCTO_COL]),
                 Integer.parseInt(properties[Entity.OCTO_ROW]));
         Entity entity = Entity.createOctoNotFull(properties[Entity.OCTO_ID],
                 Integer.parseInt(properties[Entity.OCTO_LIMIT]),
                 pt,
                 Integer.parseInt(properties[Entity.OCTO_ACTION_PERIOD]),
                 Integer.parseInt(properties[Entity.OCTO_ANIMATION_PERIOD]),
                 this.imageStore.getImageList(Entity.OCTO_KEY));
         this.world.tryAddEntity(entity);
      }

      return properties.length == Entity.OCTO_NUM_PROPERTIES;
   }

   private boolean parseObstacle(String [] properties)
   {
      if (properties.length == Entity.OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[Entity.OBSTACLE_COL]),
                 Integer.parseInt(properties[Entity.OBSTACLE_ROW]));
         Entity entity = Entity.createObstacle(properties[Entity.OBSTACLE_ID],
                 pt, this.imageStore.getImageList(Entity.OBSTACLE_KEY));
         this.world.tryAddEntity(entity);
      }

      return properties.length == Entity.OBSTACLE_NUM_PROPERTIES;
   }

   private boolean parseFish(String [] properties)
   {
      if (properties.length == Entity.FISH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Entity.FISH_COL]),
                 Integer.parseInt(properties[Entity.FISH_ROW]));
         Entity entity = Entity.createFish(properties[Entity.FISH_ID],
                 pt, Integer.parseInt(properties[Entity.FISH_ACTION_PERIOD]),
                 this.imageStore.getImageList(Entity.FISH_KEY));
         this.world.tryAddEntity(entity);
      }

      return properties.length == Entity.FISH_NUM_PROPERTIES;
   }

   private boolean parseAtlantis(String [] properties)
   {
      if (properties.length == Entity.ATLANTIS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Entity.ATLANTIS_COL]),
                 Integer.parseInt(properties[Entity.ATLANTIS_ROW]));
         Entity entity = Entity.createAtlantis(properties[Entity.ATLANTIS_ID],
                 pt, this.imageStore.getImageList(Entity.ATLANTIS_KEY));
         this.world.tryAddEntity(entity);
      }

      return properties.length == Entity.ATLANTIS_NUM_PROPERTIES;
   }

   private boolean parseSgrass(String [] properties)
   {
      if (properties.length == Entity.SGRASS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Entity.SGRASS_COL]),
                 Integer.parseInt(properties[Entity.SGRASS_ROW]));
         Entity entity = Entity.createSgrass(properties[Entity.SGRASS_ID],
                 pt,
                 Integer.parseInt(properties[Entity.SGRASS_ACTION_PERIOD]),
                 this.imageStore.getImageList(Entity.SGRASS_KEY));
         this.world.tryAddEntity(entity);
      }

      return properties.length == Entity.SGRASS_NUM_PROPERTIES;
   }


   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
