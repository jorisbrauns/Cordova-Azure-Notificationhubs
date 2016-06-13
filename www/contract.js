var Exec = require("cordova/exec");
var Promise = require("./Promise");

module.exports = {
    /**
    * Asynchronously registers the device for native notifications.
    * http://msdn.microsoft.com/en-us/library/dn339332.aspx
    *
    * @config {Object} hubname, endpoint, gcm, tags (not supported currently).
    */
    register: function (config) {
        var deferral = new Promise.Deferral(),
        successCallback = function (result) {
            deferral.resolve(result);
        },
        errorCallback = function (err) {
            deferral.reject(err);
        };

        Exec(successCallback, errorCallback, "NotificationHubs", "register", [config]);

        return deferral.promise;
    },

    /**
    * Asynchronously unregisters the native registration on the application.
    * http://msdn.microsoft.com/en-us/library/microsoft.windowsazure.messaging.notificationhub.unregisternativeasync.aspx
    */
    unregister: function() {
        var deferral = new Promise.Deferral(),
        successCallback = function (result) {
            deferral.resolve(result);
        },
        errorCallback = function (err) {
            deferral.reject(err);
        };

        Exec(successCallback, errorCallback, "NotificationHubs", "unregister", []);

        return deferral.promise;
    }
};