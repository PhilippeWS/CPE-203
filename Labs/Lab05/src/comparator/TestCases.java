package comparator;

import java.util.Comparator;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestCases
{
   private static final Song[] songs = new Song[] {
         new Song("Decemberists", "The Mariner's Revenge Song", 2005),
         new Song("Rogue Wave", "Love's Lost Guarantee", 2005),
         new Song("Avett Brothers", "Talk on Indolence", 2006),
         new Song("Gerry Rafferty", "Baker Street", 1998),
         new Song("City and Colour", "Sleeping Sickness", 2007),
         new Song("Foo Fighters", "Baker Street", 1997),
         new Song("Queen", "Bohemian Rhapsody", 1975),
         new Song("Gerry Rafferty", "Baker Street", 1978)
      };

   @Test
   public void testArtistComparator()
   {
      ArtistComparator artistComparator = new ArtistComparator();
      assertTrue(artistComparator.compare(songs[0], songs[1]) < 0);
      assertTrue(artistComparator.compare(songs[1], songs[2]) > 0);
      assertTrue(artistComparator.compare(songs[3], songs[7]) == 0);
   }

   @Test
   public void testLambdaTitleComparator()
   {
      Comparator<Song> titleComparator = (song1, song2) -> ((Song) song1).getTitle().compareTo(((Song) song2).getTitle());
      assertTrue(titleComparator.compare(songs[1], songs[2]) < 0);
      assertTrue(titleComparator.compare(songs[0], songs[1]) > 0);
      assertTrue(titleComparator.compare(songs[3], songs[7]) == 0);
   }

   @Test
   public void testYearExtractorComparator()
   {
      Comparator<Song> yearComparator = Comparator.comparing(Song::getYear).reversed();
      assertTrue(yearComparator.compare(songs[2], songs[3]) < 0);
      assertTrue(yearComparator.compare(songs[1], songs[2]) > 0);
      assertTrue(yearComparator.compare(songs[0], songs[1]) == 0);
   }

   @Test
   public void testComposedComparator()
   {
      Comparator<Song> yearComparator = Comparator.comparing(Song::getYear).reversed();
      Comparator<Song> titleComparator = (song1, song2) -> ((Song) song1).getTitle().compareTo(((Song) song2).getTitle());
      ComposedComparator comparator = new ComposedComparator(yearComparator, titleComparator);

      assertTrue(comparator.compare(songs[0],songs[1]) > 0);
      assertTrue(comparator.compare(songs[5],songs[4]) > 0);
      //assertTrue(comparator.compare(songs[0],songs[1]) == 0);

   }

   @Test
   public void testThenComparing()
   {
      ArtistComparator artistComparator = new ArtistComparator();
      Comparator comparator = Comparator.comparing(Song::getTitle).thenComparing(artistComparator);
      assertTrue(comparator.compare(songs[3], songs[5]) > 0);
      assertTrue(comparator.compare(songs[3], songs[2]) < 0);
      assertTrue(comparator.compare(songs[3], songs[7]) == 0);
   }

   @Test
   public void runSort()
   {
      List<Song> songList = new ArrayList<>(Arrays.asList(songs));
      List<Song> expectedList = Arrays.asList(
         new Song("Avett Brothers", "Talk on Indolence", 2006),
         new Song("City and Colour", "Sleeping Sickness", 2007),
         new Song("Decemberists", "The Mariner's Revenge Song", 2005),
         new Song("Foo Fighters", "Baker Street", 1997),
         new Song("Gerry Rafferty", "Baker Street", 1978),
         new Song("Gerry Rafferty", "Baker Street", 1998),
         new Song("Queen", "Bohemian Rhapsody", 1975),
         new Song("Rogue Wave", "Love's Lost Guarantee", 2005)
         );
      Comparator<Song> artistComparator = (song1, song2) -> ((Song) song1).getArtist().compareTo(((Song) song2).getArtist());
      Comparator<Song> titleComparator = (song1, song2) -> ((Song) song1).getTitle().compareTo(((Song) song2).getTitle());
      Comparator<Song> yearComparator = (song1, song2) ->  ((Song) song1).getYear() - ((Song) song2).getYear();

      Comparator comparator = artistComparator.thenComparing(titleComparator).thenComparing(yearComparator);

      songList.sort(comparator);

      Comparator<Song> artist1Comparator = Comparator.comparing(Song::getArtist);


      assertEquals(songList, expectedList);
   }
}
