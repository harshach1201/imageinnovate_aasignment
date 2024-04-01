package com.harsha.assignment.exception;

import java.util.function.Supplier;

public class EmployeeNotFoundException extends Exception {

    public EmployeeNotFoundException(String msg){
        super(msg);
    }

}
