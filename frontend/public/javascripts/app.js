define([
  'Logger',
  'config',
  'controllers/WsWrapper'
], function(Logger, config, WS) {

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
    socket.connect(config.wsURL);
  };

  return {
    initialize: initialize
  };
});
