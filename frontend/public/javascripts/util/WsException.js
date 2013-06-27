define(function() {
  var WsException = function(message) {
    this.name = "WsException";
    this.message = message;
  };

  return WsException;
});
