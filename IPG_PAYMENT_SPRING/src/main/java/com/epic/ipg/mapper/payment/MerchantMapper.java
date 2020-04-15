/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.mapper.payment;

import com.epic.ipg.bean.payment.MerchantBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author chanuka_g
 */
public class MerchantMapper implements RowMapper<MerchantBean> {

    @Override
    public MerchantBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        MerchantBean merchantBean = new MerchantBean();
        
        merchantBean.setAddressId(rs.getInt(1));
        merchantBean.setMerchantName(rs.getString(2));
        merchantBean.setContactPerson(rs.getString(3));
        merchantBean.setCountryCode(rs.getString(4));
        merchantBean.setCreatedTime(rs.getDate(5));
        merchantBean.setDateOfExpiry(rs.getDate(6));
        merchantBean.setDateOfRegistry(rs.getDate(7));

        merchantBean.setPrimaryUrl(rs.getString(8));
        merchantBean.setRemarks(rs.getString(9));
        merchantBean.setStatusCode(rs.getString(10));
        merchantBean.setSecurityMechanism(rs.getString(11));
        merchantBean.setSymmetricKey(rs.getBytes(12));
        merchantBean.setDynamicReturnErrorUrl(rs.getString(13));
        merchantBean.setMerchantId(rs.getString(14));
        merchantBean.setDynamicReturnSuccessUrl(rs.getString(15));

        return merchantBean;
    }
}
