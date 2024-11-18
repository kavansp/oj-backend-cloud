package chan.project.ojbackendjudge.Judge;


import chan.project.ojbackendmodel.model.entity.QuestionSubmit;

/**
 * 调用代码沙箱的接口类
 */
public interface JudgeService {
    QuestionSubmit doCode(long questionSubmitId);
}
