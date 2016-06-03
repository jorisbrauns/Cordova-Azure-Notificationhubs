using System;
using Windows.Networking.PushNotifications;
using Microsoft.WindowsAzure.Messaging;
using Windows.Foundation;
using System.Threading.Tasks;

namespace AzureNotificationHubs
{
    public sealed class AzureNotificationHubs
    {

        private static TaskCompletionSource<string> completionSource = new TaskCompletionSource<string>();

        public static IAsyncOperation<string> register(string hubname, string connectionString)
        {
            return RegisterNotificationHub(hubname, connectionString).AsAsyncOperation();
        }

        private static async Task<string> RegisterNotificationHub(string hubname, string connectionString)
        {
            completionSource = new TaskCompletionSource<string>();

            RegisterHub(hubname, connectionString);

            return await completionSource.Task;
        }

        private static async void RegisterHub(string hubname, string connectionString)
        {
            var channel = await PushNotificationChannelManager.CreatePushNotificationChannelForApplicationAsync();

            var hub = new NotificationHub(hubname, connectionString);
            var result = await hub.RegisterNativeAsync(channel.Uri);

            // Displays the registration ID so you know it was successful
            if (result.RegistrationId != null)
            {
                completionSource.SetResult(result.RegistrationId);
            }
            else
            {
                completionSource.SetResult("RegistrationId is null");
            }
        }

    }
}
