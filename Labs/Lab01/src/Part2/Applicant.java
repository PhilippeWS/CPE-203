package Part2;

import Part1.CourseGrade;

import java.util.List;

public class Applicant {
    private String name;
    private List<CourseGrade> grades;
    private int yearsOfExperience;
    private boolean hasLetterOfRecommendation;


    public Applicant(String name, List<CourseGrade> grades){
        this.name = name;
        this.grades = grades;
    }

    public Applicant(String name, List<CourseGrade> grades, int yearsOfExperience, boolean hasLetterOfRecommendation){
        this.name = name;
        this.grades = grades;
        this.yearsOfExperience = yearsOfExperience;
        this.hasLetterOfRecommendation = hasLetterOfRecommendation;
    }

    public String getName(){
        return name;
    }

    public List<CourseGrade> getGrades(){
        return grades;
    }

    public int getYearsOfExperience(){
        return yearsOfExperience;
    }

    public boolean isHasLetterOfRecommendation(){
        return hasLetterOfRecommendation;
    }



    public CourseGrade getGradeFor(String course){
        CourseGrade selectedCourseGrade = new CourseGrade("No Course Found", 0);

        for(CourseGrade courseGrade: grades){
            if(courseGrade.getCourseName().equals(course)){
                selectedCourseGrade = courseGrade;
                break;
            }
        }
        return selectedCourseGrade;
    }
}
