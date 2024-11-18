package chan.project.ojbackendjudge.controller.inner;

import chan.project.ojbackendjudge.Judge.JudgeManager;
import chan.project.ojbackendjudge.Judge.JudgeService;
import chan.project.ojbackendmodel.model.entity.QuestionSubmit;
import chan.project.ojbackendservice.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class JudgeControllerInner implements JudgeFeignClient {
    @Resource
    private JudgeService judgeService;

    @Override
    @GetMapping("/doCode")
    public QuestionSubmit doCode(@RequestParam("questionSubmitId") Long questionSubmitId) {
        QuestionSubmit questionSubmit = judgeService.doCode(questionSubmitId);
        return questionSubmit;
    }
}
