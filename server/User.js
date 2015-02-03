var exports = module.exports;

var User = function (data) {
  this._id             = data.id             || "";
  this._token          = data.token          || "";
  this._peer           = data.peer           || "";
  this._name           = data.name           || "";
  this._email          = data.email          || "";
  this._latitude       = data.latitude       || "";
  this._longitude      = data.longitude      || "";
  this._short_location = data.short_location || "";
  this._long_location  = data.long_location  || "";
  this._connected      = data.connected      || false;
  this._host           = data.host           || false;

  this._onChangeId            = function () {};
  this._onChangeToken         = function () {};
  this._onChangePeer          = function () {};
  this._onChangeName          = function () {};
  this._onChangeEmail         = function () {};
  this._onChangeLatitude      = function () {};
  this._onChangeLongitude     = function () {};
  this._onChangeShortLocation = function () {};
  this._onChangeLongLocation  = function () {};
  this._onChangeHost          = function () {};
  this._onChangeConnected     = function () {};
};

User.prototype = {
		
  _id:        "",
  _token:     "",
  _peer:      "",
  _name:      "",
  _email:     "",
  _latitude:  "",
  _longitude: "",
  _short_location: "",
  _long_location:  "",
  
  _host:      false,
  _connected: false,
  
  _onChangeId:            function () {},
  _onChangeToken:         function () {},
  _onChangePeer:          function () {},
  _onChangeName:          function () {},
  _onChangeEmail:         function () {},
  _onChangeLatitude:      function () {},
  _onChangeLongitude:     function () {},
  _onChangeShortLocation: function () {},
  _onChangeLongLocation:  function () {},
  _onChangeHost:          function () {},
  _onChangeConnected:     function () {},
  
  _onChangeId:            function (fn) { this._onChangeId = fn;            },
  _onChangeToken:         function (fn) { this._onChangeToken = fn;         },
  _onChangePeer:          function (fn) { this._onChangePeer = fn;          },
  _onChangeName:          function (fn) { this._onChangeName = fn;          },
  _onChangeEmail:         function (fn) { this._onChangeEmail = fn;         },
  _onChangeLatitude:      function (fn) { this._onChangeLatitude = fn;      },
  _onChangeLongitude:     function (fn) { this._onChangeLongitude = fn;     },
  _onChangeShortLocation: function (fn) { this._onChangeShortLocation = fn; },
  _onChangeLongLocation:  function (fn) { this._onChangeLongLocation = fn;  },
  _onChangeHost:          function (fn) { this._onChangeHost = fn;          },
  _onChangeConnected:     function (fn) { this._onChangeConnected = fn;     },
  
  set id (val) {
	  var old = this._id;
    this._id = val;
    this._onChangeId(this, old);
  },
  
  get id () {
	  return this._id;
  },

  set token (val) {
    var old = this._token;
    this._token = val;
    this._onChangeId(this, old);
  },
  
  get token () {
    return this._token;
  },

  set peer (val) {
    var old = this._peer;
    this._peer = val;
    this._onChangePeer(this, old);
  },
  
  get peer () {
    return this._peer;
  },
  
  set name (val) {
    var old = this._name;
	  this._name = val;
	  this._onChangeName(this, old);
  },
  
  get name () {
	  return this._name;
  },
  
  set email (val) {
    var old = this._email;
    this._email = val;
    this._onChangeEmail(this, old);
  },
  
  get email () {
    return this._email;
  },
  
  set latitude (val) {
    var old = this._latitude;
    this._latitude = val;
    this._onChangeLatitude(this, old);
  },
  
  get latitude () {
    return this._latitude;
  },
  
  set longitude (val) {
    var old = this._longitude;
    this._longitude = val;
    this._onChangeLongitude(this, old);
  },
  
  get longitude () {
    return this._longitude;
  },
  
  set short_location (val) {
    var old = this._short_location;
    this._short_location = val;
    this._onChangeShortLocation(this, old);
  },
  
  get short_location () {
    return this._short_location;
  },

  set long_location (val) {
    var old = this._long_location;
    this._long_location = val;
    this._onChangeLongLocation(this, old);
  },
  
  get long_location () {
    return this._long_location;
  },

  set host (val) {
    var old = this._host;
    this._host = val;
    this._onChangeHost(this, old);
  },
  
  get host () {
    return this._host;
  },

  set connected (val) {
    var old = this._connected;
    this._connected = val;
    this._onChangeConnected(this, old);
  },
  
  get connected () {
    return this._connected;
  },

  clone: function () {
    return new User({
      id: this.id,
      token: this.token,
      peer: this.peer,
      name: this.name,
      email: this.email,
      latitude: this.latitude,
      longitude: this.longitude,
      short_location: this.short_location,
      long_location: this.long_location,
      host: this.hosts,
      connected: this.connected
    });
  },

  secure_clone: function () {
    var clone = this.clone();
    clone.token = undefined;
    return clone;
  },

  parsed: function () {
    return {
      id: this.id,
      token: this.token,
      peer: this.peer,
      name: this.name,
      email: this.email,
      latitude: this.latitude,
      longitude: this.longitude,
      short_location: this.short_location,
      long_location: this.long_location,
      host: this.hosts,
      connected: this.connected
    };
  },

  secure_parsed: function () {
    var parse = this.parsed();
    parse.token = undefined;
    delete parse.token;
    return parse;
  }
  
};

module.exports = User;