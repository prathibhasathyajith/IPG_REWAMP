/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.dao;

import com.epic.ipg.mapper.payment.CommonFileMapper;
import com.epic.ipg.bean.payment.CommonFilePaths;
import com.epic.ipg.util.common.CustomException;
import com.epic.ipg.util.varlist.CustomErrorVarList;
import com.epic.ipg.util.varlist.QueryVarList;
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
public class CommonDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public CommonFilePaths getCommonFilePath(String osType) throws CustomException {
        CommonFilePaths commonFilePaths;

        try {

            String sql = QueryVarList.SELECT_COMMON_FILEPATH;

            commonFilePaths = jdbcTemplate.queryForObject(sql, new Object[]{osType}, new CommonFileMapper());

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return commonFilePaths;
    }

    public String getCommonConfigValue(String code) throws CustomException {
        String commonValue;

        try {

            String sql = QueryVarList.SELECT_COMMON_CONFIG;

            commonValue = jdbcTemplate.queryForObject(sql, new Object[]{code}, String.class);

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return commonValue;
    }

    public String getCurrencyCode(String code) throws CustomException {
        String currencyCode;

        try {

            String sql = QueryVarList.SELECT_CURRENCY_CODE;

            currencyCode = jdbcTemplate.queryForObject(sql, new Object[]{code}, String.class);

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return currencyCode;
    }
}
