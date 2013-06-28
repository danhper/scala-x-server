define([
  'lodash',
  'model/Window'
], function(_, Window) {
  var windows = {};

  var stage; // layer container
  var root;

  var WindowManager = function() {
  };

  WindowManager.prototype.add = function(windowId, win) {
    windows[windowId] = win;
  };

  WindowManager.prototype.get = function(windowId) {
    if(!_(windows).has(windowId)) {
      throw "foo";
    }
    return windows[windowId];
  };

  WindowManager.prototype.setRoot = function(mainStage, layer) {
    if(!_.isUndefined(root)) {
      throw "foo";
    }
    stage = mainStage;
    root = layer;
    stage.add(root);
  };

  return new WindowManager();
});
