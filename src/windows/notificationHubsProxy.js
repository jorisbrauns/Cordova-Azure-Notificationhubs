cordova.commandProxy.add("notificationHubs", {
    register: function (successCallback, errorCallback, args) {
        if (args.length != 2) {
            errorCallback("Incorrect arguments");
            return;
        }
        AzureNotificationHubs.AzureNotificationHubs.register(args[0], args[1]).done(function(result){
            successCallback(result);
        }); 
    }
});