define([
  'lodash',
  'Kinectic',
  'model/WindowManager',
  'model/ClientManager'
], function(_, Kinectic, WindowManager, ClientManager) {
  var drawingRequests = {
    // TODO: set stroke and strokeWidth with GC
    PolyFillRectangleRequest: function(request, clientId) {
      var win = WindowManager.get(request.drawable);
      var client = ClientManager.get(clientId);
      _(request.rectangles).forEach(function(rectangle) {
        var rectangleNode = new Kinetic.Rect({
          x: rectangle.x,
          y: rectangle.y,
          width: rectangle.width,
          height: rectangle.height,
          stroke: 'black',
          strokeWidth: 1
        });
        win.drawable.add(rectangleNode);
        client.addNode(rectangleNode);
      });
      WindowManager.updateDisplay();
    }
  };
  return drawingRequests;
});
