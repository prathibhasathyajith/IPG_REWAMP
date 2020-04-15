/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.mapper.payment;

import com.epic.ipg.bean.payment.AddonFieldsBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author dilanka_w
 */
public class AddonFieldsMapper implements RowMapper<AddonFieldsBean> {

    @Override
    public AddonFieldsBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        AddonFieldsBean addonFieldsBean = new AddonFieldsBean();

        addonFieldsBean.setDescription(rs.getString("DESCRIPTION"));
        addonFieldsBean.setFieldName(rs.getString("FIELDNAME"));
        addonFieldsBean.setFieldSize(rs.getString("FIELDSIZE"));

        return addonFieldsBean;
    }
}
