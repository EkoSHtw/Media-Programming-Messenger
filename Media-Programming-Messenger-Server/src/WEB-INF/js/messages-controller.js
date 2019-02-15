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
		enumerable: false,
		configurable: false,
		writable: true,
		value: function () {
			if (!Controller.sessionOwner) return;
			this.displayError();
			
			try{
				const mainElement = document.querySelector("main");
				const subjectElement = document.querySelector("#subjects-template").content.cloneNode(true).firstElementChild;
				this.refreshAvatarSlider(subjectElement.querySelector("span.slider"), Controller.sessionOwner.peopleObservedReferences, person => this.toggleObservation(person.identity));
				mainElement.appendChild(subjectElement);
				
				subjectElement.querySelector("span.slider").addEventListener("click", function(event){
					const url = event.target.getAttribute("src"); // just for img click = bad
					this.displayMessageEditor(mainElement, url); // does not find function
				});
				
//	            const personAvatars = subjectElement.querySelectorAll("span.slider a");
//	            personAvatars.forEach(function(pA) {
//	                pA.addEventListener("click", event => controller.displayMessageEditor(mainElement, pA));
//	            });
//				subjectElement.querySelector(".slider").addEventListener("click", event => this.displayRootMessages());
			}
			 catch (error) {
				this.displayError(error);
			}
        }
    });
    


    /**
     * displays messages
     */

    Object.defineProperty(MessagesController.prototype, "displayMessages", {
		enumerable: false,
		configurable: false,
		value: async function () {
			if (!Controller.sessionOwner) return;
			this.displayError();
			
			try{
                //select avatar... how to get the current avatar
                mainElement.querySelector("").addEventListener("click", event => this.displayMessageEditor());
                //select plus
                mainElement.querySelector("").addEventListener("click", event => this.toggleChildMessages());
				const sectionElement = document.querySelector("");
                
                const uri = "/services/messages/"
				const message = await fetch(uri, { method: "PUT", headers: {"Content-Type": "application/json"}});
				this.refreshAvatarSlider(sectionElement.querySelector("span.slider"), message);
			}
			catch(error){
				this.displayError(error);
			}
		}
	});

    /**
     * 
     */

    Object.defineProperty(MessagesController.prototype, "displayRootMessages", {
		enumerable: false,
		configurable: false,
		value: async function () {
			if (!Controller.sessionOwner) return;
			this.displayError();
			
			try{
                const sectionElement = document.querySelector("");
                
                const uri = "/services/entities/"+ entityIdentity+"/messagesCaused"
            
                try{
                    let response = await fetch(uri, { method: "PUT", headers: {"Content-Type": "application/json"}});
                    if (!response.ok) throw new Error("HTTP " + response.status + " " + response.statusText);
                    
                    //etwas mit displaymessages...
                    this.displayMessages()
                }catch(error){
                
                }
                
				this.refreshAvatarSlider(sectionElement.querySelector("span.slider"), people);
			}
			catch(error){
				this.displayError(error);
			}
		}
	});



    Object.defineProperty(MessagesController.prototype, "toggleChildMessages", {
		enumerable: false,
		configurable: false,
		value: async function () {
			if (!Controller.sessionOwner) return;
			this.displayError();
			
			try{
			}
			catch(error){
				this.displayError(error);
			}
		}
    });
    

    Object.defineProperty(MessagesController.prototype, "displayMessageEditor", {
		enumerable: false,
		configurable: false,
		value: async function (parentElement, subjectIdentity) {
			if (!Controller.sessionOwner) return;
			this.displayError();
			
			try{
				parentElement.appendChild(document.querySelector("#messages-template").content.cloneNode(true).firstElementChild);
				parentElement.appendChild(document.querySelector("#message-output-template").content.cloneNode(true).firstElementChild);
				parentElement.appendChild(document.querySelector("#message-input-template").content.cloneNode(true).firstElementChild);
				
				const sectionElement = document.querySelector("li.message");
                const inputElements = sectionElement.querySelectorAll("img, textarea");

				sectionElement.querySelector("button").addEventListener("click", event => this.persistsMessages());
			}
			catch(error){
				this.displayError(error);
			}
		}
	});



    /**
     * Sends Messeages
     */

    Object.defineProperty(MessagesController.prototype, "persistMessages", {
		enumerable: false,
		configurable: false,
		value: async function (parentElement, subjectIdentity) {
			if (!Controller.sessionOwner) return;
			this.displayError();
			
			try{
                //call server to save messages
			}
			catch(error){
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