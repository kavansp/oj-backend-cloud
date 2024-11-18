package chan.project.ojbackendquestion.controller.inner;

import chan.project.ojbackendmodel.model.entity.Question;
import chan.project.ojbackendmodel.model.entity.QuestionSubmit;
import chan.project.ojbackendquestion.service.QuestionService;
import chan.project.ojbackendquestion.service.QuestionSubmitService;
import chan.project.ojbackendservice.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
@RestController
@RequestMapping("/inner")
public class QuestionControllerInner implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Override
    @GetMapping("/getById")
    public Question getById(@RequestParam("questionId") Long questionId){
        return questionService.getById(questionId);
    }

    @Override
    @GetMapping("getByQuestionSubmitId")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") Long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("updateQuestionSubmitById")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmitUpdate) {
        return questionSubmitService.updateById(questionSubmitUpdate);
    }
}
