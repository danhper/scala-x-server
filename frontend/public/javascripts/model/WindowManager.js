define([
  'lodash',
  'model/Window'
], function(_, Window) {
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
        throw "window " + windowId + " does not exist";
      }
      return windows[windowId];
    },

    setRoot: function(mainStage, layer) {
      if(!_.isUndefined(root)) {
        throw "cannot change root window";
      }
      stage = mainStage;
      root = layer;
      stage.add(root);
    }
  };

  return windowManager;
});
