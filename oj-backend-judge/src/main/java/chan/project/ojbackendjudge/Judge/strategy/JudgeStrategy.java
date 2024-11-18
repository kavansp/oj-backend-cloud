package chan.project.ojbackendjudge.Judge.strategy;


import chan.project.ojbackendmodel.model.codesandbox.JudgeContext;
import chan.project.ojbackendmodel.model.codesandbox.JudgeInfo;

public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
