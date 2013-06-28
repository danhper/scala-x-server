define([
  'lodash',
  'Kinectic',
  'model/WindowManager'
], function(_, Kinectic, windowManager) {
  var textRequests = {
    // TODO: set fill and font with GC
    polyText8Request: function(request) {
      var win = windowManager.get(json.drawable);
      _(request.textItems).forEach(function(textInfo) {
        win.drawable.add(new Kinetic.Text({
          x: request.x + textInfo.delta,
          y: request.y,
          text: textInfo.text,
          fontSize: 12,
          fill: 'black'
        }));
      });
    }
  };
  return textRequests;
});
