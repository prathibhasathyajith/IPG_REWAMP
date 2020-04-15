/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.controller.payment;

import com.epic.ipg.bean.payment.CommonBean;
import com.epic.ipg.util.common.Calculations;
import com.epic.ipg.util.common.CustomException;
import java.net.URL;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dilanka_w
 */
@RestController
public class CapchaController {

    @RequestMapping(value = "/captchaRefresh", method = {RequestMethod.POST}, headers = "Accept=application/json")
    public @ResponseBody
    CommonBean refreshCapcha(HttpServletRequest request) {

        CommonBean commonBean = new CommonBean();

        try {

            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

                ImageIO.write(setGraphicToCapchaImage(request), "png", bos);
                byte[] blobAsBytes = bos.toByteArray();
                blobAsBytes = Base64.encodeBase64(blobAsBytes);
                commonBean.setValue(new String(blobAsBytes));
            }

        } catch (CustomException | IOException ex) {
            Logger.getLogger(CapchaController.class.getName()).log(Level.SEVERE, null, ex);

        }

        return commonBean;

    }

    @RequestMapping(value = "/CaptchaServlet", method = RequestMethod.GET)
    public void setCaptcha(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

        Logger.getLogger(CapchaController.class.getName()).log(Level.INFO, "called CapchaController : setCaptcha");
        response.setContentType("text/html;charset=UTF-8");

        try {

            response.setContentType("image/png");
            try (OutputStream os = response.getOutputStream()) {
                ImageIO.write(setGraphicToCapchaImage(request), "png", os);
            }

        } catch (CustomException | IOException ex) {
            Logger.getLogger(CapchaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private BufferedImage setGraphicToCapchaImage(HttpServletRequest request) throws CustomException {
        int width = 86;
        int height = 32;
        Calculations calculations = new Calculations();
        char[][] data = {calculations.getRandomString().toCharArray()};

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = bufferedImage.createGraphics();

        Font font = new Font("Dialog", Font.BOLD, 22);
        g2d.setFont(font);

       

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

//        GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, height / 2, Color.WHITE, true);
        GradientPaint gp = new GradientPaint(0, 0, new Color(248, 248, 248), 0, height / 2, new Color(248, 248, 248), true);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(new Color(32, 210, 178));

        Random r = new Random();
        int index = 0;

        String captcha = String.copyValueOf(data[index]);
        request.getSession().setAttribute("captcha", captcha);

        System.out.println(request.getSession().getAttribute("captcha"));

        int x = 0;
        int y = 0;

//        for (int i = 0; i < data[index].length; i++) {
//            x += (Math.abs(r.nextInt()) % 15) + 10;
//            y = 20 + Math.abs(r.nextInt()) % 20;
//            g2d.drawChars(data[index], i, 1, x, y);
//        }

        // 0 <= x <= 70
        // 16 <= y <= 28
        for (int i = 0; i < data[index].length; i++) {
            if(i==0){
                x += (Math.abs(r.nextInt()) % 15);
            }else {
                x += (Math.abs(r.nextInt()) % 15) + 10;
            }
            y = 16 + Math.abs(r.nextInt()) % 20;

            if(x > 70) {
                x = 70;
            }

            if (y < 16) {
                y = 16;
            }else if (y > 28){
                y =28 - (y - 28);
                if(y < 16) {
                    y = 16;
                }
            }


            g2d.drawChars(data[index], i, 1, x, y);
            System.out.println("x - " + x + " y - " + y);
        }

        g2d.dispose();
        return bufferedImage;
    }

}
