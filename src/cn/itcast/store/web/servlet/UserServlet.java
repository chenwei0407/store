package cn.itcast.store.web.servlet;

import cn.itcast.store.domain.User;
import cn.itcast.store.service.UserService;
import cn.itcast.store.service.impl.UserServiceImpl;
import cn.itcast.store.utils.CookieUtils;
import cn.itcast.store.utils.MyBeanUtils;
import cn.itcast.store.utils.UUIDUtils;
import cn.itcast.store.web.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class UserServlet extends BaseServlet {

    public void findAll(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("FindAll");
    }

    //registerUI
    public String registerUI(HttpServletRequest request, HttpServletResponse response) {
        return "/jsp/register.jsp";
    }

    public String userExists(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //接收用户名
        String username = request.getParameter("username");

        UserService userService = new UserServiceImpl();

        User user = null;
//        user = userService.findUserByUserName(username);

        if (null != user) {
            response.getWriter().print("11");
        } else {
            response.getWriter().print("00");
        }
        return null;
    }

    //userRegister
    public String register(HttpServletRequest request, HttpServletResponse response)
            throws SQLException{

        //获得表单提交数据并封装
        User user = MyBeanUtils.populate(User.class, request.getParameterMap());

        //1.1 处理后服务器自动生成数据
        user.setUid(UUIDUtils.getId());
        //用户激活码
        user.setCode(UUIDUtils.getUUID64());
        //0 未激活 1 激活
        user.setState(0);

        //2 处理
        UserServiceImpl userService = new UserServiceImpl();
        userService.registerUser(user);

        //3 向客户端提示：用户注册成功，请激活，并转发到提示页面
        request.setAttribute("msg", "注册成功，请邮件激活后登陆");

        //注册成功登陆
        return "/jsp/login.jsp";
    }

    public String active(HttpServletRequest request, HttpServletResponse response) {

        try {
            //1 获得激活码
            String code = request.getParameter("code");

            //2 用户激活
            UserService userService =  new UserServiceImpl();
            userService.activeUser(code);

            //3 成功提示
            request.setAttribute("msg", "激活成功，请登陆");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", e.getMessage());
        }

        return "/jsp/login.jsp";
    }

    public String loginUI(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtils.getCookieByName("remUser", request.getCookies());

        if (null != cookie) {
            request.setAttribute("remUser", cookie.getValue());
        }

        return "jsp/login.jsp";
    }

    public String login(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        //1 封装数据
        User user = MyBeanUtils.populate(User.class, request.getParameterMap());

        //# 2 记住用户名 Start
        String rememberMe = request.getParameter("rememberme");
        if ("1".equals(rememberMe)) {
            Cookie rememberMeCookie = new Cookie("remembermeCookie", user.getUsername());
            rememberMeCookie.setPath("/");
            rememberMeCookie.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(rememberMeCookie);
        } else {
            Cookie rememberMeCookie = new Cookie("remembermeCookie", "");
            rememberMeCookie.setPath("/");
            rememberMeCookie.setMaxAge(0);
            response.addCookie(rememberMeCookie);
        }

        //2 通知service进行登陆
        UserService userService = new UserServiceImpl();
        try {
            User loginUser = userService.loginUser(user);

            //3 处理成功
            if (loginUser != null) {
                //#1 自动登陆
                String autoLogin = request.getParameter("autoLogin");

                if ("1".equals(autoLogin)) {
                    //如果勾选则发送Cookie
                    Cookie autoLoginCookie = new Cookie("autoLoginCookie", user.getUsername() + "@" + user.getPassword());
                    autoLoginCookie.setPath("/");
                    autoLoginCookie.setMaxAge(60 * 60 * 24 * 7);
                    response.addCookie(autoLoginCookie);
                } else {
                    //删除Cookie
                    Cookie autoLoginCookie = new Cookie("autoLoginCookie", "");
                    autoLoginCookie.setPath("/");
                    autoLoginCookie.setMaxAge(0);
                    response.addCookie(autoLoginCookie);
                }
                //#自动登陆END

                //3.1 session 作用于记录登陆状态
                request.getSession().setAttribute("loginUser", loginUser);
                //3.2 重定向到首页
                response.sendRedirect(request.getContextPath() + "/");
                //3.3 不适用baseServlet的请求转发
                return null;
            }

            //4 处理错误
            //4.1 request 作用域记录错误信息
            request.setAttribute("msg", "用户名活密码不匹配或为未激活");
            return "/jsp/login.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "/jsp/login.jsp";
    }


    public String logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        //1 将session用户状态信息移除
        request.getSession().removeAttribute("loginUser");

        //2 重定向到首页
        response.sendRedirect(request.getContextPath() + "/UserServlet?method=registUI");

        //3 不适用baseServlet请求转发
        return null;
    }
}
