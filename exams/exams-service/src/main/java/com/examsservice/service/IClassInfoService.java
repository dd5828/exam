package com.examsservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.dto.ClassInfoDTO;
import com.exam.entity.ClassInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 何时去
 * @since 2024-09-02
 */
public interface IClassInfoService extends IService<ClassInfo> {

    /**
     * 新增班级
     * @param classInfoDTO
     */
    void insertClass(ClassInfoDTO classInfoDTO);


    ClassInfo getClassById(int id);
}
