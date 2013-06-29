define([
  'lodash'
], function(_) {

  var Client = function(id) {
    this.id = id;
  };

  _.extend(Client.prototype, {
    nodes: [],

    addNode: function(node) {
      nodes.push(node);
    },

    destroy: function() {
      _(nodes).forEach(function(node) {
        node.destroy();
      });
    }
  });

  return Client;
});
