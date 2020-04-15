$(document).ready(function () {

    var errorMsg = $('.content-wrapper > .content > div.error-line')[0].innerHTML;
    console.log(errorMsg);
    if (errorMsg == "") {
        $('.content-wrapper > .content > .error-line').hide();
    } else {
        $('.content-wrapper > .content > .error-line').show();
    }



    $("#master_ca").hide();
    $("#visa_ca").hide();
    $("#amex_ca").hide();
    new Cleave('.cardnumber', {
        creditCard: true,
        onCreditCardTypeChanged: function (type) {
            if (type == 'visa') {
                $("#master_ca").hide();
                $("#amex_ca").hide();
                $("#visa_ca").show();
            } else if (type == 'amex') {
                $("#master_ca").hide();
                $("#visa_ca").hide();
                $("#amex_ca").show();
            } else if (type == 'mastercard') {
                $("#visa_ca").hide();
                $("#amex_ca").hide();
                $("#master_ca").show();
            } else {
                $("#master_ca").hide();
                $("#visa_ca").hide();
                $("#amex_ca").hide();
            }
        }
    });
    //old
    // new Cleave('.input-cardField', {
    //     creditCard: true,
    //     onCreditCardTypeChanged: function (type) {
    //         if (type == 'visa') {
    //             $("#master_ca").hide();
    //             $("#amex_ca").hide();
    //             $("#visa_ca").show();
    //         } else if (type == 'amex') {
    //             $("#master_ca").hide();
    //             $("#visa_ca").hide();
    //             $("#amex_ca").show();
    //         } else if (type == 'mastercard') {
    //             $("#visa_ca").hide();
    //             $("#amex_ca").hide();
    //             $("#master_ca").show();
    //         } else {
    //             $("#master_ca").hide();
    //             $("#visa_ca").hide();
    //             $("#amex_ca").hide();
    //         }
    //     }
    // });


// inside Iframe
    if (inIframe()) {
        // hide sections
//        $('html').css('background-color','white');
//        $('.merchant > .logo').hide();
//        $('.merchant > .logo-bank > img').css({
//            'border-left': '0px solid',
//            'padding-bottom': '10px'
//        });
//        $('.merchant > .name').hide();
//        $('.merchant > .theme').hide();
//        $('.horizontal-line').eq(0).hide();
//        $('.title-details').hide();
//        $('.sec-form > .amount').hide();
//        $('.PGI > .box').css({
//            'width': '100%',
//            'height': '100%',
//            'margin': '0px'
//        });
//        $('.sec-button input').hide();
//        $('.sec-button > .caption').css({
//            'border':'0px solid',
//            'float':'none'
//        });
//        $('.PGI > .box > .content > .sec-form > .sec-button').css({
//            'padding':'2px 35px'
//        })

        // remove sections
        $('html').css('background-color', 'white');
        $('.merchant > .logo').remove();
        $('.merchant > .logo-bank > img').css({
            'border-left': '0px solid',
            'padding-bottom': '10px',
            'color': 'black'
        });
//        $('.content > .title-info').remove();
        $('.content > .title-info').css({
            'background-color': 'white',
            'border-bottom': '0px solid',
            'color':'black'
        });
        $('.merchant > .name').remove();
        $('.merchant > .theme').remove();
        $('.horizontal-line').eq(0).hide();
//        $('.title-details').remove();
        $('.sec-form > .amount').remove();
        $('.PGI > .box').css({
            'width': '100%',
            'height': '100%',
            'margin': '0px'
        });
        $('.sec-button input').remove();
//        $('.sec-button > .caption').css({
//            'border':'0px solid',
//            'float':'none'
//        });
        $('.sec-button > .caption').css({
            'float': 'none',
            'margin': '4px 8px',
            'border-radius': '0px',
            'border-left': '0px solid',
            'border-right': '0px solid',
            'border-top': '0px solid'
        });

        $('.PGI > .box > .content > .sec-form > .sec-button').css({
            'padding': '2px 35px'
        });
        $('.PGI > .box').css({
            'box-shadow': '0 0px 0px 0px #9a9a9a30'
        });
        $('.sec-form > .sec-button').remove();
        $('.sec-form > .sec-footer').remove();

    }

});

//return true if inside an iframe
function inIframe() {
    try {
        return window.self !== window.top;
    } catch (e) {
        return true;
    }
}

