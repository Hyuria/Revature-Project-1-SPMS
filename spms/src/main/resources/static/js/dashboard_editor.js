let committee = null;
let draftApproval = null;
let retrievedApproval = [];
let retrievedRequest = [];

async function getCommittee(){
    console.log('In getting committee');
    let url = baseUrl + '/committee';
    let response = await fetch(url, {credentials: 'include'});
    if (response.status === 200){
        committee = await response.json();
        setUpEditor();
    }else{
        alert('Problem fetching committee.');
    }
}

function setUpEditor(){
    console.log('In Setting Up Editor');
    document.getElementById('welcome_text').innerHTML = 'Welcome ' + author['firstName'] + ".";
    document.getElementById('new-pitch-button').innerHTML = author.role.editorRole;
    document.getElementById('points').innerHTML = 'Committee: '
    for (let x of committee){
        document.getElementById('points').innerHTML += `</br>`+ x.genre.genre.charAt(0).toUpperCase() + x.genre.genre.slice(1);
    }
    document.getElementById('list-element-pitches').innerHTML = 'Approvals';
    document.getElementById('list-element-messages').innerHTML = 'Requests';
    retrievedApprovalFunction();
}

async function retrievedApprovalFunction(){
    console.log('In Retrieving Approval Function');
    let url = baseUrl + '/editor/approvals';
    console.log(url);
    let response = await fetch(url, {credentials: 'include'});
    if (response.status === 200){
        retrievedApproval = await response.json();
        console.log('Retrieved ' + retrievedApproval.length + ' approvals.');
        console.log(retrievedApproval);
        inApprovalTab();
    }else{
        alert("Error retrieving approvals.");
    }
}


function inApprovalTab(){
    console.log('inApprovalTab Function');
    // Setup
    document.getElementById('list-element-pitches').className = 'active';
    document.getElementById('list-element-messages').className = '';
    document.getElementById('pitch-list').innerHTML = ` 
        <div id="view-pitch">
            <h3>View Pitch</h3>
            <div id="pitch-body" class="text-center no-content">
                No Selected Pitch
            </div>
        </div>

        <hr class="divider my-4" />

        <h3 class="text-center">Approvals</h3>
        <table class="table table-striped" id='pitch-table'></table>`;

    for (let x of retrievedApproval){
        document.getElementById('pitch-table').innerHTML += `
        <tr id='${x.id}' onclick='setCurrentPitchIDEditor(this.id)'>
            <th style="text-transform:capitalize">${x.type.type}</th>
            <td>${x.title}</td>
            <td>Status: ${x.status.status}</td>
        </tr>
        `;
    }

    // Listeners
    let requestBtn = document.getElementById('list-element-messages')
    requestBtn.onclick = retrievedRequestFunction;
    let logoutBtn = document.getElementById('logout');
    logoutBtn.onclick = logout;
}

function setCurrentPitchIDEditor(id){
    currentPitchId = id;
    showApproval();
}

function showApproval(){
    console.log('In showApproval()');
    let pitch = null;
    for (let x of retrievedApproval){
        if (x.id == currentPitchId){
            pitch = x;
        }
    }
    let status = pitch.status.status;
    console.log('Located Pitch');
    document.getElementById('pitch-body').innerHTML=`
        <div id="approval-body" class='text-left' style='color: black; font-size: 1rem; '>
            Status : &ensp; ${pitch.status.status}<br/>
            Title : &ensp; ${pitch.title}<br/>
            Estimated &nbsp; Completion &nbsp; Date:&ensp; ${new Date(pitch.completionDate).toISOString().substring(0, 10)}<br/>
            Type :&ensp; ${pitch.type.type}<br/>
            Genre :&ensp; ${pitch.genre.genre}<br/>
            Tagline :&ensp; ${pitch.tagline}<br/>
            Description: </br>
            <p id='description'>
                </br>${pitch.description}
            </p>
            Text : </br>
            <p id='text'>
                &ensp;${pitch.text}
            </p>
        </div>
        <div class="col text-right">
            <button type="button" class="btn btn-info btn-l" id="approvalBtn">Approval</button>
        </div>
        </br>
        <div class="row align-items-center tex">
            <div class="col-auto" style='color: black; font-size: 1rem;'>
                <label for="denial-reason">Denial Reason: </label>
            </div>
            <div class="col-auto">
                <textarea id="denial-reason" name="denial-reason" rows="4"  cols="70" class="form-control"> </textarea>
            </div>
        </div>
        <div class="col text-right">
            <button type="button" class="btn btn-danger btn-l" id="submitRequestBtn">Send Request</button>
        </div>

    `;

    document.getElementById('approvalBtn').disabled = true;
    document.getElementById('submitRequestBtn').disabled = true;

    if (status == 'awaiting pitch approval: AE' && author.role.editorRole == 'Assistant Editor'){
        document.getElementById('approvalBtn').disabled = false;
        document.getElementById('submitRequestBtn').disabled = false;
    }
    else if (status == 'awaiting pitch approval: GE' && author.role.editorRole == 'General Editor'){
        document.getElementById('approvalBtn').disabled = false;
        document.getElementById('submitRequestBtn').disabled = false;
    }
    else if (status == 'awaiting pitch approval: SE' && author.role.editorRole == 'Senior Editor'){
        document.getElementById('approvalBtn').disabled = false;
        document.getElementById('submitRequestBtn').disabled = false;
    }
    else if (status == 'awaiting draft approval'){
        document.getElementById('approvalBtn').disabled = false;
        document.getElementById('submitRequestBtn').disabled = false;
    } else {
        document.getElementById('approvalBtn').disabled = true;
        document.getElementById('submitRequestBtn').disabled = true;
    }

    let submitRequestBtn = document.getElementById('submitRequestBtn');
    submitRequestBtn.onclick = sendRequest;
    let approvalButton = document.getElementById('approvalBtn');
    approvalButton.onclick = approvalPitch;

}

