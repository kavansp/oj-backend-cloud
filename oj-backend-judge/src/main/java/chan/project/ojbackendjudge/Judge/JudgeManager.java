package chan.project.ojbackendjudge.Judge;

import chan.project.ojbackendjudge.Judge.strategy.DefaultJudgeStrategy;
import chan.project.ojbackendjudge.Judge.strategy.JavaJudgeStrategy;
import chan.project.ojbackendjudge.Judge.strategy.JudgeStrategy;
import chan.project.ojbackendmodel.model.codesandbox.JudgeContext;
import chan.project.ojbackendmodel.model.codesandbox.JudgeInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JudgeManager {

    private Map<String, JudgeStrategy> judgeStrategyMap = new HashMap<>();

    JudgeManager(){
        judgeStrategyMap.put("java",new JavaJudgeStrategy());
        judgeStrategyMap.put("default",new DefaultJudgeStrategy());
    }

    public void add(String strategy,JudgeStrategy judgeStrategy){
        judgeStrategyMap.put(strategy,judgeStrategy);
    }

    JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeStrategy judgeStrategy = judgeStrategyMap.get("default");
        String language = judgeContext.getQuestionSubmit().getLanguage();
        if(judgeStrategyMap.containsKey(language)){
            judgeStrategy = judgeStrategyMap.get(language);
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
