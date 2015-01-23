var path = [];

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
    "/res/js/marmottajax.min.js",
    "js/script.js"
    ]
  );
}
