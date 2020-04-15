/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.dao.payment;

import com.epic.ipg.bean.payment.MerchantResponseBean;
import com.epic.ipg.bean.payment.MerchantRiskBean;
import com.epic.ipg.bean.payment.MerchantSendToMpiBean;
import com.epic.ipg.bean.payment.RuleBean;
import com.epic.ipg.bean.payment.TransactionBean;
import com.epic.ipg.bean.payment.TransactionHistoryBean;
import com.epic.ipg.bean.payment.TransactionSearchBean;
import com.epic.ipg.mapper.payment.MerchantResponseMapper;
import com.epic.ipg.mapper.payment.MerchantRiskMapper;
import com.epic.ipg.mapper.payment.RuleMapper;
import com.epic.ipg.mapper.payment.TransactionMapper;
import com.epic.ipg.util.common.Common;
import com.epic.ipg.util.common.CustomException;
import com.epic.ipg.util.common.SystemDateTime;
import com.epic.ipg.util.common.TDESEncryptor;
import com.epic.ipg.util.varlist.CommonVarList;
import com.epic.ipg.util.varlist.CustomErrorVarList;
import com.epic.ipg.util.varlist.QueryVarList;
import com.epic.ipg.util.varlist.StatusVarList;
import java.util.Collections;
import java.util.List;
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
public class MerchantProcessDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    SystemDateTime systemDateTime;

    public String getTransactionInitiatedStatus(String merchantID) throws CustomException {
        String txnIniStatus;

        try {

            String sql = QueryVarList.SELECT_TXN_INITIATE_STATUS;

            txnIniStatus = jdbcTemplate.queryForObject(sql, new Object[]{merchantID}, String.class);

        } catch (EmptyResultDataAccessException e) {
            return "NO";
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return txnIniStatus;
    }

    public String getBatchNumber(String merchantID) throws CustomException {
        String batchNumer;

        try {

            String sql = QueryVarList.SELECT_BATCH_NUMBER;

            batchNumer = jdbcTemplate.queryForObject(sql, new Object[]{merchantID, StatusVarList.BATCH_OPEN_STATUS}, String.class);

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return batchNumer;
    }

    public String getCustomerEmail(String txnId) throws CustomException {
        String email;

        try {

            String sql = QueryVarList.SELECT_CUSTOMER_EMAIL;

            email = jdbcTemplate.queryForObject(sql, new Object[]{txnId}, String.class);

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return email;
    }

    public double getTodayTotalTxnAmount(String merchantID, String currentDate) throws CustomException {
        double totalAmount = 0.0;

        try {

            String sql = QueryVarList.SELECT_DAILY_TXN_AMOUNT;

            totalAmount = jdbcTemplate.queryForObject(sql, new Object[]{
                merchantID,
                currentDate,
                StatusVarList.TXN_COMPLETED,
                StatusVarList.TXN_ENGINERESRECEIVED,
                StatusVarList.TXN_ENGINEREQUESTSENT,
                StatusVarList.TXN_ENGINEREQCREATED
            }, Double.class
            );

        } catch (EmptyResultDataAccessException e) {
            return totalAmount;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return totalAmount;
    }

    public int getTodayTotalTxnCount(String merchantID, String currentDate) throws CustomException {
        int totalAmount = 0;

        try {

            String sql = QueryVarList.SELECT_DAILY_TXN_COUNT;

            totalAmount = jdbcTemplate.queryForObject(sql, new Object[]{
                merchantID,
                currentDate,
                StatusVarList.TXN_COMPLETED,
                StatusVarList.TXN_ENGINERESRECEIVED,
                StatusVarList.TXN_ENGINEREQUESTSENT,
                StatusVarList.TXN_ENGINEREQCREATED
            }, Integer.class
            );

        } catch (EmptyResultDataAccessException e) {
            return totalAmount;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return totalAmount;
    }

    public MerchantRiskBean getMerchantRiskProfile(String merchanId) throws CustomException {
        MerchantRiskBean merchantRiskBean;

        try {

            String sql = QueryVarList.SELECT_MERCHANT_RISK_PROFILE;

            merchantRiskBean = jdbcTemplate.queryForObject(sql, new Object[]{merchanId}, new MerchantRiskMapper());

            List<String> list1 = this.getBlockedBinList(merchantRiskBean.getRiskProfCode());
            List<String> list2 = this.getBlockedCountryList(merchantRiskBean.getRiskProfCode());
            List<String> list3 = this.getBlockedCurrencyList(merchantRiskBean.getRiskProfCode());
            List<String> list4 = this.getBlockedMccList(merchantRiskBean.getRiskProfCode());
            merchantRiskBean.setBin(list1);
            merchantRiskBean.setCountry(list2);
            merchantRiskBean.setCurrency(list3);
            merchantRiskBean.setMcc(list4);

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return merchantRiskBean;
    }

    public MerchantRiskBean getMerchantCustomerRiskProfile(String merchanId) throws CustomException {
        MerchantRiskBean merchantRiskBean;

        try {

            String sql = QueryVarList.SELECT_MERCHANT_CUSTOMER_RISK_PROFILE;

            merchantRiskBean = jdbcTemplate.queryForObject(sql, new Object[]{merchanId}, new MerchantRiskMapper());

            List<String> list1 = this.getBlockedBinList(merchantRiskBean.getRiskProfCode());
            List<String> list2 = this.getBlockedCountryList(merchantRiskBean.getRiskProfCode());
            List<String> list3 = this.getBlockedCurrencyList(merchantRiskBean.getRiskProfCode());
            List<String> list4 = this.getBlockedMccList(merchantRiskBean.getRiskProfCode());
            merchantRiskBean.setBin(list1);
            merchantRiskBean.setCountry(list2);
            merchantRiskBean.setCurrency(list3);
            merchantRiskBean.setMcc(list4);

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return merchantRiskBean;
    }

    public List<String> getBlockedBinList(String riskProfile) throws CustomException {
        List<String> blockBinList;

        try {

            String sql = QueryVarList.SELECT_BLOCKED_BIN;

            blockBinList = jdbcTemplate.queryForList(sql, new Object[]{riskProfile}, String.class);

        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return blockBinList;
    }

    public List<String> getBlockedCountryList(String riskProfile) throws CustomException {
        List<String> blockCountryList;

        try {

            String sql = QueryVarList.SELECT_BLOCKED_COUNTRY;

            blockCountryList = jdbcTemplate.queryForList(sql, new Object[]{riskProfile}, String.class);

        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return blockCountryList;
    }

    public List<String> getBlockedCurrencyList(String riskProfile) throws CustomException {
        List<String> blockCurrencyList;

        try {

            String sql = QueryVarList.SELECT_BLOCKED_CURRENCY;

            blockCurrencyList = jdbcTemplate.queryForList(sql, new Object[]{riskProfile}, String.class);

        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return blockCurrencyList;
    }

    public List<String> getBlockedMccList(String riskProfile) throws CustomException {
        List<String> blockMccList;

        try {

            String sql = QueryVarList.SELECT_BLOCKED_MCC;

            blockMccList = jdbcTemplate.queryForList(sql, new Object[]{riskProfile}, String.class);

        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return blockMccList;
    }

    public void insertTransactionNow(MerchantSendToMpiBean iPGTransaction)
            throws CustomException {

        iPGTransaction.setLastUpdatedTime(systemDateTime.getSystemDataAndTime());
        iPGTransaction.setCreatedTime(systemDateTime.getSystemDataAndTime());

        try {
            Object[] paramList = new Object[]{
                iPGTransaction.getiPGTransactionId(),
                iPGTransaction.getMerchantId(),
                iPGTransaction.getMerchantType(),
                iPGTransaction.getTerminalId(),
                iPGTransaction.getMerchantRemarks(),
                iPGTransaction.getCardAssociationCode(),
                TDESEncryptor.encrypt(CommonVarList.TDESENCRYPT_KEY, iPGTransaction.getCardHolderPan(), "CBC"),
                iPGTransaction.getCurrencyCode(),
                iPGTransaction.getAmount(),
                iPGTransaction.getCardName(),
                iPGTransaction.getExpiryDate(),
                iPGTransaction.getCvcNumber(),
                iPGTransaction.getCvvNumber(),//master
                iPGTransaction.getCidNumber(),//amex
                iPGTransaction.getStatusCode(),
                iPGTransaction.getBatchnumber(),
                iPGTransaction.getMerchantRefNo(),
                iPGTransaction.getCountryCode(),
                iPGTransaction.getClientIp(),
                iPGTransaction.getLastUpdatedUser(),
                systemDateTime.getSystemDataAndTime(),
                iPGTransaction.getCardHolderEmail(),
                iPGTransaction.getCardHolderMobile(),
                iPGTransaction.getCardHolderAddress(),
                iPGTransaction.getCardHolderCity(),
                Common.toMaskCardNumber(iPGTransaction.getCardHolderPan()),
                iPGTransaction.getTransactionCode()
            };

            jdbcTemplate.update(QueryVarList.INSERT_INITIAL_TRANSACTION,
                    paramList);

            TransactionHistoryBean historyBean = new TransactionHistoryBean();
            historyBean.setTransactionId(iPGTransaction.getiPGTransactionId());
            historyBean.setStatus(iPGTransaction.getStatusCode());
            historyBean.setLastUpdatedUser(iPGTransaction.getLastUpdatedUser());
            this.insertTransactionHistory(historyBean);

        } catch (DuplicateKeyException ex) {
            throw new CustomException(CustomErrorVarList.ALLREADY_ADD);
        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }
    }

    public void insertTransactionHistory(TransactionHistoryBean iPGTransactionHistoryBean)
            throws CustomException {
        try {
            Object[] paramList = new Object[]{
                iPGTransactionHistoryBean.getTransactionId(),
                iPGTransactionHistoryBean.getStatus(),
                iPGTransactionHistoryBean.getRemarks(),
                systemDateTime.getSystemDateWithMiliSeconds()
            };
            jdbcTemplate.update(QueryVarList.INSERT_TRANSACTION_HISTORY, paramList);

        } catch (DuplicateKeyException ex) {
            throw new CustomException(CustomErrorVarList.ALLREADY_ADD);
        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }

    }

    //
    //// --------------- Transaction Process start-------------------------------------------------------------
    //
    //
    public int updateTransactionStage(String transactionId, String status, String remark) throws CustomException {

        int count = 0;

        try {
            String sql = QueryVarList.UPDATE_TRANSACTION_STATE;
            Object[] paramList = new Object[]{
                status,
                systemDateTime.getSystemDataAndTime(),
                transactionId
            };
            count = jdbcTemplate.update(sql, paramList);

            if (count > 0) {
                TransactionHistoryBean historyBean = new TransactionHistoryBean();
                historyBean.setTransactionId(transactionId);
                historyBean.setStatus(status);
                historyBean.setRemarks(remark);
                this.insertTransactionHistory(historyBean);
            }

        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }

        return count;
    }

    public int updateTransactionWhen3DSecureEnable(String transactionId) throws CustomException {

        int count = 0;

        try {

            String sql = QueryVarList.UPDATE_TRANSACTION_STATE_3DSECURE_ENABLE;
            Object[] paramList = new Object[]{
                systemDateTime.getSystemDataAndTime(),
                "YES",
                transactionId
            };
            count = jdbcTemplate.update(sql, paramList);

        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }

        return count;
    }

    public List<RuleBean> getIpgRules(String ipgTrigerSequence) throws CustomException {
        List<RuleBean> ruleBeanList;
        try {

            String sql = QueryVarList.SELECT_RULES_IPG_LEVEL;

            ruleBeanList = jdbcTemplate.query(sql, new Object[]{ipgTrigerSequence, CommonVarList.IPGRULE_IPG_LEVEL},
                    new RuleMapper());
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return ruleBeanList;
    }

    public List<RuleBean> getIpgMerchantRules(String ipgTrigerSequence, String merchantId) throws CustomException {
        List<RuleBean> ruleBeanList;
        try {

            String sql = QueryVarList.SELECT_RULES_MERCHANT_LEVEL;

            ruleBeanList = jdbcTemplate.query(sql, new Object[]{
                merchantId,
                ipgTrigerSequence,
                CommonVarList.IPGRULE_MERCHANT_LEVEL},
                    new RuleMapper());
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return ruleBeanList;
    }

    public List<TransactionBean> getMerchantTransactionAll(TransactionSearchBean searchBean) throws CustomException {
        List<TransactionBean> transactionBeanList;
        try {

            String sql = QueryVarList.SELECT_MERCHANT_TXN_DETAIL;

            transactionBeanList = jdbcTemplate.query(sql, new Object[]{searchBean.getFromDate(), searchBean.getMerchantId(), searchBean.getStatus()},
                    new TransactionMapper());
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return transactionBeanList;
    }

    public int getMerchantTransactionCount(TransactionSearchBean searchBean) throws CustomException {
        int txncount;
        try {

            String sql = QueryVarList.SELECT_MERCHANT_TXN_COUNT;

            txncount = jdbcTemplate.queryForObject(sql, new Object[]{searchBean.getFromDate(), searchBean.getMerchantId(), searchBean.getStatus()},
                    Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return txncount;
    }

    public int getCustomerTransactionCount(TransactionSearchBean searchBean) throws CustomException {
        int txncount;
        try {

            String sql = QueryVarList.SELECT_CUSTOMER_TXN_COUNT;

            txncount = jdbcTemplate.queryForObject(sql, new Object[]{searchBean.getFromDate(), searchBean.getCardNumber(), searchBean.getStatus()},
                    Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return txncount;
    }

    public List<TransactionBean> getCustomerTransactionAll(TransactionSearchBean searchBean) throws CustomException {
        List<TransactionBean> transactionBeanList;
        try {

            String sql = QueryVarList.SELECT_CUSTOMER_TXN_DETAIL;

            transactionBeanList = jdbcTemplate.query(sql, new Object[]{searchBean.getFromDate(), searchBean.getCardNumber(), searchBean.getStatus()},
                    new TransactionMapper());
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return transactionBeanList;
    }

    public String get3DSecureEnableStatus(String merchantID) throws CustomException {
        String authRequired;

        try {

            String sql = QueryVarList.SELECT_3DSECURE_ENABLE_STATUS;

            authRequired = jdbcTemplate.queryForObject(sql, new Object[]{merchantID}, String.class);

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return authRequired;
    }

    public TransactionBean getTxnData(String txnId) throws CustomException {
        TransactionBean transactionBean;

        try {
            String sql = QueryVarList.SELECT_TRANSACTION_DATA;

            transactionBean = jdbcTemplate.queryForObject(sql, new Object[]{txnId}, new TransactionMapper());

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return transactionBean;
    }

    public MerchantResponseBean getMerchantForResponseByTxnID(String txnId) throws CustomException {
        MerchantResponseBean responseBean;

        try {

            String sql = QueryVarList.SELECT_MERCHANT_BY_TXN;

            responseBean = jdbcTemplate.queryForObject(sql, new Object[]{txnId}, new MerchantResponseMapper());

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return responseBean;
    }

    public void insertIPG3DSecureVerificationRequest(MerchantSendToMpiBean iPGMerchantSendToMpiBean)
            throws CustomException {

        iPGMerchantSendToMpiBean.setLastUpdatedTime(systemDateTime.getSystemDataAndTime());
        iPGMerchantSendToMpiBean.setCreatedTime(systemDateTime.getSystemDataAndTime());

        try {
            Object[] paramList = new Object[]{
                iPGMerchantSendToMpiBean.getiPGTransactionId(),
                iPGMerchantSendToMpiBean.getCardHolderPan(),
                iPGMerchantSendToMpiBean.getAquBin(),
                iPGMerchantSendToMpiBean.getMerchantId(),
                iPGMerchantSendToMpiBean.getMerchantName(),
                iPGMerchantSendToMpiBean.getCountryCode(),
                iPGMerchantSendToMpiBean.getPurchasedDateTime(),
                iPGMerchantSendToMpiBean.getAmount(),
                iPGMerchantSendToMpiBean.getCurrencyCode(),
                iPGMerchantSendToMpiBean.getExponent(),
                iPGMerchantSendToMpiBean.getPassword(),
                iPGMerchantSendToMpiBean.getExpiryDate(),
                iPGMerchantSendToMpiBean.getDeviceCategory(),
                iPGMerchantSendToMpiBean.getAcceptHeader(),
                iPGMerchantSendToMpiBean.getUserAgent(),
                null,
                iPGMerchantSendToMpiBean.getDescription(),
                "SYSTEM"
            };
            jdbcTemplate.update(QueryVarList.INSERT_3DSECURE_VEREQ,
                    paramList);

        } catch (DuplicateKeyException ex) {
            throw new CustomException(CustomErrorVarList.ALLREADY_ADD);
        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }
    }

    public String getDefaultMerchant(String merchantID) throws CustomException {
        String defaultMerchant;

        try {

            String sql = QueryVarList.SELECT_DEFAULT_MERCHANT;
            defaultMerchant = jdbcTemplate.queryForObject(sql, new Object[]{merchantID}, String.class);

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return defaultMerchant;
    }

}
