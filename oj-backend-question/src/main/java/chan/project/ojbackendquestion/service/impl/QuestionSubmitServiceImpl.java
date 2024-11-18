package chan.project.ojbackendquestion.service.impl;

import chan.project.ojbackendcommon.common.ErrorCode;
import chan.project.ojbackendcommon.constant.CommonConstant;
import chan.project.ojbackendcommon.exception.ThrowUtils;
import chan.project.ojbackendcommon.utils.SqlUtils;
import chan.project.ojbackendmodel.model.dto.questionSubmit.QuestionSubmitAddRequest;
import chan.project.ojbackendmodel.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import chan.project.ojbackendmodel.model.entity.QuestionSubmit;
import chan.project.ojbackendmodel.model.entity.User;
import chan.project.ojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import chan.project.ojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import chan.project.ojbackendmodel.model.vo.QuestionSubmitVO;
import chan.project.ojbackendmodel.model.vo.UserVO;
import chan.project.ojbackendquestion.mapper.QuestionSubmitMapper;
import chan.project.ojbackendquestion.rabbitmq.MyMessageProducer;
import chan.project.ojbackendquestion.service.QuestionSubmitService;
import chan.project.ojbackendservice.service.JudgeFeignClient;
import chan.project.ojbackendservice.service.UserFeignClient;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 题目提交服务实现
 *
 * @author <a href="https://github.com/kavansp">kavansp</a>
 *
 */
@Service
@Slf4j
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit> implements QuestionSubmitService {

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    @Lazy
    private JudgeFeignClient judgeFeignClient;

    @Resource
    private MyMessageProducer myMessageProducer;
    /**
     * 校验数据
     *
     * @param questionSubmit
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validQuestionSubmit(QuestionSubmit questionSubmit, boolean add) {
        ThrowUtils.throwIf(questionSubmit == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        Long id = questionSubmit.getId();
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        String judgeInfo = questionSubmit.getJudgeInfo();
        Integer status = questionSubmit.getStatus();
        Long questionId = questionSubmit.getQuestionId();
        //判断编程语言是否正确
        QuestionSubmitLanguageEnum enumByValue = QuestionSubmitLanguageEnum.getEnumByValue(language);
        ThrowUtils.throwIf(ObjectUtil.isEmpty(enumByValue), ErrorCode.PARAMS_ERROR,"请选择正确的编程语言");
        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(code), ErrorCode.PARAMS_ERROR,"代码为空");
            ThrowUtils.throwIf(ObjectUtils.isEmpty(questionId), ErrorCode.PARAMS_ERROR,"题目不存在");
        }
    }

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = questionSubmitQueryRequest.getId();
        Long notId = questionSubmitQueryRequest.getNotId();
        String title = questionSubmitQueryRequest.getTitle();
        String searchText = questionSubmitQueryRequest.getSearchText();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();
        Long userId = questionSubmitQueryRequest.getUserId();
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();

        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(language), "language", language);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取题目提交封装
     *
     * @param questionSubmit
     * @param request
     * @return
     */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, HttpServletRequest request) {
        // 对象转封装类
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 1. 关联查询用户信息
        Long userId = questionSubmit.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userFeignClient.getById(userId);
        }
        UserVO userVO = userFeignClient.getUserVO(user);
        questionSubmitVO.setUser(userVO);
        return questionSubmitVO;
    }

    /**
     * 分页获取题目提交封装
     *
     * @param questionSubmitPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, HttpServletRequest request) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit -> {
            return QuestionSubmitVO.objToVo(questionSubmit);
        }).collect(Collectors.toList());

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionSubmitList.stream().map(QuestionSubmit::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        questionSubmitVOList.forEach(questionSubmitVO -> {
            Long userId = questionSubmitVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionSubmitVO.setUser(userFeignClient.getUserVO(user));
        });
        // endregion

        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

    @Override
    public long doQuestion(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        ThrowUtils.throwIf(questionSubmitAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 将实体类和 DTO 进行转换
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitAddRequest, questionSubmit);
        // 数据校验
        this.validQuestionSubmit(questionSubmit, true);
        // 填充默认值
        questionSubmit.setUserId(loginUser.getId());
        questionSubmit.setJudgeInfo("{}");
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        //写入数据库
        this.save(questionSubmit);
        // 执行代码沙箱
        myMessageProducer.sendMessage("code_exchange","my_routingKey", String.valueOf(questionSubmit.getId()));
//        CompletableFuture.runAsync(()->{
//            judgeFeignClient.doCode(questionSubmit.getId());
//        });
        // 返回新写入的数据 id
        long newQuestionSubmitId = questionSubmit.getId();
        return newQuestionSubmitId;
    }

}
