package com.ka.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ka.Mapper.EmployeeMapper;
import com.ka.Pojo.Employee;
import com.ka.Pojo.PageRequest;
import com.ka.Service.EmployeeService;
import com.ka.Service.Impl.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeServiceImpl employeeService;

    @PostMapping("/login")
    public Result login(HttpServletRequest request, @RequestBody Employee employee)
    {
        Result login = employeeService.login(employee);
        if (login.getCode()==1)
        {
            request.getSession().setAttribute("employee",employee.getId());

        }
        return login;
    }
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request)
    {
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功！");
    }
    //新增员工
    @PostMapping
    public Result<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());
        //设置初始密码，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        Long empId = (Long) request.getSession().getAttribute("employee");

        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return Result.success("新增员工成功");
    }
    @GetMapping
    public Result<String> findByPage(@RequestBody PageRequest pageRequest)
    {
        log.info("page={},pageSize={},name={}", pageRequest.getPage(), pageRequest.getPageSize(), pageRequest.getName());

        Result byPage = employeeService.findByPage(pageRequest);

        return byPage;

    }
    @PutMapping
    public Result<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return Result.success("员工信息修改成功");
    }
    //根据id查询员工信息
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable String id){
        log.info("根据id查对象");
        Employee emp = employeeService.getById(id);
        if(emp!=null){
            return Result.success(emp);
        }
        return Result.error("没有查询到该用户信息");
    }


}
