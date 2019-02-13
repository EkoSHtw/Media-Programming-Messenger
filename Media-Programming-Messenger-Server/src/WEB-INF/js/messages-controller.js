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
            
            mainElement.querySelector("button").addEventListener("click", event => this.sendMessage());
        }
    });
    


    /**
     * Sends Messeages
     */


    Object.defineProperty(PeopleController.prototype, "sendMessage", {
		enumerable: false,
		configurable: false,
		value: async function () {
			if (!Controller.sessionOwner) return;
			this.displayError();
			
			try{
				const sectionElement = document.querySelector("section.message input");
				const inputElements = sectionElement.querySelectorAll("input");
				
				const message = JSON.parse(await Controller.xhr("/services/people", "GET", {"Accept": "application/json"}, email, given, family, street, city));
				
				this.refreshAvatarSlider(sectionElement.querySelector("span.slider"), people);
			}
			catch(error){
				this.displayError(error);
			}
		}
	});


    window.addEventListener("load", event => {
        const anchor = document.querySelector("header li:nth-of-type(4) > a");
		const controller = new MessagesController();
		anchor.addEventListener("click", event => controller.display());
	});

})