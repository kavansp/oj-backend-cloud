package chan.project.ojbackendjudge.Judge.codesandbox.impl;


import chan.project.ojbackendjudge.Judge.codesandbox.CodeSandBox;
import chan.project.ojbackendmodel.model.codesandbox.CodeRequest;
import chan.project.ojbackendmodel.model.codesandbox.CodeResponse;

import java.util.List;

public class RemoteCodeSandbox implements CodeSandBox {
    @Override
    public CodeResponse executeCode(CodeRequest codeRequest){
        List<String> inputList = codeRequest.getInputList();
        String code = codeRequest.getCode();
        String language = codeRequest.getLanguage();
        System.out.println("输入参数"+inputList);
        System.out.println("代码"+code);
        System.out.println("语言"+language);
        return new CodeResponse();
    }
}
