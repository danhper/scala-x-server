define([
  'lodash',
  'Kinectic',
  'model/WindowManager',
  'model/ClientManager'
], function(_, Kinectic, WindowManager, ClientManager) {
  var textRequests = {
    // TODO: set fill and font with GC
    PolyText8Request: function(request, clientId) {
      var win = WindowManager.get(request.drawable);
      var client = ClientManager.get(clientId);
      _(request.textItems).forEach(function(textInfo) {
        var textNode = new Kinetic.Text({
          x: request.x + textInfo.delta,
          y: request.y,
          text: textInfo.text,
          fontSize: 12,
          fill: 'black'
        });
        win.drawable.add(textNode);
        client.addNode(textNode);
      });
      WindowManager.updateDisplay();
    }
  };
  return textRequests;
});
