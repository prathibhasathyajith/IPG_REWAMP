/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.mapper.payment;

import com.epic.ipg.bean.payment.MPIResponseBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author chanuka_g
 */
public class TransactionECIMapper implements RowMapper<MPIResponseBean> {

    @Override
    public MPIResponseBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        MPIResponseBean mPIResponseBean = new MPIResponseBean();
        mPIResponseBean.setEci(rs.getString("ECI"));
        mPIResponseBean.setCardAsscociationCode(rs.getString("CARDASSOCIATIONCODE"));
        return mPIResponseBean;
    }
}
