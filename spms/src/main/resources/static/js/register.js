let baseUrl = 'http://localhost:8080';
let loggedUser = null;

awaiting()

function awaiting(){
    console.log("In awaiting function");
    let registerBtn = document.getElementById('loginBtn');
    registerBtn.onclick = register;
}

async function register() {
    console.log("In register function");
    let url = baseUrl + '/users?';
    url += 'user=' + document.getElementById('username').value + '&';
    url += 'pass=' + document.getElementById('pwd').value + '&';
    url += 'firstname=' + document.getElementById('firstname').value + '&';
    url += 'lastname=' + document.getElementById('lastname').value;
    console.log(url);
    let response = await fetch(url, {credentials: 'include', method: 'POST'});
    
    switch (response.status) {
        case 200: // successful
            document.getElementById('usernameHelp').innerHTML = '';
            checkLogin();
            break;
        case 400: // invalid username
            document.getElementById('username').value = '';
            document.getElementById('usernameHelp').innerHTML = 'Username already exist. Please try again or login.';
            break;
        default: // other error
            document.getElementById('lastnameHelp').innerHTML = 'Something went wrong. Please try again later.';
            break;
    }
}

async function checkLogin() {
    let url = baseUrl + '/users';
    let response = await fetch(url, {credentials: 'include'});
    console.log('Fetch response: ' + response);
    if (response.status === 200){
        loggedUser = await response.json();
        window.location.href = "D:/2011-nov9-usf/Project1/spms/src/main/resources/static/dashboard.html"
    }else{
        awaiting();
    }
}