<%-- 
    Document   : merchantaddon_new
    Created on : Oct 22, 2018, 12:08:26 PM
    Author     : prathibha_w
--%>


<%@page import="com.epic.ipg.bean.payment.AddonFieldsBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.epic.ipg.bean.payment.MerchantAddonBean"%>
<%@page import="com.epic.ipg.util.varlist.CardAssociationVarList"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>

        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge"></meta>
        <link href="<%=request.getContextPath()%>/resources/assets_new/css/main.css" rel="stylesheet" />
        <link href="<%=request.getContextPath()%>/resources/assets_new/css/responsive.css" rel="stylesheet" />
        <script src="<%=request.getContextPath()%>/resources/assets_new/js/jquery.min.js"></script>


        <title>EPIC IPG Merchant Addon</title>
        <%
            String path = request.getContextPath();
            MerchantAddonBean iPGMerchantAddonBean = (MerchantAddonBean) session.getAttribute("addondetail");
            String fontfamily = iPGMerchantAddonBean.getFontFamily();
            String bytelogo = (String) session.getAttribute("logoPathByte");

            //            String logoPath = path + iPGMerchantAddonBean.getLogoPath();
            String logoPath = iPGMerchantAddonBean.getLogoPath();
            String merchantName = iPGMerchantAddonBean.getMerchantName();

            //            String imagenamearr[] = logoPath.split("\\");
            //            String imagename = imagenamearr[imagenamearr.length-1];
            String logoPath2 = "../resources/img/addonImages/cvvcvc.jpg";
            String xcordinate = iPGMerchantAddonBean.getxCordinate();
            String ycordinate = iPGMerchantAddonBean.getyCordinate();
            String displayText = iPGMerchantAddonBean.getDisplayText();
            String themeColor = iPGMerchantAddonBean.getThemeColor();
            String fontFamily = iPGMerchantAddonBean.getFontFamily();
            String fontStyle = iPGMerchantAddonBean.getFontStyle();
            String remarks = iPGMerchantAddonBean.getRemarks();
            String fontSize = iPGMerchantAddonBean.getFontSize();
            int fontSizeThemeText = Integer.parseInt(iPGMerchantAddonBean.getFontSize()) + 5;
            String lastupdatedUser = iPGMerchantAddonBean.getLastUpdatedUser();
            String fontColor = "#303";
            String visa = CardAssociationVarList.VISA;
            String masterCard = CardAssociationVarList.MARSTERCARD;
            String amex = CardAssociationVarList.AMEX;

            ArrayList<AddonFieldsBean> merchantAddonFields = (ArrayList<AddonFieldsBean>) session.getAttribute("merchantAddonFields");
        %>

        <script>
            function sessionTimeOut() {
                setTimeout(function () {
                    document.form1.action = "${pageContext.request.contextPath}/IPGMerchantTransactionServlet?perform=processPaymentTimeout";
                    document.form1.submit();
                }, 300000);

            }
            var color = "<%=themeColor%>";

            function captchaRefresh() {
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: "${pageContext.request.contextPath}/captchaRefresh",
                    data: JSON.stringify(""),
                    dataType: 'json',
                    success: function (data) {
                        $('#wordVerifyimg').prop("src", "data:image/jpeg;base64," + data.value);
                    }
                });
            }

            // IFRAME access function //
            //------------------------------------------------------------------//

            window.addEventListener('message', function (event) {

                // IMPORTANT: Check the origin of the data! 
                if (~event.origin.indexOf('http://127.0.0.1:50489')) {
                    // The data has been sent from your site 

                    // The data sent with postMessage is stored in event.data 
                    console.log('if');
                    console.log(event.data);
                    if (event.data == 'submit') {
                        var validateMsg = validateIframeBased();
                        if (validateMsg) {
                            $('#form_credit_card_details').attr('target', '_parent');
                        } else {
                            $('#form_credit_card_details').attr('target', '_self');
                        }
                        $('#form_credit_card_details').submit();
                    }

                    if (event.data == 'validate') {
                        validateIframeBased();
                    }


                } else {
                    // The data hasn't been sent from your site! 
                    // Be careful! Do not use it. 
                    console.log('if');
                    console.log(event.data);
                    
                    if (event.data == 'submit') {
                        var validateMsg = validateIframeBased();
                        if (validateMsg) {
                            $('#form_credit_card_details').attr('target', '_parent');
//                            alert("_parent");
                        } else {
                            $('#form_credit_card_details').attr('target', '_self');
                        }
                        $('#form_credit_card_details').submit();
                    }

                    if (event.data == 'validate') {
                        validateIframeBased();
                    }
                    return;
                }
            });



            function validateIframeBased() {
                var response = true;

                var firstname = $('#id_firstname').val();
                var cardNumber = $('#id_cardNumber').val().replace(" ", "");
                var cvcNum = $('#id_cvcNum').val();
                var month = $('#id_month').val();
                var year = $('#id_year').val();
                var code = $('#id_code').val();

                $.ajax({
                    url: '${pageContext.request.contextPath}/IPGFieldValidateFlagServlet',
                    async: false,
                    crossDomain: true,
                    data: JSON.stringify({
                        firstname: firstname,
                        cardNumber: cardNumber,
                        cvcNum: cvcNum,
                        month: month,
                        year: year,
                        code: code
                    }),
                    method: "POST",
                    headers: {
                        "content-type": "application/json",
                        "cache-control": "no-cache"
                    },
                    success: function (data) {
                        response = data;
                    }
                });

                return response;
            }

            //------------------------------------------------------------------//

        </script>
        <style>
            .merchat-bg-color {
                background-color: #<%=themeColor%>;
            }

            .merchat-fnt-color {
                color: #<%=themeColor%>;
            }

            .merchat-font {
                font-family: <%=fontFamily%>;
            }

            .merchat-font-style {
                font-style: <%=fontStyle%>;
            }

        </style>
    </head>


    <body onload="sessionTimeOut()">
        <div class="PGI">

            <div class="box">
                <div class="content">
                    <div class="title-info merchat-bg-color merchat-font">
                        Payment Gateway
                    </div>
                    <div class="message merchat-font">
                        <div class="msg">${displayMessage}</div>
                        <button type="button" class="close">&times;</button>
                    </div>
                    <div class="merchant">
                        <div class="logo">
                            <img src="data:image/jpeg;base64,<%=bytelogo%>" width="auto" height="30" />
                        </div>
                        <div class="logo-bank">
                            <img src="<%=request.getContextPath()%>/resources/assets_new/image/hnb.svg" height="35" width="100%" />
                        </div>
                        <div class="name merchat-font merchat-font-style"><%=merchantName%></div>
                        <div class="theme merchat-font"><%=displayText%></div>
                    </div>
                    <div class="horizontal-line" style="float: none;"></div>
                    <div class="title-details merchat-font">
                        <div class="title">
                            Enter your credit card details test
                        </div>
                        <div class="detail">
                            Please make sure you filling all mandatory information marked with <span style="font-family:cursive">*</span>
                        </div>
                    </div>
                    <form target="_self" sandbox="allow-top-navigation allow-scripts allow-forms" class="sec-form"  autocomplete="off" name="form1" id="form_credit_card_details"  method="post" action="${pageContext.request.contextPath}/IPGMerchantTransactionServlet" >
                        <div class="amount">
                            <div class="label merchat-font">
                                <div class="cont merchat-bg-color">Amount</div>
                                <div class="line"></div>
                                <div class="amnt merchat-bg-color"><span id="ccode">${sessionScope["currency"]}</span>${sessionScope["totalAmount"]}</div>
                            </div>
                        </div>
                        <div class="horizontal-line"></div>
                        <div class="sec-mandatory">
                            <div class="sec-field">
                                <label class="${requestScope["nameColor"]}">Name on Card<span class="mandatory">*</span></label><img class="infosvg" src="<%=request.getContextPath()%>/resources/assets_new/image/info.svg" />
                                <div class="input-field">
                                    <input class="input_  ${requestScope["nameClass"]}" type="text" name="firstname" value="epic" id="id_firstname"/>
                                </div>
                                <div class="tooltip">
                                    <img src="<%=request.getContextPath()%>/resources/assets_new/image/info/cardname.svg" width="auto" height="100" alt="Name on Card info" />
                                </div>
                            </div>
                            <div class="sec-field">
                                <label class="${requestScope["cardColor"]}">Credit/Debit Card Number<span class="mandatory">*</span></label><img class="infosvg" src="<%=request.getContextPath()%>/resources/assets_new/image/info.svg" />
                                <div class="input-field">
                                    <input name="cardNumber" class="input-cardField ${requestScope["cardClass"]}" maxlength="19" size="19" value="4216890006499566" id="id_cardNumber"/>
                                    <span class="caImage">
                                        <img src="<%=request.getContextPath()%>/resources/assets_new/image/cc/visa.svg" width="100%" height="30" alt="Card Association" id="visa_ca" style="display:none"/>
                                        <img src="<%=request.getContextPath()%>/resources/assets_new/image/cc/amex.svg" width="100%" height="30" alt="Card Association" id="amex_ca" style="display:none"/>
                                        <img src="<%=request.getContextPath()%>/resources/assets_new/image/cc/master.svg" width="100%" height="30" alt="Card Association" id="master_ca" style="display:none"/>
                                    </span>
                                </div>
                                <div class="tooltip">
                                    <img src="<%=request.getContextPath()%>/resources/assets_new/image/info/cardnum.svg" width="auto" height="100" alt="Credit/Debit Card Number info" />
                                </div>
                            </div>
                            <div class="sec-field sec-field_2">
                                <label class="${requestScope["cvcColor"]}">CVV Value<span class="mandatory">*</span></label><img class="infosvg" src="<%=request.getContextPath()%>/resources/assets_new/image/info.svg" />
                                <div class="input-field">
                                    <input name="cvcNum" class="input_ ${requestScope["cvcClass"]}" maxlength="4" placeholder="Security Code" maxlength="3" value="123" id="id_cvcNum" onkeyup="$(this).val($(this).val().replace(/[^0-9]/g, ''))"/>
                                </div>
                                <div class="tooltip">
                                    <img src="<%=request.getContextPath()%>/resources/assets_new/image/info/cvv.svg" width="auto" height="100" alt="CVV info" />
                                </div>
                            </div>
                            <div class="sec-field sec-field_2">
                                <label class="${requestScope["monthColor"]}">Expiry Date<span class="mandatory">*</span></label><img class="infosvg" src="<%=request.getContextPath()%>/resources/assets_new/image/info.svg" />
                                <div class="input-field">
                                    <input name="month" class="input_m ${requestScope["monthClass"]}" maxlength="2" value="12" placeholder="MM" id="id_month" onkeyup="$(this).val($(this).val().replace(/[^0-9]/g, ''))"/>
                                    <input name="year" class="input_y ${requestScope["monthClass"]}" maxlength="2" value="20" placeholder="YY" id="id_year" onkeyup="$(this).val($(this).val().replace(/[^0-9]/g, ''))"/>
                                </div>
                                <div class="tooltip">
                                    <img src="<%=request.getContextPath()%>/resources/assets_new/image/info/exdate.svg" width="auto" height="100" alt="Expiry Date info" />
                                </div>
                            </div>
                            <div class="sec-field sec-field_3">
                                <label class="${requestScope["codeColor"]}">Word Verification<span class="mandatory">*</span></label>
                                <div class="input-field">
                                    <img id="wordVerifyimg" src="<%=request.getContextPath()%>/CaptchaServlet" class="wv-captchaImg" alt="word-vridfication"/>
                                    <img src="<%=request.getContextPath()%>/resources/assets_new/image/refresh.svg" class="wv-refresh" onclick="captchaRefresh()"/>
                                    <input name="code" class="input_captcha ${requestScope["codeClass"]}" type="text" placeholder="Type here" id="id_code"/>
                                </div>
                            </div>
                        </div>
                        <div class="horizontal-line"></div>
                        <div class="sec-optional">
                            <table id="optional">
                                <%
                                    for (int i = 0; i < merchantAddonFields.size(); i = i + 2) {
                                %> 
                                <tr>
                                    <td>
                                        <div class="input-field">
                                            <input autocomplete="off" id="<%=merchantAddonFields.get(i).getFieldName()%>" name="<%=merchantAddonFields.get(i).getFieldName()%>" class="input_" maxlength="<%=merchantAddonFields.get(i).getFieldSize()%>" placeholder="<%=merchantAddonFields.get(i).getDescription()%>" />
                                        </div>
                                    </td>
                                    <% if (merchantAddonFields.size() > i+1) {%> 
                                    <td>
                                        <div class="input-field">
                                            <input autocomplete="off" id="<%=merchantAddonFields.get(i + 1).getFieldName()%>" name="<%=merchantAddonFields.get(i + 1).getFieldName()%>" class="input_" maxlength="<%=merchantAddonFields.get(i + 1).getFieldSize()%>" placeholder="<%=merchantAddonFields.get(i + 1).getDescription()%>" />
                                        </div>
                                    </td>
                                    <% } %>
                                </tr>
                                <% }%>
                            </table>
                        </div>
                        <div class="sec-button">
                            <form>
                                <input type="button" value="Cancel" name="cancel" onClick="javascript: history.go(-1)" id="ipgBack" class="cancelbtn" />
                            </form>
                            <input type="submit" value="Pay" name="pay" class="paybtn" onclick="this.disabled = true; submit()"/>
                            <div class="caption"><a href="https://www.epictechnology.lk">powered by Epic Lanka (PVT) LTD</a></div>
                        </div>
                    </form>
                    <div class="sec-footer"></div>
                </div>
            </div>
        </div>

        <script src="<%=request.getContextPath()%>/resources/assets_new/js/jquery.min.js"></script>
        <script src="<%=request.getContextPath()%>/resources/assets_new/js/cleave.js"></script>
        <script src="<%=request.getContextPath()%>/resources/assets_new/js/main.js"></script>
    </body>
</html>
