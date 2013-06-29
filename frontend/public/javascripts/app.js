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
      switch(data.type) {
        case "request":
          var content = data.content;
          if(!_(requests).has(content.action)) {
            Logger.warn("method " + content.action + " does not exist");
          } else {
            requests[content.action](content.request);
          }
          break;
        case "clientUpdate":
          break;
        default:
          Logger.error("unkonwn request type " + data.type);
      }

    });

    socket.connect(config.wsURL);
  };

  return {
    initialize: initialize
  };
});
