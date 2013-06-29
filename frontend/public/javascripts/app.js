define([
  'Logger',
  'config',
  'util/WsWrapper',
  'util/WsEventHandler'
], function(Logger, config, WS, WsEventHandler) {

  var initialize = function() {
    Logger.useDefaults();
    Logger.debug("initializing application with config:");
    Logger.debug(config);
    initializeWebsocket();
  };

  var initializeWebsocket = function() {
    var socket = new WS();

    socket.addOnOpenListener('connect', WsEventHandler.echoOnConnect);
    socket.addOnMessageListener('echo', WsEventHandler.echoOnMessage);
    socket.addOnMessageListener('handleRequest', WsEventHandler.handleRequest);

    socket.connect(config.wsURL);
  };

  return {
    initialize: initialize
  };
});
