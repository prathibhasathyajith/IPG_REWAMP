/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.interceptor;

import com.epic.ipg.util.common.SessionBean;
import com.epic.ipg.util.varlist.CommonVarList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author chanuka
 */
public class CheckAccessInteceptor implements HandlerInterceptor {

    @Autowired
    private SessionBean sessionBean;

    /**
     * @return the sessionBean
     */
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        boolean status;

        if (handler instanceof HandlerMethod) {

            HandlerMethod method = (HandlerMethod) handler;
            String methodName = method.getMethod().getName();

            Logger.getLogger(CheckAccessInteceptor.class.getName()).log(Level.INFO, "Called CheckAccessInteceptor :{0}", methodName);

            if (sessionBean.getSystemUser() != null) {

                ServletContext sc = request.getServletContext();
                HashMap<String, String> userMap = (HashMap<String, String>) sc.getAttribute(CommonVarList.USERMAP);
                String sessionId = userMap.get(sessionBean.getSystemUser());

                if (sessionId.equals(request.getSession(false).getId())) {
                    status = true;

                } else {//multi access
                    RequestDispatcher rd = request.getRequestDispatcher("LogoutUserLogin/ERROR_ACCESSPOINT");
                    rd.forward(request, response);
                    Logger.getLogger(CheckAccessInteceptor.class.getName()).log(Level.INFO, "multi access denied :");

                    status = false;
                }
            } else {//session expire

                RequestDispatcher rd = request.getRequestDispatcher("LogoutUserLogin/ERROR_USER_INFO");
                rd.forward(request, response);
                Logger.getLogger(CheckAccessInteceptor.class.getName()).log(Level.INFO, "session expire :");

                status = false;
            }
        } else {
            status = true;
        }
        return status;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {

        Logger.getLogger(CheckAccessInteceptor.class.getName()).log(Level.INFO, "called post handler :");
        Logger.getLogger(CheckAccessInteceptor.class.getName()).log(Level.INFO, "Request URL::{0}", request.getRequestURL());

    }

    @Override
    public void afterCompletion(HttpServletRequest hsr, HttpServletResponse hsr1, Object o, Exception excptn) throws Exception {
        Logger.getLogger(CheckAccessInteceptor.class.getName()).log(Level.INFO, "called afterCompletion :");

    }

}
