module.exports = {
    register: function (config, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "NotificationHubs", "register", [config]);
    }
};