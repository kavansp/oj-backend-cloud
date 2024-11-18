package chan.project.ojbackendmodel.model.codesandbox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 返回封装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CodeResponse {
    private List<String> Output;
    /**
     * 代码执行结果类
     */
    private JudgeInfo judgeInfo;
    /**
     * 执行状态
     */
    private Integer status;
    /**
     * 执行信息
     */
    private String message;
}
