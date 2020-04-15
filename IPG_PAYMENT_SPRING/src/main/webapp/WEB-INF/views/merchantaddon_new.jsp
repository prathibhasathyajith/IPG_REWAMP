<%--
  Created by IntelliJ IDEA.
  User: prathibha_w
  Date: 4/6/2020
  Time: 5:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.epic.ipg.bean.payment.AddonFieldsBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.epic.ipg.bean.payment.MerchantAddonBean"%>
<%@page import="com.epic.ipg.util.varlist.CardAssociationVarList"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<title>EpicPay Internet Payment Gateway </title>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"></meta>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/assets_v1/styles.css">
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
        .m-theme-text {
            color: #<%=themeColor%>;
        }

        .m-theme-background {
            background: #<%=themeColor%>;
        }

        .m-theme-border {
            border: solid 2px #<%=themeColor%>;
        }

        .m-theme-input {
            caret-color: #<%=themeColor%>;
        }

        .m-theme-input:focus {
            border: solid 2px #<%=themeColor%> !important;
        }
    </style>

</head>

<body onload="sessionTimeOut()">
<!-- action bar for mobile and tab views -->
<div class="actionbar m-theme-background">
    <div class="back"><img src="<%=request.getContextPath()%>/resources/assets_v1/images/back.svg" alt="back" onClick="javascript: history.go(-1)"></div>
    <div class="title">Complete Payment</div>
