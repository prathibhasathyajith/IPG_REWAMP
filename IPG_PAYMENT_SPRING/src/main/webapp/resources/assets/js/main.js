function ColorLuminance(hex, lum) {

    // validate hex string
    hex = String(hex).replace(/[^0-9a-f]/gi, '');
    if (hex.length < 6) {
        hex = hex[0] + hex[0] + hex[1] + hex[1] + hex[2] + hex[2];
    }
    lum = lum || 0;

    // convert to decimal and change luminosity
    var rgb = "#", c, i;
    for (i = 0; i < 3; i++) {
        c = parseInt(hex.substr(i * 2, 2), 16);
        c = Math.round(Math.min(Math.max(0, c + (c * lum)), 255)).toString(16);
        rgb += ("00" + c).substr(c.length);
    }

    return rgb;
}
function invertColor(hex, bw) {
    if (hex.indexOf('#') === 0) {
        hex = hex.slice(1);
    }
    // convert 3-digit hex to 6-digits.
    if (hex.length === 3) {
        hex = hex[0] + hex[0] + hex[1] + hex[1] + hex[2] + hex[2];
    }
    if (hex.length !== 6) {
        throw new Error('Invalid HEX color.');
    }
    var r = parseInt(hex.slice(0, 2), 16),
        g = parseInt(hex.slice(2, 4), 16),
        b = parseInt(hex.slice(4, 6), 16);
    if (bw) {
        return (r * 0.299 + g * 0.587 + b * 0.114) > 186
            ? '#000000'
            : '#FFFFFF';
    }
    // invert color components
    r = (255 - r).toString(16);
    g = (255 - g).toString(16);
    b = (255 - b).toString(16);
    // pad each with zeros and return
    return "#" + padZero(r) + padZero(g) + padZero(b);
}

function padZero(str, len) {
    len = len || 2;
    var zeros = new Array(len).join('0');
    return (zeros + str).slice(-len);
}
$(document).ready(function (e) {

    $(".ipg-inputfield").click(function (e) {
        e.stopPropagation();
    });
    $(".ipg-overlay").click(function () {
        $(".ipg-modalcont").fadeOut(100);
        $(".ipg-overlay").fadeOut(100);
    });
    $(".ipg-modalcont").click(function () {
        $(".ipg-modalcont").fadeOut(100);
        $(".ipg-overlay").fadeOut(100);
    });


    // card name info
    $("#cardName").click(function () {
        $(".ipg-modal-title").text("Card Name");
        $(".ipg-info-image").attr({"src": "resources/assets/image/info/cardname.png", "alt": "Card Name"});
        $(".ipg-info-text").text("Card Name information text");
        $(".ipg-modalcont").fadeIn(100);
        $(".ipg-overlay").fadeIn(100);
    });
    // card number info
    $("#cardNumber").click(function () {
        $(".ipg-modal-title").text("Card Number");
        $(".ipg-info-image").attr({"src": "resources/assets/image/info/cardnumber.png", "alt": "Card Number"});
        $(".ipg-info-text").text("Card Number information text");
        $(".ipg-modalcont").fadeIn(100);
        $(".ipg-overlay").fadeIn(100);
    });
    // cvv info
    $("#cvv").click(function () {
        $(".ipg-modal-title").text("CVV Value");
        $(".ipg-info-image").attr({"src": "resources/assets/image/info/cvv.png", "alt": "CVV"});
        $(".ipg-info-text").text("CVV value information text");
        $(".ipg-modalcont").fadeIn(100);
        $(".ipg-overlay").fadeIn(100);
    });
    // expiry date info
    $("#exdate").click(function () {
        $(".ipg-modal-title").text("Expiry Date");
        $(".ipg-info-image").attr({"src": "resources/assets/image/info/expirydate.png", "alt": "Expiry Date"});
        $(".ipg-info-text").text("Expiry Date information text");
        $(".ipg-modalcont").fadeIn(100);
        $(".ipg-overlay").fadeIn(100);
    });
    // proceed button click event
    $("#ipgProceed").click(function () {
//        alert("proceed");
    });
    // back button click event
    $("#ipgBack").click(function () {
//        alert("back");
    });

//    $(".dy-merchant-name").css("color", ColorLuminance(myColor, -0.6));
    $(".ipg-merchant-name-dy").css("color", invertColor(myColor,true));
    $(".ipg-merchant-theme-dy").css("color", ColorLuminance(myColor, -0.6));
    $(".ipg-formgroup-button > .ipg-button-dy").css({'border':'1px solid '+invertColor(myColor,true),'color':invertColor(myColor,true)});
    $(".ipg-main-content-dy").css({'box-shadow': 'inset 0 0 0 2px '+ColorLuminance(myColor, -0.6)+', 0px 2px 8px 0px rgba(0, 0, 0, 0.15)'});


// customer details

    $("legend").css("color", invertColor(ColorLuminance(myColor, -0.6),true));
    $("legend").css("background", ColorLuminance(myColor, -0.6));
    $(".cusreq-btn").css("color", invertColor(myColor,true));
    $(".cusreq-box .cont .theme").css("color", ColorLuminance(myColor, -0.6));
    
    
    $(".radio").click(function (){
        console.log($(this).children().eq(1));
        $("#cardType-visa").prop("checked",false);
        $("#cardType-mas").prop("checked",false);
        $("#cardType-amex").prop("checked",false);
        $(this).children().eq(1).prop("checked",true);
    });
});
//document.getElementsByClassName("dy-merchant-name")[0].style.color = ColorLuminance(myColor,-0.5);