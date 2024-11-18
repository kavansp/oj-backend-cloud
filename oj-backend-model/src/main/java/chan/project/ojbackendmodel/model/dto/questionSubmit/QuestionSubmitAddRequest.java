package chan.project.ojbackendmodel.model.dto.questionSubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建题目提交请求
 *
 * @author <a href="https://github.com/kavansp">kavansp</a>
 * 
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题信息（json 对象）
     */
    private String judgeInfo;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}