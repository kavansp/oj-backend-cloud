package chan.project.ojbackendmodel.model.codesandbox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CodeRequest {
    public List<String> inputList;
    private String code;
    private String language;
}
