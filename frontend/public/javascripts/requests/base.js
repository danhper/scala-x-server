define([
  'lodash',
  'Kinectic',
  'model/Window',
  'model/WindowManager'
], function(_, Kinectic, Window, windowManager) {
  var baseRequests = {
    createGCRequest: function(request) {
    },

    createWindowRequest: function(request) {
      var newWindow = Window.createWindowRequest(request);
      windowManager.add(newWindow);
    }
  };
  return baseRequests;
});
