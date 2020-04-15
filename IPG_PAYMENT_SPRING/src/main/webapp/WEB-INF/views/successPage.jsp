<%-- 
    Document   : successPage_new
    Created on : Jun 11, 2018, 12:11:38 PM
    Author     : prathibha_s
--%>
<%@page import="com.epic.ipg.bean.payment.MerchantResponseBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>

    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="<%=request.getContextPath()%>/resources/assets/css/main.css" rel="stylesheet" />
        <link href="<%=request.getContextPath()%>/resources/assets/css/responsive.css" rel="stylesheet" />
        <script src="<%=request.getContextPath()%>/resources/assets/js/jquery.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/addonjs/custom-form-elements.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/addonjs/wufoo.js"></script>
        <script language=\"javascript\">
            function autoSubmit() {
                document.form1.submit();
            }
                       
        </script>
    </head>

    <body>
        <!--=================================================================================-->
        <!--    bank details   -->
        <div class="ipg-bank-details">
            <div class="ipg-bank-image" id="ipgBankLogo">
                <!--<img src="<%=request.getContextPath()%>/resources/assets/image/other/Bank_of_Ceylon.png" alt="Bank Image" />-->
                <img src="<%=request.getContextPath()%>/resources/assets/image/other/hnb.png" alt="Bank Image" />
            </div>
            <div class="ipg-bank-name" id="ipgBankName">
                Hatton National Bank
            </div>
            <div class="ipg-title">
                Internet Payment Gateway
            </div>
        </div>
        <!--    end bank details    -->
        <!--=================================================================================-->

        <div class="ipg-main-content-s">

            <!--=================================================================================-->
            <!--        merchant details    -->
            <div class="ipg-merchant-details">
                <div class="ipg-merchant-image">
                    <img src="<%=request.getContextPath()%>/resources/assets/image/other/nike-logo-47A65A59D5-seeklogo.com.png" alt="Merchant Image" />
                </div>
                <div class="ipg-merchant-name">
                    Nike (Pvt) Ltd
                </div>
                <div class="ipg-merchant-theme">
                    Just Do It.
                </div>
                <div class="ipg-merchant-other"></div>
            </div>
            <!--        end merchant details     -->
            <!--=================================================================================-->



            <div class="ipg-horizontal-line-s"></div>

            <!--==============payment receipt================-->
            <div class="ipg-payment-recepit-main">
                <div class="ipg-payrec-title">Payment Receipt</div>
                <div class="ipg-horizontal-line-s"></div>


                <%

                    //List merList = (List) request.getAttribute("merList");
//                    List lst = (List) request.getAttribute("lst");
                    String message = request.getAttribute("message").toString();
                    String txnId = request.getAttribute("txnId").toString();
                    MerchantResponseBean responseBean = (MerchantResponseBean) request.getAttribute("responseBean");
                    String isSuccessTxn = request.getAttribute("isSuccessTxn").toString();

                    //remove due to hold the success or error page
                    out.print("<script language=\"javascript\">function autoSubmit(){setTimeout(function(){document.form1.submit();},15000);}</script>");
                    out.print("</head>");

                    out.print("<body onload=\"autoSubmit();\">");

                    out.print("<div class='ipg-payrec-details'>");
                    //out.print("<div class='ipg-payrec-msg '>" + message + "</div>");

                    //table
                    out.print("<table class='ipg-payrec-table'>");
                    out.print("<tbody>");

                    out.print("<tr>");
                    out.print("<td>Payment Status</td>");
                    out.print("<td>:</td>");
                    out.print("<td>" + message + "</td>");
                    out.print("</tr>");

                    out.print("<tr>");
                    out.print("<td>Payment Amount </td>");
                    out.print("<td>:</td>");
                    out.print("<td>" + responseBean.getCurrencyCode() + " " + responseBean.getAmount() + "</td>");
                    out.print("</tr>");

                    out.print("<tr>");
                    out.print("<td>Account No</td>");
                    out.print("<td>:</td>");
                    out.print("<td>" + responseBean.getAccountNo() + "</td>");
                    out.print("</tr>");

                    out.print("<tr>");
                    out.print("<td>Payment Reference</td>");
                    out.print("<td>:</td>");
                    out.print("<td>" + txnId + "</td>");
                    out.print("</tr>");

                    out.print("<tr>");
                    out.print("<td>Date Time</td>");
                    out.print("<td>:</td>");
                    out.print("<td>" + responseBean.getTxnDateTime() + "</td>");
                    out.print("</tr>");

                    out.print("</tbody></table>");

                    out.print("<div class='ipg-horizontal-line-s'></div>");
                    out.print("<div class='ipg-payrec-rederect'>Redirecting to the Merchant page</div>");
                    out.print("</div></div>");

                    out.print("<form id=\"form1\" method=\"post\" name=\"form1\" action=\"" + request.getContextPath() + "/returnToMerchant\"/>");

                    out.print("<input type=\"hidden\" name=\"maresponse\" value=\"" + message + "\" />");
                    out.print("<input type=\"hidden\" name=\"txnid\" value=\"" + txnId + "\" />");
                    out.print("<input type=\"hidden\" name=\"successReturnUrl\" value=\"" + responseBean.getDinamicReturnSuccessURL() + "\" />");
                    out.print("<input type=\"hidden\" name=\"errorReturnUrl\" value=\"" + responseBean.getDinamicReturnErrorURL() + "\" />");
                    out.print("<input type=\"hidden\" name=\"txnRefNo\" value=\"" + responseBean.getAccountNo() + "\" />");
                    out.print("<input type=\"hidden\" name=\"isSuccessTxn\" value=\"" + isSuccessTxn + "\" />");

                    out.print("</form>");

                %>

            </div>


        </div>
        <div class="ipg-footer-s">
            IPG powered by Epic Lanka (Pvt) Ltd.
        </div>

        <script src="<%=request.getContextPath()%>/resources/assets/js/main.js"></script>
    </body>

</html>
