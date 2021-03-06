package my.work.stock.system.web.controller;

import my.work.stock.system.domain.entity.UserInfo;
import my.work.stock.system.domain.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class RestPassword {
    private static final Logger logger = LoggerFactory.getLogger(RestPassword.class);

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/system/resetpassword", method = RequestMethod.GET)
    public ModelAndView get() {
        Map<String, String> model = new HashMap<>();
        return new ModelAndView("system/resetpassword", model);
    }

    @RequestMapping(value = "/system/resetpassword", method = RequestMethod.POST)
    public ModelAndView post(@RequestParam String password, @RequestParam String newPassword, @RequestParam String rePassword) {
        Subject subject = SecurityUtils.getSubject();
        UserInfo temp = (UserInfo) subject.getPrincipal();
        logger.info("userName:{}", temp.getUserName());
        logger.info("password:{}", temp.getPassword());
        UserInfo userInfo = userInfoService.findByUserName(temp.getUserName());
        Map<String, String> model = new HashMap<>();
        if (StringUtils.equals(userInfo.getPassword(), password)) {
            if (StringUtils.equals(newPassword, rePassword)) {
                userInfo.setPassword(newPassword);
                userInfoService.save(userInfo);
            } else {
                model.put("wrongPassword", "两次密码输入不一致!");
            }
        } else {
            model.put("wrongPassword", "旧密码不正确!");
        }
        return new ModelAndView("system/resetpassword", model);
    }
}
