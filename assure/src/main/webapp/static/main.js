const base_url = "http://localhost:9000/pos";
const MAX_LENGTH = 30;

function highlightItem(head_name) {
    let elements = document.getElementsByClassName('nav-link');
    Array.from(elements).forEach(element => {
        console.log(element.className);
        console.log(element);
        if (element.text == head_name) {
            element.classList.add("active");
        } else {
            element.classList.remove("active");
        }
    });
}

function showSuccessToast(message) {
    $('#infoToast').toast({ autohide: true, delay: 3000 });
    $("#infoToast").css('background-color', 'lightgreen');
    $("#infoToast .toast-body span").text(message);
    $("#infoToast .toast-body span").css("fontSize", "20px");
    $('#infoToast').toast('show');
}

function showErrorToast(message) {
    $('#infoToast').toast({ autohide: false });
    $("#infoToast").css('background-color', 'red');
    $("#infoToast .toast-body span").text(message);
    $("#infoToast .toast-body span").css("fontSize", "16px");
    $('#infoToast').toast('show');
}

function isNonNegInt(num) {
    num = String(num);
    const DIGIT_EXPRESSION = /^\d$/;
    const isDigit = (character) => {
        return character && DIGIT_EXPRESSION.test(character);
    };
    for (var i = 0; i < num.length; i++) {
        if (!isDigit(num.charAt(i))) {
            return false;
        }
    }
    return true;
}

function isNonNegNum(num) {
    num = String(num);
    if(num.length === 0) {
        return false;
    }
    var cnt = 0;
    const DIGIT_EXPRESSION = /^\d$/;
    const isDigit = (character) => {
        return character && DIGIT_EXPRESSION.test(character);
    };
    for (var i = 0; i < num.length; i++) {
        var ch = num.charAt(i);
        if (ch == '.') {
            cnt++;
        } else if (!isDigit(ch)) {
            return false;
        }
    }
    return cnt < 2;
}