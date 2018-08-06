var exec = require('cordova/exec');

// exports.coolMethod = function (arg0, success, error) {
//     exec(success, error, 'BackgroundServiceAndroid', 'coolMethod', [arg0]);
// };


function BackgroundServiceAndroid() {
    console.log("BackgroundServiceAndroid.js: is created");
}

BackgroundServiceAndroid.prototype.echo = function (arg0, success, error) {
    exec(success, error, 'BackgroundServiceAndroid', 'echo', [arg0]);
};

BackgroundServiceAndroid.prototype.getCallback = function (callback, success, error) {
    BackgroundServiceAndroid.prototype.callbackResult = callback;
    exec(success, error, "BackgroundServiceAndroid", 'callback', []);
}

// CALLBACK RESULT//
BackgroundServiceAndroid.prototype.callbackResult = (payload) => {
    console.log("Received callbackResult => ", payload);
}

var backgroundService = new BackgroundServiceAndroid();
module.exports = backgroundService;