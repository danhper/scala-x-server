define([
  'Logger'
], function(Logger) {

  var Window = function(attrs) {
    Logger.debug("initializing window with args");
    Logger.debug(attrs);
    if(attrs) {
      _(attrs).forEach(function(value, key) {
        this[key] = value;
      }, this);
    }
    this.root = this.root || false;
    if(!this.root && _.isUndefined(this.parent)) {
      Logger.error("no parent defined");
    }
  };

  _.extend(Window.prototype, {
    children: {},

    addChild: function(child) {
      this.children[child.id] = child;
      this.drawable.add(child.drawable);
    }
  });

  return Window;
});
