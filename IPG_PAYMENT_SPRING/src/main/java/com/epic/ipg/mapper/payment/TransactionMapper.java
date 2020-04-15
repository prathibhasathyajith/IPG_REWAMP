/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.mapper.payment;

import com.epic.ipg.bean.payment.TransactionBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author chanuka_g
 */
public class TransactionMapper implements RowMapper<TransactionBean> {

    @Override
    public TransactionBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        TransactionBean txnBean = new TransactionBean();

        txnBean.setiPGTransactionId(rs.getString("IPGTRANSACTIONID"));
        txnBean.setMerchantName(rs.getString("merchantname"));
        txnBean.setCardAssociationCode(rs.getString("CARDASSOCIATION"));
        txnBean.setCardHolderPan(rs.getString("CARDHOLDERPAN"));
        txnBean.setMerchantId(rs.getString("MERCHANTID"));
        txnBean.setAmount(Double.parseDouble(rs.getString("AMOUNT")));
        txnBean.setStatusCode(rs.getString("STATUS"));
        txnBean.setPurchasedDateTime(Timestamp.valueOf(rs.getString("CREATEDTIME")));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        java.util.Date parsedDate;

        // remove override and throw ParseException instead
        try {
            parsedDate = dateFormat.parse(rs.getString("CREATEDTIME"));
            java.sql.Timestamp timeStampDate = new java.sql.Timestamp(parsedDate.getTime());
            txnBean.setCreatedTime(timeStampDate);

        } catch (ParseException ex) {
            Logger.getLogger(TransactionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return txnBean;
    }
}
