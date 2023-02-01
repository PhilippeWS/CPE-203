package Part2;

import Part1.CourseGrade;
import Part1.SimpleIf;
import org.junit.Assert;
import org.junit.jupiter.api.Test;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCases {
   /*
    * This test is just to get you started.
    */
   @Test
   public void testGetName()   {
      // This will not compile until you implement the Applicant class
      List<CourseGrade> grades = Arrays.asList(
         new CourseGrade("Intro to CS", 100),
         new CourseGrade("Data Structures", 95),
         new CourseGrade("Algorithms", 91),
         new CourseGrade("Computer Organization", 91),
         new CourseGrade("Operating Systems", 75),
         new CourseGrade("Non-CS", 83)
      );
      Applicant testApplicant = new Applicant("Aakash", grades);
      assertEquals("Aakash", testApplicant.getName());
   }

   @Test
   public void testAnalyzeApplicant2_1(){
      List<CourseGrade> grades = Arrays.asList(
              new CourseGrade("Intro to CS", 99),
              new CourseGrade("Data Structures", 97),
              new CourseGrade("Algorithms", 94),
              new CourseGrade("Computer Organization", 100),
              new CourseGrade("Operating Systems", 92),
              new CourseGrade("Non-CS", 93)
      );

      Applicant testApplicant = new Applicant("Phil",grades, 2,  true);
      assertTrue(SimpleIf.analyzeApplicant2(testApplicant, 2, 90));
   }

   @Test
   public void testAnalyzeApplicant2_2(){
      List<CourseGrade> grades = Arrays.asList(
              new CourseGrade("Intro to CS", 95),
              new CourseGrade("Data Structures", 89),
              new CourseGrade("Algorithms", 90),
              new CourseGrade("Computer Organization", 88),
              new CourseGrade("Operating Systems", 86),
              new CourseGrade("Non-CS", 91)
      );

      Applicant testApplicant = new Applicant("Josh",grades, 2,  true);
      assertFalse(SimpleIf.analyzeApplicant2(testApplicant, 3, 85));
   }

   @Test
   public void testAnalyzeApplicant2_3(){
      List<CourseGrade> grades = Arrays.asList(
              new CourseGrade("Intro to CS", 83),
              new CourseGrade("Data Structures", 91),
              new CourseGrade("Algorithms", 82),
              new CourseGrade("Computer Organization", 78),
              new CourseGrade("Operating Systems", 96),
              new CourseGrade("Non-CS", 89)
      );

      Applicant testApplicant = new Applicant("Sam",grades, 2,  true);
      assertFalse(SimpleIf.analyzeApplicant2(testApplicant, 2, 80));
   }

   @Test
   public void testAnalyzeApplicant2_4(){
      List<CourseGrade> grades = Arrays.asList(
              new CourseGrade("Intro to CS", 94),
              new CourseGrade("Data Structures", 95),
              new CourseGrade("Algorithms", 94),
              new CourseGrade("Computer Organization", 97),
              new CourseGrade("Operating Systems", 92),
              new CourseGrade("Non-CS", 91)
      );

      Applicant testApplicant = new Applicant("Phil",grades, 3,  false);
      assertFalse(SimpleIf.analyzeApplicant2(testApplicant, 3, 90));
   }

   /*
    * The tests below here are to verify the basic requirements regarding
    * the "design" of your class.  These are to remain unchanged.
    */
   @Test
   public void testImplSpecifics()
      throws NoSuchMethodException   {
      final List<String> expectedMethodNames = Arrays.asList(
         "getName",
         "getGrades",

         "getYearsOfExperience",
         "isHasLetterOfRecommendation", "getGradeFor"
      );

      final List<Class> expectedMethodReturns = Arrays.asList(
         String.class,
         List.class,

         int.class,
         boolean.class,
         CourseGrade.class
      );

      final List<Class[]> expectedMethodParameters = Arrays.asList(
         new Class[0],
         new Class[0], new Class[0], new Class[0],
         new Class[] { String.class }
         );

      verifyImplSpecifics(Applicant.class, expectedMethodNames,
         expectedMethodReturns, expectedMethodParameters);
   }

   private static void verifyImplSpecifics(
      final Class<?> clazz,
      final List<String> expectedMethodNames,
      final List<Class> expectedMethodReturns,
      final List<Class[]> expectedMethodParameters)
      throws NoSuchMethodException    {
      assertEquals(0, Applicant.class.getFields().length,
              "Unexpected number of public fields");

      final List<Method> publicMethods = Arrays.stream(
         clazz.getDeclaredMethods())
            .filter(m -> Modifier.isPublic(m.getModifiers()))
            .collect(Collectors.toList());

      assertTrue(expectedMethodNames.size()+1 >= publicMethods.size(),
              "Unexpected number of public methods");

      assertTrue(expectedMethodNames.size() == expectedMethodReturns.size(),
              "Invalid test configuration");
      assertTrue(expectedMethodNames.size() == expectedMethodParameters.size(),
              "Invalid test configuration");

      for (int i = 0; i < expectedMethodNames.size(); i++)       {
         Method method = clazz.getDeclaredMethod(expectedMethodNames.get(i),
            expectedMethodParameters.get(i));
         assertEquals(expectedMethodReturns.get(i), method.getReturnType());
      }
   }
}
