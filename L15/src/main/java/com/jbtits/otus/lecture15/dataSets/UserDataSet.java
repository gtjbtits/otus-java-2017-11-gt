package com.jbtits.otus.lecture15.dataSets;

import com.jbtits.otus.lecture15.utils.ArrayUtils;
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
                "password='" + password + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", phones=" + phones +
                ", id=" + getId() +
                '}';
    }

    @Column(name = "name", unique = true)
    private String name;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "password")
    private String password;

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
                Objects.equals(password, that.password) &&
                Objects.equals(address, that.address);
    }

    private boolean arePhonesEqual(List<PhoneDataSet> phones, List<PhoneDataSet> thatPhones) {
        return ArrayUtils.doubleEach(phones, thatPhones, PhoneDataSet::equals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password, age, address);
    }
}
