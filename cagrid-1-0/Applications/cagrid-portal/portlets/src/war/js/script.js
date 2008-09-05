function changeMenuStyle(obj, new_style) {
    obj.className = new_style;
}

function showCursor() {
    document.body.style.cursor = 'hand'
}

function hideCursor() {
    document.body.style.cursor = 'default'
}

function confirmDelete() {
    if (confirm('Are you sure you want to delete?')) {
        return true;
    } else {
        return false;
    }
}

function isValidURL(url){
    var RegExp = /^(([\w]+:)?\/\/)?(([\d\w]|%[a-fA-f\d]{2,2})+(:([\d\w]|%[a-fA-f\d]{2,2})+)?@)?([\d\w][-\d\w]{0,253}[\d\w]\.)+[\w]{2,4}(:[\d]+)?(\/([-+_~.\d\w]|%[a-fA-f\d]{2,2})*)*(\?(&?([-+_~.\d\w]|%[a-fA-f\d]{2,2})=?)*)?(#([-+_~.\d\w]|%[a-fA-f\d]{2,2})*)?$/;
    if(RegExp.test(url)){
        return true;
    }else{
        return false;
    }
}

