var exports = module.exports;

var Users = require("./Users.js");

var User = function (name, id) {
  this._id = id || Users.uuid();
  this._name = name || this.id;
};

User.prototype = {
		
  _id: "",
  _name: "",
  _onChangeId: function () {},
  
  onChangeId: function (fn) {
	this._onChangeId = fn;
  },
  
  set id (val) {
	var old = this._id;
    this._id = val;
    this._onChangeId(this, old);
  },
  
  get id () {
	return this._id;
  },
  
  set name (val) {
	this._name = val;
  },
  
  get name () {
	return this._name;
  }
  
};
module.exports = User;