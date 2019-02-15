"use-strict";

(function(){

    const Controller = de_sb_messenger.Controller;

    const MessagesController = function(){
        Controller.call(this);
    }

    MessagesController.prototype = Object.create(Controller.prototype);
	MessagesController.prototype.constructor = MessagesController;

    /**
	 * Displays the associated view.
	 */
	Object.defineProperty(MessagesController.prototype, "display", {
		writable: true,
		value: function () {
			if (!Controller.sessionOwner) return;
			
			this.displayError();
			try {
				let rootSubjectReferences = [ Controller.sessionOwner.identity ];
				rootSubjectReferences.push.apply( rootSubjectReferences, Controller.sessionOwner.peopleObservedReferences );
				
				let mainElement = document.querySelector("main");
				let subjectElement = document.querySelector("#subjects-template").content.cloneNode(true).firstElementChild;
				let messagesElement = document.querySelector("#messages-template").content.cloneNode(true).firstElementChild;
				this.refreshAvatarSlider(subjectElement.querySelector("span.slider"), rootSubjectReferences, person => this.displayMessageEditor(mainElement, person.identity));
				mainElement.appendChild(subjectElement);
				mainElement.appendChild(messagesElement);
				
				this.displayRootMessages(rootSubjectReferences);
			} catch (error) {
				this.displayError(error);
			}
        }
    });
    
    /**
     * 
     */
    Object.defineProperty(MessagesController.prototype, "displayRootMessages", {
		value: async function (rootSubjectReferences) {

			this.displayError();			
			try {
                let sectionElement = document.querySelector("main section.messages");
                
                let messageReferences = [];
                for (let rootSubjectReference of rootSubjectReferences) {
                	let person = await Controller.entityCache.get(rootSubjectReference);
                	messageReferences.push.apply(messageReferences, person.messageAuthoredReferences);
//                	messageReferences.push.apply(messageReferences, person.messageCausedReferences);
                }
                
                 this.displayMessages(sectionElement, messageReferences);
			} catch (error) {
				this.displayError(error);
			}
		}
	});

    /**
     * displays messages
     */

    Object.defineProperty(MessagesController.prototype, "displayMessages", {
		value: async function (parentMessageElement, messageReferences) {
			this.displayError();
			try {
				 let promises = [], messages = [];
	             for (let messageReference of messageReferences) {
	            	const uri = "/services/messages/" + messageReference;
	             	let promise = fetch(uri, { method: "GET", headers: {"Accept": "application/json"}, credentials: "include" });
	               	promises.push(promise);
	             }
	                
	             for (let promise of promises) {
	               	let response = await promise;
	               	if (!response.ok) throw new Error("HTTP " + response.status + " " + response.statusText);
	               	messages.push.apply(messages, await response.json());
	             }
				
				let messageList = parentMessageElement.querySelector("ul");
                for (let message of messages) {
                	let messageCell = document.querySelector("#message-output-template").content.cloneNode(true).firstElementChild;
                	let avatar = messageCell.querySelector("img.avatar");
                	avatar.src = "/services/people/" + message.authorReference + "/avatar";
                	avatar.addEventListener("click", event => this.displayMessageEditor(messageCell, message.subjectReference));
                	
                	let toggle = messageCell.querySelector("img.plus");
                	toggle.addEventListener("click", event => this.toggleChildMessages(messageCell, message));
                	
                	messageList.appendChild(messageCell);
                }
			} catch (error) {
				this.displayError(error);
			}
		}
	});


    Object.defineProperty(MessagesController.prototype, "toggleChildMessages", {
		value: async function (messageCell, message) {

			this.displayError();
			try{
			}
			catch(error){
				this.displayError(error);
			}
		}
    });
    

    Object.defineProperty(MessagesController.prototype, "displayMessageEditor", {
		value: async function (parentElement, subjectIdentity) {
			this.displayError();
			try {
				let editorCell = document.querySelector("#message-input-template").content.cloneNode(true).firstElementChild;
				let avatar = editorCell.querySelector("img.avatar");
				avatar.src = "/services/people/" + Controller.sessionOwner.identity + "/avatar";
                let output = editorCell.querySelector("output");
                output.value = Controller.sessionOwner.name.given + " " + Controller.sessionOwner.name.family;
                
                editorCell.querySelector("button").addEventListener("click", event => this.persistMessage(parentElement, subjectIdentity));
				parentElement.appendChild(editorCell);
			} catch (error) {
				this.displayError(error);
			}
		}
	});
    



    /**
     * Sends Messeages
     */
    Object.defineProperty(MessagesController.prototype, "persistMessage", {
		value: async function (parentElement, subjectIdentity) {
			this.displayError();
			try {
				let editorCell = parentElement.querySelector("li.message-input");
				let messageBody = editorCell.querySelector("textarea").value;
				editorCell.remove();
				
				const uri = "/services/messages";
				let response = await fetch(uri, { method: "POST", headers: {"Content-Type": "text/plain"}, credentials: "include", body: messageBody });
				if (!response.ok) throw new Error("HTTP " + response.status + " " + response.statusText);
				
				let messageIdentity = await response.text();
				this.displayMessages(parentElement, [messageIdentity]);
			} catch (error) {
				this.displayError(error);
			}
		}
	});


    window.addEventListener("load", event => {
        const anchor = document.querySelector("header li:nth-of-type(2) > a");
		const controller = new MessagesController();
		anchor.addEventListener("click", event => controller.display());
	});

} ());