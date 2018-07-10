package cn.itcast.store.web.base;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class BaseServlet extends javax.servlet.http.HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) {

        try {
            //取得请求参数method
            String methodName = req.getParameter("method");

            //*默认方法名
            if (methodName == null) {
                methodName = "execute";
            }

            //获取当前运行类，需要指定的方法
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

            //执行方法
            String jspPath = (String) method.invoke(this, req, resp);

            //如果有子类的返回值，将请求到指定的jsp页面
            if (jspPath != null) {
                req.getRequestDispatcher(jspPath).forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 默认方法，用于子类的复写
     *
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        //NO Operation
        return null;
    }
}
