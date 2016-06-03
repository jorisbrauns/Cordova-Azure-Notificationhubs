module.exports = {
    register: function (hubname, connectionString, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "notificationHubs", "register", [hubname, connectionString]);
    }
};