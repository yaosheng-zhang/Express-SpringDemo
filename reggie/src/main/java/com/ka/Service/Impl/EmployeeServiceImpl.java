package com.ka.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ka.Controller.Result;
import com.ka.Mapper.EmployeeMapper;
import com.ka.Pojo.Employee;
import com.ka.Pojo.PageRequest;
import com.ka.Service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;


    @Override
    public Result login(Employee employee) {
        //1、将页面提交的密码进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2、根据页面提交的用户名来查数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = (Employee) employeeMapper.selectOne(queryWrapper);
        //3.判断emp
        if (emp==null)
        {
            return Result.error("没有此账号！");
        }
        if (!password.equals(emp.getPassword()))
        {
            return Result.error("密码错误！");
        }
        //4.判断员工状态
        if (emp.getStatus()==0)
        {
            return Result.error("账号被禁用");
        }
        //6.登陆成功

        return Result.success(emp);
    }

    @Override
    public Result logout(Employee employee) {

        return null;
    }

    @Override
    public void save(Employee employee) {
        employeeMapper.insert(employee);
    }

    @Override
    public Result findByPage(PageRequest pageRequest) {

        //1.构造分页器
        Page<Employee> page = new Page<>(pageRequest.getPage(), pageRequest.getPageSize());
        //2.查找条件构造器
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = Wrappers.lambdaQuery();
        //3.添加过滤条件
        employeeLambdaQueryWrapper.like(Employee::getName,pageRequest.getName());
        //4.添加排序规则
        employeeLambdaQueryWrapper.orderByAsc(Employee::getCreateTime);
        //5.执行查询
        employeeMapper.selectPage(page, employeeLambdaQueryWrapper);


        return  Result.success(page);
    }

    @Override
    public void updateById(Employee employee) {
        employeeMapper.updateById(employee);
    }

    @Override
    public Employee getById(String id) {
        Employee employee = employeeMapper.selectById(id);
        return employee;
    }


}