async function sendRequest(){
    console.log('In send request');
    let request = {};
    request.id = 0;
    for (let x of retrievedApproval){
        if (x.id == currentPitchId){
            request.story = x;
        }
    }
    request.sender = loggedUser;
    request.receiver = request.story.author.user;
    request.message = document.getElementById('denial-reason').value;

    console.log("JSON: " + JSON.stringify(request));

    let url = baseUrl + '/editor/requests';
    let response = await fetch(url, {credentials: 'include', method:'POST', body:JSON.stringify(request)})
    if (response.status === 200){
        alert('Successfully submitted Request/Reason');
        retrievedApprovalFunction();
    }else{
        alert('Error with submission.')
    }
}

async function approvalPitch(){
    console.log('In Approve Pitch');
    let url = baseUrl + '/editor/approvals/' + currentPitchId;
    let response = await fetch(url, {credentials: 'include', method:'PUT'});
    if (response.status ===  200){
        retrievedApprovalFunction();
    }else{
        alert('Error with approval.')
    }

}

async function retrievedRequestFunction(){
    let url = baseUrl + '/editor/requests';
    let response = await fetch(url, {credentials: 'include'});
    if (response.status === 200){
        retrievedRequest = await response.json();
        console.log('Retrieved ' + retrievedRequest.length + ' requests.');
        console.log(retrievedRequest);
        inRequestTab();
    }else{
        alert("Error retrieving requests.");
    }
}

function inRequestTab(){
    // Setup
    document.getElementById('list-element-pitches').className = '';
    document.getElementById('list-element-messages').className = 'active';
    document.getElementById('pitch-list').innerHTML = ` 
        <div id="view-pitch">
            <h3>View Request</h3>
            <div id="pitch-body" class="text-center no-content">
                No Selected Request
            </div>
        </div>

        <hr class="divider my-4" />

        <h3 class="text-center">Past Requests</h3>
        <table class="table table-striped" id='pitch-table'></table>`;
    
    for (let x of retrievedRequest){
        document.getElementById('pitch-table').innerHTML = `
        <tr id='${x.id}' onclick='setCurrentMessageIDEditor(this.id)'>
            <th style="text-transform:capitalize">${x.story.type.type}</th>
            <td>${x.story.title}</td>
            <td>Status: ${x.story.status.status}</td>
        </tr>
        `;
    }
    
    // Listeners
    let approvals = document.getElementById('list-element-pitches')
    approvals.onclick = retrievedApprovalFunction;
    let logoutBtn = document.getElementById('logout');
    logoutBtn.onclick = logout;
}

function setCurrentMessageIDEditor(id){
    currentMessageId = id;
    showMessageEditor();
}

function showMessageEditor(){
    console.log('In showMessageEditor()');
    let message = null;
    for (let x of retrievedRequest){
        if (x.id == currentMessageId){
            message = x;
        }
    }
    console.log('Located Message');
    document.getElementById('pitch-body').innerHTML = `
        <div class="text-left">
            <h5 style="color:black;font-size:100%; margin-bottom:10px">${message.story.title} : Pitch Denial Reason</h5>
            <p></p>
            </b>
            <p style="color:black;font-size:85%;">${message.message}</p>
            </b>
            <p style="color:grey;font-size:60%;">Please submit any changes and then our editors will approval it as soon as possible.</p>
        </div>
    `;

}