</div>
<div class="content-wrapper">
    <div class="header">
        <div class="merchant-logo">
            <img src="data:image/jpeg;base64,<%=bytelogo%>" />
            <div class="title"><%=merchantName%></div>
        </div>
        <img src="<%=request.getContextPath()%>/resources/assets_v1/images/bank_logo.svg" class="bank-logo">
    </div>
    <div class="content">
        <div class="error-line">${displayMessage}</div>
        <div class="form-container">
            <div class="title">Card Details</div>
            <div class="amount">
                <div class="label">Amount</div>
                <div class="wrapper m-theme-background">
                    ${sessionScope["currency"]}<span>${sessionScope["totalAmount"]}</span>
                </div>
            </div>
            <form class="card-detail-form" target="_self" sandbox="allow-top-navigation allow-scripts allow-forms" autocomplete="off" name="form1" id="form_credit_card_details"  method="post" action="${pageContext.request.contextPath}/IPGMerchantTransactionServlet">
                <div class="input-row">
                    <label class="with-info" >Cardholder Name</label>
                    <div class="info-tip">
                        <div class="info-icon m-theme-background">
                            <img src="<%=request.getContextPath()%>/resources/assets_v1/images/info-icon.svg" class="icon">
                        </div>
                        <img src="<%=request.getContextPath()%>/resources/assets_v1/images/tip-cardname.svg" class="tip">
                    </div><br>
                    <input class="m-theme-input cardname input-width ${requestScope["nameClass"]}" type="text" name="firstname" value="epic" id="id_firstname" placeholder="Jhon Doe" maxlength="22" size="24">
                </div>

                <div class="input-row">
                    <label class="with-info" >Card Number</label>
                    <div class="info-tip">
                        <div class="info-icon m-theme-background">
                            <img src="<%=request.getContextPath()%>/resources/assets_v1/images/info-icon.svg" class="icon">
                        </div>
                        <img src="<%=request.getContextPath()%>/resources/assets_v1/images/tip-cardnumber.svg" class="tip">
                    </div><br>
                    <input class="m-theme-input cardnumber input-width ${requestScope["cardClass"]}" type="text" id="card-number" name="cardNumber" placeholder="xxxx xxxx xxxx xxxx" maxlength="19" size="19" value="4216890006499566" id="id_cardNumber" >
                    <img src="<%=request.getContextPath()%>/resources/assets_v1/images/visa-logo.png" class="card-logo" style="display: none" id="visa_ca">
                    <img src="<%=request.getContextPath()%>/resources/assets_v1/images/american-express.svg" class="card-logo" style="display: none" id="amex_ca">
                    <img src="<%=request.getContextPath()%>/resources/assets_v1/images/mastercard.svg" class="card-logo" style="display: none" id="master_ca">
                </div>

                <div class="input-row card-expiry">
                    <label class="with-info" >Card Expiry</label>
                    <div class="info-tip">
                        <div class="info-icon m-theme-background">
                            <img src="<%=request.getContextPath()%>/resources/assets_v1/images/info-icon.svg" class="icon">
                        </div>
                        <img src="<%=request.getContextPath()%>/resources/assets_v1/images/tip-cardexpiry.svg" class="tip">
                    </div><br>
                    <select id="id_month" onchange="optionSelect(this)" name="month" class="${requestScope["monthClass"]}">
                        <option value="" selected hidden>month</option>
                        <option value="01">01</option>
                        <option value="02">02</option>
                        <option value="03">03</option>
                        <option value="04">04</option>
                        <option value="05">05</option>
                        <option value="06">06</option>
                        <option value="07">07</option>
                        <option value="08">08</option>
                        <option value="09">09</option>
                        <option value="10">10</option>
                        <option value="11">11</option>
                        <option value="12">12</option>
                    </select>
                    <select id="id_year" onchange="optionSelect(this)" name="year" class="${requestScope["monthClass"]}">
                        <option value="" selected hidden>year</option>
                        <option value="20">2020</option>
                        <option value="21">2021</option>
                        <option value="22">2022</option>
                        <option value="23">2023</option>
                        <option value="24">2024</option>
                        <option value="25">2025</option>
                        <option value="26">2026</option>
                        <option value="27">2027</option>
                    </select>
                </div>
                <div class="input-row">
                    <label class="with-info" >CVV</label>
                    <div class="info-tip">
                        <div class="info-icon m-theme-background">
                            <img src="<%=request.getContextPath()%>/resources/assets_v1/images/info-icon.svg" class="icon">
                        </div>
                        <img src="<%=request.getContextPath()%>/resources/assets_v1/images/tip-cardcvv.svg" class="tip">
                    </div><br>
                    <input class="m-theme-input cvv ${requestScope["cvcClass"]}" type="text" name="cvcNum" id="id_cvcNum" onkeyup="$(this).val($(this).val().replace(/[^0-9]/g, ''))" placeholder="123" maxlength="3" size="8">
                </div>
                <div class="input-row captcha">
                    <label>Word Verification
                    </label><br>
                    <input class="m-theme-input captcha ${requestScope["codeClass"]}" type="text" id="id_code" name="code" placeholder="Type Here" size="10">
                    <div class="captcha"></div>
                    <img src="" class="reload-icon">
                    <div class="captcha-wrapper">
                        <img id="wordVerifyimg" src="<%=request.getContextPath()%>/CaptchaServlet" alt="word-vridfication">
                    </div>
                    <div class="captcha-refresh">
                        <img src="<%=request.getContextPath()%>/resources/assets_v1/images/captcha-refresh.svg" alt="" onclick="captchaRefresh()">
                    </div>
                </div>
                <div class="seperator"></div>
                <%
                    for (int i = 0; i < merchantAddonFields.size(); i = i + 2) {
                %>
                <div class="input-row mobile">
                    <label><%=merchantAddonFields.get(i).getDescription()%>
                    </label><br>
                    <input class="m-theme-input mobile" type="text" name="<%=merchantAddonFields.get(i).getFieldName()%>" id="<%=merchantAddonFields.get(i).getFieldName()%>" name="" placeholder="<%=merchantAddonFields.get(i).getDescription()%>" maxlength="<%=merchantAddonFields.get(i).getFieldSize()%>">
                </div>
                <% if (merchantAddonFields.size() > i+1) {%>
                <div class="input-row">
                    <label ><%=merchantAddonFields.get(i+1).getDescription()%>
                    </label><br>
                    <input class="m-theme-input email" type="text" name="<%=merchantAddonFields.get(i + 1).getFieldName()%>" id="<%=merchantAddonFields.get(i + 1).getFieldName()%>" maxlength="<%=merchantAddonFields.get(i + 1).getFieldSize()%>" placeholder="<%=merchantAddonFields.get(i + 1).getDescription()%>">
                </div>
                <% } %>
                <% }%>
                <div class="button-wrapper">
                    <form>
                        <input type="button" value="Cancel" name="cancel" onClick="javascript: history.go(-1)" id="ipgBack" class="secondary-btn" />
                    </form>
                    <input type="submit" value="Pay Now" name="pay" class="primary-btn m-theme-text m-theme-border" onclick="this.disabled = true; submit()"/>
                </div>
            </form>
        </div>
    </div>
    <div class="footer">
        <img src="<%=request.getContextPath()%>/resources/assets_v1/images/bank_logo.svg" class="bank-logo">
        <img src="<%=request.getContextPath()%>/resources/assets_v1/images/epic-pay-logo.svg" alt="" class="epicpay-logo">
    </div>
</div>
</body>
<script src="<%=request.getContextPath()%>/resources/assets_new/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/assets_new/js/cleave.js"></script>
<script src="<%=request.getContextPath()%>/resources/assets_new/js/main.js"></script>
<script>
    function optionSelect(element) {
        var selectedValue = document.getElementById(element.id).value;
        if (selectedValue != "") {
            document.getElementById(element.id).style.color = "#333333"
        }
    }
</script>

</html>
