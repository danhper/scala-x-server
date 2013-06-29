define([
  'lodash',
  'Kinectic',
  'model/Window',
  'model/WindowManager',
  'model/ClientManager'
], function(_, Kinectic, Window, WindowManager, ClientManager) {
  var baseRequests = {
    CreateGCRequest: function(request) {
    },

    CreateRootRequest: function(request) {
      WindowManager.createRoot(request);
    },

    CreateWindowRequest: function(request, clientId) {
      var win = WindowManager.createFromRequest(request, clientId);
      ClientManager.get(clientId).addNode(win.drawable);
    },

    MapWindowRequest: function(request) {
      var win = WindowManager.get(request.window);
      win.drawable.show();
    }
  };

  return baseRequests;
});
