/**
 * de_sb_messenger.PeopleController: messenger people controller.
 * Copyright (c) 2013 Sascha Baumeister
 */
"use strict";

(function () {
	// imports
	const Controller = de_sb_messenger.Controller;

	// parameter names for person filter query
	const QUERY_PARAMETER_NAMES = Object.freeze(["email", "givenName", "familyName", "street", "city"]);


	/**
	 * Creates a new people controller that is derived from an abstract controller.
	 */
	const PeopleController = function () {
		Controller.call(this);
	}
	PeopleController.prototype = Object.create(Controller.prototype);
	PeopleController.prototype.constructor = PeopleController;


	/**
	 * Displays the associated view.
	 */
	Object.defineProperty(PeopleController.prototype, "display", {
		enumerable: false,
		configurable: false,
		writable: true,
		value: function () {
			if (!Controller.sessionOwner) return;
			this.displayError();

			try {
				const observingElement = document.querySelector("#people-observing-template").content.cloneNode(true).firstElementChild;
				const observedElement = document.querySelector("#people-observed-template").content.cloneNode(true).firstElementChild;
				this.refreshAvatarSlider(observingElement.querySelector("span.slider"), Controller.sessionOwner.peopleObservingReferences, person => this.toggleObservation(person.identity));
				this.refreshAvatarSlider(observedElement.querySelector("span.slider"), Controller.sessionOwner.peopleObservedReferences, person => this.toggleObservation(person.identity));

				const mainElement = document.querySelector("main");
				mainElement.appendChild(observingElement);
				mainElement.appendChild(observedElement);
				mainElement.appendChild(document.querySelector("#candidates-template").content.cloneNode(true).firstElementChild);
				mainElement.querySelector("button").addEventListener("click", event => this.queryPeople());
			} catch (error) {
				this.displayError(error);
			}
		}
	});


	/**
	 * Performs a REST based filter criteria query for matching people, and refreshes
	 * the people view's bottom avatar slider with the result.
	 */
	Object.defineProperty(PeopleController.prototype, "queryPeople", {
		enumerable: false,
		configurable: false,
		value: async function () {
			if (!Controller.sessionOwner) return;
			this.displayError();
			
			try{
				const sectionElement = document.querySelector("section.candidates");
				const inputElements = sectionElement.querySelectorAll("input");

				const email = inputElements[0].value.trim();
				const given = inputElements[1].value.trim();
				const family = inputElements[2].value.trim();
				const street = inputElements[3].value.trim();
				const city = inputElements[4].value.trim();
				
				const people = JSON.parse(await Controller.xhr("/services/people", "GET", {"Accept": "application/json"}, email, given, family, street, city));
				
				this.refreshAvatarSlider(sectionElement.querySelector("span.slider"), people);
			}
			catch(error){
				this.displayError(error);
			}
		}
	});


	/**
	 * Updates the session owner's observed people with the given person. Removes
	 * the given person if it is already observed by the owner, or adds it if not.
	 * @param {String} personIdentity the identity of the person to add or remove
	 */
	Object.defineProperty(PeopleController.prototype, "toggleObservation", {
		enumerable: false,
		configurable: false,
		value: async function (personIdentity) {
			if (!Controller.sessionOwner) return;
			this.displayError();
			
			try{
				//TODO is header requester identity already there? probably not but its needed? Yes
				const response = JSON.parse(await this.xhr("/services/people/"+ personIdentity +"/peopleObserved", "PUT", {"Accept": "application/json", "Content-Type": "application/json"} ));
				// adding and removing is handled by the service
				if (!response.ok) throw new Error("HTTP " + response.status + " " + response.statusText);
			}catch(error){
				this.displayError(error);
			}
		}
	});


	/**
	 * Perform controller callback registration during DOM load event handling.
	 */
	window.addEventListener("load", event => {
		const anchor = document.querySelector("header li:nth-of-type(3) > a");
		const controller = new PeopleController();
		anchor.addEventListener("click", event => controller.display());
	});
} ());