package com.examsservice.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.constant.MessageConstant;
import com.common.context.BaseContext;
import com.common.exception.AccountNotFoundException;
import com.common.result.PageResult;
import com.exam.dto.*;
import com.exam.entity.ExaminationPapers;
import com.exam.entity.PaperQuestions;
import com.exam.entity.QuestionOptions;
import com.exam.vo.ExamintionPapersListVo;
import com.exam.vo.ExamintionPapersPageQueryVO;
import com.exam.vo.QuestionTypeCountVO;
import com.exam.vo.QuestionVO;
import com.examsservice.mapper.ExaminationPapersMapper;
import com.examsservice.mapper.PaperQuestionsMapper;
import com.examsservice.mapper.QuestionMapper;
import com.examsservice.mapper.QuestionOptionMapper;
import com.examsservice.service.IExaminationPapersService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 何时去
 * @since 2024-09-21
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExaminationPapersServiceImpl extends ServiceImpl<ExaminationPapersMapper, ExaminationPapers> implements IExaminationPapersService {

    private final ExaminationPapersMapper papersMapper;//试卷
    private final PaperQuestionsMapper paperQuestionsMapper;//试卷与题目关联
    private final QuestionMapper QuestionMapper;//题目
    private final QuestionOptionMapper questionOptionMapper;//题目选项

    /**
     * 新增试卷
     * @param papersDTO
     */
    @Override
    public Integer insert(ExaminationPapersDTO papersDTO) {
        //获取当前登录用户id
        Long userId = BaseContext.getCurrentId();

        ExaminationPapers papers=ExaminationPapers.builder()
                .paperId(papersDTO.getPaperId())
                .paperPictures(papersDTO.getPaperPictures())
                .paperTitle(papersDTO.getPaperTitle())
                .categoryId(papersDTO.getCategoryId())
                .description(papersDTO.getDescription())
                .duration(papersDTO.getDuration())
                .totalScore(papersDTO.getTotalScore())
                .startTime(papersDTO.getStartTime())
                .endTime(papersDTO.getEndTime())
                .createdAt(LocalDateTime.now())
                .createUserId(userId)
                .build();
        papersMapper.insert(papers);
        return papers.getPaperId();

    }


    /**
     * 试卷新增试题
     */
    @Override
    @Transactional
    public void insertQuestion(AddTestPaperQuestionDTO questionDTO) {

        //判定集合是否为空
        if (questionDTO==null|| questionDTO.getIds().isEmpty()){
            throw new AccountNotFoundException(MessageConstant.QUESTION_Found);
        }

        Long paperId = questionDTO.getPaperId();
        List<Long> questionIds = questionDTO.getIds();
        // 2. 查询已存在的试题ID列表
        List<Long> existingIds = papersMapper.selectExistingQuestionIds(paperId, questionIds);
        // 3. 判断是否存在重复试题
        if (!existingIds.isEmpty()) {
            throw new AccountNotFoundException(MessageConstant.QUESTION_EXIST+"试卷中已存在以下试题：" + existingIds);
        }
        // 4.批量插入
        papersMapper.batchInsert(questionDTO);
    }


    /**
     * 根据试卷id查询题型数量
     */
    @Override
    public List<QuestionTypeCountVO> countQuestionTypesByPaperId(Integer id) {
        List<QuestionTypeCountVO> list=papersMapper.countQuestionTypesByPaperId(id);
        return list;
    }


    @Override
    @Transactional
    public void updateInfo(ExaminationPapersDTO papersDTO) {
        lambdaUpdate()
                .set(papersDTO.getPaperPictures() != null && !papersDTO.getPaperPictures().isEmpty(), ExaminationPapers::getPaperPictures, papersDTO.getPaperPictures())
                .set(papersDTO.getDescription() != null && !papersDTO.getDescription().isEmpty(), ExaminationPapers::getDescription, papersDTO.getDescription())
                .set(papersDTO.getCategoryId() != null && papersDTO.getCategoryId() > 0, ExaminationPapers::getCategoryId, papersDTO.getCategoryId())
                .set(papersDTO.getDuration() != null && papersDTO.getDuration() > 0, ExaminationPapers::getDuration, papersDTO.getDuration())
                .set(papersDTO.getStatus() != null, ExaminationPapers::getStatus, papersDTO.getStatus())
                .set(papersDTO.getStartTime() != null, ExaminationPapers::getStartTime, papersDTO.getStartTime())
                .set(papersDTO.getEndTime() != null, ExaminationPapers::getEndTime, papersDTO.getEndTime())
                .set(papersDTO.getPaperTitle() != null && !papersDTO.getPaperTitle().isEmpty(), ExaminationPapers::getPaperTitle, papersDTO.getPaperTitle())
                .set(papersDTO.getTotalScore() != null, ExaminationPapers::getTotalScore, papersDTO.getTotalScore())
                .set(ExaminationPapers::getUpdatedAt, LocalDateTime.now())
                .set(ExaminationPapers::getUpdateUserId, BaseContext.getCurrentId())
                .eq(ExaminationPapers::getPaperId, papersDTO.getPaperId())
                .update();
    }

    /**
     * 设置题型分数
     * @param typeScore
     */
    @Override
    public void updateTypeScore(QuestionTypeScore typeScore) {

        List<QuestionTypeCountVO> voList = typeScore.getQuestionTypeCountVOList();
        voList.forEach(list->{
            log.info("分数：{}",list);
            double average= (double) list.getTypeScore() /list.getCount();
            log.info("分数2：{}",average);
            papersMapper.updateTypeScore(typeScore.getPaperId(),list.getQuestionType(),average);
        });

    }

    /**
     * 试卷分页查询
     * @param dto
     * @return
     */
    @Override
    public PageResult pageQuery(ExamintionPapersPageQueryDTO dto) {

        //1.基于PageHelper插件实现动态分页查询
        //1.1 构造分页查询参数（页码，每页记录数）
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<ExamintionPapersPageQueryVO> page=papersMapper.pageQuery(dto);
        return new PageResult(page.getTotal(), page.getResult());

    }

    @Override
    public void delecte(int id) {
        //1.根据id查询试卷状态，是否发布中
        ExaminationPapers papers = papersMapper.selectById(id);
        if(papers.getStatus()==1){
            throw new AccountNotFoundException(MessageConstant.PAPER_RELEASEIN_PROGRESS);
        }
        //2。未发布可删除
        papersMapper.deleteById(id);
    }

    /**
     * 根据Id查询试卷详情
     * @param id
     * @return
     */
    @Override
    public ExamintionPapersListVo getPaperById(int id) {
        // 1. 查询试卷基本信息
        ExaminationPapers papers = papersMapper.selectById(id);
        if (papers == null) {
            throw new RuntimeException("试卷不存在，ID: " + id);
        }

        // 2. 构建响应对象
        ExamintionPapersListVo examintionPapersListVo = new ExamintionPapersListVo();
        examintionPapersListVo.setPaper(papers);

        // 3. 查询试卷题目关联关系
        List<PaperQuestions> paperQuestionsList = paperQuestionsMapper.selectList(
                new LambdaQueryWrapper<PaperQuestions>().eq(PaperQuestions::getPaperId, id)
        );

        // 4. 如果试卷没有题目，直接返回
        if (paperQuestionsList.isEmpty()) {
            examintionPapersListVo.setQuestionVOList(Collections.emptyList());
            return examintionPapersListVo;
        }

        // 5. 构建题目列表
        List<QuestionVO> questionVOList = new ArrayList<>();

        for (PaperQuestions paperQuestion : paperQuestionsList) {
            Integer questionId = paperQuestion.getQuestionId();

            try {
                // 5.1 查询题目基本信息
                QuestionVO question = QuestionMapper.getById(questionId);
                if (question == null) {
                    continue; // 题目不存在则跳过
                }

                // 5.2 查询题目选项
                List<QuestionOptions> questionOptions = questionOptionMapper.getByIdList(questionId);

                //查询题目设置分数
                double score=paperQuestionsMapper.getById(id,questionId).getTypeScore();

                // 5.3 构建题目VO对象
                QuestionVO questionVO = QuestionVO.builder()
                        .title(question.getTitle())
                        .id(question.getId())
                        .tags(question.getTags())
                        .categoryName(question.getCategoryName())
                        .questionType(question.getQuestionType())
                        .totalAnswerCount(question.getTotalAnswerCount())
                        .wrongAnswerCount(question.getWrongAnswerCount())
                        .createUser(question.getCreateUser())
                        .updateUser(question.getUpdateUser())
                        .createTime(question.getCreateTime())
                        .updateTime(question.getUpdateTime())
                        .score(score)
                        .questionOptions(questionOptions != null ? questionOptions : Collections.emptyList())
                        .build();

                questionVOList.add(questionVO);

            } catch (Exception e) {
                // 记录错误日志，但不中断整个流程
                log.warn("获取题目信息失败，题目ID: {}", questionId, e);
            }
        }

        // 6. 设置题目列表并返回
        examintionPapersListVo.setQuestionVOList(questionVOList);
        return examintionPapersListVo;
    }
}
