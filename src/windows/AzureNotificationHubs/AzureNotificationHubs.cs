using System;
using Windows.Networking.PushNotifications;
using Microsoft.WindowsAzure.Messaging;
using Windows.Foundation;
using System.Threading.Tasks;

namespace AzureNotificationHubs
{
    public sealed class AzureNotificationHubs
    {
        private static TaskCompletionSource<string> _completionSource = new TaskCompletionSource<string>();

        // Public api entrypoint
        public static IAsyncOperation<string> register(string hubname, string connectionString)
        {
            return RegisterNotificationHubs(hubname, connectionString).AsAsyncOperation();
        }

        public static IAsyncOperation<string> unRegister(string hubname, string connectionString)
        {
            return UnRegister(hubname, connectionString).AsAsyncOperation();
        }

        private static async Task<string> UnRegister(string hubname, string connectionString)
        {
            _completionSource = new TaskCompletionSource<string>();

            UnRegisterHub(hubname, connectionString);

            return await _completionSource.Task;
        }

        private static async Task<string> RegisterNotificationHubs(string hubname, string connectionString)
        {
            _completionSource = new TaskCompletionSource<string>();

            RegisterHub(hubname, connectionString);

            return await _completionSource.Task;
        }

        private static async void RegisterHub(string hubname, string connectionString)
        {
            var channel = await PushNotificationChannelManager.CreatePushNotificationChannelForApplicationAsync();

            var hub = new NotificationHub(hubname, connectionString);
            var result = await hub.RegisterNativeAsync(channel.Uri);

            // Displays the registration ID so you know it was successful
            _completionSource.SetResult(result.RegistrationId ?? "RegistrationId is null");
        }

        private static async void UnRegisterHub(string hubname, string connectionString)
        {
            var channel = await PushNotificationChannelManager.CreatePushNotificationChannelForApplicationAsync();
            var hub = new NotificationHub(hubname, connectionString);
            await hub.UnregisterNativeAsync();
            _completionSource.SetResult( "success" );
        }

    }
}
