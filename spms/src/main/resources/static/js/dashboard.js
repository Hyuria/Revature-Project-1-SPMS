let baseUrl = 'http://localhost:8080';
let loggedUser = null;
let author = null;
let retrievedPitches = [];
let retrievedMessages = [];
let currentPitchId = null;
let currentMessageId = null;


getUser();


async function getUser() {
    // userController checkLogin()
    let url = baseUrl + '/users';
    let response = await fetch(url, {credentials: 'include'});
    if (response.status === 200){
        loggedUser = await response.json();
        getAuthorEditor();
    }else{
        window.location.href = "D:/2011-nov9-usf/Project1/spms/src/main/resources/static/login.html"
    }
}

async function getAuthorEditor(){
    let url = baseUrl + '/users/' + loggedUser['id'];
    let response = await fetch(url, {credentials: 'include'});
    if (response.status === 200){
        author = await response.json();
        if (author.role != null){
            getCommittee();
        }else{
            setUpAuthor();
        }
    }else{
        alert('Error getting User Data');
    }
}

function setUpAuthor(){
    document.getElementById('welcome_text').innerHTML = 'Welcome ' + author['firstName'] + ".";
    document.getElementById('points').innerHTML = 'Points: ' + author['points'];

    retrievedPitchesFunction();
}

