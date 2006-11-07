
/** Wait page before Tree is Loaded **/
 function wait() {
      document.getElementById("main").style.visibility="hidden";
      document.getElementById("wait").style.visibility="visible";
    }

  function confirmPassword(){
       var form = document.forms["registrationForm"];
       var password = form["registrationForm:password"].value;
       var passwordConfirm = form["registrationForm:passwordConfirm"].value;
       if (password == passwordConfirm)
         return true;
       else {
        alert("Password and password confirm fields don't match");
        return false;
        }
  }


function changeMenuStyle(obj, new_style) {
  obj.className = new_style;
}

function showCursor(){
	document.body.style.cursor='hand'
}

function hideCursor(){
	document.body.style.cursor='default'
}

function confirmDelete(){
  if (confirm('Are you sure you want to delete?')){
    return true;
    }else{
    return false;
  }
  }





