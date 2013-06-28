define([
  'config',
  'WindowManager'
], function(config, windowManager) {
  var Window = function() {

  };

  var createRoot = function(request) {
    var stage = new Kinetic.Stage({
      container: config.containerId,
      width: request.width,
      height: request.height
    });
    var root = new Kinetic.Layer();
    windowManager.setRoot(stage, root);
  };

  Window.createFromRequest = function(request) {
  };

  return Window;
});
