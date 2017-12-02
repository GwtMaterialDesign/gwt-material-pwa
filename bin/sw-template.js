var cacheName = '{{cacheName}}';
var filesToCache = [  {{#each filesToCache}}
'{{.}}'{{#unless @last}},{{/unless}}
    {{/each}}
    ];


self.addEventListener('install', function (e) {
    console.log('[ServiceWorker] Install');
    e.waitUntil(
        caches.open(cacheName).then(function (cache) {
            console.log('[ServiceWorker] Caching app shell');
            return cache.addAll(filesToCache);
        })
    );
});


self.addEventListener('activate', function (e) {
    console.log('[ServiceWorker] Activate');
    e.waitUntil(
        caches.keys().then(function (keyList) {
            return Promise.all(keyList.map(function (key) {
                console.log('[ServiceWorker] Removing old cache', key);
                if (key !== cacheName) {
                    return caches.delete(key);
                }
            }));
        })
    );
});

self.addEventListener('fetch', function(event) {
    event.respondWith(
        // Try the cache
        caches.match(event.request).then(function(response) {
            // Fall back to network
            return response || fetch(event.request);
        }).catch(function() {
            // If both fail, show a generic fallback:
            // return caches.match('/offline.html');
            // However, in reality you'd have many different
            // fallbacks, depending on URL & headers.
            // Eg, a fallback silhouette image for avatars.
            sendMessageToAllClients("failingServer");
            return new Response("Can't Connect to server");
        })
    );
});

self.addEventListener('message', function (e) {
    if (e.data == 'skipWaiting') {
        self.skipWaiting();
    }
});

self.addEventListener('push', function(event) {
    console.log('[Service Worker] Push Received.');
    console.log(`[Service Worker] Push had this data: "${event.data.text()}"`);

    var jsonObject = JSON.parse(event.data.text());

    const title = jsonObject.title;
    const options = {
        body: jsonObject.description,
        icon: jsonObject.image
    };

    event.waitUntil(self.registration.showNotification(title, options));
});

self.addEventListener('notificationclick', function(event) {
    console.log('[Service Worker] Notification click Received.');

    event.notification.close();

    event.waitUntil(
        clients.openWindow('https://www.google.com.ph')
    );
});

function sendMessageToClient(client, message){
    return new Promise(function(resolve, reject){
        var messageChannel = new MessageChannel();

        messageChannel.port1.onmessage = function(e){
            if(e.data.error){
                reject(e.data.error);
            }else{
                resolve(e.data);
            }
        };

        client.postMessage(message, [messageChannel.port2]);
    });
}

function sendMessageToAllClients(msg){
    clients.matchAll().then(clients => {
        clients.forEach(client => {
            sendMessageToClient(client, msg).then(m => console.log("SW Received Message: "+m));
        })
    })
}