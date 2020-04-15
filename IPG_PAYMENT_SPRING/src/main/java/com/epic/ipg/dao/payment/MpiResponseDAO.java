/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.dao.payment;

import com.epic.ipg.bean.payment.MPIResponseBean;
import com.epic.ipg.mapper.payment.TransactionECIMapper;
import com.epic.ipg.util.common.CustomException;
import com.epic.ipg.util.common.SystemDateTime;
import com.epic.ipg.util.varlist.CustomErrorVarList;
import com.epic.ipg.util.varlist.QueryVarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author chanuka_g
 */
@Repository
@Scope("prototype")
public class MpiResponseDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SystemDateTime systemDateTime;

    public void insertIPG3DSecureVerificationResponse(MPIResponseBean mPIResponseBean)
            throws CustomException {
        try {
            Object[] paramList = new Object[]{
                mPIResponseBean.getTransactionId(),
                mPIResponseBean.getCavv(),
                mPIResponseBean.getEci(),
                "SYSTEM"
            };
            jdbcTemplate.update(QueryVarList.INSERT_3DSECURE_VERES,
                    paramList);

        } catch (DuplicateKeyException ex) {
            throw new CustomException(CustomErrorVarList.ALLREADY_ADD);
        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }
    }

    public int updateTransactionStageAfterMPIRes(String status, MPIResponseBean iPGMPIResponseBean) throws CustomException {

        int count = 0;

        try {
            String sql = QueryVarList.UPDATE_TRANSACTION_MPI_RES;
            Object[] paramList = new Object[]{
                status,
                iPGMPIResponseBean.getCavv(),
                iPGMPIResponseBean.getCvv(),
                iPGMPIResponseBean.getEci(),
                systemDateTime.getSystemDataAndTime(),
                iPGMPIResponseBean.getTransactionId()
            };

            count = jdbcTemplate.update(sql, paramList);

        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }

        return count;
    }

    public boolean checkECIAllow(MPIResponseBean iPGMPIResponseBean, String authStatus) throws CustomException {
        String eciAllow;
        try {
            String sql = QueryVarList.SELECT_ALLOW_ECI;

            eciAllow = jdbcTemplate.queryForObject(sql, new Object[]{iPGMPIResponseBean.getCardAsscociationCode(),
                iPGMPIResponseBean.getEci(), authStatus}, String.class);
            if (eciAllow != null && eciAllow.length() > 0) {
                return true;
            }

        } catch (EmptyResultDataAccessException e) {
            return false;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }
        return false;
    }

    public MPIResponseBean getTxnECIData(String txnId) throws CustomException {
        MPIResponseBean mPIResponseBean;

        try {
            String sql = QueryVarList.SELECT_ECI_DATA;

            mPIResponseBean = jdbcTemplate.queryForObject(sql, new Object[]{txnId}, new TransactionECIMapper());

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return mPIResponseBean;
    }
}
