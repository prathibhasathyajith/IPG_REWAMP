<%-- 
    Document   : customerdetails_new
    Created on : Apr 9, 2020, 10:43:12 AM
    Author     : prathibha_w
--%>

<%@page import="com.epic.ipg.bean.payment.MerchantAddonBean"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/assets_v1/styles.css">
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

        .container.selected {
            border: 2px solid #<%=themeColor%> !important;
        }
    </style>

</head>

<body>
    <!-- action bar for mobile and tab views -->
    <div class="actionbar m-theme-background">
        <div class="back"><img src="<%=request.getContextPath()%>/resources/assets_v1/images/back.svg" alt="back"></div>
        <div class="title">Requested Payment</div>
    </div>
    <div class="content-wrapper">
        <div class="header">
            <div class="merchant-logo">
                <img src="data:image/jpeg;base64,<%=bytelogo%>">
                <div class="title"><%=merchantName%></div>
            </div>

            <img src="<%=request.getContextPath()%>/resources/assets_v1/images/bank_logo.svg" class="bank-logo">
        </div>
        <div class="content">
            <form class="request-detail-form" name="customerDetailsForm" action="${pageContext.request.contextPath}/IPGCustomerRequestServlet" method="POST" >

                <input value="${requestBean.currencyCode}" name="currencyCode" hidden="true">
                <input value="<%= (int) (Math.random() * 150)%>" name="txnRefNo" hidden="true">
                <input  value="${requestBean.amount}" name="amount" hidden="true">


                <div class="receipt-container">
                    <div class="title">Requested Payment Details</div>
                    <div class="receipt-content m-theme-border">
                        <table>
                            <tr>
                                <td class="label">Customer Name </td>
                                <td class="connector">
                                    <div></div>
                                </td>
                                <td class="detail"><input value="${requestBean.customerName}" class="" id="cdName" placeholder="Customer Name" autocomplete="off" readonly="true"></td>
                            </tr>
                        </table>
                        <table>
                            <tr>
                                <td class="label">Customer Email</td>
                                <td class="connector">
                                    <div></div>
                                </td>
                                <td class="detail"><input value="${requestBean.email}"  class="" id="cdEmail" aria-describedby="emailHelp" placeholder="Enter email" autocomplete="off" readonly="true"></td>
                            </tr>
                        </table>
                        <table>
                            <tr>
                                <td class="label">Currency</td>
                                <td class="connector">
                                    <div></div>
                                </td>
                                <td class="detail"><input value="${requestBean.currency}" name="currency" class="" id="cdcurrency" placeholder="Currency" readonly="true"></td>
                            </tr>
                        </table>
                        <table>
                            <tr>
                                <td class="label">Amount</td>
                                <td class="connector">
                                    <div></div>
                                </td>
                                <td class="detail"><input value="${requestBean.dispalyAmount}" name="dispalyAmount" class="" id="cdAmount" placeholder="Amount" readonly="true"></td>
                            </tr>
                        </table>
                        <table>
                            <tr>
                                <td class="label">Order ID</td>
                                <td class="connector">
                                    <div></div>
                                </td>
                                <td class="detail"><input value="${requestBean.iPGTransactionRequestId}"  class="" id="cdOrderID" placeholder="Order ID" readonly="true"></td>
                            </tr>
                        </table>
                        <table>
                            <tr>
                                <td class="label">Merchant ID</td>
                                <td class="connector">
                                    <div></div>
                                </td>
                                <td class="detail"><input value="${requestBean.merchantId}" name="merchantId" class="" id="cdOrderID" placeholder="Merchant ID" readonly="true"></td>
                            </tr>
                        </table>
                    </div>

                    <div class="card-type">
                        <div class="instruction">Select card type for this payment</div>
                        <div class="card first" id="VISA">
                            <div class="container selected">
                                <img class="card-logo" src="<%=request.getContextPath()%>/resources/assets_v1/images/visa.svg" alt="">
                                <div class="label">Visa Card</div>
                            </div>
                        </div>
                        <div class="card second" id="MASTER">
                            <div class="container">
                                <img class="card-logo" src="<%=request.getContextPath()%>/resources/assets_v1/images/mastercard.svg" alt="">
                                <div class="label">Master Card</div>
                            </div>
                        </div>
                        <div class="card third" id="AMEX">
                            <div class="container">
                                <img class="card-logo" src="<%=request.getContextPath()%>/resources/assets_v1/images/american-express.svg" alt="">
                                <div class="label">Amex Card</div>
                            </div>
                        </div>
                    </div>
                                
                    <input type="hidden" name="cardType" id="cardType" value="VISA"/>

                    <div class="button-wrapper">
                        <button type="submit" class="primary-btn m-theme-text m-theme-border">Proceed</button>
                    </div>

                </div>
            </form>
        </div>
        <div class="footer">
            <img src="<%=request.getContextPath()%>/resources/assets_v1/images/bank_logo.svg" class="bank-logo">
            <img src="<%=request.getContextPath()%>/resources/assets_v1/images/epic-pay-logo.svg" alt="" class="epicpay-logo">
        </div>
    </div>
</body>
<script src="<%=request.getContextPath()%>/resources/assets_new/js/jquery.min.js"></script>
<script>
      $(document).ready(function() {

        $('.card-type > .card > .container').click(function(e) {
            $('.card-type > .card > .container').removeClass('selected');
            this.className += ' selected';
            $('#cardType').val(this.parentElement.id);
        })


    });
</script>

</html>