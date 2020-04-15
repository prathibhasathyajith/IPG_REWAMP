/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.mapper.payment;

import com.epic.ipg.bean.payment.MerchantResponseBean;
import com.epic.ipg.util.common.Common;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author chanuka_g
 */
public class MerchantResponseMapper implements RowMapper<MerchantResponseBean> {

    @Override
    public MerchantResponseBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        MerchantResponseBean responseBean = new MerchantResponseBean();

        responseBean.setPrimaryURL(rs.getString(1));
        responseBean.setSecondaryURL(rs.getString(2));
        responseBean.setAccountNo(rs.getString(3));
        responseBean.setMerchantID(rs.getString(4));
        responseBean.setDinamicReturnSuccessURL(rs.getString(5));
        responseBean.setDinamicReturnErrorURL(rs.getString(6));
        responseBean.setTxnDateTime(rs.getString(7).substring(0, 19));
        responseBean.setAmount(Common.toCurrencyFormat(String.format("%.2f", new BigDecimal(rs.getString(8)))));
        responseBean.setCurrencyCode(rs.getString(9));

        return responseBean;
    }
}
