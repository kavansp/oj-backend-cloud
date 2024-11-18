package chan.project.ojbackendquestion.service;

import chan.project.ojbackendmodel.model.dto.questionSubmit.QuestionSubmitAddRequest;
import chan.project.ojbackendmodel.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import chan.project.ojbackendmodel.model.entity.QuestionSubmit;
import chan.project.ojbackendmodel.model.entity.User;
import chan.project.ojbackendmodel.model.vo.QuestionSubmitVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;


import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交服务
 *
 * @author <a href="https://github.com/kavansp">kavansp</a>
 * 
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 校验数据
     *
     * @param questionSubmit
     * @param add 对创建的数据进行校验
     */
    void validQuestionSubmit(QuestionSubmit questionSubmit, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);
    
    /**
     * 获取题目提交封装
     *
     * @param questionSubmit
     * @param request
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, HttpServletRequest request);

    /**
     * 分页获取题目提交封装
     *
     * @param questionSubmitPage
     * @param request
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, HttpServletRequest request);

    /**
     *
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestion(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
}
