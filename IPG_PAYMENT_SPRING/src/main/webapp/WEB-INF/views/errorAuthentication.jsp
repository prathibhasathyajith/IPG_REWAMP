<%--
    Document   : errorpage
    Created on : Jul 6, 2010, 9:24:47 AM
    Author     : upul
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252" />
        <title>EPIC IPG Merchant Addon</title>
        <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/addonjs/custom-form-elements.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/addonjs/wufoo.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/style1.css" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/form.css" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/theme.css" />
        <script>

            function sessionTimeOut() {
                setTimeout(function () {
                    document.form1.action = "${pageContext.request.contextPath}/processPaymentTimeout";
                    document.form1.submit();
                }, 3000);

            }
        </script>
    </head>
    <body onload="sessionTimeOut()">

        <div id="main_container">

            <div id="header">
                <div class="logo"><img src="<%=request.getContextPath()%>/resources/img/addonImages/new/banklogohnb.png" alt="" title="" border="0" width="150" height="auto"/></div>
            </div>
            <div id="main_content">
                <div class="center_content">

                    <form name="form1" id="form_credit_card_details" class="" 
                          method="post" 
                          action="${pageContext.request.contextPath}/IPGMerchantTransactionServlet"></form>



                    <div class="new_products">
                        <div class="prod_box">
                            <div id="form_container">

                                <%
                                    String errorMessage = request.getAttribute("errorMessage").toString();

                                    out.print("<form id=\"form_208353\" class=\"appnitro\" enctype=\"multipart/form-data\" method=\"post\" action=\"\">");
                                    out.print("<div class=\"form_description\">");
                                    out.print("<h2>Sorry </h2>");
                                    out.print("<p>" + errorMessage + "</p>");
                                    out.print("</div>");
                                    out.print("</form>");
                                %>
                            </div>
                        </div>
                        <div class="clear"></div>
                    </div>
                </div>
            </div>

            <div id="footer">
                <div class="right_footer">© EPIC Lanka Technologies 2018</div>
            </div>

        </div>
    </body>
</html>