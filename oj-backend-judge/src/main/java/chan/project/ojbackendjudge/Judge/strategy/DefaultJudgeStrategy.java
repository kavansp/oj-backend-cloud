package chan.project.ojbackendjudge.Judge.strategy;

import chan.project.ojbackendmodel.model.codesandbox.JudgeContext;
import chan.project.ojbackendmodel.model.codesandbox.JudgeInfo;
import chan.project.ojbackendmodel.model.dto.question.JudgeCase;
import chan.project.ojbackendmodel.model.dto.question.JudgeConfig;
import chan.project.ojbackendmodel.model.entity.Question;
import chan.project.ojbackendmodel.model.entity.QuestionSubmit;
import chan.project.ojbackendmodel.model.enums.JudgeInfoMessageEnum;
import cn.hutool.json.JSONUtil;

import java.util.List;

public class DefaultJudgeStrategy implements JudgeStrategy{
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext){
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();

        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.SUCCESS;
        //输入与输出答案数量不一致
        if(inputList.size() != outputList.size()){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfo.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfo;
        }
        //遍历判断答案是否匹配
        for (int i = 0; i < outputList.size(); i++) {
            if(!judgeCaseList.get(i).equals(inputList.get(i))){
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfo.setMessage(judgeInfoMessageEnum.getText());
                return judgeInfo;
            }
        }
        //判断题目是否符合限制
        Long exeMemory = judgeInfo.getExeMemory();
        Long exeTime = judgeInfo.getExeTime();
        JudgeConfig judgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);
        //内存超限
        if(exeMemory > judgeConfig.getMemoryLimit()){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfo.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfo;
        }
        //时间超限
        if(exeTime > judgeConfig.getTimeLimit()){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfo.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfo;
        }
        judgeInfo.setMessage(judgeInfoMessageEnum.getText());
        return judgeInfo;
    }
}
