/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.bean.payment;

/**
 *
 * @author dilanka_w
 */
public class EmailTemplateBean {

    private String message;
    private String subject;
    private String imgpath;
    private boolean imagetagExist;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public boolean isImagetagExist() {
        return imagetagExist;
    }

    public void setImagetagExist(boolean imagetagExist) {
        this.imagetagExist = imagetagExist;
    }

}
