package Part1;

public class SimpleLoop {
  /**
   * Computes the sum of integers between low and high (inclusive). Yes, this can
   * be done without a loop, but the point is to practice the syntax for a loop.
   * 
   * @param low
   * @param high
   * @return An integer that is the sum of numbers between low and high
   */
  public static int sum(int low, int high) {
    int sum = 0;
    for(int startingNumber = low;startingNumber<=high; startingNumber++){
      sum += startingNumber;
    }
    return sum; // Clearly not correct
  }
}