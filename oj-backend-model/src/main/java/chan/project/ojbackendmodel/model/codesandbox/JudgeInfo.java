package chan.project.ojbackendmodel.model.codesandbox;

import lombok.Data;

/**
 * 代码执行结果信息
 */
@Data
public class JudgeInfo {
    /**
     * 执行信息
     */
    private String message;
    /**
     * 执行时间(就个人见解来说执行时间不应该是一个字段，而是一个数组，执行的时候会有很多个输入用例，记录的应该是每个输入用例的执行时间)
     */
    private Long ExeTime;
    /**
     * 消耗内存
     */
    private Long ExeMemory;
}
