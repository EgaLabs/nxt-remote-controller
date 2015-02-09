/*
 * @user https://github.com/Egatuts
 * @repo https://github.com/Egatuts/nxt-remote-controller.git
 * @file https://github.com/Egatuts/nxt-remote-controller/blob/master/server/server.js
 * @license MIT (http://www.opensource.org/licenses/MIT)
 */

/*
 * Shortcuts for console chaining.
 */
var
  c        = console || {},
  nope     = function () {},
  chaining = function (a, ctx, b) {
    return function () {
      b.apply(ctx, arguments);
      return a;
    };
  },
  quit = function () {
    Log.e.apply(this, arguments);
    process.exit(1);
  },
  chalk = require("chalk");

/*
 * Log utils with chaining.
 * e.g. console.log(let1).error(errno);
 */
var Log = (function (root) {
  root.d = chaining(root, c, c.log      || nope);
  root.e = chaining(root, c, c.error    || nope);
  root.w = chaining(root, c, c.warn     || nope);
  root.i = chaining(root, c, c.info     || nope);
  root.t = chaining(root, c, c.time     || nope);
  root.te = chaining(root, c, c.timeEnd || nope);
  return root;
})({});

/*
 * Loading dependencies.
 */
Log.t("Start process")
   .i( chalk.yellow("Iniciando servidor…") )
   .i( chalk.yellow("Cargando dependencias…") );

var _          = require("underscore"),
    bodyParser = require("body-parser"),
    chalk      = require("chalk"),
    express    = require("express"),
    fs         = require("fs"),
    http       = require("http"),
    https      = require("https"),
    io         = require("socket.io"),
    ip         = require("ip")
    jsonwt     = require("jsonwebtoken"),
    md5        = require("MD5"),
    mime       = require("mime"),
    peer       = require("peer"),
    socketJwt  = require("socketio-jwt"),
    url        = require("url"),
    User       = require("./User.js"),
    Users      = require("./Users.js"),

    app = express(),
    _users = new Users();

Log.i( chalk.green("Dependencias cargadas") )
   .i( chalk.yellow("Cargando archivo de configuración… ") +
       chalk.bgMagenta("(") +
       chalk.bgYellow("seetings.json") +
       chalk.bgMagenta(")")
     );

/*
 * Loading configuration and setting up some variables.
 */
var config = require("../settings.json"),
    args = process.argv.slice(2),

    isWindows = /^win/.test(process.platform);
    _dirname = isWindows ? __dirname.replace(/\\server$/, "") : __dirname.replace(/\/server$/, ""),

    http_cfg  = config.http;
    https_cfg = config.https;

    unsecure_ip   = args[0] || http_cfg.ip    || "localhost" || "127.0.0.1";
    unsecure_port = args[1] || http_cfg.port  || 80;
    secure_ip     = args[2] || https_cfg.ip   || "localhost" || "127.0.0.1";
    secure_port   = args[3] || https_cfg.port || 443;

Log.i( chalk.green("Archivo de configuración cargado.") )
   .i( chalk.yellow("Cargando certificados…") );

/*
* We load certificates.
*/
try {
  var key  = fs.readFileSync(_dirname + https_cfg.key,  "utf-8");
  var cert = fs.readFileSync(_dirname + https_cfg.cert, "utf-8");
} catch (e) {
  if (e.errno === 34 || e.code === "ENOENT") {
    quit( chalk.red("El archivo ") +
          chalk.bgRed(e.path) +
          chalk.red(" no existe. Ejecuta los Shell scripts de ") +
          chalk.yellow("/sh-scripts") +
          " para crear los certificados.");
  } else if (e.code === "EACCESS") {
    quit( chalk.red("Error de acceso. Tu (o el programa) no tienes los permisos requeridos.") );
  }
  quit( chalk.red("Error inesperado: " + e.message) );
}
Log.i( chalk.green("Certificados cargados.") );

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
  var path = _dirname + config.redirect,
      reqPath = path + url.parse(request.url).pathname,
      exists = false,
      readUri = "",
      file = null;

  try {
    exists = fs.existsSync(reqPath) && fs.lstatSync(reqPath).isFile();
  } catch (e) {
    quit( chalk.red("Unknown error: " + e.message) );
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
    "Location": "https://" + request.headers.host + ":" + secure_port + url.parse(request.url).pathname
  });

};


/*
 * Dummy HTTP server that only handles requests and redirects them to HTTPS.
 */
var HttpServer = http.createServer(redirect);

/*
 * We enable gzip compression.
 */
app.use( express.compress() );
app.use( bodyParser.json() );
app.use( bodyParser.urlencoded({
  extended: true
}) );
app.set("secretPass", (Math.random() * 0xffffffff | 0).toString());

/*
 * We serve all paths defined in settings.json.
 * If the virtual path is a file we serve it as a static one.
 * Else as a normal directory.
 */
