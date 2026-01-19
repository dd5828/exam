package com.examsservice.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.entity.ClassInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 何时去
 * @since 2024-09-02
 */
@Mapper
public interface ClassInfoMapper extends BaseMapper<ClassInfo> {

    @Select("SELECT * FROM class_info WHERE class_id = #{studentId}")
    ClassInfo selectByClassId(int studentId);
}
