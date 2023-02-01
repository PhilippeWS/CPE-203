package Part1;

public class SimpleArray {

  /**
   * Write a function that takes in an applicant's numerical scores and returns
   * Boolean values to tell us if they are above a certain threshold.
   * 
   * For example, if the applicant's scores are [80, 85, 89, 92, 76, 81], and the
   * threshold value is 85, return [false, false, true, true, false, false].
   * 
   * @param scores    The applicant's array of scores
   * @param threshold The threshold value
   * @return An array of boolean values: true if the score is higher than
   *         threshold, false otherwise
   */
  public static boolean[] applicantAcceptable(int[] scores, int threshold) {
    /*
     * TO DO: This size is not right. Fix it to work with any input array. The
     * length of an array is accessible through an array's length field (e.g.,
     * values.length).
     */
    boolean[] highScores = new boolean[scores.length];

    /*
     * TO DO: The output array, highScores, should hold as its elements the
     * appropriate boolean (true or false) value.
     * 
     * Write a loop to compute the acceptability of the scores based on the
     * threshold and place the result into the output array.
     */
    for(int index = 0; index<scores.length-1; index++){
      if(scores[index]>threshold){
        highScores[index]=true;
      }else {
        highScores[index]=false;
      }
    }
    return highScores;
  }
}
