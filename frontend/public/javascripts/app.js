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
    Logger.debug("available requests: ");
    Logger.debug(requests);
    initializeWebsocket();
  };

  var initializeWebsocket = function() {
    var socket = new WS();

    socket.addOnOpenListener('connect', function() {
      Logger.debug('connected');
    });

    socket.addOnMessageListener('echo', function(message) {
      Logger.debug(message.data);
    });

    socket.addOnMessageListener('handleRequest', function(message) {
      var data = JSON.parse(message.data);
      if(!_(requests).has(data.type)) {
        Logger.warn("method " + data.type + " does not exist");
      } else {
        requests[data.type](data.request);
      }
    });

    socket.connect(config.wsURL);
  };

  return {
    initialize: initialize
  };
});
