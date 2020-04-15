<%-- 
    Document   : oops
    Created on : Aug 23, 2018, 11:24:49 AM
    Author     : prathibha_w
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<html>

    <head>
        <script src="${pageContext.request.contextPath}/resources/assets/js/jquery.min.js"></script>

        <link href="https://fonts.googleapis.com/css?family=Raleway" rel="stylesheet" type="text/css">

            <style>
                html,
                body {
                    margin: 0;
                    padding: 0;
                    display: table;
                    width: 100%;
                    height: 100%;
                    margin: 0 auto;
                }

                .oops1 {
                    text-align: center;
                    margin: 0 auto;
                    width: 100%;
                    height: 100%;
                    display: table-cell;
                    vertical-align: middle;
                    background: linear-gradient(to right, #f5f5f5 0%, #e4e4e4 100%);
                    padding: 25px;
                    box-sizing: border-box
                }

                .oops2 {
                    margin: 20px;
                    font-family: Raleway;
                    color: #4CAF50;
                    font-weight: bolder;
                    letter-spacing: 2px;
                    font-style: normal;
                    font-size: 30px;
                }

                .oops3 {
                    font-family: Raleway;
                    color: #7f8c8d;
                    font-weight: bolder;
                    letter-spacing: 2px;
                    font-style: italic;
                    font-size: 12px;
                }
                .footer{
                    font-family: Raleway;
                    color: #3F51B5;
                    font-weight: bolder;
                    letter-spacing: 2px;
                    font-size: 10px;
                    margin: 20px;
                }

                .svg {
                    text-align: center;
                    width: 100px;
                    margin: 0 auto;
                }

            </style>
            <script>
                $(document).ready(function () {

                    $(".oops2").hide();
                    $(".oops3").hide();

                    $(".oops2").fadeIn(1000);
                    $(".oops3").fadeIn(1000);


                });

            </script>
    </head>

    <body>
        <div class="oops1">
            <div class="svg">
                <svg id="Layer_1" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 339 339">
                    <title>face</title>
                    <circle cx="169.5" cy="169.5" r="160.5" style="fill:none;stroke:#7f8c8d;stroke-miterlimit:10;stroke-width:18px" />
                    <circle cx="238.5" cy="119.5" r="29.5" style="fill:#7f8c8d" />
                    <circle cx="102.42" cy="119.5" r="29.5" style="fill:#7f8c8d" />
                    <path d="M600,503s19-54,73.5-53S743,503.8,743,503.8" transform="translate(-504 -258)" style="fill:none;stroke:#7f8c8d;stroke-linecap:round;stroke-miterlimit:10;stroke-width:17px" />
                </svg>
            </div>
            <div class="oops2">
                OOPS!
            </div>
            <div class="oops3">
                "Never say 'Oops'.
                <br>Always say 'Ah interesting.'"
            </div>
            <div class="footer">
                EPIC-IPG
            </div>
        </div>

    </body>

</html>
