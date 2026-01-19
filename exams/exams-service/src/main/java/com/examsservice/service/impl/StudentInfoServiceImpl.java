package com.examsservice.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.constant.MessageConstant;
import com.common.exception.AccountNotFoundException;
import com.common.result.PageResult;
import com.exam.dto.StudentDTO;
import com.exam.dto.StudentsPageQueryDTO;
import com.exam.entity.ClassInfo;
import com.exam.entity.StudentInfo;
import com.exam.entity.User;
import com.exam.vo.StudentInfoVo;
import com.examsservice.mapper.ClassInfoMapper;
import com.examsservice.mapper.StudentInfoMapper;
import com.examsservice.service.IStudentInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
public class StudentInfoServiceImpl extends ServiceImpl<StudentInfoMapper, StudentInfo> implements IStudentInfoService {

    private final StudentInfoMapper studentInfoMapper;
    private final PasswordEncoder passwordEncoder;//加密
    private final ClassInfoMapper classInfoMapper;//班级

    /**
     * 新增学生
     * @param studentDTO
     */
    @Override
    public void insertStudent(StudentDTO studentDTO) {

        String studentNumber=studentDTO.getStudentNumber();//设置学号
        if(studentNumber.length()<6&&!studentNumber.isEmpty()){//判断学号是否小于6位，且不为空
            throw new AccountNotFoundException(MessageConstant.StudentId_less_than);
        }

        //判断学号是否存在
        LambdaQueryWrapper<StudentInfo> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StudentInfo::getStudentNumber,studentNumber);//构造查询条件
        if(!studentInfoMapper.selectList(lambdaQueryWrapper).isEmpty()){
            throw new AccountNotFoundException(MessageConstant.StudentId_EXISTS);
        }

        String password=studentDTO.getPassword();//密码
        if(password.isEmpty()){//如果密码为空
            password=studentNumber.substring(studentNumber.length() - 6);//设置密码为学号后6位
        }
        //对密码加密
        password=passwordEncoder.encode(password);

        StudentInfo studentInfo=StudentInfo.builder()
                .studentName(studentDTO.getStudentName())//学生姓名
                .studentNumber(studentDTO.getStudentNumber())//学生学号
                .classId(studentDTO.getClassId())//班级编号
                .password(password)//密码
                .build();
        //执行新增
        studentInfoMapper.insert(studentInfo);

    }

    /**
     * 学生分页查询
     * @param studentsPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(StudentsPageQueryDTO studentsPageQueryDTO) {
        //基于PageHelper插件实现动态分页查询
        PageHelper.startPage(studentsPageQueryDTO.getPage(), studentsPageQueryDTO.getPageSize());
        Page<StudentInfo> page = studentInfoMapper.pageQuery(studentsPageQueryDTO);
        long total=page.getTotal();
        List<StudentInfo> recoreds=page.getResult();

       List<StudentInfoVo> hhh=new ArrayList<>();
        for (StudentInfo studentInfo : recoreds) {
            // 对每个 studentInfo 进行操作
            log.info("分页班级id:{}",studentInfo.getStudentId());
            ClassInfo classInfo = classInfoMapper.selectByClassId(studentInfo.getClassId());

            if (classInfo!= null) {
                String className = classInfo.getClassName();
                String major = classInfo.getMajor();

                // 创建 StudentInfoVo 对象并设置属性
                StudentInfoVo studentInfoVo = StudentInfoVo.builder()
                        .studentId(studentInfo.getStudentId())
                        .studentNumber(studentInfo.getStudentNumber())
                        .studentName(studentInfo.getStudentName())
                        .className(className)
                        .major(major)
                        .createTime(studentInfo.getCreateTime())
                        .updateTime(studentInfo.getUpdateTime())
                        .build();
                // 将 StudentInfoVo 对象添加到 hhh 列表中
                hhh.add(studentInfoVo);
            } else {
                // 如果 classInfo 为 null，可以进行一些处理，比如设置默认值或者记录日志
                throw new AccountNotFoundException(MessageConstant.UNKNOWN_ERROR);

            }
        }
        return new PageResult(total,hhh);
    }


    /**
     * 修改学生信息
     * @param studentDTO
     */
    @Override
    public void updateStudentInfo(StudentDTO studentDTO) {

        if(!studentDTO.getPassword().isEmpty()){
            String password=passwordEncoder.encode(studentDTO.getPassword());
            StudentDTO studentDTO1=new StudentDTO();
            studentDTO1.setStudentId(studentDTO.getStudentId());
            studentDTO1.setStudentName(studentDTO.getStudentName());
            studentDTO1.setStudentNumber(studentDTO.getStudentNumber());
            studentDTO1.setClassId(studentDTO.getClassId());
            studentDTO1.setPassword(password);
            studentInfoMapper.updateStudent(studentDTO1);
        }else {
            studentInfoMapper.updateStudent(studentDTO);
        }

    }
}
