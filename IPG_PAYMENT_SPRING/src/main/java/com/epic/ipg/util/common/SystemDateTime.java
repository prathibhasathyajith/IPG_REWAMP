/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.util.common;

import com.epic.ipg.util.varlist.CustomErrorVarList;
import java.sql.Timestamp;
import java.util.Calendar;
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
public class SystemDateTime {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Timestamp getSystemDataAndTime() {
        Timestamp dateTime;

        try {

            String sql = "SELECT SYSDATE FROM DUAL ";

            dateTime = jdbcTemplate.queryForObject(sql, new Object[]{}, Timestamp.class);

        } catch (Exception ex) {
            return new Timestamp(Calendar.getInstance().getTimeInMillis());
        }

        return dateTime;
    }
    
    public String getSystemDateWithMiliSeconds() throws CustomException {
        String dateTime;

        try {

            String sql = "SELECT TO_CHAR(SYSTIMESTAMP, 'DD-MON-YY HH.MI.SS.FF3 AM') SYSTEMTIMESTAMP FROM DUAL";

            dateTime = jdbcTemplate.queryForObject(sql, new Object[]{}, String.class);

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception ex) {
            throw new CustomException(CustomErrorVarList.DATA_ACCESS_ERROR);
        }

        return dateTime;
    }
}
