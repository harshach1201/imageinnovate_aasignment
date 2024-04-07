package com.harsha.assignment.entity;

import jakarta.persistence.*;

@Entity
public class MobileNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String mobileNumber;
    @ManyToOne
    @JoinColumn(name = "employeeId")
    private Employee employee;

    public MobileNumber() {
    }

    public MobileNumber(String mobileNumber, Employee employee) {
        this.mobileNumber = mobileNumber;
        this.employee = employee;
    }

    public long getId() {
        return id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
