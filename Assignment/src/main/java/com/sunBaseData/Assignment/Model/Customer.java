package com.sunBaseData.Assignment.Model;

import jakarta.persistence.*;

@Entity
public class Customer
{
    @Id
    @GeneratedValue(generator = "id_seq", strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "id_seq", sequenceName = "id_sequence", initialValue = 1, allocationSize = 1)
    @Column(name = "Id")
    private int id;
    @Column(name = "First Name", nullable = false)
    private String firstName;
    @Column(name = "Last Name")
    private String lastName;
    @Column(name = "Address", nullable = false)
    private String address;
    @Column(name = "City", nullable = false)
    private String city;
    @Column(name = "State", nullable = false)
    private String state;
    @Column(name = "Email", nullable = false)
    private String email;
    @Column(name = "Phone", length = 10, nullable = false)
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
