package Part1;

public class BetterLoop {
  /**
   * Accept an applicant if they have at least 4 grades above 85. Their non-CS
   * GPA counts as a grade in this case.
   * 
   * @param scores The applicant's list of scores
   * @return true if the applicant meets the requirements
   */
  public static boolean atLeastFourOver85(int[] scores) {
    /*
     * Use a FOR-EACH loop. How would you keep count of the number of scores over 85?
     */
    boolean fourAbove85 = false;
    int above85 = 0;
    if(scores.length >= 4){
      for(int score: scores){
        if(score > 85){
          above85++;
        }
      }
      if(above85 >= 4){
        fourAbove85=true;
      }
    }else{}

    return fourAbove85;
  }

  /**
   * Compute an applicant's average score in their 5 CS courses (that is, you must
   * NOT consider the final item in the array, the non-CS GPA).
   * 
   * @param scores
   * @return The average score
   */
  public static double average(int[] scores) {
    /*
     * A "normal" for-loop can sometimes be more useful than a for-each loop. How would
     * you solve this problem with a for loop?
     */
    int avg = 0;
    for(int index=0; index < scores.length-2; index++){
      avg+= scores[index];
    }
    avg = avg/(scores.length);

    return avg;
  }
}
