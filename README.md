# Cordova-Azure-Notificationhubs
Cordova plugin for Azure Notification Hubs. This plugin offers support to receive and handle native push notifications from Azure Notification Hubs with a single unified API, and with no dependency on any other plugins.

Send push notifications to any platform, from any backend. Use Notification Hubs to:
- Reach all major platforms including iOS, Android, Windows
- Use with any backend, .NET, Java, PHP, or Python, cloud or on-premises
- Broadcast push notifications to millions of users at once or tailor notifications to individual users
- Tailor notifications by audience, language, location, and other personal preferences
- Scale instantly to millions of devices
- Target content to dynamic user segments
- Achieve extreme scale

##Universal Windows Platform Sample
Before you're app is able to retreive push notifications from the Azure Notification Hubs you need to associate the app with the Windows Store and configure the WNS in Azure.

        azureNotificationHubs.register("<HubName>","<DefaultListenSharedAccessSignature>", function (result) {
                document.getElementById("notificationid").innerHTML = result;
        }, function () {
                console.log("Plugin failed registering azure notification hubs");
        });

### Important!
Make sure to configure your build.json with the following information (replace path with your own certificate and publisherid):

        "windows": {
                "release": {
                    "packageCertificateKeyFile": "res\\native\\windows\\CordovaApp.pfx",
                    "publisherId": "<replace with your publisherId>"
                },
                "debug": {
                    "packageCertificateKeyFile": "res\\native\\windows\\CordovaApp.pfx",
                    "publisherId": "<replace with your publisherId>"
                }
        }
