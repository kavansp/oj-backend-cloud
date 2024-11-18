package chan.project.ojbackendservice.service;

import chan.project.ojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "oj-backend-judge",path = "api/judge/inner")
public interface JudgeFeignClient {
    @GetMapping("/doCode")
    QuestionSubmit doCode(@RequestParam("questionSubmitId") Long questionSubmitId);

}
