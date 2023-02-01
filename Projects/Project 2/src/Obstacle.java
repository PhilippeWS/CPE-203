import processing.core.PImage;

import java.util.List;

public class Obstacle implements Entity{
    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_NUM_PROPERTIES = 4;
    public static final int OBSTACLE_ID = 1;
    public static final int OBSTACLE_COL = 2;
    public static final int OBSTACLE_ROW = 3;

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;

    public Obstacle(String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
    }


    public static Obstacle createObstacle(String id, Point position, List<PImage> images)
    {
        return new Obstacle(id, position, images, 0, 0, 0);
    }


    @Override
    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    @Override
    public int getActionPeriod() {
        return this.actionPeriod;
    }

    @Override
    public Point getPosition() {
        return this.position;
    }
}
