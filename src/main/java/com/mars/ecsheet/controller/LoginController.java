package com.mars.ecsheet.controller;

import com.mars.ecsheet.dao.UserDAO;
import com.mars.ecsheet.entity.UserEntity;
import com.mars.ecsheet.repository.WorkBookRepository;
import com.mars.ecsheet.repository.WorkSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Mars
 * @date 2020/10/28
 * @description
 */
@RestController
public class LoginController {
    @Autowired
    private WorkBookRepository workBookRepository;

    @Autowired
    private WorkSheetRepository workSheetRepository;
    @GetMapping("login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }
    @PostMapping("login/check")
    public void loginimpl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserDAO ud = new UserDAO();
        UserEntity u = new UserEntity();
        u = ud.getOneUser(username);
        String PASSWORD = u.getPwd();
        if(Objects.equals(PASSWORD, password)) {
            int uid = u.getUid();
            String UID = Integer.toString(uid);
            response.sendRedirect("/index?username=" + username + "&uid=" + UID);
        }
        else
        {
            response.sendRedirect("/login");
        }
    }

}
