package com.ka.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ka.Pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
