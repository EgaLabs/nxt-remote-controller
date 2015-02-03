var
  path       = [],
  letters    = "a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð",
  validName  = new RegExp("^(((["+letters+"]+)((-["+letters+"]+)*)(('(["+letters+"]+)?)*)|())( ?)){1,4}$"),
  validEmail = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
  validCoord = /^[+-]?([0-9]|[1-9][0-9]|[1][0-7][0-9]|180)(\.[0-9]+)?$/,
  validToken = /^[a-fA-F0-9]{32}$/,
  anything   = /^.+$/,
  userdata   = depot("userdata"),
  fields     = ["name", "email", "latitude", "longitude", "short_location", "long_location"],
  validators = [validName, validEmail, validCoord, validCoord, anything, anything],
  storage    = function (data) {
    if (!data) return userdata.get(userdata.ids[0]);
    return userdata.update(userdata.ids[0], data);
  },
  i = 0,
  privateData = null;

/*
 * Init storage.
 */
if ( !storage() ) {
  userdata.save({
    name: "",
    email: "",
    latitude: "",
    longitude: "",
    short_location: "",
    long_location: "",
    token: "",
    timestamp: ""
  });
}

privateData = storage();
for (; i < fields.length; i++) {
  if ( !validators[i].test(privateData[ fields[i] ]) ) {
    storage({ token: "", peer: "" });
    window.location.href = "/login";
  }
}

if (!Modernizr.websockets) {
  path.push("css/error-nosockets.css");
} else if (!Modernizr.getusermedia) {
  path.push("css/error-nomedia.css");
}

if (path.length > 0) {
  Modernizr.load.call(this, ["css/error.css"].concat(path));
} else {
  Modernizr.load(
    [
      "/res/js/md5.min.js",
      "/res/js/keymaster.min.js",
      "/socket.io/socket.io.js",
      "/res/js/peer.min.js",
      "/res/js/Masonry.min.js",
      "/res/js/Mustache.min.js",
      "/res/js/when-then.min.js",
      "/res/js/marmottajax.min.js",
      "js/script.js"
    ]
  );
}
