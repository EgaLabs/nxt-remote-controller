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
var chaining = function (a, b) {
  return function () {
	b.apply(this, arguments);
	return a;  
  };
};

var Log  = {
  d  : chaining(Log, c.log     || nope),
  e  : chaining(Log, c.error   || nope),
  w  : chaining(Log, c.warn    || nope),
  i  : chaining(Log, c.info    || nope),
  t  : chaining(Log, c.time    || nope),
  te : chaining(Log, c.timeEnd || nope)
};
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
    "Location": "https://" + https_cfg.ip + ":" + https_cfg.port + url.parse(resquest.url).pathname
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