function inPitchesTab(){
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

        <h3 class="text-center">Submitted Pitches and Stories</h3>
        <table class="table table-striped" id='pitch-table'></table>`;

    for (let x of retrievedPitches){
        document.getElementById('pitch-table').innerHTML += `
        <tr id='${x.id}' onclick='setCurrentPitchID(this.id)'>
            <th style="text-transform:capitalize">${x.type.type}</th>
            <td>${x.title}</td>
            <td>Status: ${x.status.status}</td>
        </tr>
        `;
    }

    // Listeners
    let newPitch = document.getElementById('pitch-button');
    newPitch.onclick = inNewPitchTab;
    let messages = document.getElementById('list-element-messages')
    messages.onclick = retrievedMessagesFunction;
    let logoutBtn = document.getElementById('logout');
    logoutBtn.onclick = logout;
}

function setCurrentPitchID(id){
    currentPitchId = id;
    showPitch()
}

function showPitch(){
    console.log('In showPitch()');
    let pitch = null;
    for (let x of retrievedPitches){
        if (x.id == currentPitchId){
            pitch = x;
        }
    }
    console.log('Located Pitch');
    document.getElementById('pitch-body').innerHTML = `
    <form class="compose-pitch">
        <div class="row align-items-center ">
            <div class="col-auto ">
                <label for="pitch-title" >Title *: </label>
            </div>
            <div class="col-auto my-auto">
                <input id="pitch-title" name="pitch-title" type="text" class="form-control" placeholder="Title" value = '${pitch.title}'/>
                <small id="titleHelp" class="form-text text-white"></small>
            </div>
        </div>
        <div class="row align-items-center ">
            <div class="col-auto">
                <label for="pitch-complettion-date" >Completion Date *: </label>
            </div>
            <div class="col-auto">
                <input id="pitch-completion-date" name="pitch-completion-date" type="date" class="form-control" placeholder="Completion Date" value='${new Date(pitch.completionDate).toISOString().substring(0, 10)}'/>
                <small id="dateHelp" class="form-text text-white"></small>
            </div>
        </div>
        <div class="row align-items-center ">
            <div class="col-auto">
                <label for="pitch-story-type" >Story Type *: </label>
            </div>
            <div class="col-auto">
                <select name="pitch-story-type" id="pitch-story-type" class="form-control" style="font-size: medium;">
                        <option id='novel' value="novel">Novel (50p)</option>
                        <option id="novella" value="novella">Novella (25p)</option>
                        <option id="short story" value="short story">Short Story (20p)</option>
                        <option id="article" value="article">Article (10p)</option>
                </select>
                <small id="typeHelp" class="form-text text-white"></small>
            </div>
        </div>
        <div class="row align-items-center ">
            <div class="col-auto">
                <label for="pitch-genre" >Genre *: </label>
            </div>
            <div class="col-auto">
                <select name="pitch-genre" id="pitch-genre" class="form-control" style="font-size: medium;">
                        <option id="action" value="action">Action</option>
                        <option id="adventure" value="adventure">Adventure</option>
                        <option id="fantasy" value="fantasy">Fantasy</option>
                        <option id="horror" value="horror">Horror</option>
                        <option id="mystery" value="mystery">Mystery</option>
                        <option id="romance" value="romance">Romance</option>
                        <option id="science_fiction" value="science_fiction">Science Fiction</option>
                </select>
                <small id="genreHelp" class="form-text text-white"></small>
            </div>
        </div>
        <div class="row align-items-center ">
            <div class="col-auto">
                <label for="pitch-tagline" >Tagline *: </label>
            </div>
            <div class="col-auto">
                <input id="pitch-tagline" name="pitch-tagline" type="text" class="form-control" placeholder="Tagline" value='${pitch.tagline}'/>
                <small id="taglineHelp" class="form-text text-white"></small>
            </div>
        </div>
        <div class="row align-items-center ">
            <div class="col-auto">
                <label for="pitch-description" >Detailed Description *: </label>
            </div>
            <div class="col-auto">
                <input id="pitch-description" name="pitch-description" type="text" class="form-control" placeholder="Description" value='${pitch.description}'/>
                <small id="descriptionHelp" class="form-text text-white"></small>
            </div>
        </div>
        <div class="row align-items-center ">
            <div class="col-auto">
                <label for="pitch-story">Story: </label>
            </div>
            <div class="col-auto">
                <textarea id="pitch-story" name="pitch-story" rows="4"  cols="70" class="form-control" value='${pitch.text}'> </textarea>
            </div>
        </div>
        <div class="col text-center">
            <button type="button" class="btn btn-light btn-l" id="updateBtn">Update</button>
        </div>
    </form>
    `;
    
    let selectTypeIndex = document.getElementById(pitch.type.type).index;
    document.getElementById('pitch-story-type').options[selectTypeIndex].selected=true;

    let selectGenreIndex = document.getElementById(pitch.genre.genre).index;
    document.getElementById('pitch-genre').options[selectGenreIndex].selected=true;

    let updateBtn = document.getElementById('updateBtn');
    updateBtn.onclick=checkPitchUpdate;

}

function inMessagesTab(){

    // Setup
    document.getElementById('list-element-pitches').className = '';
    document.getElementById('list-element-messages').className = 'active';
    document.getElementById('pitch-list').innerHTML = ` 
        <div id="view-pitch">
            <h3>View Messages</h3>
            <div id="pitch-body" class="text-center no-content">
                No Selected Message
            </div>
        </div>

        <hr class="divider my-4" />

        <h3 class="text-center">Messages</h3>
        <table class="table table-striped" id='pitch-table'></table>`;
    
    for (let x of retrievedMessages){
        document.getElementById('pitch-table').innerHTML = `
        <tr id='${x.id}' onclick='setCurrentMessageID(this.id)'>
            <th style="text-transform:capitalize">${x.story.type.type}</th>
            <td>${x.story.title}</td>
            <td>Status: ${x.story.status.status}</td>
        </tr>
        `;
    }

    // Listeners
    let newPitch = document.getElementById('pitch-button');
    newPitch.onclick = inNewPitchTab;
    let pitches = document.getElementById('list-element-pitches')
    pitches.onclick = retrievedPitchesFunction;
    let logoutBtn = document.getElementById('logout');
    logoutBtn.onclick = logout;
}

function setCurrentMessageID(id){
    currentMessageId = id;
    showMessage();
}

function showMessage(){
    console.log('In showMessage()');
    let message = null;
    for (let x of retrievedMessages){
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

function inNewPitchTab(){
    console.log('in new pitch tab')

    // Setup
    document.getElementById('list-element-pitches').className = '';
    document.getElementById('list-element-messages').className = '';
    document.getElementById('pitch-list').innerHTML = `
    <form class="compose-pitch">
        <div class="row">
            <h3> New Pitch </h3>
        </div>
        <div class="row">
            <h5> Anything field with * is required.</h5>
        </div>
        <div class="row align-items-center ">
            <div class="col-auto ">
                <label for="pitch-title" >Title *: </label>
            </div>
            <div class="col-auto my-auto">
                <input id="pitch-title" name="pitch-title" type="text" class="form-control" placeholder="Title"/>
                <small id="titleHelp" class="form-text text-white"></small>
            </div>
        </div>
        <div class="row align-items-center ">
            <div class="col-auto">
                <label for="pitch-complettion-date" >Completion Date *: </label>
            </div>
            <div class="col-auto">
                <input id="pitch-completion-date" name="pitch-completion-date" type="date" class="form-control" placeholder="Completion Date"/>
                <small id="dateHelp" class="form-text text-white"></small>
            </div>
        </div>
        <div class="row align-items-center ">
            <div class="col-auto">
                <label for="pitch-story-type" >Story Type *: </label>
            </div>
            <div class="col-auto">
                <select name="pitch-story-type" id="pitch-story-type" class="form-control" style="font-size: medium;">
                        <option value="novel">Novel (50p)</option>
                        <option value="novella">Novella (25p)</option>
                        <option value="short story">Short Story (20p)</option>
                        <option value="article">Article (10p)</option>
                </select>
                <small id="typeHelp" class="form-text text-white"></small>
            </div>
        </div>
        <div class="row align-items-center ">
            <div class="col-auto">
                <label for="pitch-genre" >Genre *: </label>
            </div>
            <div class="col-auto">
                <select name="pitch-genre" id="pitch-genre" class="form-control" style="font-size: medium;">
                        <option value="action">Action</option>
                        <option value="adventure">Adventure</option>
                        <option value="fantasy">Fantasy</option>
                        <option value="horror">Horror</option>
                        <option value="mystery">Mystery</option>
                        <option value="romance">Romance</option>
                        <option value="science_fiction">Science Fiction</option>
                </select>
                <small id="genreHelp" class="form-text text-white"></small>
            </div>
        </div>
        <div class="row align-items-center ">
            <div class="col-auto">
                <label for="pitch-tagline" >Tagline *: </label>
            </div>
            <div class="col-auto">
                <input id="pitch-tagline" name="pitch-tagline" type="text" class="form-control" placeholder="Tagline"/>
                <small id="taglineHelp" class="form-text text-white"></small>
            </div>
        </div>
        <div class="row align-items-center ">
            <div class="col-auto">
                <label for="pitch-description" >Detailed Description *: </label>
            </div>
            <div class="col-auto">
                <input id="pitch-description" name="pitch-description" type="text" class="form-control" placeholder="Description"/>
                <small id="descriptionHelp" class="form-text text-white"></small>
            </div>
        </div>
        <div class="row align-items-center ">
            <div class="col-auto">
                <label for="pitch-story">Story: </label>
            </div>
            <div class="col-auto">
                <textarea id="pitch-story" name="pitch-story" rows="4"  cols="70" class="form-control"> </textarea>
            </div>
        </div>
        <div class="col text-center">
            <button type="button" class="btn btn-light btn-l" id="submitBtn">Submit</button>
        </div>
    </form>
    `;

    //Listeners
    let newPitch = document.getElementById('pitch-button');
    newPitch.onclick = inNewPitchTab;
    let pitches = document.getElementById('list-element-pitches')
    pitches.onclick = inPitchesTab;
    let messages = document.getElementById('list-element-messages')
    messages.onclick = inMessagesTab;
    let submitPitchBtn = document.getElementById('submitBtn');
    submitPitchBtn.onclick = checkPitch;
    let logoutBtn = document.getElementById('logout');
    logoutBtn.onclick = logout;
}

function checkPitch(){
    console.log('Checking Pitch');
    let story = {};
    story.id=0;
    story.author = author;
    story.title = document.getElementById('pitch-title').value;
    story.completionDate = document.getElementById('pitch-completion-date').value;
    story.type = document.getElementById('pitch-story-type').value;
    story.genre = document.getElementById('pitch-genre').value;
    story.tagline = document.getElementById('pitch-tagline').value;
    story.description = document.getElementById('pitch-description').value;
    story.text = document.getElementById('pitch-story').value;

    clearError;

    if (story.title == ''){
        document.getElementById('pitch-title').className = document.getElementById('pitch-title').className + ' error';
        document.getElementById('titleHelp').innerHTML = 'Title cannot be empty';
    } else if (story.completionDate == ''){
        document.getElementById('pitch-completion-date').className = document.getElementById('pitch-completion-date').className + ' error';
        document.getElementById('dateHelp').innerHTML = 'Completion Date cannot be empty. Please estimate a time for completion date.';
    } else if (story.type == 'novel' && author['points'] < 50){
        document.getElementById('pitch-story-type').className = document.getElementById('pitch-story-type').className + ' error';
        document.getElementById('typeHelp').innerHTML = 'Story type exceed alloted points. Please wait until existing pitches pass approval or delete old pitches.';
    } else if (story.type == 'novellas' && author['points'] < 25){
        document.getElementById('pitch-story-type').className = document.getElementById('pitch-story-type').className + ' error';
        document.getElementById('typeHelp').innerHTML = 'Story type exceed alloted points. Please wait until existing pitches pass approval or delete old pitches.';
    } else if (story.type == 'short stories' && author['points'] < 20){
        document.getElementById('pitch-story-type').className = document.getElementById('pitch-story-type').className + ' error';
         document.getElementById('typeHelp').innerHTML = 'Story type exceed alloted points. Please wait until existing pitches pass approval or delete old pitches.';
    } else if (story.type == 'articles' && author['points'] < 10){
        document.getElementById('pitch-story-type').className = document.getElementById('pitch-story-type').className + ' error';
        document.getElementById('typeHelp').innerHTML = 'Story type exceed alloted points. Please wait until existing pitches pass approval or delete old pitches.';
     } else if (story.tagline == ''){
        document.getElementById('pitch-tagline').className = document.getElementById('pitch-tagline').className + ' error';
        document.getElementById('titleHelp').innerHTML = 'Tagline cannot be empty. Please include a interesting tagline to hook readers looking at the cover.';
    } else if (story.description == ''){
        document.getElementById('pitch-description').className = document.getElementById('pitch-description').className + ' error';
        document.getElementById('titleHelp').innerHTML = 'Description cannot be empty. Please include a detailed description that helps our editors evaluate your pitch.';
    } else {
        console.log('Pass Check');
        submitPitch();
    }

    let submitPitchBtn = document.getElementById('submitBtn');
    submitPitchBtn.onclick = checkPitch;
}


function checkPitchUpdate(){
    let story = null;
    for (let x of retrievedPitches){
        if (x.id == currentPitchId){
            story = x;
        }
    }
    let currentStoryValue = story.type.pointValue;

    clearError;

    if (document.getElementById('pitch-title').value == ''){
        document.getElementById('pitch-title').className = document.getElementById('pitch-title').className + ' error';
        document.getElementById('titleHelp').innerHTML = 'Title cannot be empty';
    } else if (document.getElementById('pitch-completion-date').value == ''){
        document.getElementById('pitch-completion-date').className = document.getElementById('pitch-completion-date').className + ' error';
        document.getElementById('dateHelp').innerHTML = 'Completion Date cannot be empty. Please estimate a time for completion date.';


    } else if (document.getElementById('pitch-story-type').value == 'novel' && author['points'] < 50 - currentStoryValue){
        document.getElementById('pitch-story-type').className = document.getElementById('pitch-story-type').className + ' error';
        document.getElementById('typeHelp').innerHTML = 'Story type exceed alloted points. Please wait until existing pitches pass approval or delete old pitches.';
    } else if (document.getElementById('pitch-story-type').value == 'novella' && author['points'] < 25 - currentStoryValue){
        document.getElementById('pitch-story-type').className = document.getElementById('pitch-story-type').className + ' error';
        document.getElementById('typeHelp').innerHTML = 'Story type exceed alloted points. Please wait until existing pitches pass approval or delete old pitches.';
    } else if (document.getElementById('pitch-story-type').value == 'short story' && author['points'] < 20 - currentStoryValue){
        document.getElementById('pitch-story-type').className = document.getElementById('pitch-story-type').className + ' error';
         document.getElementById('typeHelp').innerHTML = 'Story type exceed alloted points. Please wait until existing pitches pass approval or delete old pitches.';
    } else if (document.getElementById('pitch-story-type').value == 'article' && author['points'] < 10 - currentStoryValue){
        document.getElementById('pitch-story-type').className = document.getElementById('pitch-story-type').className + ' error';
        document.getElementById('typeHelp').innerHTML = 'Story type exceed alloted points. Please wait until existing pitches pass approval or delete old pitches.';


     } else if (document.getElementById('pitch-tagline').valuee == ''){
        document.getElementById('pitch-tagline').className = document.getElementById('pitch-tagline').className + ' error';
        document.getElementById('titleHelp').innerHTML = 'Tagline cannot be empty. Please include a interesting tagline to hook readers looking at the cover.';
    } else if (document.getElementById('pitch-description').value == ''){
        document.getElementById('pitch-description').className = document.getElementById('pitch-description').className + ' error';
        document.getElementById('titleHelp').innerHTML = 'Description cannot be empty. Please include a detailed description that helps our editors evaluate your pitch.';
    } else {
        updatePitch();
    }
    let submitPitchBtn = document.getElementById('updateBtn');
    submitPitchBtn.onclick = checkPitchUpdate;
}

function clearError(){
    let story = {};
    story.id=0;
    story.author = author;
    story.title = document.getElementById('pitch-title');
    story.completionDate = document.getElementById('pitch-completion-date');
    story.type = document.getElementById('pitch-story-type');
    story.genre = document.getElementById('pitch-genre');
    story.tagline = document.getElementById('pitch-tagline');
    story.description = document.getElementById('pitch-description');
    story.text = document.getElementById('pitch-story');

    story.title.innerHTML = '';
    story.completionDate.innerHTML = '';
    story.type.innerHTML = '';
    story.genre.innerHTML = '';
    story.tagline.innerHTML = '';
    story.description.innerHTML = '';
    story.text.innerHTML = '';

    story.title.className = 'form-control';
    story.completionDate.className = 'form-control';
    story.type.className = 'form-control';
    story.genre.className = 'form-control';
    story.tagline.className = 'form-control';
    story.description.className = 'form-control';
    story.text.className = 'form-control';
}


async function submitPitch(){
    console.log('Submit Pitch')

    let story = {};
    story.title = document.getElementById('pitch-title').value;
    story.completionDate = document.getElementById('pitch-completion-date').value;
    story.type = document.getElementById('pitch-story-type').value;
    story.genre = document.getElementById('pitch-genre').value;
    story.tagline = document.getElementById('pitch-tagline').value;
    story.description = document.getElementById('pitch-description').value;
    story.text = document.getElementById('pitch-story').value;

    console.log("JSON: " + JSON.stringify(story));

    let url = baseUrl + '/author';
    let response = await fetch(url, {credentials: 'include', method:'POST', body:JSON.stringify(story)});
    if (response.status === 200){
        alert('Submitted pitch successfully.');
    }else{
        alert('Something went wrong.');
    }
    getAuthorEditor();
}

async function updatePitch(){
    console.log('in update pitch function')

    let story = null;

    for (let x of retrievedPitches){
        if (x.id == currentPitchId){
            story = x;
        }
    }

    console.log('located old pitch');
    story.title = document.getElementById('pitch-title').value;
    story.completionDate = document.getElementById('pitch-completion-date').value;
    let newtype = document.getElementById('pitch-story-type').value;
    story.genre = document.getElementById('pitch-genre').value;
    story.tagline = document.getElementById('pitch-tagline').value;
    story.description = document.getElementById('pitch-description').value;
    story.text = document.getElementById('pitch-story').value;

    console.log('New Type value:' + newtype);
    switch(newtype){
        case 'novel':
            console.log('Changing story type to novel');
            story.author.points = story.author.points - (50 - story.type.pointValue);
            story.type = newtype;
            break;
        case 'novella':
            console.log('Changing story type to novella');
            story.author.points = story.author.points - (25 - story.type.pointValue);
            story.type = newtype;
            break;
        case 'short story':
            console.log('Changing story type to short story');
            story.author.points = story.author.points - (20 - story.type.pointValue);
            story.type = newtype;
            break;
        case 'article':
            console.log('Changing story type to article');
            story.author.points = story.author.points - (10 - story.type.pointValue);
            story.type = newtype;
            break;
    }


    console.log("JSON: " + JSON.stringify(story));

    let url = baseUrl + '/author';
    let response = await fetch(url, {credentials: 'include', method:'PUT', body:JSON.stringify(story)});
    if (response.status === 200){
        alert('Updated pitch successfully.');
    }else{
        alert('Something went wrong.');
    }
    getAuthorEditor();
}

async function retrievedPitchesFunction(){
    console.log('In Retrieving Pitches Function');
    let url = baseUrl + '/author/stories';
    console.log(url);
    let response = await fetch(url, {credentials: 'include'});
    if (response.status === 200){
        retrievedPitches = await response.json();
        console.log('Retrieved ' + retrievedPitches.length + ' pitches.');
        console.log(retrievedPitches);
        inPitchesTab();
    }else{
        alert("Error retrieving pitches.");
    }
}

async function retrievedMessagesFunction(){
    let url = baseUrl + '/author/messages';
    let response = await fetch(url, {credentials: 'include'});
    if (response.status === 200){
        retrievedMessages = await response.json();
        console.log('Retrieved ' + retrievedMessages.length + ' messages.');
        console.log(retrievedMessages);
        inMessagesTab();
    }else{
        alert("Error retrieving pitches.");
    }
}


async function logout() {
    let url = baseUrl + '/users';
    let response = await fetch(url, {credentials: 'include', method:'DELETE'});

    if (response.status != 200) {
        alert('Something went wrong.')
    } else {
        loggedUser = null;
        author = null;
        retrievedPitches = [];
        retrievedMessages = [];
        retrievedApproval = [];
        retrievedRequest = []
        currentPitchId = null;
        committee = null;
        retrievedApproval = [];
        retrievedRequest = [];
        window.location.href = "D:/2011-nov9-usf/Project1/spms/src/main/resources/static/index.html";
    }
}