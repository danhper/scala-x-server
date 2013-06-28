define([
  'requests/text',
  'requests/drawing',
  'requests/base'
], function(textRequests, drawingRequests, baseRequests) {
  return _.merge({}, _.values(arguments));
});
