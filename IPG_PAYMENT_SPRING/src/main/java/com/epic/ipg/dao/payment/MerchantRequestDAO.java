/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.dao.payment;

import com.epic.ipg.bean.payment.AddonFieldsBean;
import com.epic.ipg.bean.payment.MerchantAddonBean;
import com.epic.ipg.bean.payment.MerchantBean;
import com.epic.ipg.mapper.payment.MerchantMapper;
import com.epic.ipg.mapper.payment.MerchantAddonMapper;
import com.epic.ipg.bean.payment.ServiceChargeBean;
import com.epic.ipg.mapper.payment.AddonFieldsMapper;
import com.epic.ipg.mapper.payment.ServiceChargeMapper;
import com.epic.ipg.util.common.CustomException;
import com.epic.ipg.util.varlist.CustomErrorVarList;
import com.epic.ipg.util.varlist.QueryVarList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author chanuka_g
 */
@Repository
@Scope("prototype")
public class MerchantRequestDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public MerchantAddonBean getMerchantAddon(String merchanId) throws CustomException {
        MerchantAddonBean merchantAddonBean;

        try {

            String sql = QueryVarList.SELECT_MERCHANT_ADDON;

            merchantAddonBean = jdbcTemplate.queryForObject(sql, new Object[]{merchanId}, new MerchantAddonMapper());

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return merchantAddonBean;
    }

    public MerchantBean getMerchantDetail(String merchanId) throws CustomException {
        MerchantBean merchantBean;

        try {

            String sql = QueryVarList.SELECT_MERCHANT_DETAIL;

            merchantBean = jdbcTemplate.queryForObject(sql, new Object[]{merchanId}, new MerchantMapper());

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return merchantBean;
    }

    public ServiceChargeBean getServiceCharge(String merchanId) throws CustomException {
        ServiceChargeBean serviceChargeBean;

        try {

            String sql = QueryVarList.SELECT_SERVICE_CHARGE;

            serviceChargeBean = jdbcTemplate.queryForObject(sql, new Object[]{merchanId}, new ServiceChargeMapper());

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return serviceChargeBean;
    }

    public String getMerchantTerminal(String mId, String currencyCode) throws CustomException {
        String terminalId;

        try {

            String sql = QueryVarList.SELECT_MERCHANAT_TERMINAL;

            terminalId = jdbcTemplate.queryForObject(sql, new Object[]{mId, currencyCode}, String.class);

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return terminalId;
    }

    public String getMerchantCredential(String mId, String cardType) throws CustomException {
        String merchantPass;

        try {

            String sql = QueryVarList.SELECT_MERCHANT_CREDENTIAL;

            merchantPass = jdbcTemplate.queryForObject(sql, new Object[]{mId, cardType}, String.class);

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return merchantPass;
    }

    public List<AddonFieldsBean> getMerchantAddonFields(String merchantId) throws CustomException {
        List<AddonFieldsBean> addonFieldBeanList;

        try {

            String sql = QueryVarList.SELECT_MERCHANT_ADDON_FIELDS;
            addonFieldBeanList = jdbcTemplate.query(sql, new Object[]{merchantId}, new AddonFieldsMapper());

        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return addonFieldBeanList;
    }

}
