package chan.project.ojbackendmodel.model.codesandbox;

import chan.project.ojbackendmodel.model.dto.question.JudgeCase;
import chan.project.ojbackendmodel.model.entity.Question;
import chan.project.ojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
