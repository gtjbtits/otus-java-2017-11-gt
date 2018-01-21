package com.jbtits.otus.lecture7;

public class MoneyCell implements Comparable<MoneyCell> {
    private Denomination denomination;
    private int count;

    MoneyCell(Denomination denomination) {
        this.denomination = denomination;
    }

    public int getCount() {
        return count;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public int sum() {
        return count * denomination.getValue();
    }

    public int delagatedSum(int amount) {
        return amount * denomination.getValue();
    }

    public void put(int notes) {
        count += notes;
    }

    public void get(int notes) {
        if (count < notes) {
            throw new NotEnoughNotes();
        }
        count -= notes;
    }

    public int compareTo(MoneyCell o) {
        return denomination.getValue() - o.getDenomination().getValue();
    }
}
