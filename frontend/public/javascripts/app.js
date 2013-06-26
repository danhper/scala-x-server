define([
  'Logger',
  'config'
], function(Logger, config) {

  var initialize = function() {
    Logger.useDefaults();
    Logger.debug("initializing application with config:");
    Logger.debug(config);
  };

  return {
    initialize: initialize
  };
});
