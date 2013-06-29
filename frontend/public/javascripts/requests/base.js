define([
  'lodash',
  'Kinectic',
  'model/Window',
  'model/WindowManager'
], function(_, Kinectic, Window, windowManager) {
  var baseRequests = {
    CreateGCRequest: function(request) {
    },

    CreateRootRequest: function(request) {
      windowManager.createRoot(request);
    },

    CreateWindowRequest: function(request) {
      windowManager.createFromRequest(request);
    },

    MapWindowRequest: function(request) {
      var win = windowManager.get(request.window);
      win.drawable.show();
    }
  };

  return baseRequests;
});
