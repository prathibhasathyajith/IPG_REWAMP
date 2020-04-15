<%-- 
    Document   : customerdetails
    Created on : Sep 28, 2018, 3:03:52 PM
    Author     : prathibha_w
--%>

<%@page import="com.epic.ipg.bean.payment.MerchantAddonBean"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <link href="<%=request.getContextPath()%>/resources/assets/css/main.css" rel="stylesheet" />

        <%
            String bytelogo = (String) session.getAttribute("logoPathByte");
            MerchantAddonBean iPGMerchantAddonBean = (MerchantAddonBean) session.getAttribute("addondetail");

            String merchantName = iPGMerchantAddonBean.getMerchantName();

            String displayText = iPGMerchantAddonBean.getDisplayText();
            String themeColor = iPGMerchantAddonBean.getThemeColor();
            String fontFamily = iPGMerchantAddonBean.getFontFamily();
            String fontStyle = iPGMerchantAddonBean.getFontStyle();

        %>
        <script>
            var myColor = "<%=themeColor%>";
        </script>
        <style>
            .cusreq-background{
                background-color: #<%=themeColor%>;
                border-top: 1px solid #<%=themeColor%>;
            }
            label{
                font-size:13px;
                font-family:Raleway;
            }
            legend{
                border-bottom: 3px solid #<%=themeColor%>;
            }
            body ,
            .cusreq-btn , 
            .cusreq-box .cont{
                font-family: <%=fontFamily%>;
            }
            .cusreq-box .cont .name{
                font-style: <%=fontStyle%>;
            }
        </style>
    </head>
    <body class="cusreq">
        <div class="cusreq-mainCont">
            <div class="cusreq-insideBox">
                <div class="cusreq-box">
                    <div class="top">
                        <div class="logo">
                            <img src="data:image/jpeg;base64,<%=bytelogo%>" height="50" width="auto" />
                        </div>
                        <div class="cont">
                            <div class="name"><%=merchantName%></div>
                            <div class="theme"><%=displayText%></div>
                        </div>
                    </div>
                    <div class="inside">
                        <div class="content">
                            <form name="customerDetailsForm" action="${pageContext.request.contextPath}/IPGCustomerRequestServlet" method="POST" >

                                <input value="${requestBean.currencyCode}" name="currencyCode" hidden="true">
                                <input value="<%= (int) (Math.random() * 150)%>" name="txnRefNo" hidden="true">
                                <input  value="${requestBean.amount}" name="amount" hidden="true">
                                
                                <fieldset>
                                    <legend class="">Requested Customer Details</legend>
                                    <table class="cusreq-table">
                                        <tr>
                                            <td><label for="customerName">Customer Name</label></td>
                                            <td>&#x25B8;</td>
                                            <td><input value="${requestBean.customerName}" class="form-control cusreq-input" id="cdName" placeholder="Customer Name" autocomplete="off" readonly="true"></td>
                                        </tr>
                                        <tr>
                                            <td><label for="email">Customer Email</label></td>
                                            <td>&#x25B8;</td>
                                            <td><input value="${requestBean.email}"  class="form-control cusreq-input" id="cdEmail" aria-describedby="emailHelp" placeholder="Enter email" autocomplete="off" readonly="true"></td>
                                        </tr>
                                        <tr>
                                            <td><label for="currency">Currency</label></td>
                                            <td>&#x25B8;</td>
                                            <td><input value="${requestBean.currency}" name="currency" class="form-control cusreq-input" id="cdcurrency" placeholder="Currency" readonly="true"></td>
                                        </tr>
                                        <tr>
                                            <td><label for="amount">Amount</label></td>
                                            <td>&#x25B8;</td>
                                            <td><input value="${requestBean.dispalyAmount}" name="dispalyAmount" class="form-control cusreq-input" id="cdAmount" placeholder="Amount" readonly="true"></td>
                                        </tr>
                                        <tr>
                                            <td><label for="txnID">Order ID</label></td>
                                            <td>&#x25B8;</td>
                                            <td><input value="${requestBean.iPGTransactionRequestId}"  class="form-control cusreq-input" id="cdOrderID" placeholder="Order ID" readonly="true"></td>
                                        </tr>
                                        <tr>
                                            <td><label for="merchantID">Merchant ID</label></td>
                                            <td>&#x25B8;</td>
                                            <td><input value="${requestBean.merchantId}" name="merchantId" class="form-control cusreq-input" id="cdOrderID" placeholder="Merchant ID" readonly="true"></td>
                                        </tr>
                                    </table>
                                    <div class="form-group">
                                        <label for="cardType">Card Type</label><span style="color: red;font-size: 20px;">*</span>
                                        <div class="radio-button">
                                            <div class="radio">
                                                <img src="<%=request.getContextPath()%>/resources/assets/image/cc/american-express.svg" height="20" width="100%" />
                                                <input type="radio" name="cardType" class="selectclass" id="cardType-amex" value="AMEX" />
                                            </div>
                                            <div class="radio">
                                                <img src="<%=request.getContextPath()%>/resources/assets/image/cc/mastercard.svg" height="20" width="100%" />
                                                <input type="radio" name="cardType" class="selectclass" id="cardType-mas" value="MASTER" />
                                            </div>
                                            <div class="radio">
                                                <img src="<%=request.getContextPath()%>/resources/assets/image/cc/visa1.svg" height="20" width="100%" />
                                                <input type="radio" name="cardType" class="selectclass" id="cardType-visa" value="VISA" checked="checked">
                                            </div>
                                        </div>
                                    </div>
                                    <button type="submit" class="cusreq-btn-sub cusreq-btn cusreq-background">SUBMIT</button>
                                </fieldset>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="<%=request.getContextPath()%>/resources/assets/js/jquery.min.js"></script>
        <script src="<%=request.getContextPath()%>/resources/assets/js/main.js"></script>    
    </body>
</html>
