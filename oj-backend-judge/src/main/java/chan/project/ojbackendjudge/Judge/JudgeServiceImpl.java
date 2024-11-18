package chan.project.ojbackendjudge.Judge;

import chan.project.ojbackendcommon.common.ErrorCode;
import chan.project.ojbackendcommon.exception.BusinessException;
import chan.project.ojbackendjudge.Judge.codesandbox.CodeSandBox;
import chan.project.ojbackendjudge.Judge.codesandbox.CodeSandBoxFactory;
import chan.project.ojbackendjudge.Judge.codesandbox.CodeSandBoxProxy;
import chan.project.ojbackendmodel.model.codesandbox.CodeRequest;
import chan.project.ojbackendmodel.model.codesandbox.CodeResponse;
import chan.project.ojbackendmodel.model.codesandbox.JudgeContext;
import chan.project.ojbackendmodel.model.codesandbox.JudgeInfo;
import chan.project.ojbackendmodel.model.dto.question.JudgeCase;
import chan.project.ojbackendmodel.model.entity.Question;
import chan.project.ojbackendmodel.model.entity.QuestionSubmit;
import chan.project.ojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import chan.project.ojbackendservice.service.QuestionFeignClient;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {


    @Resource
    private QuestionFeignClient questionFeignClient;

    @Value("${codeSandBox.type}")
    private String type;

    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doCode(long questionSubmitId) {
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if(questionSubmit == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getById(questionId);
        if(question == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        Integer status = questionSubmit.getStatus();
        //判断当前题目的状态是否为已经在判题，保证异步执行的原子性
        if(QuestionSubmitStatusEnum.getEnumByValue(status) != QuestionSubmitStatusEnum.WAITING){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "当前题目正在判题中，请稍后再试");
        }
        //将题目更改状态为正在判题 这里可以更改查询条件，创建一个对象只有id和需要修改的值，这样的执行效率更高
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        //创建代码沙箱
        CodeSandBoxFactory codeSandBoxFactory = new CodeSandBoxFactory();
        CodeSandBox codeSandBox = codeSandBoxFactory.getCodeSandBox(type);
        codeSandBox = new CodeSandBoxProxy(codeSandBox);
        //获得测试输入用例并执行代码沙箱
        List<JudgeCase> judgeCaseList = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        CodeRequest codeRequest = new CodeRequest();
        codeRequest.setCode(questionSubmit.getCode())
                .setLanguage(questionSubmit.getLanguage())
                .setInputList(inputList);
        CodeResponse codeResponse = codeSandBox.executeCode(codeRequest);
        //使用策略模式判断不同类型的代码执行是否通过
        JudgeInfo responseJudgeInfo = codeResponse.getJudgeInfo();
        List<String> outputList = codeResponse.getOutput();

        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(responseJudgeInfo)
                .setQuestion(question)
                .setQuestionSubmit(questionSubmit)
                .setJudgeCaseList(judgeCaseList)
                .setInputList(inputList)
                .setOutputList(outputList);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        //代码修改状态是否成功
        QuestionSubmit updateQuestionSubmit = new QuestionSubmit();
        updateQuestionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        updateQuestionSubmit.setId(questionSubmitId);
        updateQuestionSubmit.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        boolean b = questionFeignClient.updateQuestionSubmitById(updateQuestionSubmit);
        if(!b){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"状态更新失败");
        }
        questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        return questionSubmit;
    }
}
