define([
  'lodash',
  'Logger'
], function(_, Logger, WindowManager) {

  var Client = function(id) {
    Logger.debug("creating client with id " + id);
    this.id = id;
  };

  _.extend(Client.prototype, {
    nodes: [],

    addNode: function(node) {
      this.nodes.push(node);
    },

    destroy: function() {
      _(this.nodes).forEach(function(node) {
        node.destroy();
      });
    }
  });

  return Client;
});
