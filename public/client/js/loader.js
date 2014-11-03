var
  path       = [],
  letters    = "a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð",
  validName  = new RegExp("^(((["+letters+"]+)((-["+letters+"]+)*)(('(["+letters+"]+)?)*)|())( ?)){1,4}$"),
  validEmail = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
  validCoord = /^[+-]?([0-9]|[1-9][0-9]|[1][0-7][0-9]|180)\.[0-9]{15}$/,
  anything   = /^.+$/,
  storage    = depot("userdata"),
  fields     = ["name",    "email",    "latitude", "longitude", "location", "token"   ],
  validators = [validName, validEmail, validCoord, validCoord,  anything,   validToken],
  data       = function (data) {
    if (arguments.length === 0) return storage.get(0);
    return storage.update(0, data);
  };
  
/*
 * Init storage.
 */
storage.save({});
    
if (!Modernizr.websockets) {
  path.push("css/error-nosockets.css");
} else if (!Modernizr.getusermedia) {
  path.push("css/error-nomedia.css");
}

if (path.length > 0) {
  Modernizr.load.call(this, ["css/error.css"].concat(path));
} else {
  /**Modernizr.load(
    [
      "/socket.io/socket.io.js",
      "/res/js/Masonry.min.js",
      "/res/js/Mustache.min.js",
      "/res/js/when-then.min.js",
      "/res/js/pegasus.min.js",
      "js/script.js"
    ]
  );**/
}