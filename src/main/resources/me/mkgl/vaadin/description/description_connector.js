window.me_mkgl_vaadin_description_Description = function () {

  var self = this;
  var fieldElement = this.getElement(this.getParentId());

  // Handle changes from the server-side
  this.onStateChange = function () {
    if (!self.element) {
      self.element = self.appendDescription(this.getState().text)
    } else {
      self.element.innerHTML = this.getState().text;
    }
  };

  this.appendDescription = function (text) {
    var description = document.createElement("div");
    description.id = "desc-id-" + self.getParentId();
    description.classList.add("v-caption");
    description.classList.add("v-caption-description");
    description.style.display = "block";
    description.setAttribute("aria-hidden", "true");
    description.setAttribute("for", fieldElement.id);
    description.innerHTML = text;

    var describedBy = fieldElement.getAttribute("aria-describedby") ? fieldElement.getAttribute("aria-describedby") + " " + description.id : description.id;
    fieldElement.setAttribute("aria-describedby", describedBy);

    var parent = fieldElement.parentElement;
    if (parent) parent.insertBefore(description, fieldElement.nextSibling);

    return description;
  };

  this.onUnregister = function () {
    var parent = fieldElement.parentElement;
    if (parent) parent.removeChild(self.element);
  }


};