/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.mapper.payment;

import com.epic.ipg.bean.payment.RuleBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author chanuka_g
 */
public class RuleMapper implements RowMapper<RuleBean> {

    @Override
    public RuleBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        RuleBean ruleBean = new RuleBean();
        ruleBean.setRuleID(Integer.parseInt(rs.getString("RULEID")));
        ruleBean.setRuleScope(rs.getString("RULESCOPECODE"));
        ruleBean.setOperator(rs.getString("OPERATOR"));
        ruleBean.setRuleType(rs.getString("RULETYPECODE"));
        ruleBean.setStartValue(rs.getString("STARTVALUE"));
        ruleBean.setEndValue(rs.getString("ENDVALUE"));
        ruleBean.setTrigerSeqID(rs.getString("TRIGGERSEQUENCEID"));
        return ruleBean;
    }
}
