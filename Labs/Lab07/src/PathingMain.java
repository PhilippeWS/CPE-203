import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

import processing.core.*;

public class PathingMain extends PApplet
{
   private List<PImage> imgs;
   private int current_image;
   private long next_time;
   private PImage background;
   private PImage obstacle;
   private PImage goal;
   private List<Point> path;

   private static final int TILE_SIZE = 32;

   private static final int ANIMATION_TIME = 100;

   private GridValues[][] grid;
   private static final int ROWS = 15;
   private static final int COLS = 20;

   private static enum GridValues { BACKGROUND, OBSTACLE, GOAL, SEARCHED };

   private Point wPos;

   private boolean drawPath = false;

	public void settings() {
      size(640,480);
	}
	
	/* runs once to set up world */
   public void setup()
   {

      path = new LinkedList<>();
      wPos = new Point(2, 2);
      imgs = new ArrayList<>();
      imgs.add(loadImage("images/wyvern1.bmp"));
      imgs.add(loadImage("images/wyvern2.bmp"));
      imgs.add(loadImage("images/wyvern3.bmp"));

      background = loadImage("images/grass.bmp");
      obstacle = loadImage("images/vein.bmp");
      goal = loadImage("images/water.bmp");

      grid = new GridValues[ROWS][COLS];
      initialize_grid(grid);

      current_image = 0;
      next_time = System.currentTimeMillis() + ANIMATION_TIME;
      noLoop();
      draw();
   }

	/* set up a 2D grid to represent the world */
   private static void initialize_grid(GridValues[][] grid)
   {
      for (int row = 0; row < grid.length; row++)
      {
         for (int col = 0; col < grid[row].length; col++)
         {
            grid[row][col] = GridValues.BACKGROUND;
         }
      }

		//set up some obstacles
      for (int row = 2; row < 8; row++)
      {
         grid[row][row + 5] = GridValues.OBSTACLE;
      }

      for (int row = 8; row < 12; row++)
      {
         grid[row][19 - row] = GridValues.OBSTACLE;
      }

      for (int col = 1; col < 8; col++)
      {
         grid[11][col] = GridValues.OBSTACLE;
      }

      grid[2][10] = GridValues.GOAL;
   }

   private void next_image()
   {
      current_image = (current_image + 1) % imgs.size();
   }

	/* runs over and over */
   public void draw()
   {
      // A simplified action scheduling handler
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         next_image();
         next_time = time + ANIMATION_TIME;
      }

      draw_grid();
      draw_path();

