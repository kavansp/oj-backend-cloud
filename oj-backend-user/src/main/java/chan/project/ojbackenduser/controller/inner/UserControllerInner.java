package chan.project.ojbackenduser.controller.inner;

import chan.project.ojbackendcommon.common.ErrorCode;
import chan.project.ojbackendcommon.exception.BusinessException;
import chan.project.ojbackendcommon.exception.ThrowUtils;
import chan.project.ojbackendmodel.model.dto.user.UserLoginRequest;
import chan.project.ojbackendmodel.model.entity.User;
import chan.project.ojbackendmodel.model.vo.UserVO;
import chan.project.ojbackendservice.service.UserFeignClient;
import chan.project.ojbackenduser.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static chan.project.ojbackendcommon.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/inner")
public class UserControllerInner implements UserFeignClient {

    @Resource
    private UserService userService;


    @Override
    @GetMapping("/getLoginUser")
    public User getLoginUser(HttpServletRequest request) {
        User loginUser = UserFeignClient.super.getLoginUser(request);
        User user = userService.getById(loginUser);
        ThrowUtils.throwIf(user == null,ErrorCode.NOT_LOGIN_ERROR,"登录信息异常");
        return user;
    }

    @Override
    @GetMapping("/getById")
    public User getById(@RequestParam("userId") Long userId) {
        return userService.getById(userId);
    }

    @Override
    @GetMapping("/getIds")
    public List<User> listByIds(@RequestParam("idList") Collection<Long> idList) {
        return userService.listByIds(idList);
    }
}
