package com.epic.ipg.controller.payment;

import com.epic.ipg.bean.payment.MerchantBean;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author chanuka_g
 */
@Controller
public class IndexController {

    private static final String MV_REDIRECT = "redirect:";

    @RequestMapping(path = {"/"}, method = RequestMethod.GET)
    public String sayHello(Model model) {
        return "index";
    }

    @RequestMapping(value = "/returnToMerchant", method = RequestMethod.POST)
    public ModelAndView returnToMerchant(HttpServletRequest request) {

        String isSuccessTxn = request.getParameter("isSuccessTxn");

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        if ("true".equals(isSuccessTxn)) {
            return new ModelAndView(MV_REDIRECT + request.getParameter("successReturnUrl"));
        } else {
            return new ModelAndView(MV_REDIRECT + request.getParameter("errorReturnUrl"));
        }

    }

    @RequestMapping(value = "/processPaymentTimeout", method = RequestMethod.POST)
    public ModelAndView redirectSessionExpire(HttpServletRequest request) {

        Logger.getLogger(IndexController.class.getName()).log(Level.INFO, "called MerchantRequestController : redirectSessionExpire");

        MerchantBean merchantBean = (MerchantBean) request.getSession(false).getAttribute("merchantBean");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new ModelAndView(MV_REDIRECT + merchantBean.getPrimaryUrl());

    }

    @RequestMapping(value = "/LogoutUserLogin/{message}")
    public ModelAndView logoutUserLogin(HttpServletRequest request, @PathVariable Map<String, String> pathVars, Model model) {

        Logger.getLogger(IndexController.class.getName()).log(Level.INFO, "called LoginController : logout with error");

        HttpSession session = request.getSession(false);
        if (session != null) {

            session.invalidate();
        }

        ModelAndView modelAndView;

        modelAndView = new ModelAndView("sessionexpire");

        return modelAndView;

    }
}
