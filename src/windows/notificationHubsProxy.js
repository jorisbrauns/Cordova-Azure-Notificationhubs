cordova.commandProxy.add("NotificationHubs", {
    register: function (successCallback, errorCallback, args) {
        
        AzureNotificationHubs.AzureNotificationHubs.register(args.hubnane, args.endpoint).done(function(result){
            successCallback(result);
        }); 
    }
});