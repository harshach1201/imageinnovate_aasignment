package com.harsha.assignment.controller;

import com.harsha.assignment.dto.EmployeeDTO;
import com.harsha.assignment.entity.Employee;
import com.harsha.assignment.exception.EmployeeNotFoundException;
import com.harsha.assignment.repository.EmployeeRepository;
import com.harsha.assignment.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

    EmployeeService employeeService;

    @PostMapping("/saveOrUpdateEmployee")
    public ResponseEntity<String> saveOrUpdate(@RequestBody Employee employee){
        long id = employeeService.save(employee);
        return ResponseEntity.ok("Employee Saved in DB successfully!!! with id "+id);
    }

    @GetMapping(value = "/fetchEmployeeById/{employeeId}")
    public ResponseEntity<EmployeeDTO> fetchEmployeeById(@PathVariable String employeeId) throws EmployeeNotFoundException {
        EmployeeDTO employee = employeeService.findEmplyeeById(Integer.parseInt(employeeId));
        return ResponseEntity.ok(employee);
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
}
