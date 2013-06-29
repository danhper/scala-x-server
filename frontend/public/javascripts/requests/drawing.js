define([
  'lodash',
  'Kinectic',
  'model/WindowManager'
], function(_, Kinectic, windowManager) {
  var drawingRequests = {
    // TODO: set stroke and strokeWidth with GC
    PolyFillRectangleRequest: function(request) {
      var win = windowManager.get(request.drawable);
      _(request.rectangles).forEach(function(rectangle) {
        win.drawable.add(new Kinetic.Rect({
          x: rectangle.x,
          y: rectangle.y,
          width: rectangle.width,
          height: rectangle.height,
          stroke: 'black',
          strokeWidth: 1
        }));
      });
      windowManager.updateDisplay();
    }
  };
  return drawingRequests;
});