      image(imgs.get(current_image), wPos.x * TILE_SIZE, wPos.y * TILE_SIZE);
   }

   private void draw_grid()
   {
      for (int row = 0; row < grid.length; row++)
      {
         for (int col = 0; col < grid[row].length; col++)
         {
            draw_tile(row, col);
         }
      }
   }

   private void draw_path()
   {
      if (drawPath)
      {
         for (Point p : path)
         {
            fill(128, 0, 0);
            rect(p.x * TILE_SIZE + TILE_SIZE * 3 / 8,
               p.y * TILE_SIZE + TILE_SIZE * 3 / 8,
               TILE_SIZE / 4, TILE_SIZE / 4);
         }
      }
   }

   private void draw_tile(int row, int col)
   {
      switch (grid[row][col])
      {
         case BACKGROUND:
            image(background, col * TILE_SIZE, row * TILE_SIZE);
            break;
         case OBSTACLE:
            image(obstacle, col * TILE_SIZE, row * TILE_SIZE);
            break;
         case SEARCHED:
            fill(0, 128);
            rect(col * TILE_SIZE + TILE_SIZE / 4,
               row * TILE_SIZE + TILE_SIZE / 4,
               TILE_SIZE / 2, TILE_SIZE / 2);
            break;
         case GOAL:
            image(goal, col * TILE_SIZE, row * TILE_SIZE);
            break;
      }
   }

   public static void main(String args[])
   {
      PApplet.main("PathingMain");
   }

   public void keyPressed()
   {
      if (key == ' ')
      {
         //Clear grid for proper repetitiion
         initialize_grid(grid);
         //clear out prior path
         path.clear();
         //example - replace with dfs\

         path.add(wPos);
         grid[wPos.y][wPos.x] = GridValues.SEARCHED;
         do{
            depthFirstSearch(path.get(path.size()-1), grid, path);
            redraw();
         } while(grid[path.get(path.size()-1).y][path.get(path.size()-1).x] != GridValues.GOAL);

      }
      else if (key == 'p')
      {
         drawPath ^= true;
         redraw();
      }
   }

	/* replace the below with a depth first search 
		this code provided only as an example of moving in
		in one direction for one tile - it mostly is for illustrating
		how you might test the occupancy grid and add nodes to path!
	*/
   private boolean depthFirstSearch(Point pos, GridValues[][] grid, List<Point> path)
   {
      Point nextPoint = new Point(pos.x +1, pos.y );

      Function<Point, Boolean> pointValidator = (Point point) -> (withinBounds(point, grid)  &&
              grid[point.y][point.x] != GridValues.OBSTACLE &&
              grid[point.y][point.x] != GridValues.SEARCHED);
      /*
      Function<Point, Point> pointDirector = (Point point) ->
      {
         Boolean pointValidity = pointValidator.apply((point));

         if(!pointValidity){
            int directionCase = 0;
            while(!pointValidity){
               switch (directionCase){
                  case 0:
                     point = new Point(pos.x, pos.y+1);
                     pointValidity = pointValidator.apply((point));
                     directionCase++;
                     break;
                  case 1:
                     point = new Point(pos.x-1, pos.y);
                     pointValidity = pointValidator.apply((point));
                     directionCase++;
                     break;
                  case 2:
                     point = new Point(pos.x, pos.y-1);
                     pointValidity = pointValidator.apply((point));
                     directionCase++;
                     break;
                  default:
                     directionCase = 0;
                     path.remove(path.size()-1);
                     point = path.get(path.size()-1);
                     depthFirstSearch(point, grid, path);
                     pointValidity = pointValidator.apply(point);
                     break;
               }
            }
            return point;
         }else{
            return point;
         }
      };

       */

      Boolean pointValidity = pointValidator.apply(nextPoint);

      if(!pointValidity) {
         int directionCase = 0;
         while (!pointValidity) {
            switch (directionCase) {
               case 0: //Set next point Down
                  nextPoint = new Point(pos.x, pos.y + 1);
                  pointValidity = pointValidator.apply(nextPoint);
                  directionCase++;
                  break;
               case 1: //Set next point Left
                  nextPoint = new Point(pos.x - 1, pos.y);
                  pointValidity = pointValidator.apply(nextPoint);
                  directionCase++;
                  break;
               case 2: //Set next point Right
                  nextPoint = new Point(pos.x, pos.y - 1);
                  pointValidity = pointValidator.apply(nextPoint);
                  directionCase++;
                  break;
               default: //Since none are correct, break out of the loop to allow other logic to handle.
                  pointValidity = true;
                  break;
            }
         }
      }

      //Check if next point is invalid, since
      if (pointValidator.apply(nextPoint)) {
         path.add(nextPoint);
         //check if my right neighbor is the goal
         if (grid[nextPoint.y][nextPoint.x] == GridValues.GOAL) {
            path.add(0, nextPoint);
            return true;
         }
         //set this value as searched
      	 grid[nextPoint.y][nextPoint.x] = GridValues.SEARCHED;
         return false;
      }else{
         path.remove(path.size() - 1);
         depthFirstSearch(path.get(path.size()-1), grid, path);
         return false;
      }
   }

   private static boolean withinBounds(Point p, GridValues[][] grid)
   {
      return p.y >= 0 && p.y < grid.length &&
         p.x >= 0 && p.x < grid[0].length;
   }


}
