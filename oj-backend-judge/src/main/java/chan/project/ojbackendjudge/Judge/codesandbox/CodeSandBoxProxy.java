package chan.project.ojbackendjudge.Judge.codesandbox;

import chan.project.ojbackendmodel.model.codesandbox.CodeRequest;
import chan.project.ojbackendmodel.model.codesandbox.CodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码增强类，能扩展类的一些能力，类似于aop
 */
@Slf4j
public class CodeSandBoxProxy implements CodeSandBox {

    private CodeSandBox codeSandBox;

    public CodeSandBoxProxy(CodeSandBox codeSandBox) {
        this.codeSandBox = codeSandBox;
    }

    @Override
    public CodeResponse executeCode(CodeRequest codeRequest) {
        log.info("执行代码，参数：{}", codeRequest.toString());
        CodeResponse codeResponse = codeSandBox.executeCode(codeRequest);
        log.info("执行代码，结果：{}", codeResponse.toString());
        return codeResponse;
    }
}
