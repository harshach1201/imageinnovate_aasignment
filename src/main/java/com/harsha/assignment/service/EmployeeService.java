package com.harsha.assignment.service;

import com.harsha.assignment.dto.EmployeeDTO;
import com.harsha.assignment.entity.Employee;
import com.harsha.assignment.entity.MobileNumber;
import com.harsha.assignment.exception.EmployeeNotFoundException;
import com.harsha.assignment.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    EmployeeRepository employeeRepository;

    public long save(Employee employee) {
        List<MobileNumber> mobileNumberList = employee.getMobileNumberList();
        if (mobileNumberList != null) {
            for (MobileNumber mobileNumber : mobileNumberList) {
                mobileNumber.setEmployee(employee);
            }
        }
        Employee employee1 = employeeRepository.save(employee);
        return employee1.getEmployeeId();
    }

    public EmployeeDTO findEmplyeeById(long id) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isPresent()){
            return mapToDto(employee.get());
        } else {
            throw new EmployeeNotFoundException("Cannot find employee with given id :"+id);
        }
    }

    private EmployeeDTO mapToDto(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getEmployeeId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setYearlySalary(getYearlyIncome(employee));
        dto.setTaxAmount(calculateTax(employee));
        dto.setCessAmount(calculateCess(employee));
        return dto;
    }

    private double calculateCess(Employee employee) {
        double cess = 0.0;
        double income = getYearlyIncome(employee);
        if(income>2500000){
            cess = (income-2500000)*2/100;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        cess=Double.parseDouble(df.format(cess));
        return cess;
    }

    private double getYearlyIncome(Employee employee) {
        LocalDate dateOfJoining = employee.getDateOfJoining();
        int date = dateOfJoining.getDayOfMonth();
        if(date>30) date=30;
        double income=0.0;
        double firstMonthSal = employee.getSalary();
        double monthlySal = (employee.getSalary()/(30-date+1))*30;
        if(getStartOfCurrentFinancialYear(employee.getDateOfJoining())){
            //If the employee Joined before the start of this financial year.
            income= monthlySal*12;
        } else {
            //If the employee Joined in this financial year.
            int noOfMonthsInCurrentyear = getNoOfMonths(employee.getDateOfJoining());
            income = firstMonthSal + (noOfMonthsInCurrentyear * monthlySal);
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        income=Double.parseDouble(df.format(income));
        return income;
    }

    private boolean getStartOfCurrentFinancialYear(LocalDate doj) {
        Year currentYear = Year.now();

        LocalDate startOfFinancialYear = LocalDate.of(currentYear.getValue(), 4, 1);
        return doj.isBefore(startOfFinancialYear);
    }

    private int getNoOfMonths(LocalDate dateOfJoining) {
        int noOfMonths=0;
        int joiningMonth = dateOfJoining.getMonthValue();
        if (joiningMonth < 3) {
            noOfMonths+=3-joiningMonth;
        } else if (joiningMonth>=4) {
            noOfMonths+=3+(12-joiningMonth);
        }
        return noOfMonths;
    }

    private double calculateTax(Employee employee) {
        double income = getYearlyIncome(employee);
        double tax = 0.0;

        if (income <= 250000) {
            tax = 0;
        }
        else if (income > 250000 && income <= 500000) {
            tax = (income - 250000) * 0.05;
        }
        else if (income > 500000 && income <= 1000000) {
            tax = (250000 * 0.05) + (income - 500000) * 0.10;
        }
        else {
            tax = (250000 * 0.05) + (500000 * 0.10) + (income - 1000000) * 0.20;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        tax=Double.parseDouble(df.format(tax));
        return tax;
    }
    
    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
}
