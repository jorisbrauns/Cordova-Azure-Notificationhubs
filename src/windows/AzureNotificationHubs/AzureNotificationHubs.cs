using System;
using Windows.Networking.PushNotifications;
using Microsoft.WindowsAzure.Messaging;
using Windows.Foundation;
using System.Threading.Tasks;
using System.IO;
using System.Runtime.Serialization.Json;
using Windows.UI.Popups;

namespace AzureNotificationHubs
{
    public sealed class AzureNotificationHubs
    {

        private static TaskCompletionSource<string> levelCompletionSource = new TaskCompletionSource<string>();

        public static IAsyncOperation<string> register(string hubname, string connectionString)
        {
            return RegisterNotificationHub(hubname, connectionString).AsAsyncOperation();
        }

        private static async Task<string> RegisterNotificationHub(string hubname, string connectionString)
        {
            levelCompletionSource = new TaskCompletionSource<string>();

            RegisterHub(hubname, connectionString);

            return await levelCompletionSource.Task;
        }

        private static async void RegisterHub(string hubname, string connectionString)
        {
            var channel = await PushNotificationChannelManager.CreatePushNotificationChannelForApplicationAsync();

            
            var hub = new NotificationHub(hubname, connectionString);
            var result = await hub.RegisterNativeAsync(channel.Uri);

            // Displays the registration ID so you know it was successful
            if (result.RegistrationId != null)
            {
                levelCompletionSource.SetResult(result.RegistrationId);
            } else
            {
                levelCompletionSource.SetResult("RegistrationId is null");
            }
        }

    }
}
