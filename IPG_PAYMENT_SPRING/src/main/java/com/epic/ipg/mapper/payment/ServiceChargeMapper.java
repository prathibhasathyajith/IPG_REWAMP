/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.mapper.payment;

import com.epic.ipg.bean.payment.ServiceChargeBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author chanuka_g
 */
public class ServiceChargeMapper implements RowMapper<ServiceChargeBean> {

    @Override
    public ServiceChargeBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        ServiceChargeBean serviceCharge = new ServiceChargeBean();

        serviceCharge.setValue(rs.getString(1));//updated by mahesh 2013-07-17
        serviceCharge.setRate(rs.getString(2));

        return serviceCharge;
    }
}
