define([
  'Logger'
], function(Logger) {

  var initialize = function() {
    Logger.useDefaults();
    Logger.debug("initializing application");
  };

  return {
    initialize: initialize
  };
});
