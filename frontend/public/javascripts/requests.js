define([
  'requests/text',
  'requests/drawing',
  'requests/base'
], function() {
  var requests = {};
  _(arguments).forEach(function(r) {
    _.merge(requests, r);
  });
  return requests;
});
