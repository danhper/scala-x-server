define([
  'lodash',
  'Logger',
  'requests',
  'model/Client',
  'model/ClientManager'
], function(_, Logger, Requests, Client, ClientManager) {

  var MessageHandler = {
    clientUpdate: function(content) {
      if(content.action === "add") {
        var client = new Client(content.clientId);
        ClientManager.add(client);
      } else if(content.action === "remove") {
        ClientManager.remove(content.clientId);
      } else {
        Logger.error("unkonwn action " + content.action);
      }
    },

    request: function(content) {
      if(!_(Requests).has(content.action)) {
        Logger.warn("method " + content.action + " does not exist");
      } else {
        Requests[content.action](content.request, content.clientId);
      }
    }
  };

  var WsEventHandler = {
    echoOnConnect: function() {
      Logger.debug('connect');
    },

    echoOnMessage: function(message) {
      Logger.debug(message.data);
    },

    handleRequest: function(message) {
      var data = JSON.parse(message.data);
      if(_(MessageHandler).has(data.type)) {
        MessageHandler[data.type](data.content);
      } else {
        Logger.error("unkonwn request type " + data.type);
      }

    }
  };

  return WsEventHandler;
});
