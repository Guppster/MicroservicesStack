"use strict";

var api = new API(AJAXRequest, "http://hotellnx114.torolab.ibm.com:8080");

window.onload = () => api.getServices().then(selectServices);

function selectServices(services) {
  // services-container
  var container = document.createElement("div");
  container.className = "services-container";

  // services-title
  var containerTitle = document.createElement("p");
  containerTitle.className = "services-title";
  containerTitle.appendChild(document.createTextNode("Services"));
  container.appendChild(containerTitle);

  // services
  var list = document.createElement("ul");
  list.className = "services";
  container.appendChild(list);

  // form
  var form = document.getElementById("service-form");

  Object.keys(services).forEach((key) => {
    new Service(list, key, services[key], form);
  });

  // attach to DOM
  var outer = document.getElementById("select-services");
  while (outer.firstChild) {
    outer.removeChild(outer.firstChild);
  }
  outer.appendChild(container);
}

class Service {

  constructor(parent, name, instances, form) {
    this.name = name;
    this.expanded = false;

    // service-container
    this.element = document.createElement("li");
    this.element.className = "service-container";

    // instances
    this.list = document.createElement("ul");
    this.list.className = "instances";
    this.list.style.display = "none";

    this.instances = [];
    instances.forEach((instance) => {
      // if (instance.status === "Ready") {
      this.instances.push(new Instance(this.list, instance, form));
      // }
    });

    // availability
    this.available = this.instances.length;

    // service-title
    let elementTitle = document.createElement("p");
    elementTitle.className = "service-title";
    elementTitle.appendChild(document.createTextNode(this.name));
    elementTitle.onclick = () => this.toggle();
    this.element.appendChild(elementTitle);

    // service-status
    let elementStatus = document.createElement("span");
    elementStatus.className = "service-status";
    elementStatus.appendChild(document.createTextNode(this.available + " instances ready"));
    elementTitle.appendChild(elementStatus);

    // attach to parent
    this.element.appendChild(this.list);
    parent.appendChild(this.element);
  }

  toggle() {
    // this.instances.forEach((instance) => instance.toggle());

    if (this.expanded) {
      this.list.style.display = "none";
      this.expanded = false;
    } else {
      this.list.style.display = "block";
      this.expanded = true;
    }
  }

}

class Instance {

  constructor(parent, instance, form) {
    // container
    this.instance = instance;
    this.link = document.createElement("a");

    // form entry
    this.form = new Entry(form, instance.Name, instance.Properties);
    this.link.onclick = () => this.form.open();

    let element = document.createElement("li");
    element.className = "instance-container";
    this.link.appendChild(element);

    // instance-title
    let elementTitle = document.createElement("p");
    elementTitle.className = "instance-title";
    elementTitle.appendChild(document.createTextNode(this.instance.Url + " @ " + this.instance.Version));
    element.appendChild(elementTitle);

    // instance-status
    let elementStatus = document.createElement("span");
    elementStatus.className = "instance-status";
    elementStatus.appendChild(document.createTextNode(this.instance.Status));
    elementTitle.appendChild(elementStatus);


    parent.appendChild(this.link);
  }

}

class Entry {

  constructor(parent, name, fields) {
    this.parent = parent;
    this.name = name;

    this.title = document.createElement("p");
    this.title.className = "form-title";
    this.title.appendChild(document.createTextNode(this.name));

    this.form = document.createElement("form");
    this.inputs = [];

    fields.forEach((field) => {
      let group = document.createElement("div");
      group.className = "form-group";
      this.form.appendChild(group);

      let label = document.createElement("label");
      label.appendChild(document.createTextNode(field));
      group.appendChild(label);

      let input = document.createElement("input");
      this.inputs.push(input);
      input.type = "text";
      input.name = field;
      group.appendChild(input);
    });

    // actions
    let actions = document.createElement("div");
    actions.className = "actions";

    let submit = document.createElement("button");
    submit.appendChild(document.createTextNode("Submit"));
    submit.onclick = () => this.submit();
    actions.appendChild(submit);

    let cancel = document.createElement("button");
    cancel.appendChild(document.createTextNode("Cancel"));
    cancel.onclick = () => this.close();
    actions.appendChild(cancel);

    this.form.appendChild(actions);
  }

  open() {
    while (this.parent.firstChild) {
      this.parent.removeChild(this.parent.firstChild);
    }
    this.parent.appendChild(this.title);
    this.parent.appendChild(this.form);
    this.parent.classList.add("active");
  }

  submit() {
    var request = {
      Service: this.name,
      Properties: {}
    };
    this.inputs.forEach((input) => {
      request.Properties[input.name] = input.value;
    });

    api.createRun(request).then(() => this.close());

    // To not make the page reload
    return false;
  }

  close() {
    this.parent.classList.remove("active");

    return false;
  }

}
