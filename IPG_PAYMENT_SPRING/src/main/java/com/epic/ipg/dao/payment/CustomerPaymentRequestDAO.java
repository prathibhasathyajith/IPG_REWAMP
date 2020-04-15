/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.dao.payment;

import com.epic.ipg.bean.payment.CustomerPaymentRequestBean;
import com.epic.ipg.mapper.payment.CustomerPaymentMapper;
import com.epic.ipg.util.common.CustomException;
import com.epic.ipg.util.common.SystemDateTime;
import com.epic.ipg.util.varlist.CustomErrorVarList;
import com.epic.ipg.util.varlist.QueryVarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dilanka_w
 */
@Repository
@Scope("prototype")
public class CustomerPaymentRequestDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SystemDateTime systemDateTime;

    public CustomerPaymentRequestBean getTxnData(String txnId) throws CustomException {
        CustomerPaymentRequestBean requestBean;

        try {
            String sql = QueryVarList.SELECT_TRANSACTION_REQUEST_DATA;

            requestBean = jdbcTemplate.queryForObject(sql, new Object[]{txnId}, new CustomerPaymentMapper());

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return requestBean;
    }

}
