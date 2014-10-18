require(["esri/map",
            "esri/Color",
            "esri/graphic",
            "esri/layers/GraphicsLayer",
            "esri/symbols/SimpleMarkerSymbol",
            "esri/geometry/Point",
            "dojo/domReady!"], function(Map, Color, Graphic, GraphicsLayer, SimpleMarkerSymbol, Point) {

            var map = new Map("map", {
                center: [-0.084242, 51.508725],
                zoom: 16,
                basemap: "topo"
                });

                map.on("load", function () {});


            function clearMap() {
                map.graphics.clear();
            }

            function renderEventOnMap(event) {
                var color = new Color([255,0,0,0.5]);
                renderLocation(SimpleMarkerSymbol.STYLE_SQUARE, color, event.location);

                for (i = 0; i < event.helpers.length; i++) {
                    color = new Color([100, 100, 100, 1]);
                    renderLocation(SimpleMarkerSymbol.STYLE_CROSS, color, event.helpers[i].location);
                }
            }

            function renderLocation(style, color, location) {
                var symbol = new SimpleMarkerSymbol().setStyle(style).setColor(color);
                var point = new Point([location.latitude, location.longitude]);
                var graphic = new Graphic(point, symbol);
                map.graphics.add(graphic);
            }


            var stompClient = null;

            function connect() {
                var socket = new SockJS('websockets/events');
                stompClient = Stomp.over(socket);
                stompClient.connect({}, function(frame) {
                    console.log('Connected: ' + frame);
                    stompClient.subscribe('/topic/events', function(response){
                        updateMap(JSON.parse(response.body));
                    });
                });
            }

            function disconnect() {
                stompClient.disconnect();
                setConnected(false);
                console.log("Disconnected");
            }

            function sendName() {
                var name = document.getElementById('name').value;
                stompClient.send("/app/events", {}, {});
            }

            function updateMap(events) {

                    clearMap();
                    for (i = 0; i < events.length; i++) {
                        renderEventOnMap(events[i]);
                    }

            }

            connect();

    });



