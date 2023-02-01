package comparator;

import java.util.ArrayList;
import java.util.Comparator;

public class ArtistComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        return ((Song) o1).getArtist().compareTo(((Song) o2).getArtist());
    }
}
