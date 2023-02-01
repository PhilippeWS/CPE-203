/*
Name: Philippe Wylezek-Serrano
Section: 3
*/
public class Lab00 {
    public static void main(String[] arguments) {
        //declaring and initializing some variables
        int x = 5;
        String y = "hello";
        double z = 9.8;

        //printing the variables
        System.out.printf("x: %o, y: %s, z: %.1f \n", x, y ,z);

        //a list (make an array in java)
        int[] nums = {3, 6, -1, 2};

        for (int num : nums)
        {
            System.out.println(num);
        }

        //call a function
        System.out.println("Found: " + charCount(y, 'l'));

        //a counting for loop
        for(int i = 1; i <= 10; i++)
        {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    //function counts the given character in the given string
    //str str -> int
    public static int charCount(String s, char c){
        char[] sAsArray = s.toCharArray();
        int numFound = 0;
        for (char letter:sAsArray) {
            if(letter == c){
                numFound++;
            }
        }
        return numFound;
    }
}
