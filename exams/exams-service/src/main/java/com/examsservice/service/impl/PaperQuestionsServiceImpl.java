package com.examsservice.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.entity.PaperQuestions;
import com.examsservice.mapper.PaperQuestionsMapper;
import com.examsservice.service.IPaperQuestionsService;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 何时去
 * @since 2024-09-18
 */
@Service
public class PaperQuestionsServiceImpl extends ServiceImpl<PaperQuestionsMapper, PaperQuestions> implements IPaperQuestionsService {

}
