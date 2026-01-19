package com.examsservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.constant.MessageConstant;
import com.common.context.BaseContext;
import com.common.exception.AccountNotFoundException;
import com.common.result.PageResult;
import com.exam.dto.QuestionDTO;
import com.exam.dto.QuestionPageQueryDTO;
import com.exam.entity.PaperQuestions;
import com.exam.entity.Questions;
import com.exam.entity.QuestionOptions;
import com.exam.entity.User;
import com.exam.vo.QuestionVO;
import com.exam.vo.questionTypeVo;
import com.examsservice.mapper.PaperQuestionsMapper;
import com.examsservice.mapper.QuestionMapper;
import com.examsservice.mapper.QuestionOptionMapper;
import com.examsservice.mapper.UserMapper;
import com.examsservice.service.QuestionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Questions> implements QuestionService {

    private final QuestionMapper questionMapper;//题目
    private final QuestionOptionMapper questionOptionMapper;//题目选项
    private final PaperQuestionsMapper paperQuestionsMapper;//试卷
    private final UserMapper userMapper;

    /**
     * 新增菜品
     * @param questionDTO
     */
    @Override
    @Transactional//开启事务管理，要么新增成功，否则失败，保证数据一致性
    public void saveWithOption(QuestionDTO questionDTO) {
        //1，向题目表插入一条数据
        //1.1.获取当前登录用户id（从线程拿取）
        Long currentId = BaseContext.getCurrentId();
        //1.2.构造插入数据
        Questions question= Questions.builder()
                .title(questionDTO.getTitle())//题目标题
                .tags(questionDTO.getTags())//题目标签
                .categoryId(questionDTO.getCategoryId())//题目所属分类id
                .questionType(questionDTO.getQuestionType())//题型
                .createTime(LocalDateTime.now())//当前时间
                .createUserId(currentId)//创建者id
                .updateUserId(currentId)//修改者id
                .build();
        //1.3.执行插入
        questionMapper.insert(question);

        //2.1.判断用户传递的题目选项是否为空
        //2.2.根据新增题目表返回的id，插入题目选项
//        List<QuestionOption> questionOptions=questionDTO.getQuestionOptions();
        List<QuestionOptions> options = questionDTO.getQuestionOptions();
        if(options!=null&& !options.isEmpty()){
            options.forEach(option -> {
                log.info("选项内容:{}",option.getOptionContent());
                // 设置分类id
                option.setQuestionId(question.getId());
            });
            //向题目选项表插入多条数据
            questionOptionMapper.insertBatch(options);
        }

    }

    /**
     * 题目分页查询
     * @param questionPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(QuestionPageQueryDTO questionPageQueryDTO) {
        //1.基于PageHelper插件实现动态分页查询
        //1.1 构造分页查询参数（页码，每页记录数）
        PageHelper.startPage(questionPageQueryDTO.getPage(), questionPageQueryDTO.getPageSize());
        Page<QuestionVO> page=questionMapper.pageQuery(questionPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id查询题目详情
     * @param id
     * @return
     */
    @Override
    public QuestionVO getByIdWith(long id) {
        //1.1.获取题目详情
//        Questions questions = questionMapper.getById(id);    // mabitis自动映射错误字段，转为拷贝属性来解决
        Questions hhh = questionMapper.selectById(id);
        System.out.println(hhh);
        //1.2.拷贝属性VO
        //1.2.1 获取对象
        QuestionVO questionVO=new QuestionVO();
//        //1.2.2 拷贝属性
        BeanUtils.copyProperties(hhh,questionVO);
        //再获取创建题目修改题目的用户
        //1.3.1创建用户信息
        User userCreate=userMapper.selectById(hhh.getCreateUserId());
        User userUpdate=userMapper.selectById(hhh.getUpdateUserId());
        questionVO.setCreateUser(userCreate.getName());
        questionVO.setUpdateUser(userUpdate.getName());



        //2.获取题目选项详情
        List<QuestionOptions> questionOptions=questionOptionMapper.getByIdList(id);
        questionVO.setQuestionOptions(questionOptions);
        return questionVO;
    }


    /**
     * 批量删除题目
     * @param ids
     */
    @Override
    @Transactional //开启事务管理，保证数据一致性
    public void deleteBatch(List<Long> ids) {

        //1.判断题目是否关联试卷
        //1.1.构造查询条件----ids集合id==question_id
        QueryWrapper<PaperQuestions> queryWrapperPaper=new QueryWrapper<>();
        queryWrapperPaper.in("question_id", ids);
        List<PaperQuestions> relatedPaperQuestions = paperQuestionsMapper.selectList(queryWrapperPaper);
//        List<PaperQuestions> paperQuestions = paperQuestionsMapper.selectBatchIds(ids);
        if(relatedPaperQuestions!=null&& !relatedPaperQuestions.isEmpty()){
            throw new AccountNotFoundException(MessageConstant.QUESTION_BE_RELATED_BY_TEST_PAPERE);
        }
        //2.执行删除

        //2.1.删除题目选项
        //2.1.1.构造删除条件----ids集合id==question_id
        QueryWrapper<QuestionOptions> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("question_id", ids);
        //2.2.删除题目选项
        questionOptionMapper.delete(queryWrapper);

        //2.2.删除题目
        questionMapper.deleteBatchIds(ids);
    }


    /**
     * 修改题目
     * @param questionDTO
     */
    @Override
    @Transactional
    public void updateBatch(QuestionDTO questionDTO) {
        //使用 Lambda 表达式构建更新操作
        //1.修改题目本体
        lambdaUpdate()
                .set(questionDTO.getTitle()!=null,Questions::getTitle,questionDTO.getTitle())
                .set(questionDTO.getTags()!=null,Questions::getTags,questionDTO.getTags())
                .set(questionDTO.getCategoryId()!=null,Questions::getCategoryId,questionDTO.getCategoryId())
                .set(questionDTO.getQuestionType()!=null,Questions::getQuestionType,questionDTO.getQuestionType())
                .set(Questions::getUpdateTime,LocalDateTime.now())
                .eq(Questions::getId,questionDTO.getId()).update();


        //2.修改题目选项
        List<QuestionOptions> list = questionDTO.getQuestionOptions();
        //2.1.判断题目选项是否为空
        if(list!=null&& !list.isEmpty()){
            //2.2.删除原有选项
            //2.2.1.构造删除条件
            QueryWrapper<QuestionOptions> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("question_id", questionDTO.getId());
            questionOptionMapper.delete(queryWrapper);
            //2.3.新增选项
            list.forEach(list1->{
                //设置选项对应题目id
                list1.setQuestionId(questionDTO.getId());
            });
            //2.4.插入数据
            questionOptionMapper.insertBatch(list);

        }
    }

    @Override
    public List<questionTypeVo> getBatch() {
        List<questionTypeVo> voList=questionMapper.getBatch();
        log.info("顶顶顶顶"+voList);
        return questionMapper.getBatch();
    }


}
