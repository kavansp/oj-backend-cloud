package chan.project.ojbackendjudge.Judge.codesandbox;


import chan.project.ojbackendmodel.model.codesandbox.CodeRequest;
import chan.project.ojbackendmodel.model.codesandbox.CodeResponse;

public interface CodeSandBox {
    CodeResponse executeCode(CodeRequest codeRequest);
}
