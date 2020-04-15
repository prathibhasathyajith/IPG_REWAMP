<%-- 
    Document   : merchantaddon_new
    Created on : May 28, 2018, 2:22:57 PM
    Author     : prathibha_s
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
        <link href="<%=request.getContextPath()%>/resources/assets/css/main.css" rel="stylesheet" />
        <link href="<%=request.getContextPath()%>/resources/assets/css/responsive.css" rel="stylesheet" />
        <script src="<%=request.getContextPath()%>/resources/assets/js/jquery.min.js"></script>


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
            var myColor = "<%=themeColor%>";

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

            window.addEventListener('message', function (event) {

                // IMPORTANT: Check the origin of the data! 
                if (~event.origin.indexOf('http://127.0.0.1:52934')) {
                    // The data has been sent from your site 

                    // The data sent with postMessage is stored in event.data 
                    console.log(event.data);
                    if (event.data == 'submit') {
                        $('#form_credit_card_details').submit();
                    }

                } else {
                    // The data hasn't been sent from your site! 
                    // Be careful! Do not use it. 
                    console.log('else');
                    console.log(event.data);
                    return;
                }
            });
        </script>
        <style>
            .ipg-merchant-name-dy{
                font-family: <%=fontFamily%>;
                font-style: <%=fontStyle%>;
                font-size:  <%=fontSize%>px;
            }
            .ipg-merchant-theme-dy{
                font-size:  <%=fontSizeThemeText%>px;
                font-weight: bolder;
                letter-spacing: 1px;
                text-align: center;
            }
            .ipg-main-content-dy{
                border: 2px solid #<%=themeColor%>;
                box-shadow: inset 0 0 0 8px #050a12, 0px 2px 8px 0px rgba(0, 0, 0, 0.15);
            }
            .ipg-horizontal-line-dy{
                /*background-color:#<%=themeColor%>;*/
                background-color:rgba(204, 204, 204, 0.85);
            }
            .ipg-formgroup-button > .ipg-button-dy{
                border: 1px solid #ffffff;
                background: #<%=themeColor%>;
                color: white;
            }
            #wordVerify{display: block}

            .imageIPGnew{
                height: 20px;
                padding: 0px;
                margin: 1px;
                float: left;
            }


        </style>
        <style>
            @media all and (-ms-high-contrast:none)
            {
                #wordVerify{display: block}/* IE10 */
                *::-ms-backdrop, #wordVerify{display: block}/* IE11 */
            }
        </style>

    </head>


    <body onload="sessionTimeOut()">
        <!--=================================================================================-->
        <!--    bank details   -->
        <!--        <div class="ipg-bank-details">
                    <div class="ipg-bank-image" id="ipgBankLogo">
                        <img src="<%=request.getContextPath()%>/resources/assets/image/other/Bank_of_Ceylon.png" alt="Bank Image" />
                        <img src="<%=request.getContextPath()%>/resources/assets/image/other/hnb.png" alt="Bank Image"/>
                    </div>
                                <div class="ipg-bank-name" id="ipgBankName">
                                    Bank of Ceylon
                                    Hatton National Bank
                                
                    </div>
                    <div class="ipg-title">
                        Internet Payment Gateway
                    </div>
                </div>-->
        <!--    end bank details    -->
        <!--=================================================================================-->

        <div class="ipg-main-content ipg-main-content-dy">

            <!--=================================================================================-->
            <!--        merchant details    -->
            <div class="ipg-merchant-details">
                <div class="ipg-merchant-image">
                    <!--<img src="<%=request.getContextPath()%>/resources/assets/image/other/hnb.png" alt="Merchant Image" />-->
                    <!--<img src="data:image/jpeg;base64,<%=bytelogo%>" alt="Merchant Image" />-->
                </div>
                <!--                <div class="ipg-merchant-name-dy">
                <%=merchantName%>
            </div>-->
                <!--                <div class="ipg-merchant-theme-dy ">
                <%=displayText%>
            </div>-->
                <!--<div class="ipg-merchant-other"></div>-->
            </div>
            <!--        end merchant details     -->
            <!--=================================================================================-->

            <!--<div class="ipg-horizontal-line ipg-horizontal-line-dy"></div>-->

            <!--        constant details-->
            <div class="ipg-info">
                <img class="imageMerchntNew" src="data:image/jpeg;base64,<%=bytelogo%>" alt="Merchant Image" />
                <!--<img class="imageMerchntNew" src="<%=request.getContextPath()%>/resources/assets/image/other/Bank_of_Ceylon.png" alt="Merchant Image" />-->
                <div class="ipg-info-1" style="font-size: 15px;">
                    <%=merchantName%>
                </div>
                <div class="ipg-info-1">
                    Enter your credit card details
                </div>
                <!--                <div class="ipg-info-2">
                                    Please make sure you fill in all mandatory infromation marked with *
                                </div>-->
            </div>

            <div class="ipg-horizontal-line ipg-horizontal-line-dy"></div>

            <div class="ipg-txn">
                <div class="ipg-txntext">Your transaction amount with service charges</div>
                <div class="ipg-txnvalue">${sessionScope["currency"]}    ${sessionScope["totalAmount"]}</div>
            </div>

            <!--error/other messages-->
            <div class="ipg-messageBox">
                <label class="ipg-error-label">${displayMessage} </label>
            </div>

            <!--        form        -->
            <form target="_parent" autocomplete="off" name="form1" id="form_credit_card_details" class=""  method="post" action="${pageContext.request.contextPath}/IPGMerchantTransactionServlet">
                <div class="ipg-formFull">

                    <!--            mandatory fields        -->
                    <div class="ipg-form" id="ipg-form-1">
                        <div class="ipg-formgroup">
                            <label class="ipg-label"><span class="star">*</span>Name On Card</label>
                            <span class="ipg-spanclass ipg-spanclass-dy" id="cardName">
                                <input   name="firstname" class="ipg-inputfield ${requestScope["nameClass"]}"  type="text" maxlength="255" value="epic"/>
                            </span>
                        </div>
                        <div class="ipg-formgroup">
                            <label class="ipg-label"><span class="star">*</span>Card Number</label>
                            <span class="ipg-spanclass ipg-spanclass-dy" id="cardNumber">
                                <input id="cardNumber"  name="cardNumber" class="ipg-inputfield ${requestScope["cardClass"]}" type="text" maxlength="19" size="19" value="4216890006499566"/>
                            </span>
                        </div>


                        <% if (session.getAttribute("cardType").equals(visa)) {%>

                        <div class="ipg-formgroup float-new">
                            <label class="ipg-label"><span class="star">*</span>CVV Value</label>
                            <span class="ipg-spanclass ipg-spanclass-dy" id="cvv">
                                <input style="width: 97%;" id="cvcNum"  name="cvcNum" class="ipg-inputfield ${requestScope["cvcClass"]}" id="cvvinput" type="text" maxlength="3" value="123"/>
                            </span>
                        </div>

                        <%}%>
                        <div class="ipg-formgroup">
                            <label class="ipg-label"><span class="star">*</span>Expiry Date</label>
                            <div class="ipg-formgroup-exdate">
                                <input id="month" name="month" class="ipg-inputfield ${requestScope["monthClass"]}" size="2" maxlength="2" value="12" type="text" placeholder="MM"/>/
                                <span class="ipg-spanclass ipg-spanclass-dy" id="exdate">
                                    <input id="year"  name="year" class="ipg-inputfield ${requestScope["monthClass"]}" size="2" maxlength="2" value="20" type="text" placeholder="YY"/>
                                </span>

                            </div>

                        </div>

                    </div>

                    <div class="ipg-horizontal-line ipg-horizontal-line-dy"></div>

                    <!--            not mandatory fields - dynamic fields       -->
                    <div class="ipg-form" id="dynamicDiv">
                        <%
                            for (int i = 0; i < merchantAddonFields.size(); i++) {
                        %> 
                        <div class="ipg-formgroup">
                            <label class="ipg-label"><%=merchantAddonFields.get(i).getDescription()%></label>
                            <input autocomplete="off" id="<%=merchantAddonFields.get(i).getFieldName()%>"  name="<%=merchantAddonFields.get(i).getFieldName()%>" class="ipg-inputfield"  type="text" maxlength="<%=merchantAddonFields.get(i).getFieldSize()%>" value=""/>
                        </div>
                        <% }%>
                    </div>
                    <div class="ipg-form">
                        <div class="ipg-formgroup" id="wordVerify">
                            <label class="ipg-label" id="wordVerifylabel">Word Verification</label></br>
                            <img style="float: left;" id="wordVerifyimg" class="wordveri" src="<%=request.getContextPath()%>/CaptchaServlet" width="100" height="auto" alt="word-vridfication" />
                            <img style=" margin-top: 8px; margin-bottom: 17px;margin-left: 4px;margin-top: 9px;float: left" src="<%=request.getContextPath()%>/resources/assets/image/refresh.png" onclick="captchaRefresh()" alt="Refresh"width="25" height="25"/>

                            <input id="code"  name="code" class="ipg-inputfield-wv ${requestScope["codeClass"]}" type="text" maxlength="20" size="20" value=""/>
                        </div>
                    </div>

                    <!--<div class="ipg-horizontal-line ipg-horizontal-line-dy"></div>-->

                    <!--         button        -->
                    <div class="ipg-form">
                        <div class="ipg-formgroup-button">
                            <img src="<%=request.getContextPath()%>/resources/assets/image/other/hnb.png" alt="Bank Image" class="imageIPGnew"/>
                            <div style="float: left;margin: 6px">Payment Gateway</div>
                            <input type="submit" value="Proceed" class="ipg-button ipg-button-dy" id="ipgProceed" name="proceed" onclick="this.disabled = true;
                                    submit()"/>
                            <form>
                                <input type="button" value="Back" onClick="javascript: history.go(-1)" class="ipg-button ipg-button-dy" id="ipgBack"/>
                            </form>
                        </div>
                    </div>

                </div>
            </form>
            <!--        end form    -->

        </div>
        <div class="ipg-footer">
            IPG powered by Epic Lanka (Pvt) Ltd.
        </div>

        <!--    modal-->
        <div class="ipg-infomodal">
            <div class="ipg-overlay"></div>
            <div class="ipg-modalcont">
                <div class="ipg-modal-title"></div>
                <img class="ipg-info-image" />
                <div class="ipg-info-text"></div>
            </div>
        </div>


        <script src="<%=request.getContextPath()%>/resources/assets/js/main.js"></script>

    </body>

</html>