var express = require("express");
var app = express();

var isWindows = /^win/.test(process.platform);
var match = isWindows ? /\\server/ : /\/server/;
var index = match.exec(__dirname).index || __dirname.length;
var _dirname = __dirname.slice(0, index);

app.use("/", express.static(_dirname + "/public/res") );

module.exports.app = app;