_.each(config.paths, function (pathObject, index) {

  var path    = pathObject,
      real    = path.real,
      virtual = path.virtual,
      object = {
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
  Log.i( chalk.red("Denegado acceso con token: " + token.substr(0, 50) + "…") );
  accept.apply(ctx, args);
};

io = io.listen(HttpsServer);

io.set("authorization", function (handshakeData, accept) {
  var token = url.parse(handshakeData.url, true).query.token;
  try {
    jsonwt.verify(token, app.get("secretPass"), function (err, data) {
      if (err) {
        denyAccess(accept, [err, false], this, token);
      } else {
        handshakeData.decoded_token = _.extend(data, { token: token, connected: false, peer: md5(token) });
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
  }),
  hosts = getHosts(),
  users = getUsers(),
  secure_user = {},
  user;
  if (_users.existsByToken(data)) {
    user = _users.findByToken(data);
    user.id = socket.id;
    user.connected = true;
  } else {
    user = new User(data);
    _users.saveById(user);
  }

  Log.i( chalk.green(
    (user.host ? "Host" : "Usuario") +
    " conectado '" + user.name +
    "' con socket id '" + user.id +
    "'' y id P2P '" + user.peer +
    "'.")
  );

  secure_user[_users.originalIdByToken(user)] = user.secure_parsed();

  socket.on("disconnect", function () {
    Log.i( chalk.red(
      (user.host ? "Host" : "Usuario") +
      " desconectado '" + user.name +
      "' con socket id '" + user.id +
      "' y id P2P '" + user.peer +
      "'.")
    );
    user.connected = false;
    io.emit("leave_member", { members: secure_user });
    io.emit("hosts_count", { count: _.size(getHosts()) });
  });

  if (user.host === true) {

    secure_user.peer = undefined;
    delete secure_user.peer;

    /*
     * We update the hosts count and add a new host to all non-host users.
     */
    _.each(users, function (value, key) {
      io.to(value.id).emit("join_member", { members: secure_user });
      io.to(value.id).emit("hosts_count", { count: _.size(hosts) });
    });

    /*
     * New member joined event to the user connected.
     */
    socket.emit("join_member", { members: users });

    /*
     * When the host emits a call event.
     */
    socket.on("call", function (peer) {
      _.each(getUsers(), function (value, key) {
        if (peer === value.peer) {
          Log.i( chalk.blue(
            "Host '" + user.name +
            "' (" + user.peer + ") llamando al usuario '" +
            value.name + "' (" + peer + ").")
          );

          /*
           * We send the call to the user.
           */
          io.to(value.id).emit("receive_call", { from: user.peer, name: user.name, email: user.email });
        }
      });
    });

    /*
     * When the host inits the streaming with the previously connected client.
     */
    socket.on("init_stream", function (peer) {
      _.each(getUsers(), function (value, key, index) {
        if (peer === value.peer) {
          io.to(value.id).emit("init_stream", { from: user.peer });
        }
      });
    });

    /*
     * When the host stops the streaming with the previously connected client.
     */
    socket.on("stop_stream", function (peer) {
      _.each(getUsers(), function (value, key, index) {
        if (peer === value.peer) {
          io.to(value.id).emit("stop_stream", { from: user.peer });
        }
      });
    });

  } else if (user.host === false) {

    /*
     * We emit the new client to all the hosts.
     */
    _.each(hosts, function (value, key) {
      io.to(value.id).emit("join_member", { members: secure_user });
    });

    /*
     * We emit the all the hosts to the new client and update its hosts count number.
     */
    socket.emit("join_member", { members: hosts });
    socket.emit("hosts_count", { count: _.size(hosts) });

    /*
     * When the user has answered the call from the host.
     */
    socket.on("answer", function (data) {
      _.each(getHosts(), function (value, key) {
        if (data.to === value.peer) {
          Log.i( chalk.green(
              "Usuario '" + user.name +
              "' (" + user.peer + ") ha aceptado la llamada de '" +
              value.name + "' (" + data.to + ")."
            )
          );

          /*
           * We emit the event to the host.
           */
          io.to(value.id).emit("answered", { from: user.peer, state: data.state });
        }
      });
    });

    /*
     * When the client sends a motor order.
     */
    socket.on("motor", function (data) {
      var
        x = data.x,
        y = data.y,
        parsed = data.parsed,
        mod = Math.min(1, Math.sqrt(x * x + y * y)),
        angle = Math.atan(y / x) || 0,
        left  = x,
        right = y;

      /*
       * We parse as it was a joystick coordinates,
       */
      if ((x !== 0 || y !== 0) && !parsed) {
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

      /*
       * We just send them to the robot because they are parsed.
       */
      }

      /*
       * We send the orders to the host.
       */
      _.each(getHosts(), function (value, key) {
        if (data.to !== value.peer) return;
        io.to(value.id).emit("motors", {
          b: Math.max(-1, Math.min(1, right)),
          c: Math.max(-1, Math.min(1,  left)),
          from: user.peer
        });
      });

    });

    /*socket.on("flash", function (data) {
      _.each(getHosts(), function (value, key) {
        if (data.to !== value.peer) return;
        io.to(value.id).emit("flash", {
          state: data.state,
          from: user.peer
        });
      });
    });*/
  }
});


/*
 * Peer server initialization.
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

/*
 ***************************************************************************
 * We start the server listening at the end of the script because of the routes.
 */
HttpServer.listen(unsecure_port, unsecure_ip);
HttpsServer.listen(secure_port, secure_ip);

Log.te("Start process")
   .i( chalk.bgGreen("HTTP server running at:") + " " +  chalk.bgYellow("http://%s:%d"), unsecure_ip, unsecure_port)
   .i( chalk.bgGreen("HTTPS server running at:") + " " +  chalk.bgYellow("https://%s:%d"), secure_ip, secure_port)
   .i( chalk.bgGreen("SocketIO server running at:") + " " + chalk.bgYellow("wss://%s:%d"), secure_ip, secure_port)
   .i( chalk.bgGreen("PeerJS server running at:") + " " + chalk.bgYellow("https://%s:9000"), secure_ip )
   .i( chalk.bgBlue("Your internal IP address is:") + " " + chalk.bgYellow(ip.address()) );
