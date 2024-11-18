package chan.project.ojbackendservice.service;

import chan.project.ojbackendmodel.model.entity.Question;
import chan.project.ojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "oj-backend-question",path = "api/question/inner")
public interface QuestionFeignClient {

    @GetMapping("/getById")
    Question getById(@RequestParam("questionId") Long questionId);


    @GetMapping("getByQuestionSubmitId")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") Long questionSubmitId);

    @PostMapping("updateQuestionSubmitById")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmitUpdate);
}
