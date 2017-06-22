'use strict';

class AJAXRequest {

  // Sends a plain GET request
  static get(url) {
    return new Promise(function (resolve, reject) {
      let req = new XMLHttpRequest();
      req.open("GET", url);
      req.onload = function () {
        if (req.status === 200) {
          if (req.response) {
            resolve(JSON.parse(req.response));
          } else {
            resolve();
          }
        } else {
          reject(new Error(req.statusText));
        }
      };

      req.onerror = function () {
        reject(new Error("Network error"));
      }

      req.send();
    });
  }

  // Sends a POST request with obj as the body
  static post(url, obj) {
    var objStr = JSON.stringify(obj);
    return new Promise(function (resolve, reject) {
      let req = new XMLHttpRequest();
      req.open("POST", url);
      req.setRequestHeader("Content-Type", "application/json");
      req.onload = function () {
        if (req.status === 200) {
          if (req.response) {
            resolve(JSON.parse(req.response));
          } else {
            resolve();
          }
        } else {
          reject(new Error(req.statusText));
        }
      };

      req.onerror = function () {
        reject(new Error("Network error"));
      }

      req.send(objStr);
    });
  }

}
