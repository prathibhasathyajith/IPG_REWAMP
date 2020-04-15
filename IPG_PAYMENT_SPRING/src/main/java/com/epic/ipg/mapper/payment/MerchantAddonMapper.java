/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.mapper.payment;

import com.epic.ipg.bean.payment.MerchantAddonBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author chanuka
 */
public class MerchantAddonMapper implements RowMapper<MerchantAddonBean> {

    @Override
    public MerchantAddonBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        MerchantAddonBean merchantAddOn = new MerchantAddonBean();

        merchantAddOn.setMerchantId(rs.getString(1));//updated by mahesh 2013-07-17
        merchantAddOn.setLogoPath(rs.getString(2));
        merchantAddOn.setxCordinate(rs.getString(3));
        merchantAddOn.setyCordinate(rs.getString(4));
        merchantAddOn.setDisplayText(rs.getString(5));
        merchantAddOn.setThemeColor(rs.getString(6));
        merchantAddOn.setFontFamily(rs.getString(7));
        merchantAddOn.setFontStyle(rs.getString(8));
        merchantAddOn.setFontSize(rs.getString(9));
        merchantAddOn.setRemarks(rs.getString(10));
        merchantAddOn.setLastUpdatedUser(rs.getString(11));
        merchantAddOn.setMerchantName(rs.getString(12));

        return merchantAddOn;
    }
}
