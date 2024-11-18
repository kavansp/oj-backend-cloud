package chan.project.ojbackendservice.service;

import chan.project.ojbackendcommon.common.ErrorCode;
import chan.project.ojbackendcommon.exception.BusinessException;
import chan.project.ojbackendmodel.model.dto.user.UserQueryRequest;
import chan.project.ojbackendmodel.model.entity.User;
import chan.project.ojbackendmodel.model.enums.UserRoleEnum;
import chan.project.ojbackendmodel.model.vo.LoginUserVO;
import chan.project.ojbackendmodel.model.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static chan.project.ojbackendcommon.constant.UserConstant.USER_LOGIN_STATE;
@FeignClient(name = "oj-backend-user",path = "api/user/inner")
public interface UserFeignClient{

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @GetMapping("/getUserPermitNull")
    default User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    default UserVO getUserVO(User user){
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    //这里的request不能传入到feign的实现类中，会报错
    @GetMapping("/getLoginUser")
    default User getLoginUser(HttpServletRequest request){
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        User user = getById(currentUser.getId());
        if(user == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"登录信息异常");
        }
        return user;
    }

    default boolean isAdmin(User user){
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }
    
    @GetMapping("/getById")
    User getById(@RequestParam("userId") Long userId);

    @GetMapping("/getIds")
    List<User> listByIds(@RequestParam("idList") Collection<Long> idList);

}
