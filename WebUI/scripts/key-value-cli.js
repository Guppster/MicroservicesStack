'use strict';

const SLASH = '/';
const SPACE = ' ';
const COLON = ':';
const UNDERSCORE = '_';
const DASH = '-';

class API {

  constructor(http, baseURL) {
    this.http = http;
    this.baseURL = baseURL;
    this.ready = false;

    this.http.get(this.baseURL + SLASH + 'status')
             .then(() => {
               this.ready = true;
             }, (e) => {
               console.log("API is not ready. Subsequent requests may fail");
             });
  }

  getServices() {
    return this.http.get(this.baseURL + SLASH + 'services');
  }

  createRun(properties) {
    return this.http.post(this.baseURL + SLASH + 'run' + SLASH + 'create', properties)
  }
}
