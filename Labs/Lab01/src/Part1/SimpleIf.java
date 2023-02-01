package Part1;

import Part2.Applicant;

public class SimpleIf {

  /**
   * Takes an applicant's average score and accepts the applicant if the average
   * is higher than 85.
   * 
   * @param avg The applicant's average score
   * @param threshold The threshold score
   * @return true if the applicant's average is over the threshold, and false otherwise
   */
  public static boolean analyzeApplicant(double avg, double threshold) {
    /*
     * TO DO: Write an if statement to determine if the avg is larger than the threshold and
     * return true if so, false otherwise
     */
    boolean avgLargerThanThreshhold5 = false;

    if(avg>threshold){
      avgLargerThanThreshhold5 = true;
    }
    return avgLargerThanThreshhold5; // A bit pessimistic!
  }

  /**
   * The test is very strict, it only accepts applicants who:
   * Have above a certain grade threshold for every class, regardless if it is a CS class or not.
   * Have some predefined years of experience.
   * Have a letter of recommendation.
   * (The required years of experience and threshold obviously may vary per employer)
   *
   * This filter will only allow the best and brightest to be employed at Moogle, no one else.
   *
  */
  public static boolean analyzeApplicant2(Applicant applicant, int minimumExperience, int scoreThreshold){
    boolean isHireable = false;

    if(applicant.isHasLetterOfRecommendation()){
      if(applicant.getYearsOfExperience() >= minimumExperience){
        boolean isHighScoring = true;
        for(CourseGrade courseGrade : applicant.getGrades()){
          if(courseGrade.getScore() < scoreThreshold){
            isHighScoring = false;
          }
        }
        if(isHighScoring){
          isHireable = true;
        }
      }
    }
    return isHireable;
  }

  /**
   * Takes two applicants' average scores and returns the score of the applicant
   * with the higher average.
   * 
   * @param avg1 The first applicant's average score
   * @param avg2 The second applicant's average score
   * @return the higher average score
   */
  public static double maxAverage(double avg1, double avg2) {
    /*
     * TO DO: Write an if statement to determine which average is larger and return
     * that value.
     */
    double largerAverage;

    if(avg1 > avg2){
      largerAverage = avg1;
    }else{
      largerAverage = avg2;
    }

    return largerAverage; // Clearly not correct, but testable.
  }
}
