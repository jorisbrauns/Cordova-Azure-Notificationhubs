cordova.commandProxy.add("NotificationHubs", {
    register: function (successCallback, errorCallback, args) {
        AzureNotificationHubs.AzureNotificationHubs.register(args[0].hubname, args[0].endpoint).done(function (result) {
            successCallback(result);
        });
    },
    unRegister: function (successCallback, errorCallback, args) {
        AzureNotificationHubs.AzureNotificationHubs.unRegister(args[0].hubname, args[0].endpoint).done(function (result) {
            successCallback(result);
        });
    }
});