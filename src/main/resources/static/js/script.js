'use strict';

document.querySelector('#welcomeForm').addEventListener('submit', connect, true)
document.querySelector('#dialogueForm').addEventListener('submit', sendMessage, true)

var stompClient = null;
var name = null;

function connect(event) {
		document.querySelector('#welcome-page').classList.add('hidden');
		document.querySelector('#dialogue-page').classList.remove('hidden');

		var socket = new SockJS('/websocketChat');
		stompClient = Stomp.over(socket);

		stompClient.connect({}, connectionSuccess);
	event.preventDefault();
}

function connectionSuccess() {
	stompClient.subscribe('/topic/chat', onMessageReceived);

	stompClient.send("/app/newUser", {}, JSON.stringify({
		sender : name,
		type : 'newUser'
	}))

}

function sendMessage(event) {
	var messageContent = document.querySelector('#chatMessage').value.trim();
	var userName = getCookie("userName");
	if (messageContent && stompClient) {
		var chatMessage = {
			sender : userName,
			content : document.querySelector('#chatMessage').value,
			type : 'CHAT'
		};

		stompClient.send("/app/sendMessage", {}, JSON
				.stringify(chatMessage));
		document.querySelector('#chatMessage').value = '';
	}
	event.preventDefault();
}

function onMessageReceived(payload) {
	var message = JSON.parse(payload.body);

	var messageElement = document.createElement('li');

	if (message.type === 'newUser') {
		messageElement.classList.add('event-data');
		setCookie("userName",message.sender,1);
		message.content = message.sender + ' has joined the chat';
	} else if (message.type === 'Leave') {
		messageElement.classList.add('event-data');
		message.content = message.sender + 'has left the chat';
	} else {
		messageElement.classList.add('message-data');

		var element = document.createElement('i');
		var text = document.createTextNode(message.sender[0]);
		element.appendChild(text);

		messageElement.appendChild(element);

		var usernameElement = document.createElement('span');
		var usernameText = document.createTextNode(message.sender);
		usernameElement.appendChild(usernameText);
		messageElement.appendChild(usernameElement);
	}

	var textElement = document.createElement('p');
	var messageText = document.createTextNode(message.content);
	textElement.appendChild(messageText);

	messageElement.appendChild(textElement);

	document.querySelector('#messageList').appendChild(messageElement);
	document.querySelector('#messageList').scrollTop = document
			.querySelector('#messageList').scrollHeight;

}
function setCookie(name, value, days) {
    const d = new Date();
    d.setTime(d.getTime() + (days * 24 * 60 * 60 * 1000)); // Expiry time
    let expires = "expires=" + d.toUTCString();
    document.cookie = name + "=" + value + ";" + expires + ";path=/";
}
function deleteCookie(name) {
    document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 UTC;path=/";
}
function getCookie(name) {
    const nameEQ = name + "=";
    const ca = document.cookie.split(';'); // Split cookies into an array
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i].trim(); // Trim whitespace around the cookie
        if (c.indexOf(nameEQ) === 0) {
            return c.substring(nameEQ.length, c.length); // Return cookie value
        }
    }
    return null; // Return null if cookie not found
}


// Listen for the tab closing or navigation away
window.addEventListener("beforeunload", function() {
    deleteCookie("username");
});