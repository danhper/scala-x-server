define([
  'lodash',
  'Logger',
  'config',
  'model/Window'
], function(_, Logger, config, Window) {
  var windows = {};

  var stage; // layer container
  var root;

  var windowManager = {
    add: function(win) {
      win.parent.addChild(win);
      windows[win.id] = win;
    },

    get: function(windowId) {
      if(!_(windows).has(windowId)) {
        Logger.error("window " + windowId + " does not exist");
      }
      return windows[windowId];
    },

    createRoot: function(request) {
      stage = new Kinetic.Stage({
        container: config.containerId,
        width: request.width,
        height: request.height
      });
      root = new Kinetic.Layer();
      var rootWindow = new Window({
        id: request.id,
        root: true,
        drawable: root
      });
      stage.add(root);
      windows[rootWindow.id] = rootWindow;
    },

    createFromRequest: function(request) {
      var parent = this.get(request.parent);
      var drawable = new Kinetic.Group({
        x: request.x,
        y: request.y,
        width: request.width,
        height: request.height,
        visible: false
      });
      var newWindow = new Window({
        id: request.id,
        parent: parent,
        drawable: drawable
      });
      this.add(newWindow);
    },

    updateDisplay: function() {
      stage.draw();
    }
  };

  return windowManager;
});
