/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.mapper.payment;

import com.epic.ipg.bean.payment.CustomerPaymentRequestBean;
import com.epic.ipg.util.common.Common;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author dilanka_w
 */
public class CustomerPaymentMapper implements RowMapper<CustomerPaymentRequestBean> {

    @Override
    public CustomerPaymentRequestBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        CustomerPaymentRequestBean txnBean = new CustomerPaymentRequestBean();

        txnBean.setiPGTransactionRequestId(rs.getString("IPGTRANSACTIONREQUESTID"));
        txnBean.setMerchantId(rs.getString("MERCHANTID"));
        txnBean.setCustomerName(rs.getString("CUSTOMERNAME"));
        txnBean.setAmount(Double.parseDouble(rs.getString("AMOUNT")));
        txnBean.setEmail(rs.getString("EMAIL"));
        txnBean.setCurrencyCode(rs.getString("CURRENCYCODE"));
        txnBean.setCurrency(rs.getString("CURRENCY"));
        txnBean.setDispalyAmount(Common.toCurrencyFormat(String.format("%.2f", new BigDecimal(rs.getString("AMOUNT")))));

        return txnBean;
    }

}
