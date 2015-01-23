var exports = module.exports;

var Users = require("./Users.js");
var md5   = require("MD5");

var User = function (data) {
  this._id        = data.id || Users.uuid();
  this._name      = data.name || "";
  this._email     = data.email || "";
  this._latitude  = data.latitude || "";
  this._longitude = data.longitude || "";
  this._short_location = data.short_location || "";
  this._long_location  = data.long_location || "";
  this._host      = data.host || false;
  this._connected = data.connected || false;
  if (!!data.password) this.password = data.password;
};

User.prototype = {
		
  _id:        "",
  _name:      "",
  _email:     "",
  _latitude:  "",
  _longitude: "",
  _short_location: "",
  _long_location:  "",
  
  _host:      false,
  _connected: false,
  
  _onChangeId:        function () {},
  _onChangeName:      function () {},
  _onChangeEmail:     function () {},
  _onChangeLatitude:  function () {},
  _onChangeLongitude: function () {},
  _onChangeLocation:  function () {},
  
  onChangeId:        function (fn) { this._onChangeId        = fn; },
  onChangeName:      function (fn) { this._onChangeName      = fn; },
  onChangeEmail:     function (fn) { this._onChangeEmail     = fn; },
  onChangeLatitude:  function (fn) { this._onChangeLatitude  = fn; },
  onChangeLongitude: function (fn) { this._onChangeLongitude = fn; },
  onChangeLocation:  function (fn) { this._onChangeLocation  = fn; },
  
  set id (val) {
	  var old = this._id;
    this._id = val;
    this._onChangeId(this, old);
  },
  
  get id () {
	  return this._id;
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
    this._onChangeLocation(this, old, this._long_location);
  },
  
  get short_location () {
    return this._short_location;
  },

  set long_location (val) {
    var old = this._long_location;
    this._long_location = val;
    this._onChangeLocation(this, this._short_location, old);
  },
  
  get long_location () {
    return this._long_location;
  }
  
};

module.exports = User;