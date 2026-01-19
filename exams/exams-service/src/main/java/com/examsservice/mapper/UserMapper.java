package com.examsservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.dto.StudentsPageQueryDTO;
import com.exam.entity.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {


}
