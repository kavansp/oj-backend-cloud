package chan.project.ojbackendjudge.Judge.codesandbox;


import chan.project.ojbackendjudge.Judge.codesandbox.impl.ExampleCodeSandBox;
import chan.project.ojbackendjudge.Judge.codesandbox.impl.RemoteCodeSandbox;

/**
 * 代码工厂类
 * @author <a href="https://github.com/kavansp">kavansp</a>
 */
public class CodeSandBoxFactory {

    public CodeSandBox getCodeSandBox(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandBox();
            case "remote":
                return new RemoteCodeSandbox();
            default:
                return null;
        }
    }
}
