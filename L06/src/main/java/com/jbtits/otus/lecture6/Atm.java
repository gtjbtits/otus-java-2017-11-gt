package com.jbtits.otus.lecture6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.util.*;

public class Atm {

    private final SortedList<MoneyCell> cells;

    public Atm() {
        ObservableList<MoneyCell> list = FXCollections.observableArrayList();
        list.addAll(
            new MoneyCell(Denomination.TEN),
            new MoneyCell(Denomination.HUNDRED),
            new MoneyCell(Denomination.THOUSAND));
        cells = new SortedList<>(list, Collections.reverseOrder());
    }

    public boolean put(Denomination denomination, int amount) {
        boolean success = false;
        for (MoneyCell cell : cells) {
            if (cell.getDenomination().equals(denomination)) {
                cell.put(amount);
                success = true;
            }
        }
        return success;

    }

    public void get(final int sum) {
        Map<Denomination, Integer> memento = new HashMap<>();
        int toGo = sum;
        for (MoneyCell cell : cells) {
            int needle = Integer.min(cell.getCount(), toGo / cell.getDenomination().getValue());
            if (needle > 0) {
                memento.put(cell.getDenomination(), needle);
                toGo -= cell.delagatedSum(needle);
            }
        }
        if (toGo > 0) {
            throw new NotEnoughNotes();
        } else {
            for (MoneyCell cell : cells) {
                if (memento.containsKey(cell.getDenomination())) {
                    cell.get(memento.get(cell.getDenomination()));
                }
            }
        }
    }

    public int sum() {
        return cells.stream().mapToInt(c -> c.sum()).sum();
    }
}
