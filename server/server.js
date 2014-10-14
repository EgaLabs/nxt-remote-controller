/*
 * @user https://github.com/Egatuts
 * @repo https://github.com/Egatuts/nxt-remote-controller.git
 * @file https://github.com/Egatuts/nxt-remote-controller/blob/master/server/server.js
 * @license MIT (http://www.opensource.org/licenses/MIT)
 */

/*
 * Shortcuts.
 */
var c        = console || {};
var nope     = function () {};
var chaining = function (a, ctx, b) {
  return function () {
	  b.apply(ctx, arguments);
	  return a;
  };
};

var Log = (function (root) {

  root.d = chaining(root, c, c.log      || nope);
  root.e = chaining(root, c, c.error    || nope);
  root.w = chaining(root, c, c.warn     || nope);
  root.i = chaining(root, c, c.info     || nope);
  root.t = chaining(root, c, c.time     || nope);
  root.te = chaining(root, c, c.timeEnd || nope);

  return root;
})({});
var quit = function () {
	Log.e.apply(this, arguments);
	process.exit(1);
};


/*
 * Loading dependencies.
 */
Log.t("start").d("Starting server...");

var fs      = require("fs");
var http    = require("http");
var https   = require("https");
var url     = require("url");
var mime    = require("mime");
var _       = require("underscore");
var express = require("express");
var io      = require("socket.io");
var app     = express();
var Users   = require("./Users.js");
var User    = require("./User.js");
Log.i("Dependencies: OK.");



/*
 * Loading configuration and setting up some variables.
 */
var config = require("../settings.json");
var args = process.argv.slice(2);

var isWindows = /^win/.test(process.platform);
var _dirname = isWindows ? __dirname.replace(/\\server$/, "") : __dirname.replace(/\/server$/, "");

var http_cfg  = config.http;
var https_cfg = config.https;

var unsecure_ip   = args[0] || http_cfg.ip    || "localhost" || "127.0.0.1";
var unsecure_port = args[1] || http_cfg.port  || 80;
var secure_ip     = args[2] || https_cfg.ip   || "localhost" || "127.0.0.1";
var secure_port   = args[3] || https_cfg.port || 443;


Log.i("Config file (settings.json): OK.");



/**
 * Handles file requests, supported methods and redirects to HTTPS.
 */
var redirect = function (request, response) {

  /*
   * Exit if request method is not GET.
   */
  if (request.method !== "GET") {
	response.writeHead(405);
	response.end("Unsupported request method. Only GET requests allowed.");
	return;
  }

  /*
   * Permanently redirect to HTTPS. Temporally disallowed for testing purposes.
   */
  /*
  response.writeHead(301, {
    "Content-Length": "0",
    "Location": "https://" + secure_ip + ":" + secure_port + url.parse(resquest.url).pathname
  });
   */

  /*
   * If redirect didn't worked we send the redirect path.
   */
  var path = _dirname + config.redirect;
  var reqPath = path + url.parse(request.url).pathname;
  var exists = false;
  var readUri = "";
  var file = null;

  try {
    exists = fs.existsSync(reqPath) && fs.lstatSync(reqPath).isFile();
  } catch (e) {
	quit("Unknown error: " + e.message);
  }
  readUri = exists ? reqPath : path + "/index.html";

  response.setHeader("Content-Type", mime.lookup(readUri) );
  file = fs.createReadStream( readUri );
  file.pipe( response );

};


/*
 * Dummy HTTP server that only handles requests and redirects them to HTTPS.
 */
var HttpServer = http.createServer(redirect);


/*
 * We load certificates.
 */
try {
  var key  = fs.readFileSync(_dirname + https_cfg.key,  "utf-8");
  var cert = fs.readFileSync(_dirname + https_cfg.cert, "utf-8");
} catch (e) {
  if (e.errno === 34 || e.code === "ENOENT") {
    quit("The file " + e.path + " doesn't exists. Run the Shell script located at /sh-scripts/cert.sh.");
  } else if (e.code === "EACCESS") {
	quit("Access error. You (or the program) don't have the required permission.");
  }
  quit("Unknown error: " + e.message);
}
Log.i("Loaded certificates.");


/*
 * We enable gzip compression.
 */
app.use( express.compress() );

/*
 * We serve all paths defined in settings.json.
 * If the virtual path is a file we serve it as a static one.
 * Else as a normal directory.
 */
_.each(config.paths, function (pathObject, index) {
	
  var path    = pathObject;
  var real    = path.real;
  var virtual = path.virtual;
  var object = {
    index: "index.html"
  };
  
  if (_.isString(path.static)) {
	  object = require(_dirname + path.static);
  } else if (_.isObject(path.static)) {
    object = path.static;
  }
  
  if (path.settings) {
    require(_dirname + path.settings)(app, config, path, _dirname);
  } else {
    app.use(virtual, express.static(_dirname + real, object) );
  }
  
});

/*
 * Broken code.
 _.each(config.vhosts, function (file, location) {
  var conf = require(_dirname + file);
  if (!conf.app) throw new Error("Virtual host file must export server config to as \"app\".");
  app.use( express.vhost(location, conf.app) );
});*/

/*
 * HTTPS server using express.
 */
var HttpsServer = https.createServer({
  key : key,
  cert: cert
}, app);




/*
 ***************************************************************************
 * Here starts sockets connection.
 */
var users = new Users();
var user;
io = io.listen(HttpsServer);
io.on("connection", function (socket) {
  user = new User(null, socket.id);
  users.add(user);
  Log.i("Added user: " + user);
});





/*
 ***************************************************************************
 * We start the server listening at the end of the script because of the routes.
 */


HttpServer.listen(unsecure_port, unsecure_ip);
HttpsServer.listen(secure_port, secure_ip);

Log.te("start")
   .d("HTTP server running at: http://%s:%d", unsecure_ip, unsecure_port)
   .d("HTTPS server running at: https://%s:%d", secure_ip, secure_port);
