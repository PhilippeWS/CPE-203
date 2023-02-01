/*
Viewport ideally helps control what part of the world we are looking at for drawing only what we see
Includes helpful helper functions to map between the viewport and the real world
 */


final class Viewport
{
   private int row;
   private int col;
   private int numRows;
   private int numCols;
   private int rowDelta;
   private int colDelta;

   public Viewport(int numRows, int numCols)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.rowDelta = 0;
      this.colDelta = 0;
   }

   public int getCol() {return col;}

   public int getRow() {return row;}

   public int getNumCols() {return numCols;}

   public int getNumRows() {return numRows;}

   public int getRowDelta() {return rowDelta;}

   public int getColDelta() {return colDelta;}

   public void incrementDeltas(int colDelta, int rowDelta) {

      this.colDelta += colDelta;
      this.rowDelta += rowDelta;

      if(this.colDelta > 20){
         this.colDelta = 20;
      }
      if(this.rowDelta > 15){
         this.rowDelta = 15;
      }if(this.colDelta < 0){
         this.colDelta = 0;
      }if(this.rowDelta < 0){
         this.rowDelta = 0;
      }
   }

   public Point viewportToWorld(int col, int row)
   {
      return new Point(col + this.col, row + this.row);
   }

   public Point worldToViewport(int col, int row)
   {
      return new Point(col - this.col, row - this.row);
   }

   public int clamp(int value, int low, int high)
   {
      return Math.min(high, Math.max(value, low));
   }

   public void shift(int col, int row)
   {
      this.col = col;
      this.row = row;
   }

   public boolean contains(Point p)
   {
      return p.getY() >= this.row && p.getY() < this.row + this.numRows &&
              p.getX() >= this.col && p.getX() < this.col + this.numCols;
   }


}
