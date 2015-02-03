var path = [];
if (!Modernizr.getusermedia) {
  path.push("css/error-nomedia.css");
}

if (path.length > 0) {
  Modernizr.load.call(this, ["css/error.css"].concat(path));
} else {
  Modernizr.load(
    [
      "/res/js/peer.min.js",
      "js/script.js"
    ]
  );
}