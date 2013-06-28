define([
  'config',
  'WindowManager'
], function(config, windowManager) {

  var Window = function(attrs) {
    if(attrs) {
      _(attrs).forEach(function(value, key) {
        this[key] = value;
      });
    }
    this.root = this.root || false;
    if(!this.root && _.isUndefined(this.parent)) {
      throw "no parent defined";
    }
  };

  var createRoot = function(request) {
    var stage = new Kinetic.Stage({
      container: config.containerId,
      width: request.width,
      height: request.height
    });
    var rootLayer = new Kinetic.Layer();
    var rootWindow = new Window({
      root: true,
      drawable: root
    });
    windowManager.setRoot(stage, rootWindow);
    return rootWindow;
  };

  _.extend(Window.prototype, {
    children: {},

    createFromRequest: function(request) {
      if(request.parent < 0) {
        return createRoot(request);
      }
      var parent = windowManager.get(request.parent);
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
      return newWindow;
    },

    addChild: function(child) {
      children[child.id] = child;
    }
  });

  return Window;
});
