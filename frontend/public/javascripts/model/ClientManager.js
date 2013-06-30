define([
  'lodash',
  'Logger',
  'model/Client',
  'model/WindowManager'
], function(_, Logger, Client, WindowManager) {

  var clients = {};

  var ClientManager = function() {

  };

  _.extend(ClientManager.prototype, {
    add: function(client) {
      clients[client.id] = client;
    },

    get: function(id) {
      if(!_(clients).has(id)) {
        Logger.error("client " + id + " not found");
      }
      return clients[id];
    },

    remove: function(id, destroy) {
      if(typeof destroy === 'undefined') {
        destroy = true;
      }
      if(destroy) {
        clients[id].destroy();
        WindowManager.updateDisplay();
      }
      delete clients[id];
    }
  });

  return new ClientManager();
});
