package Part1;

/*
 * This file should remain unchanged.
 */
public class CourseGrade {
  private String courseName;
  private int score; 

  /**
   * This is a constructor. It creates a new CourseGrade with the specified scores.
   * @param courseName
   * @param score 
   */
  public CourseGrade(String courseName, int score) {
    this.courseName = courseName;
    this.score = score;
  }

  public String getCourseName() {
    return this.courseName;
  }

  public int getScore() {
    return this.score;
  }
}
