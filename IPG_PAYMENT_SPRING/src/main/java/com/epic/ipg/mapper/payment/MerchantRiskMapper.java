/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.mapper.payment;

import com.epic.ipg.bean.payment.MerchantRiskBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author chanuka_g
 */
public class MerchantRiskMapper implements RowMapper<MerchantRiskBean> {

    @Override
    public MerchantRiskBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        MerchantRiskBean riskBean = new MerchantRiskBean();

        riskBean.setMerCountry(rs.getString("COUNTRYCODE"));
        riskBean.setRiskProfCode(rs.getString("RISKPROFILECODE"));
        riskBean.setMaxSingleTxnLimit(rs.getString("MAXIMUMSINGLETXNLIMIT"));
        riskBean.setMaxDailyTxnCount(rs.getString("MAXIMUMTXNCOUNT"));
        riskBean.setMinSingleTxnLimit(rs.getString("MINIMUMSINGLETXNLIMIT"));
        riskBean.setMaxDailyTxnAmount(rs.getString("MAXIMUMTOTALTXNAMOUNT"));

        return riskBean;
    }
}
