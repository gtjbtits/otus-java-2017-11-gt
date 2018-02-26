package com.jbtits.otus.lecture10.dataSets;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class PhoneDataSet extends DataSet {
    @Column(name="number")
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "number='" + number + '\'' +
                '}';
    }

    @Override
    public int hashCode() {

        return Objects.hash(number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneDataSet that = (PhoneDataSet) o;
        return Objects.equals(number, that.number);
    }
}
