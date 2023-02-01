package comparator;

import java.util.Comparator;

public class ComposedComparator implements Comparator<Song> {
    private Comparator<Song> comparator1, comparator2;

    public ComposedComparator(Comparator<Song> comparator1, Comparator<Song> comparator2){
        this.comparator1 = comparator1;
        this.comparator2 = comparator2;
    }

    @Override
    public int compare(Song o1, Song o2) {
        if (this.comparator1.compare(o1, o2) == 0){
            return comparator2.compare(o1,o2);
        }else{
            return comparator1.compare(o1,o2);
        }
    }
}
