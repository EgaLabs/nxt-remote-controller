/**
 * Main script for client page.
 * @author Esaú García (EgaTuts).
 * @version 1.0.0
 */
(function (root, DOC, M, Mustache, Masonry, io, W, depot, marmottajax, key, undefined) {
  "use strict";

  /*
   * Returns a random number between the given range.
   */
  var _range = function (min, max) {
    return (max - min) * M.random() + min;
  };

  /*
   * Returns a number truncated with the given number of decimal part.
   */
  var _fixed = function (num, round) {
    return (num * M.pow(10, round) | 0) / M.pow(10, round);
  };

  /*
   * Transforms HTML string into real HTML elements.
   */
  var _toHTML = (function (elem) {
    var child = null;
    return function (html) {
      elem.innerHTML = html;
      child = elem.children[0];
      elem.innerHTML = "";
      return child;
    };
  })(DOC.createElement("div"))

  /*
   * Renders an HTML Mustache template.
   */
  var _render = function (data, settings) {
    return _toHTML(Mustache.to_html(data, settings));
  };

  /*
   * Creates a renderer function to allow easy templating of the same HTML template.
   */
  var _template_renderer_ = function (element) {
    return function (settings) {
      return _render(element.innerHTML, settings);
    };
  };

  /*
   * Returns the size that will take to render an X number of columns.
   */
  var _sizeOfColumns = function (number, width, margin) {
    return number * width + (number - 1) * margin;
  };

  /*
   * Returns the number of columns that can take place in a limited given space.
   */
  var _numberOfColumns = function (space, width, margin) {
    return ( (space + margin) / (width + margin) ) | 0;
  };

  /*
   * Returns a function that saves and gets the data of the given data object.
   */
  var _storage_saver_ = function (id) {
    var storage = depot(id);
    return function (data) {
      if (!data) return storage.get(storage.ids[0]);
      return storage.update(storage.ids[0], data);
    };
  };

  /*
   * Creates a video element.
   */
  var _createVideoElement = function (id, width, height) {
    var video = document.createElement("video");
    video.width  = width;
    video.height = height;
    video.id = id;
    video.className = "video";
    video.controls = true;
    return video;
  };

  /*
   * Returns a new PeerJS instance.
   */
  var _getPeerInstance = function (id) {
    return new Peer(id, {
      host: root.location.host,
      port: 9000,
      path: "/",
      debug: true,
      config: {
        "iceServers": [
          { url: "stun:stun01.sipphone.com"      }, { url: "stun:stun.ekiga.net"           },
          { url: "stun:stun.fwdnet.net"          }, { url: "stun:stun.ideasip.com"         },
          { url: "stun:stun.iptel.org"           }, { url: "stun:stun.rixtelecom.se"       },
          { url: "stun:stun.schlund.de"          }, { url: "stun:stun.l.google.com:19302"  },
          { url: "stun:stun1.l.google.com:19302" }, { url: "stun:stun2.l.google.com:19302" },
          { url: "stun:stun3.l.google.com:19302" }, { url: "stun:stun4.l.google.com:19302" },
          { url: "stun:stunserver.org"           }, { url: "stun:stun.softjoys.com"        },
          { url: "stun:stun.voiparound.com"      }, { url: "stun:stun.voipbuster.com"      },
          { url: "stun:stun.voipstunt.com"       }, { url: "stun:stun.voxgratia.org"       },
          { url: "stun:stun.xten.com"            },

          { url: "turn:numb.viagenie.ca",                 credential: "muazkh",                       username: "webrtc@live.com"     },
          { url: "turn:192.158.29.39:3478?transport=udp", credential: "JZEOEt2V3Qb0y27GRntt2u2PAYA=", username: "28224511:1379330808" },
          { url: "turn:192.158.29.39:3478?transport=tcp", credential: "JZEOEt2V3Qb0y27GRntt2u2PAYA=", username: "28224511:1379330808" }
        ]
      }
    });
  };


  ///////////////////////////////////////
  /// Fallback method of getUserMedia ///
  ///////////////////////////////////////
  navigator.getMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;


  ///////////////////////////////
  /// Masonry plugin edition. ///
  ///////////////////////////////

  /*
   * Resize the parent to fit its content.
   */
  Masonry.prototype.calculateResize = function () {
    var
      length         = this.items.length,
      margin         = this.gutter,
      columnWidth    = this.columnWidth - margin,
      maximumWidth   = root.innerWidth - margin * 2,
      maximumColumns = _numberOfColumns(maximumWidth, columnWidth, margin),
      columns        = length > maximumColumns ? maximumColumns : length;
    this.element.style.width = _sizeOfColumns(columns, columnWidth, margin) + "px";
    return this;
  };

  /*
   * Adds a set of elements to the container and performs the layout.
   */
  Masonry.prototype.addElements = function (elements) {
    var i = 0, element;
    for (; i < elements.length; i++) {
      element = elements[i];
      this.trigger("beforeLayoutComplete", [ this.element, element, this.items.length + 1 ]);
      this.element.appendChild(element);
    }
    this.appended(elements);
    this.calculateResize().layout();
    return this;
  };

  /*
   * Removes a set of elements and performs the layout.
   */
  Masonry.prototype.removeElements = function (elements) {
    var i = 0, element;
    for (; i < elements.length; i++ ) {
      element = elements[i];
      this.trigger("beforeLayoutComplete", [ this.element, element, this.items.length + 1 ]);
    }
    this.remove(elements);
    this.calculateResize().layout();
    return this;
  };

  /*
   * Renderer function to render the host template and the login template.
   */
  var _renderHost    = _template_renderer_(document.getElementById("host-template")),
      _renderLogin   = _template_renderer_(document.getElementById("add-template")),
      _renderControl = _template_renderer_(document.getElementById("controls-template")),
      _userdata      = _storage_saver_("userdata"),

      section_wrapper    = DOC.querySelectorAll("section")[1],
      hosts_container    = DOC.getElementById("container"),
      controll_container = DOC.getElementById("controll-ui-container"),
      active_hosts       = DOC.getElementById("active-hosts"),
      tip_container      = DOC.getElementById("loading"),
      tip_message        = DOC.getElementById("loading-tip"),

      HOSTS_WIDTH          = 280,
      HOSTS_GUTTER         = 32,
      HOSTS_SELECTOR       = ".host",
      HOSTS_LAYOUT_MODE    = "fitRows",
      HOSTS_LOCATION_WIDTH = 240,
      VIDEO_WIDTH          = 640,
      VIDEO_HEIGHT         = 320,
      VIDEO_GUTTER         = 32,
      VIDEO_SELECTOR       = ".video",
      VIDEO_LAYOUT_MODE    = "fitRows",
      CONNECTED_PEER       = "",
      MEDIA_STREAM         = null,
      PEER_INSTANCE        = null,

      REASON_TOKEN_SIGNATURE_ERROR = "JsonWebTokenError: invalid signature",
      REASON_TOKEN_EXPIRED_ERROR   = "TokenExpiredError: jwt expired";

  /*
   * Masonry instances (global for testing purposes).
   */
  root.hosts_masonry = new Masonry(hosts_container, {
    itemSelector: HOSTS_SELECTOR,
    layoutMode: HOSTS_LAYOUT_MODE,
    columnWidth: HOSTS_WIDTH,
    gutter: HOSTS_GUTTER
  });

  root.videos_masonry = new Masonry(controll_container, {
    itemSelector: VIDEO_SELECTOR,
    layoutMode: VIDEO_LAYOUT_MODE,
    columnWidth: VIDEO_WIDTH,
    gutter: VIDEO_GUTTER
  });

  /*
   * onResize window event.
   */
  root.onresize = function () {
    hosts_masonry.calculateResize().layout();
    videos_masonry.calculateResize().layout();
  };

  /*
   * We assign an event to resize the parent when the layout is completed.
   */
  hosts_masonry.on("beforeLayoutComplete", function () {
    this.calculateResize();
  });

  /*
   * We add the video elements.
   */
  videos_masonry.addElements([
    _createVideoElement("remote", VIDEO_WIDTH, VIDEO_HEIGHT),
    _renderControl()
  ]);

  /*
   * We add the two hosts cards, one to redirect to the login page and the second one as an example.
   * The example contains random coordinates.
   */
  var latitude  = _range(-90, 90),
      longitude = _range(-180, 180);

  hosts_masonry.addElements([
    _renderLogin({
      image: "/res/img/misc/add-icon.png",
      location: "/login/",
      title: "¿Not what you expected?",
      description: "Be a streamer.",
      alt: "¡Be a streamer!"
    }),
    _renderHost({
      email: "example@domain.com",
      image: "/img/example.png",
      name: "Example",
      short_location: "Spain",
      long_location: "Spain",
      short_latitude: _fixed(latitude, 5),
      long_latitude: latitude,
      short_longitude: _fixed(longitude, 5),
      long_longitude: longitude
    })
  ]);

  /////////////////////////////////////////////////
  /// Async tasks, login, sockets and streaming ///
  /////////////////////////////////////////////////

  /*
   * Requests the token by sending the profile data.
   */
  var requestToken = function () {
    var profile = _userdata();
    profile.host = false;

    /*
     * AJAX request with promise.
     */
    marmottajax.post({
      url: "/request-token",
      options: profile,
      json: true
    }).then(function (result) {
      /*
       * The request failed due to errors on the profile data.
       */
      if (result.state === -1) {
        root.location.href = "/login";
        return;
      }

      /*
       * Everything OK.
       */
      _userdata({ token: result.token, peer: md5(result.token) });
      connect();
    });
  };

  /*
   * Starts the SocketIO connection to have a low-latency real-time bi-directional communication socket.
   */
  var connect = function () {
    /*
     * global for testing purposes.
     */
    root.SOCKET = io.connect("", {
      "force new connection": true,
      query: "token=" + _userdata().token
    });

    /*
     * When the user is connected to the server.
     */
    SOCKET.on("connect", function (){
      tip_container.classList.add("hide");
    });

    /*
     * When we are disconnected from the server due to an external error.
     */
    SOCKET.on("disconnect", function () {
      var items = hosts_masonry.items,
          elements = [],
          i = 2;
      tip_container.classList.remove("hide");
      active_hosts.innerHTML = "-";
      for (; i < items.length; i++) {
        elements.push(items[i].element);
      }
      hosts_masonry.removeElements(elements);
    });

    /*
     * When there is an error when connecting, usually because of an error on the access token.
     */
    SOCKET.on("error", function (reason) {
      if (reason === REASON_TOKEN_SIGNATURE_ERROR || reason === REASON_TOKEN_EXPIRED_ERROR) {
        if (SOCKET !== null) SOCKET.disconnect();
        SOCKET = null;
        root.setTimeout(requestToken, 2000);
      }
    });

    /*
     * When the server send us the number of connected hosts.
     */
    SOCKET.on("hosts_count", function (data) {
      active_hosts.innerHTML = data.count;
      tip_container.classList.add("hide");
    });

    /*
     * When a new client enters the "room".
     * We render the data based on the template and then add some visual effects like title for long location format
     * and add ellipsis to the short location if it doesn't fit the size specified on HOSTS_LOCATION_WIDTH.
     */
    SOCKET.on("join_member", function (data) {
      var hosts = data.members,
          elements = [],
          element,
          coords,
          location,
          i = 0,
          host, id;
      for (id in hosts) {
        if (!hosts.hasOwnProperty(id)) continue;
        host = hosts[id];
        elements.push(_renderHost({
          id: id,
          email: host.email,
          name: host.name,
          image: "https://gravatar.com/avatar/" + md5(host.email) + ".png?s=150&d=blank",
          short_latitude: _fixed(host.latitude, 5),
          long_latitude: host.latitude,
          short_longitude: _fixed(host.longitude, 5),
          long_longitude: host.longitude,
          short_location: host.short_location,
          long_location: host.long_location
        }));
      }
      if (!(elements.length > 0)) return;
      hosts_masonry.addElements(elements);
      for (; i < elements.length; i++) {
        element  = elements[i];
        coords   = element.querySelector(".coords");
        location = element.querySelector(".short_location");

        location.title = host.long_location;
        location.style.height = coords.offsetHeight + "px";
        location.style.height = HOSTS_LOCATION_WIDTH - coords.offsetWidth + "px";
      }
    });

    /*
     * When a member leaves the "room".
     * We do a "for" loop and remove all the clients from the interface.
     * If the peerID equals the actual connection peer it means the hosts was gone so we return to the main interface.
     */
    SOCKET.on("leave_member", function (data) {
      var members = data.members,
          elements = [],
          id, member;
      for (id in members) {
        if (!members.hasOwnProperty(id)) continue;
        member = members[id];
        if (member.peer === CONNECTED_PEER) {
          section_wrapper.classList.remove("stream");
          CONNECTED_PEER = null;
          if (MEDIA_STREAM !== null) MEDIA_STREAM.stop();
        }
        elements.push(DOC.getElementById(id));
      }
      hosts_masonry.removeElements(elements);
    });

    /*
     * When a hosts inits a video streaming.
     */
    SOCKET.on("receive_call", function (data) {
      /*
       * This probably means the connection is being recovered.
       */
      if (CONNECTED_PEER === data.from) {
        SOCKET.emit("answer", { state: true, to: data.from });
        return;

      /*
       * This means we are connected to someone and another client is calling as so we don't answer.
       */
      } else if (CONNECTED_PEER !== null && CONNECTED_PEER !== "" && CONNECTED_PEER !== data.from) {
        SOCKET.emit("answer", { state: false, to: data.from });
        return;
      }
      var answer = window.confirm("Do you want to answer the call from '" + data.name + "' (" + data.email + ")?");
      SOCKET.emit("answer", { state: answer, to: data.from });
      if (answer) CONNECTED_PEER = data.from;
    });

    /*
     * When the stream has started from the remote.
     */
    var video         = DOC.getElementById("remote"),
        vendor        = window.URL || window.webkitURL;

    SOCKET.on("init_stream", function (data) {
      if (CONNECTED_PEER !== data.from) {
        return;
      }
      section_wrapper.classList.add("stream");
      var PEER_INSTANCE = _getPeerInstance(_userdata().peer);
      PEER_INSTANCE.on("call", function (call) {
        call.answer();
        call.on("stream", function (stream) {
          if (navigator.mozGetUserMedia) {
            video.mozSrcObject = stream;
          } else {
            video.src = vendor.createObjectURL(stream);
          }
          video.play();
        });
      });
    });

    /*
     * When the stream has finished (and the controlling).
     */
    SOCKET.on("stop_stream", function (data) {
      if (CONNECTED_PEER !== data.from) {
        return;
      }
      section_wrapper.classList.remove("stream");
      CONNECTED_PEER = null;
      PEER_INSTANCE  = null;
      video.src = "";
    });
  };

  /*
   * Power element.
   */
  var
    controls      = DOC.getElementById("controls"),
    power_control = DOC.getElementById("power"),
    flash_state   = false,
    last_time     = 0,
    passed_time   = true,
    time_trigger  = true,

    GAMEPAD = null,
    LOOPING = true,
    BUTTON_MAPPING = {
      "A": 0,
      "B": 1,
      "X": 2,
      "Y": 3,
      "LB": 4,
      "RB": 5,
      "LT": 6,
      "RT": 7,
      "BACK": 8,
      "SELECT": 8,
      "START": 9,
      "MENU": 9,
      "LS": 10,
      "LEFT_STICK": 10,
      "RS": 11,
      "RIGHT_STICK": 11,
      "UP": 12,
      "DPAD_UP": 12,
      "DOWN": 13,
      "DPAD_DOWN": 13,
      "LEFT": 14,
      "DPAD_LEFT": 14,
      "RIGHT": 15,
      "DPAD_RIGHT": 15
    },
    AXIS_MAPPING = {
      "LX": 0,
      "LEFT_X": 0,
      "LY": 1,
      "LEFT_Y": 1,
      "RX": 2,
      "RIGHT_X": 2,
      "RY": 3,
      "RIGHT_Y": 3
    };

  /*
   * Returns a function that executes a checker that MUST returns the values on key events
   * that will be passed to the callback handler if doesn't equal the last time values (used to avoid
   * consequent event firing on keyholding).
   */
  var _one_press_ = function (checker, callback) {
    var
      latest  = [],
      matches = 0,
      result,
      size,
      res,
      i;
    return function (event, down) {
      result  = checker();
      size    = result.length;
      matches = 0;
      for (i = 0; i < size; i++) {
        if (latest[i] === result[i]) matches++;
        latest[i] = result[i];
      }
      if (matches !== size) res = callback(event, result, down);
      if (res !== undefined) return res;
    };
  },

  /*
   * When the direction has changed on pressed arrows.
   */
  onDirectionChange = _one_press_(function () {
    var
      keys  = key.activeKeys(),
      up    = keys.indexOf("up")    > -1 ?  1 : 0,
      right = keys.indexOf("right") > -1 ?  1 : 0,
      down  = keys.indexOf("down")  > -1 ? -1 : 0,
      left  = keys.indexOf("left")  > -1 ? -1 : 0,
      x = (left + right) * power_control.value,
      y = (up + down) * power_control.value;
    return [x, y];
  }, function (event, data, down) {
    var action = down ? "add" : "remove";
    DOC.getElementById(event.keyCode).classList[action]("pressed");
    if (CONNECTED_PEER === null || CONNECTED_PEER === "" || SOCKET === null) {
      event.preventDefault();
      return;
    }
    SOCKET.emit("motor", {
      x: data[0],
      y: data[1],
      to: CONNECTED_PEER
    });
    event.preventDefault();
  }),

  _totalValueWASD = function () {
    var
      keys  = key.activeKeys(),
      w     = keys.indexOf("w") > -1 ?  1 : 0,
      d     = keys.indexOf("d") > -1 ?  1 : 0,
      s     = keys.indexOf("s") > -1 ? -1 : 0,
      a     = keys.indexOf("a") > -1 ? -1 : 0,
      total = w + d + s + a;
    if (total > 0) total =  1;
    if (total < 0) total = -1;
    return total;
  },

  onPowerChangeRelease = function (event, data, down) {
    var action = down ? "add" : "remove";
    DOC.getElementById(event.keyCode).classList[action]("pressed");
    power_control.value = power_control.value - - data[1] * 0.05;
    event.preventDefault();
  },

  onPowerChange = _one_press_(function () {
    var
      total = _totalValueWASD(),
      now  = Date.now();
    passed_time = now - last_time > 100;
    if (passed_time) {
      last_time = now;
      time_trigger = !time_trigger;
    }
    return [time_trigger, total];
  }, onPowerChangeRelease),

  onFlashChange = _one_press_(function () {
    flash_state = !flash_state;
    return [flash_state];
  }, function (event, data, down) {
    var action = down ? "add" : "remove";
    DOC.getElementById(event.keyCode).classList[action]("pressed");
    if (CONNECTED_PEER === null || CONNECTED_PEER === "" || SOCKET === null) {
      event.preventDefault();
      return;
    }
    /*SOCKET.emit("flash", {
      state: flash_state,
      to: CONNECTED_PEER
    });*/
    event.preventDefault();
  });

  /*
   * We bind the events to the directional arrows the WASD keys and the spacebar.
   */
  key.on("up, right, down, left", function (event) {
    onDirectionChange.call(this, event, true);
  }, function (event) {
    onDirectionChange.call(this, event, false);
  });

  key.on("w, d, s, a", function (event) {
    onPowerChange.call(this, event, true);
  }, function (event) {
    var total = _totalValueWASD();
    onPowerChangeRelease.call(this, event, [null, total], false);
  });

  key.on("space", function (event) {
    onFlashChange.call(this, event, true);
  }, function (event) {
    onFlashChange.call(this, event, false);
  });

  /*
   * Gamepad connection and disconnection events.
   */
  var onGamepadConnected = function (event) {
    controls.classList.remove("keyboard");
    controls.classList.add("gamepad");
  };
  root.ongamepadconnected = onGamepadConnected;

  /*
   *  When the gamepad is disconnected.
   */
  root.ongamepaddisconnected = function () {
    GAMEPAD = null;
    controls.classList.remove("gamepad");
    controls.classList.add("keyboard");
  };

  /*
   *  Gamepad tick used to track buttons only when they change.
   */
  var _isInDeadZone = function (zone) {
    var range = Math.abs(zone);
    return function (value) {
      return (value < range) && (value > -range);
    };
  },

  _isActiveAxis = function (deadzone) {
    return function (latest, now) {
      return latest !== now && !deadzone(now);
    };
  };

  var customDeadZone = _isInDeadZone(0.15),
      customActive = _isActiveAxis(customDeadZone),
      ids = ["LX", "LY", "RX", "RY", "A", "B", "X", "Y", "LB", "LT", "RB", "RT", "SELECT", "START", "LS", "RS", "UP", "DOWN", "LEFT", "RIGHT"],
      latest_axis = [];

  root.ongamepadtick = _one_press_(function () {
    var buttons = [],
        i = 0,
        button;
    for (; i < GAMEPAD.buttons.length; i++) {
      button = GAMEPAD.buttons[i];
      if (i === BUTTON_MAPPING.LT || i === BUTTON_MAPPING.RT) {
        buttons.push(button.value);
      } else {
        buttons.push(button.pressed);
      }
    }
    return Array.prototype.concat(GAMEPAD.axes, buttons, [power_control.value]);
  }, function (event, data, down) {
    var
      X1 = data[ AXIS_MAPPING["LX"] ],
      Y1 = data[ AXIS_MAPPING["LY"] ] * -1,
      M1 = Math.sqrt(X1 * X1 + Y1 * Y1),
      X2 = data[ AXIS_MAPPING["RX"] ],
      Y2 = data[ AXIS_MAPPING["RY"] ] * -1,
      M2 = Math.sqrt(X2 * X2 + Y2 * Y2),
      LT = data[ BUTTON_MAPPING["LT"] + 4] * -1,
      RT = data[ BUTTON_MAPPING["RT"] + 4],
      B  = data[ BUTTON_MAPPING["B"] ] ? 0 : 1,
      power = 0.5 + (LT / 2) + (RT / 2),
      parsed = false,
      is1Active = customDeadZone(M1) ? customActive(latest_axis[0], M1) : true,
      is2Active = customDeadZone(M2) ? customActive(latest_axis[1], M2) : true,
      i = 0,
      isNotActive = false,
      element,
      X3, Y3;
    if (is1Active && !is2Active) {
      X3 = X1;
      Y3 = Y1;
    } else if (!is1Active && is2Active) {
      X3 = X2;
      Y3 = Y2;
    } else {
      X3 = Y1;
      Y3 = Y2;
      parsed = true;
    }
    X3 *= B;
    Y3 *= B;
    latest_axis = [M1, M2];
    power_control.value = power;
    for (; i < data.length; i++) {
      isNotActive = false;
      element = null;
      if (i === 0) {
        element = DOC.getElementById("LS");
        isNotActive = !is1Active;
        console.log(element, isNotActive);
      } else if (i === 2) {
        element = DOC.getElementById("RS");
        isNotActive = !is2Active;
      } else if (i > 3 && !(i === 14 || i === 15)) {
        element = DOC.getElementById( ids[i] );
        isNotActive = data[ BUTTON_MAPPING[ids[i]] + 4] === false || data[ BUTTON_MAPPING[ids[i]] + 4] === 0;
      }

      if (!element) continue;
      element.classList[!isNotActive ? "remove" : "add"]("normal");
      element.classList[!isNotActive ? "add" : "remove"]("active");
    }
    if (CONNECTED_PEER === null || CONNECTED_PEER === "" || SOCKET === null) return;
    SOCKET.emit("motor", {
      x: X3,
      y: Y3,
      parsed: parsed,
      to: CONNECTED_PEER
    });
  });

  /*
   *  We execute the gamepadconnected handler if existed a gamepad before the page was loaded.
   */
  if (!!navigator.getGamepads()[0]) onGamepadConnected({ gamepad: navigator.getGamepads()[0] });

  /*
   *  The loop that detects faces and gamepads.
   */
  var loop = (function () {
    var gamepads;
    return function () {
      if (!LOOPING) return;
      gamepads = navigator.getGamepads();
      if (!GAMEPAD && !!gamepads[0]) {
        GAMEPAD = gamepads[0];
        if (root.ongamepadconnected) root.ongamepadconnected();
      } else if (!!GAMEPAD && !gamepads[0]) {
        GAMEPAD = null;
        if (root.ongamepaddisconnected) root.ongamepaddisconnected();
      }
      if (!!GAMEPAD) {
        if (root.ongamepadtick) root.ongamepadtick();
      }
      root.requestAnimationFrame(loop);
    };
  })();
  LOOPING = true;
  loop();

  /*
   *  Start the request if there isn't token or directly connect if exists.
   */
  if (_userdata().token === "") {
    requestToken();
  } else {
    connect();
  }

})(this, document, Math, Mustache, Masonry, io, when, depot, marmottajax, KeyboardJS);
