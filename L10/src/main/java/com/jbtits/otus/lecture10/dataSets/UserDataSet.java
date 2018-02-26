package com.jbtits.otus.lecture10.dataSets;

import com.jbtits.otus.lecture10.utils.ArrayUtils;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user")
@Proxy(lazy = false)
public class UserDataSet extends DataSet {
    @Override
    public String toString() {
        return "UserDataSet{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", phones=" + phones +
                ", id=" + getId() +
                '}';
    }

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @OneToOne(cascade = CascadeType.ALL)
    private AddressDataSet address;

    public List<PhoneDataSet> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDataSet> phones) {
        this.phones = phones;
    }

    @OneToMany(cascade = CascadeType.ALL)
    private List<PhoneDataSet> phones;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDataSet that = (UserDataSet) o;
        if (!arePhonesEqual(phones, that.phones)) return false;
        return getId() == that.getId() && age == that.age &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address);
    }

    private boolean arePhonesEqual(List<PhoneDataSet> phones, List<PhoneDataSet> thatPhones) {
        return ArrayUtils.doubleEach(phones, thatPhones, PhoneDataSet::equals);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, age, address);
    }
}
