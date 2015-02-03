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
      host: "localhost",
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
  var _renderHost  = _template_renderer_(document.getElementById("host-template")),
      _renderLogin = _template_renderer_(document.getElementById("add-template")),
      _userdata    = _storage_saver_("userdata"),

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
  videos_masonry.addElements([ _createVideoElement("remote", VIDEO_WIDTH, VIDEO_HEIGHT) ]);

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
      //root.onresize();
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
    var video  = DOC.getElementById("remote"),
        vendor = window.URL || window.webkitURL;

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
    });
  };

  /*
   * When a key is pressed or released.
   */
  var onDirectionChange = (function () {
    var latest = [];
    return function () {
      if (CONNECTED_PEER === null || CONNECTED_PEER === "" || SOCKET === null) {
        return;
      }
      var
        keys  = key.activeKeys(),
        up    = keys.indexOf("up")    > -1 ?  1 : 0,
        right = keys.indexOf("right") > -1 ?  1 : 0,
        down  = keys.indexOf("down")  > -1 ? -1 : 0,
        left  = keys.indexOf("left")  > -1 ? -1 : 0,
        x = (left + right) * 0.75,
        y = (up + down) * 0.75;
      if (latest[0] === x && latest[1] === y) {
        return;
      }
      latest[0] = x;
      latest[1] = y;
      SOCKET.emit("motor", {
        x: x,
        y: y,
        to: CONNECTED_PEER
      });
    };
  })();

  /*
   * We bind the events to the directional arrows.
   */
  key.on("up, right, down, left", onDirectionChange, onDirectionChange);

  /*
   * Start the request if there isn't token or directly connect if exists.
   */
  if (_userdata().token === "") {
    requestToken();
  } else {
    connect();
  }
  
})(this, document, Math, Mustache, Masonry, io, when, depot, marmottajax, KeyboardJS);