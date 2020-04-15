<%--
  Created by IntelliJ IDEA.
  User: prathibha_w
  Date: 4/8/2020
  Time: 8:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page import="com.epic.ipg.bean.payment.MerchantResponseBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="com.epic.ipg.bean.payment.MerchantAddonBean"%>
<html>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="<%=request.getContextPath()%>/resources/assets_v1/styles.css" rel="stylesheet" />
    <script src="<%=request.getContextPath()%>/resources/assets/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/addonjs/custom-form-elements.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/addonjs/wufoo.js"></script>
    <script language=\"javascript\">
            function autoSubmit() {
                document.form1.submit();
            }

        </script>
    <%
        String bytelogo = (String) session.getAttribute("logoPathByte");
        MerchantAddonBean iPGMerchantAddonBean = (MerchantAddonBean) session.getAttribute("addondetail");
        String merchantName = iPGMerchantAddonBean.getMerchantName();
    %>
    <style>
        .m-theme-text {
            color: #20D2B2;
        }

        .m-theme-background {
            background: #20D2B2;
        }

        .m-theme-border {
            border: solid 2px #20D2B2;
        }

        .m-theme-input {
            caret-color: #20D2B2;
        }

        .m-theme-input:focus {
            border: solid 2px #20D2B2 !important;
        }
    </style>
</head>

<body>
<!-- action bar for mobile and tab views -->
<div class="actionbar m-theme-background">
    <div class="back"><img src="<%=request.getContextPath()%>/resources/assets_v1/images/back.svg" alt="back"></div>
    <div class="title">Payment Receipt</div>
</div>
<div class="content-wrapper">
    <div class="header">
        <div class="merchant-logo">
            <img src="data:image/jpeg;base64,<%=bytelogo%>" />
<%--            <img src="<%=request.getContextPath()%>/resources/assets_v1/images/merchant_logo.png">--%>
            <div class="title"><%=merchantName%></div>
        </div>

        <img src="<%=request.getContextPath()%>/resources/assets_v1/images/bank_logo.svg" class="bank-logo">
    </div>
    <div class="content">
        <div class="receipt-container">
            <div class="title">Payment Receipt</div>

            <%
                String message = request.getAttribute("message").toString();
                String txnId = request.getAttribute("txnId").toString();
                MerchantResponseBean responseBean = (MerchantResponseBean) request.getAttribute("responseBean");
                String isSuccessTxn = request.getAttribute("isSuccessTxn").toString();

                String paymetMsg = "Payment Successful";
                String successPath = "/resources/assets_v1/images/sucess-icon.svg";
                String emailMsg = "Email sent successfully";
                if(isSuccessTxn == "false") {
                    paymetMsg = "Payment Failed";
                    successPath = "/resources/assets_v1/images/error-icon.svg";
                    emailMsg = "Email sending Failed";
                }

                //remove due to hold the success or error page
                out.print("<script language=\"javascript\">function autoSubmit(){setTimeout(function(){document.form1.submit();},15000);}</script>");
                out.print("</head>");

                out.print("<body onload=\"autoSubmit();\">");

                String amount = responseBean.getCurrencyCode() + " " + responseBean.getAmount();
                String account = responseBean.getAccountNo();
                String date = responseBean.getTxnDateTime();



            %>
            <div class="receipt-content m-theme-border">
                <img src="<%=request.getContextPath()%>/resources/assets_v1/images/merchant_logo.png" alt="" class="merchant-logo">
                <div class="status"><img src="<%=request.getContextPath()%><%=successPath%>" alt=""><span><%=paymetMsg%></span></div>
                <table>
                    <tr>
                        <td class="label">Amount</td>
                        <td class="connector">
                            <div></div>
                        </td>
                        <td class="detail"><%=amount%></td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td class="label">Account No.</td>
                        <td class="connector">
                            <div></div>
                        </td>
                        <td class="detail"><%=account%></td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td class="label">Reference No.</td>
                        <td class="connector">
                            <div></div>
                        </td>
                        <td class="detail"><%=txnId%></td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td class="label">Date & Time</td>
                        <td class="connector">
                            <div></div>
                        </td>
                        <td class="detail"><%=date%></td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td class="label">Reciept Delivery</td>
                        <td class="connector">
                            <div></div>
                        </td>
                        <td class="detail"><%=emailMsg%></td>
                    </tr>
                </table>

            </div>
            <%
                out.print("<form id=\"form1\" method=\"post\" name=\"form1\" action=\"" + request.getContextPath() + "/returnToMerchant\"/>");

                out.print("<input type=\"hidden\" name=\"maresponse\" value=\"" + message + "\" />");
                out.print("<input type=\"hidden\" name=\"txnid\" value=\"" + txnId + "\" />");
                out.print("<input type=\"hidden\" name=\"successReturnUrl\" value=\"" + responseBean.getDinamicReturnSuccessURL() + "\" />");
                out.print("<input type=\"hidden\" name=\"errorReturnUrl\" value=\"" + responseBean.getDinamicReturnErrorURL() + "\" />");
                out.print("<input type=\"hidden\" name=\"txnRefNo\" value=\"" + responseBean.getAccountNo() + "\" />");
                out.print("<input type=\"hidden\" name=\"isSuccessTxn\" value=\"" + isSuccessTxn + "\" />");

                out.print("</form>");
            %>
            <div class="redirecting-message">Redirecting to the merchant page in 10 seconds..</div>
        </div>
    </div>
    <div class="footer">
        <img src="<%=request.getContextPath()%>/resources/assets_v1/images/bank_logo.svg" class="bank-logo">
        <img src="<%=request.getContextPath()%>/resources/assets_v1/images/epic-pay-logo.svg" alt="" class="epicpay-logo">
    </div>
</div>
</body>
</html>
