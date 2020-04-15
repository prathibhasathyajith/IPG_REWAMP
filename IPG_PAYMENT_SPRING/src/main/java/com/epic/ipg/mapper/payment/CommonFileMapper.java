/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.ipg.mapper.payment;

import com.epic.ipg.bean.payment.CommonFilePaths;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author chanuka_g
 */
public class CommonFileMapper implements RowMapper<CommonFilePaths> {

    @Override
    public CommonFilePaths mapRow(ResultSet rs, int rowNum) throws SQLException {

        CommonFilePaths commonFilePaths = new CommonFilePaths();

        commonFilePaths.setTxnLogPath(rs.getString("TRANSACTIONLOG"));
        commonFilePaths.setInforLogPath(rs.getString("INFOLOG"));
        commonFilePaths.setErrorLogPath(rs.getString("ERRORLOG"));
        commonFilePaths.setCertificatePath(rs.getString("CERTIFICATES"));
        commonFilePaths.setCcbtxnFiles(rs.getString("CCBTXNFILES"));
        commonFilePaths.setIpgmerchantlogin(rs.getString("IPGMERCHANLOGIN"));
        commonFilePaths.setIpgrulevalidation(rs.getString("IPGRULEVALIDATION"));
        commonFilePaths.setIpgriskvalidation(rs.getString("IPGRISKVALIDATION"));
        commonFilePaths.setMpirequest(rs.getString("MPIREQUEST"));
        commonFilePaths.setMpiresponse(rs.getString("MPIRESPONSE"));
        commonFilePaths.setSwitchrequest(rs.getString("SWITCHREQUEST"));
        commonFilePaths.setSwitchresponse(rs.getString("SWITCHRESPONSE"));
        commonFilePaths.setKeystorepath(rs.getString("KEYSTOREPATH"));
        commonFilePaths.setCertPath(rs.getString("CERTPATH"));
        commonFilePaths.setKeyPath(rs.getString("KEYPATH"));

        return commonFilePaths;
    }
}
