import processing.core.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

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

   public static final Random rand = new Random();

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
         if(entity instanceof ActiveEntity){
            if (((ActiveEntity)entity).getActionPeriod() > 0)
               ((ActiveEntity) entity).scheduleAction(this.scheduler, this.world, this.imageStore);
         }
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
            case Octopus.OCTO_KEY:
               return parseOcto(properties);
            case Obstacle.OBSTACLE_KEY:
               return parseObstacle(properties);
            case Fish.FISH_KEY:
               return parseFish(properties);
            case Atlantis.ATLANTIS_KEY:
               return parseAtlantis(properties);
            case SeaGrass.SGRASS_KEY:
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
      if (properties.length == Octopus.OCTO_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Octopus.OCTO_COL]),
                 Integer.parseInt(properties[Octopus.OCTO_ROW]));


        OctopusNotFull octoNotFull = OctopusNotFull.createOctoNotFull(properties[Octopus.OCTO_ID],
                pt, this.imageStore.getImageList(Octopus.OCTO_KEY),
                Integer.parseInt(properties[Octopus.OCTO_LIMIT]),
                Integer.parseInt(properties[Octopus.OCTO_ACTION_PERIOD]),
                Integer.parseInt(properties[Octopus.OCTO_ANIMATION_PERIOD]));

        /*
         Entity entity = new OctopusNotFull(properties[Octopus.OCTO_ID], pt, this.imageStore.getImageList(Octopus.OCTO_KEY),
                 Integer.parseInt(properties[Octopus.OCTO_LIMIT]), 0,
                 Integer.parseInt(properties[Octopus.OCTO_ACTION_PERIOD]),
                 Integer.parseInt(properties[Octopus.OCTO_ANIMATION_PERIOD]));
         */

         this.world.tryAddEntity(octoNotFull);
      }

      return properties.length == Octopus.OCTO_NUM_PROPERTIES;
   }

   private boolean parseObstacle(String [] properties)
   {
      if (properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[Obstacle.OBSTACLE_COL]),
                 Integer.parseInt(properties[Obstacle.OBSTACLE_ROW]));

         Obstacle obstacle  = Obstacle.createObstacle(properties[Obstacle.OBSTACLE_ID],
                 pt, this.imageStore.getImageList(Obstacle.OBSTACLE_KEY));

         /*
         Entity entity = new Obstacle(properties[Obstacle.OBSTACLE_ID], pt, this.imageStore.getImageList(Obstacle.OBSTACLE_KEY),
                 0,0,0);
                 /*
          */

         this.world.tryAddEntity(obstacle);
      }

      return properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES;
   }

   private boolean parseFish(String [] properties)
   {
      if (properties.length == Fish.FISH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Fish.FISH_COL]),
                 Integer.parseInt(properties[Fish.FISH_ROW]));

         Fish fish = Fish.createFish(properties[Fish.FISH_ID],
                 pt,this.imageStore.getImageList(Fish.FISH_KEY),
                 Integer.parseInt(properties[Fish.FISH_ACTION_PERIOD]));


         /*
         Entity entity = new Fish(properties[Fish.FISH_ID], pt, this.imageStore.getImageList(Fish.FISH_KEY),
                 0,0,
                 Integer.parseInt(properties[Fish.FISH_ACTION_PERIOD]), 0);
          */
         this.world.tryAddEntity(fish);
      }

      return properties.length == Fish.FISH_NUM_PROPERTIES;
   }

   private boolean parseAtlantis(String [] properties)
   {
      if (properties.length == Atlantis.ATLANTIS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Atlantis.ATLANTIS_COL]),
                 Integer.parseInt(properties[Atlantis.ATLANTIS_ROW]));


         Atlantis atlantis = Atlantis.createAtlantis(properties[Atlantis.ATLANTIS_ID],
                 pt, this.imageStore.getImageList(Atlantis.ATLANTIS_KEY));

         /*
         Entity entity = new Atlantis(properties[Atlantis.ATLANTIS_ID],
                 pt, this.imageStore.getImageList(Atlantis.ATLANTIS_KEY),
                 0, 0, 0, 0);
          */

         this.world.tryAddEntity(atlantis);
      }

      return properties.length == Atlantis.ATLANTIS_NUM_PROPERTIES;
   }

   private boolean parseSgrass(String [] properties)
   {
      if (properties.length == SeaGrass.SGRASS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SeaGrass.SGRASS_COL]),
                 Integer.parseInt(properties[SeaGrass.SGRASS_ROW]));


         SeaGrass seaGrass = SeaGrass.createSgrass(properties[SeaGrass.SGRASS_ID], pt,
                 this.imageStore.getImageList(SeaGrass.SGRASS_KEY),
                 Integer.parseInt(properties[SeaGrass.SGRASS_ACTION_PERIOD]));

         /*
         Entity entity = new SeaGrass(properties[SeaGrass.SGRASS_ID],pt, this.imageStore.getImageList(SeaGrass.SGRASS_KEY),
                 0, 0,
                 Integer.parseInt(properties[SeaGrass.SGRASS_ACTION_PERIOD]),0);
          */

         this.world.tryAddEntity(seaGrass);
      }

      return properties.length == SeaGrass.SGRASS_NUM_PROPERTIES;
   }


   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
