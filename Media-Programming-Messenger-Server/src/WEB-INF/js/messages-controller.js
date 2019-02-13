"use-strict"

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
			Controller.sessionOwner = null;
			Controller.entityCache.clear();

            const mainElement = document.querySelector("main");
            mainElement.appendChild(document.querySelector("#subjects-template").content.cloneNode(true).firstElementChild);
            mainElement.appendChild(document.querySelector("#messages-template").content.cloneNode(true).firstElementChild);
            mainElement.appendChild(document.querySelector("#message-output-template").content.cloneNode(true).firstElementChild);
            mainElement.appendChild(document.querySelector("#message-input-template").content.cloneNode(true).firstElementChild);
            mainElement.querySelector("slider").addEventListener("click", event => this.displayRootMessages());
            //mainElement.querySelector("button").addEventListener("click", event => this.sendMessage());

            this.displaySubjects();
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
                //there is no section class for input and uses li for listitems
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
				const sectionElement = document.querySelector("li.message");
				const inputElements = sectionElement.querySelectorAll("img, textarea");
				
				const message = JSON.parse(await Controller.xhr("/services/people", "GET", {"Accept": "application/json"}, email, given, family, street, city));
				
				this.refreshAvatarSlider(sectionElement.querySelector("span.slider"), people);
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
				const sectionElement = document.querySelector("li.message");
				const inputElements = sectionElement.querySelectorAll("img, textarea");
				
				const message = JSON.parse(await Controller.xhr("/services/people", "GET", {"Accept": "application/json"}, email, given, family, street, city));
				
				this.refreshAvatarSlider(sectionElement.querySelector("span.slider"), people);
			}
			catch(error){
				this.displayError(error);
			}
		}
	});



    /**
     * Sends Messeages
     */

    Object.defineProperty(MessagesController.prototype, "persistsMessages", {
		enumerable: false,
		configurable: false,
		value: async function (parentElement, subjectIdentity) {
			if (!Controller.sessionOwner) return;
			this.displayError();
			
			try{
                //there is no section class for input and uses li for listitems
				const sectionElement = document.querySelector("li.message");
				const inputElements = sectionElement.querySelectorAll("img, textarea");
				
				const message = JSON.parse(await Controller.xhr("/services/people", "GET", {"Accept": "application/json"}, email, given, family, street, city));
				
				this.refreshAvatarSlider(sectionElement.querySelector("span.slider"), people);
			}
			catch(error){
				this.displayError(error);
			}
		}
	});


    window.addEventListener("load", event => {
        const anchor = document.querySelector("header li:nth-of-type(1) > a");
		const controller = new MessagesController();
		anchor.addEventListener("click", event => controller.displayMessageEditor());
	});

})