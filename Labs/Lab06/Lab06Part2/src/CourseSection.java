import java.time.LocalTime;

class CourseSection
{
   private final String prefix;
   private final String number;
   private final int enrollment;
   private final LocalTime startTime;
   private final LocalTime endTime;

   public CourseSection(final String prefix, final String number,
      final int enrollment, final LocalTime startTime, final LocalTime endTime)
   {
      this.prefix = prefix;
      this.number = number;
      this.enrollment = enrollment;
      this.startTime = startTime;
      this.endTime = endTime;
   }

   public boolean equals(Object other){
      if(other == null || !(other instanceof CourseSection))
         return false;
      else if (other == this)
         return true;
      else
         return this.prefix.equals(((CourseSection) other).prefix) &&
                 this.number.equals(((CourseSection) other).number) &&
                 this.enrollment == ((CourseSection) other).enrollment &&
                 this.startTime.equals(((CourseSection) other).startTime) &&
                 this.endTime.equals(((CourseSection) other).endTime);

   }

   public int hashCode(){
      int hashCode = 0;

      hashCode+= this.prefix.hashCode();
      hashCode+= this.number.hashCode();
      hashCode+= this.enrollment;
      hashCode+= this.startTime.hashCode();
      hashCode+= this.endTime.hashCode();

      return hashCode*31;
   }


   // additional likely methods not defined since they are not needed for testing
}
