package chan.project.ojbackendjudge.Judge.codesandbox.impl;

import chan.project.ojbackendjudge.Judge.codesandbox.CodeSandBox;
import chan.project.ojbackendmodel.model.codesandbox.CodeRequest;
import chan.project.ojbackendmodel.model.codesandbox.CodeResponse;
import chan.project.ojbackendmodel.model.codesandbox.JudgeInfo;
import chan.project.ojbackendmodel.model.enums.JudgeInfoMessageEnum;
import chan.project.ojbackendmodel.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱
 */
public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public CodeResponse executeCode(CodeRequest codeRequest){
        List<String> inputList = codeRequest.getInputList();
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setExeTime(100L);
        judgeInfo.setExeMemory(100L);
        judgeInfo.setMessage(JudgeInfoMessageEnum.SUCCESS.getValue());

        CodeResponse codeResponse = new CodeResponse();
        codeResponse.setJudgeInfo(judgeInfo)
                .setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue())
                .setOutput(inputList)
                .setMessage("执行成功");
        return codeResponse;
    }
}
