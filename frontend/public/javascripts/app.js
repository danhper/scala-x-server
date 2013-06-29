define([
  'Logger',
  'config',
  'util/WsWrapper',
  'requests'
], function(Logger, config, WS, requests) {

  var initialize = function() {
    Logger.useDefaults();
    Logger.debug("initializing application with config:");
    Logger.debug(config);
    initializeWebsocket();
  };

  var initializeWebsocket = function() {
    var socket = new WS();

    socket.addOnOpenListener('connect', function() {
      console.log('connected');
    });

    socket.addOnMessageListener('echo', function(message) {
      console.log(message.data);
    });

    // socket.addOnMessageListener('handleRequset', function(message) {
    //   var data = JSON.parse(message);
    //   if(!_(requests).has(data.type)) {
    //     Logger.error("method " + data.type + " does not exist");
    //   }
    //   requests[data.type](data.request);
    // });

    socket.connect(config.wsURL);
  };

  return {
    initialize: initialize
  };
});
