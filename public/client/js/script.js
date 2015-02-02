/**
 * Main script for client page.
 * @author Esaú García (EgaTuts).
 * @version 1.0.0
 */
(function (root, DOC, M, Mustache, Masonry, io, W, depot, marmottajax) {
  "use strict";

  /*
   * First of all we add a little modification to the when-then library
   * to add a custom method that allows delaying async tasks.
   */

  /**
   * Allows delaying the callback function when the async tasks have finished.
   */
  W.prototype.delay = function (fn, time) {
    var
      self    = this,
      args    = null,
      timeout = time || 0;
    return self.then(function () {
      args = arguments;
      root.setTimeout(function () {
        fn.apply(self, args);
      }, timeout);
    });

  };

  /*
   * We start defining our custom methods.
   */

  /**
   * Returns a random value between the specified range.
   * @param {number} min The minimum value that can be returned.
   * @param {number} max The maximum value that can be returned.
   * @return {number} Random value between the given range.
   */
  var
    _range = function (min, max) {
      return (max - min) * M.random() + min;
    },

    /**
     * Sets the maximum number of decimals a number can have.
     * @param {number} num The number to strip.
     * @param {number} round The maximum decimal precision.
     * @return {number} The given number with given fixed length.
     */
    _fixed = function (num, round) {
      return (num * M.pow(10, round) | 0) / M.pow(10, round);
    },

    /**
     * Transforms a string containing HTML to an HTML element.
     * @param {string} html The string containing all the HTML.
     * @return {Element} The given string as an HTML element.
     */
    _toHTML = (function (elem) {
      var child = null;
      return function (html) {
        elem.innerHTML = html;
        child = elem.children[0];
        elem.innerHTML = "";
        return child;
      };
    })(DOC.createElement("div")),

    /**
     * Debounces a function to execute after a certain time that resets the timer if called more than once before being executed.
     * @param {function} fn The function to debounce.
     * @param {number} time The time to debounce in miliseconds.
     * @param {boolean} immediate Specifies if it must be executed instantly.
     */
    _debounce = (function () {
      var
        timeout = null,
        context = null,
        args    = null,
        func    = null,
        instant = null,
        later = function () {
          timeout = null;
          if ( !instant ) func.apply(context, args);
        };

      return function (fn, time, immediate) {

        return function () {
          context = this,
          args    = arguments,
          func    = fn;
          instant = !!immediate && !timeout;
          clearTimeout(timeout);
          timeout = setTimeout(later, time);
          if (instant) func.apply(context, args);
        };

      };
    })(),

    /**
     * Renders a mustache template.
     * @param {string} data The data to render with Mustache.
     * @param {object} settings Custom data to render in the template.
     * @return {element} The rendered template returned by Mustache as string as HTML.
     */
    _render = function (data, settings) {
      return _toHTML(Mustache.to_html(data, settings));
    },

    /*
     * Local variables.
     */
    container    = DOC.getElementById("container"),
    template     = DOC.getElementById("host-template"),
    add_template = DOC.getElementById("add-template"),
    tip          = DOC.getElementById("loading"),
    tip_message  = DOC.getElementById("loading-tip"),
    active_hosts = DOC.getElementById("active-hosts"),


    /**
     * Renders the host template.
     * @param {object} settings Object containing the data to render.
     * @return {element} The rendered template.
     * @see _render
     */
    renderTemplate = function (settings) {
      return _render(template.innerHTML, settings);
    },

    /*
     * Updates user data if arguments passed,
     or returns it's value if no arguments passed.
     */
    userdata = depot("userdata"),
    storage = function (data) {
      if (!data) return userdata.get(userdata.ids[0]);
      return userdata.update(userdata.ids[0], data);
    },

    /*
     * The Masonry main instance.
     */

    len         = 0,
    columnWidth = 280,
    gutter      = 32,
    maxWidth    = null,
    maxColumns  = null,
    tempColumns = null,
    tempWidth   = null,

    /*
     * Returns the width that takes up a n number of columns.
     *
     * f(x) = |x| * width + (|x| - 1) * gutter
     */
    sizeOfColumns = function (x) {
      return x * 280 + (x - 1) * 32;
    },

    /**
     * Returns the number of columns that can fit up in the given width. Is the inverted sizeOfColumns'() function.
     *
     * @param {number} x The free space to fill with columns.
     * @return {number} A natural and integer number.
     */
    possibleColumns = function (x) {
      return ( (x + 32) / 312) | 0;
    },

    /**
     * Resizes the main container.
     * @param {element} parent The main container of all columns.
     * @param {element} element The new added element.
     * @param {number} length The number of elements.
     */
    containerResizer = function (parent, element, length) {
      len         = length,
      maxWidth    = root.innerWidth - gutter * 2,
      maxColumns  = possibleColumns(maxWidth),
      tempColumns = len > maxColumns ? maxColumns : len,
      tempWidth   = tempColumns * columnWidth + (tempColumns - 1) * gutter;
      parent.style.width = tempWidth + "px";
    };

    window.masonry = new Masonry(container, {
      itemSelector: ".host",
      layoutMode: "fitRows",
      columnWidth: columnWidth,
      gutter: gutter
    });

  Masonry.prototype.addElements = function (parent, elements) {
    for ( var i = 0; i < elements.length; i++ ) {
      this.trigger("beforeLayoutComplete", [parent, elements[i], this.items.length + 1]);
      parent.appendChild( elements[i] );
      this.appended( elements[i] );
      this.layout();
    }
  };

  Masonry.prototype.removeElements = function (parent, elements) {
    for ( var i = 0; i < elements.length; i++ ) {
      this.trigger("beforeLayoutComplete", [parent, elements[i], this.items.length + 1]);
      this.remove(elements[i]);
      this.layout();
    }
  };

  /*
   * Assigning resizing functions.
   */
  root.onresize = function () {
    containerResizer(container, null, masonry.items.length);
  };

  masonry.on("beforeLayoutComplete", containerResizer);
  var
    lat = _range(-90, 90),
    lng = _range(-180, 180);
  masonry.addElements(container, [
    _render(add_template.innerHTML, {
      image: "/res/img/misc/add-icon.png",
      location: "/login/",
      title: "¿Not what you expected?",
      description: "Be a streamer.",
      alt: "¡Be a streamer!"
    }),
    renderTemplate({
      email: "example@domain.com",
      image: "/img/example.png",
      name: "Example",
      short_location: "Spain",
      long_location: "Spain",
      short_latitude: _fixed(lat, 5),
      long_latitude: lat,
      short_longitude: _fixed(lng, 5),
      long_longitude: lng
    })
  ]);

  /*
   * Here starts async tasks.
   */
  var requestToken = function () {
    var data = storage();
    data.host = false;
    marmottajax.post({
      url: "/request-token",
      options: data,
      json: true
    }).then(function (data) {
      if (data.state == -1) {
        window.location.href = "/login";
        return;
      }
      storage({ token: data.token, peer: md5(data.token) });
      connect();
    });
  }
  var connect = function () {
    window.socket = io.connect("", {
      query: "token=" + storage().token,
      "force new connection": true
    });

    socket.on("leave_member", function (data) {
      for (var member in data.members) {
        console.log(DOC.getElementById(member));
        masonry.removeElements(container, [DOC.getElementById(member)]);
        root.onresize();
      }
    });

    socket.on("join_member", function (data) {
      var
        hosts = data.members,
        elements = [],
        h;
      for (var host in hosts) {
        if (hosts.hasOwnProperty(host)) {
          var h = hosts[host],
          element = renderTemplate({
            id: host,
            email: h.email,
            name: h.name,
            image: "https://gravatar.com/avatar/" + md5(h.email) + ".png?s=150&d=blank",
            short_latitude: _fixed(h.latitude, 5),
            long_latitude: h.latitude,
            short_longitude: _fixed(h.longitude, 5),
            long_longitude: h.longitude,
            short_location: h.short_location,
            long_location: h.long_location
          });
          elements.push(element);
        }
      }
      if (elements.length > 0) {
        masonry.addElements(container, elements);
        for (var i = 0; i < elements.length; i++) {
          var element = elements[i],
              coords = element.querySelectorAll(".coords")[0],
              location = element.querySelectorAll(".short_location")[0];
          location.title = h.long_location;
          location.style.height = coords.offsetHeight + "px";
          location.style.width = 240 - coords.offsetWidth + "px";
        }
      }
    });

    socket.on("error", function (reason) {
      if (reason == "JsonWebTokenError: invalid signature" || reason === "TokenExpiredError: jwt expired") {
        socket.disconnect();
        socket = null;
        window.setTimeout(requestToken, 2000);
      }
    });

    socket.on("hosts_count", function (data) {
      active_hosts.innerHTML = data.count;
      tip.classList.add("hide");
    });

    socket.on("disconnect", function () {
      tip.classList.remove("hide");
      active_hosts.innerHTML = "-";
    });

    socket.on("connect", function (){
      tip.classList.add("hide");
    });

    socket.on("receive_call", function () {
      console.log(arguments);
    });

    /*var peer = new Peer(storage().peer, { host: "localhost", port: 9000, path: "/" });
    peer.on("call", function (call) {
      alert("call");
    });
    peer.on("stream", function (stream) {
      alert("stream");
    });*/

  };

  if (storage().token == "") {
    requestToken();
  } else {
    connect();
  }
  
})(this, document, Math, Mustache, Masonry, io, when, depot, marmottajax);