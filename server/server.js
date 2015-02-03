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

var fs        = require("fs");
var http      = require("http");
var https     = require("https");
var url       = require("url");
var mime      = require("mime");
var _         = require("underscore");
var express   = require("express");
var bodyParse = require("body-parser");
var io        = require("socket.io");
var peer      = require("peer");
var app       = express();
var socketJwt = require("socketio-jwt");
var jsonwt    = require("jsonwebtoken");
var MD5       = require("MD5");
var Users     = require("./Users.js");
var _users    = new Users();
var User      = require("./User.js");
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



/*
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

  /*
   * Permanently redirect to HTTPS. Temporally disallowed for testing purposes.
   */
  response.writeHead(301, {
    "Content-Length": "0",
    "Location": "https://localhost:" + secure_port + url.parse(request.url).pathname
  });

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
app.use( bodyParse.json() );
app.use( bodyParse.urlencoded({
  extended: true
}) );
app.set("secretPass", (Math.random() * 0xffffffff | 0).toString());

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
  
  if (!!path.settings) {
    require(_dirname + path.settings)(app, config, path, _dirname, _users);
  } else {
    app.use(virtual, express.static(_dirname + real, object) );
  }
  
});

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

var getUsers = function () {
  var tmp_users = _users.filterById(function (id, value, index) {
    return value.host === false && value.connected === true;
  }),
  users = {};
  _.each(tmp_users, function (value, key) {
    users[_users.originalIdByToken(value)] = value.secure_parsed();
  });
  return users;
},
getHosts = function () {
  var tmp_hosts = _users.filterById(function (id, value, index) {
    return value.host === true && value.connected === true;
  }),
  hosts = {};
  _.each(tmp_hosts, function (value, key, index) {
    hosts[_users.originalIdByToken(value)] = value.secure_parsed();
  });
  return hosts;
},
denyAccess = function (accept, args, ctx, token) {
  console.log("Denegado acceso con token: " + token.substr(0, 50) + "\u2026");
  accept.apply(ctx, args);
}

io = io.listen(HttpsServer);

io.set("authorization", function (handshakeData, accept) {
  var token = url.parse(handshakeData.url, true).query.token;
  try {
    jsonwt.verify(token, app.get("secretPass"), function (err, data) {
      if (err) {
        denyAccess(accept, [err, false], this, token);
      } else {
        handshakeData.decoded_token = _.extend(data, { token: token, connected: false, peer: MD5(token) });
        accept(err, true);
      }
    });
  } catch (e) {
    denyAccess(accept, [err, false], this, token);
  }
});

io.on("connection", function (socket) {

  var data = _.extend(socket.request.decoded_token, {
    connected: true,
    id: socket.id
  });

  var user;
  if (_users.existsByToken(data)) {
    user = _users.findByToken(data);
    user.id = socket.id;
    user.connected = true;
  } else {
    user = new User(data);
    _users.saveById(user);
  }

  console.log((user.host ? "Host" : "Usuario") + " conectado '" + user.name + "' con socket id '" + user.id + "'' y id P2P '" + user.peer + "'.");

  var secure_user = {},
      hosts = getHosts(),
      users = getUsers();
  secure_user[_users.originalIdByToken(user)] = user.secure_parsed();

  socket.on("disconnect", function () {
    console.log((user.host ? "Host" : "Usuario") + " desconectado '" + user.name + "' con socket id '" + user.id + "' y id P2P '" + user.peer + "'.");
    user.connected = false;
    io.emit("leave_member", { members: secure_user });
    io.emit("hosts_count", { count: _.size(hosts) - 1 });
  });

  if (user.host === true) {
    secure_user.peer = undefined;
    delete secure_user.peer;
    _.each(users, function (value, key) {
      io.to(value.id).emit("join_member", { members: secure_user });
      io.to(value.id).emit("hosts_count", { count: _.size(hosts) });
    });
    socket.emit("join_member", { members: users });
    socket.on("call", function (peer) {
      _.each(getUsers(), function (value, key) {
        if (peer === value.peer) {
          console.log("Host '" + user.name + "' (" + user.peer + ") llamando al usuario '" + value.name + "' (" + peer + ").");
          io.to(value.id).emit("receive_call", { from: user.peer, name: user.name, email: user.email });
        }
      });
    });
    socket.on("init_stream", function (peer) {
      _.each(getUsers(), function (value, key, index) {
        if (peer === value.peer) {
          io.to(value.id).emit("init_stream", { from: user.peer });
        }
      });
    });
    socket.on("stop_stream", function (peer) {
      _.each(getUsers(), function (value, key, index) {
        if (peer === value.peer) {
          io.to(value.id).emit("stop_stream", { from: user.peer });
        }
      });
    });
  } else if (user.host === false) {
    _.each(hosts, function (value, key) {
      io.to(value.id).emit("join_member", { members: secure_user });
    });
    socket.emit("join_member", { members: hosts });
    socket.emit("hosts_count", { count: _.size(hosts) });

    socket.on("answer", function (data) {
      _.each(getHosts(), function (value, key) {
        if (data.to === value.peer) {
          console.log("Usuario '" + user.name + "' (" + user.peer + ") ha aceptado la llamada de '" + value.name + "' (" + data.to + ").");
          io.to(value.id).emit("answered", { from: user.peer, state: data.state });
        }
      });
    });

    socket.on("motor", function (data) {
      var
        x = data.x,
        y = data.y,
        mod = Math.min(1, Math.sqrt(x * x + y * y)),
        angle = Math.atan(y / x) || 0,
        left  = 0,
        right = 0;
      if (x !== 0 || y !== 0) {
        if (x < 0) {
          angle += Math.PI;
        } else if (x >= 0 && y < 0) {
          angle += 2 * Math.PI;
        }

        if (x >= 0 && y >= 0) {
          left  = mod;
          right = y * Math.sin(angle);
        } else if (x < 0 && y >= 0) {
          left  = y * Math.sin(angle);
          right = mod;
        } else if (x < 0 && y < 0) {
          left  = -y  * Math.sin(angle);
          right = -mod;
        } else if (x >= 0 && y < 0) {
          left  = -mod;
          right = -y * Math.sin(angle);
        }
      }
      _.each(getHosts(), function (value, key) {
        if (data.to !== value.peer) return;
        io.to(value.id).emit("motors", {
          b: right,
          c: left,
          from: user.peer
        });
      });
    });
  }
});


/*
 *  
 */
var server = peer.PeerServer({
  port: 9000,
  debug: true,
  ssl: {
    key: key,
    cert: cert
  },
  path: "/"
});
server.on("disconnect", function (id) {

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