package com.jbtits.otus.lecture8;
import java.util.*;

public class Bag {
    private double doubles[];
    private String strings[];
    private Map<String, String> map;
    private List<String> list;
    private Queue<String> queue;
    private Set<String> set;

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof Bag)) {
            return false;
        }
        Bag bag = (Bag) o;

        return Arrays.equals(bag.doubles, doubles)
            && Arrays.equals(bag.strings, strings)
            && bag.list.equals(this.list)
            && bag.map.equals(this.map)
            && bag.set.equals(this.set);
    }

    public void setDoubles(double[] doubles) {
        this.doubles = doubles;
    }

    public void setStrings(String[] strings) {
        this.strings = strings;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void setQueue(Queue<String> queue) {
        this.queue = queue;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }
}
