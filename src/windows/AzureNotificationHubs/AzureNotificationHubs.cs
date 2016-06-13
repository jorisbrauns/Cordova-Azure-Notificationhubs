/*
	Author: Joris Brauns
	http://blog.jorisbrauns.be

	Licensed to the Apache Software Foundation (ASF) under one
	or more contributor license agreements.  See the NOTICE file
	distributed with this work for additional information
	regarding copyright ownership.  The ASF licenses this file
	to you under the Apache License, Version 2.0 (the
	"License"); you may not use this file except in compliance
	with the License.  You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing,
	software distributed under the License is distributed on an
	"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
	KIND, either express or implied.  See the License for the
	specific language governing permissions and limitations
	under the License.
*/

using System;
using Windows.Networking.NotificationHubss;
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
            var channel = await NotificationHubsChannelManager.CreateNotificationHubsChannelForApplicationAsync();

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
