import com.jbtits.otus.lecture3.GtArrayList;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GtArrayListTest {
    private List<Integer> list;

    @Before
    public void init() {
        list = new GtArrayList<>();
    }

    @Test
    public void canBeSorted() {
        list.add(3);
        list.add(1);
        list.add(2);
        Collections.sort(list);
        assertEquals(1, (int) list.get(0));
        assertEquals(2, (int) list.get(1));
        assertEquals(3, (int) list.get(2));
    }

    @Test
    public void canBeReversed() {
        list.add(1);
        list.add(2);
        list.add(3);
        Collections.reverse(list);
        assertEquals(3, (int) list.get(0));
        assertEquals(2, (int) list.get(1));
        assertEquals(1, (int) list.get(2));
    }

    @Test
    public void acceptBinarySearch() {
        list.add(1);
        list.add(2);
        list.add(3);
        int found = Collections.binarySearch(list, 3);
        assertEquals(2, found);
    }

    @Test
    public void containsTest() {
        list.add(1);
        list.add(2);
        list.add(3);
        assertTrue(list.contains(2));
        assertFalse(list.contains(5));
    }

    @Test
    public void canBeSwapped() {
        list.add(1);
        list.add(2);
        list.add(3);
        Collections.swap(list, 1,2);
        assertEquals(1, (int) list.get(0));
        assertEquals(3, (int) list.get(1));
        assertEquals(2, (int) list.get(2));
    }

    @Test
    public void canBeFilled() {
        list.add(1);
        list.add(2);
        list.add(3);
        Collections.fill(list, 4);
        assertEquals(4, (int) list.get(0));
        assertEquals(4, (int) list.get(1));
        assertEquals(4, (int) list.get(2));
    }

    @Test
    public void canBeRotated() {
        list.add(1);
        list.add(2);
        list.add(3);
        Collections.rotate(list, 1);
        assertEquals(3, (int) list.get(0));
        assertEquals(1, (int) list.get(1));
        assertEquals(2, (int) list.get(2));
    }

    @Test
    public void acceptMin() {
        list.add(1);
        list.add(2);
        list.add(3);
        int min = Collections.min(list);
        assertEquals(1, min);
    }

    @Test
    public void acceptMax() {
        list.add(1);
        list.add(2);
        list.add(3);
        int max = Collections.max(list);
        assertEquals(3, max);
    }

    @Test
    public void canBeCopied() {
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> copy = new GtArrayList<>();
        copy.add(0);
        copy.add(0);
        copy.add(0);
        Collections.copy(copy, list);
        assertTrue(list.equals(copy));
    }

    @Test
    public void canBeReplaced() {
        list.add(1);
        list.add(2);
        list.add(3);
        boolean result = Collections.replaceAll(list, 3,5);
        assertTrue(result);
        assertEquals(1, (int) list.get(0));
        assertEquals(2, (int) list.get(1));
        assertEquals(5, (int) list.get(2));
    }

    @Test
    public void sublistTest() {
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> sublist = new GtArrayList<>();
        sublist.add(2);
        sublist.add(3);
        int idx = Collections.indexOfSubList(list, sublist);
        assertEquals(1, idx);
    }
}
