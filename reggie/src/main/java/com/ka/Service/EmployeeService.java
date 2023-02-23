package com.ka.Service;


import com.ka.Controller.Result;
import com.ka.Pojo.Employee;
import com.ka.Pojo.PageRequest;
import org.springframework.stereotype.Service;


public interface EmployeeService {
    Result login(Employee employee);
    Result logout(Employee employee);

    void save(Employee employee);

    Result findByPage(PageRequest pageRequest);

    void updateById(Employee employee);

    Employee getById(String id);
}
