require.config({
  baseUrl: "assets/javascripts",

  paths: {
    domReady: 'libs/domReady',
    Logger: 'libs/logger.min',
    lodash: 'libs/lodash.min',
    Kinectic: 'libs/kinetic-v4.5.4.min'
  }
});

require([
  'app',
  'domReady'
], function(App, domReady){
  domReady(function() {
    App.initialize();
  });
});
