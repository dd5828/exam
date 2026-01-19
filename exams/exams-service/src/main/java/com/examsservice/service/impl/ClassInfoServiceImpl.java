package com.examsservice.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.constant.MessageConstant;
import com.common.exception.AccountNotFoundException;
import com.exam.dto.ClassInfoDTO;
import com.exam.entity.ClassInfo;
import com.examsservice.mapper.ClassInfoMapper;
import com.examsservice.service.IClassInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 何时去
 * @since 2024-09-02
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClassInfoServiceImpl extends ServiceImpl<ClassInfoMapper, ClassInfo> implements IClassInfoService {

    private  final ClassInfoMapper classInfoMapper;
    /**
     * 新增班级
     * @param classInfoDTO
     */
    @Override
    public void insertClass(ClassInfoDTO classInfoDTO) {
        if(classInfoDTO.getClassName().isEmpty()||classInfoDTO.getMajor().isEmpty()){//如果班级名为空
            throw new AccountNotFoundException(MessageConstant.Class_Null);
        }
        ClassInfo classInfo = ClassInfo.builder()
                .className(classInfoDTO.getClassName())
                .major(classInfoDTO.getMajor())
                .createTime(LocalDateTime.now())
                .build();
        classInfoMapper.insert(classInfo);
    }

    /**
     * 根据id查询班级
     * @param id
     * @return
     */
    @Override
    public ClassInfo getClassById(int id) {
        return classInfoMapper.selectByClassId(id);
    }
}
