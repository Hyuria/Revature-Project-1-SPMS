let baseUrl = 'http://localhost:8080';

awaiting()

function awaiting(){
    console.log("In awaiting function");
    let loginBtn = document.getElementById('loginBtn');
    loginBtn.onclick = login;
}

async function login() {
    console.log("In login function");
    let url = baseUrl + '/users?';
    url += 'user=' + document.getElementById('username').value + '&';
    url += 'pass=' + document.getElementById('pwd').value;
    console.log(url);
    let response = await fetch(url, {credentials: 'include', method: 'PUT'});
    
    switch (response.status) {
        case 200: // successful
            document.getElementById('usernameHelp').innerHTML = '';
            document.getElementById('passwordHelp').innerHTML = '';
            checkLogin();
            break;
        case 400: // incorrect password
            document.getElementById('pwd').value = '';
            document.getElementById('pwd').className = document.getElementById('pwd').className + " error";
            document.getElementById('passwordHelp').innerHTML = 'Incorrect password. Please try again.'
            document.getElementById('usernameHelp').innerHTML = '';
            break;
        case 404: // user not found
            document.getElementById('username').value = '';
            document.getElementById('pwd').value = '';
            document.getElementById('username').className = document.getElementById('username').className + " error";
            document.getElementById('usernameHelp').innerHTML = 'The user does not exist. Please register an account.'
            document.getElementById('passwordHelp').innerHTML = '';
            break;
        default: // other error
            document.getElementById('usernameHelp').innerHTML = '';
            document.getElementById('passwordHelp').innerHTML = 'Something went wrong. Please try again later.'
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