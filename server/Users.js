var Users = function () {
	this.users = {};
};

Users.prototype.uuids = [];
Users.prototype.uuid = (function () {
  var block = function () {
    return ( (Math.random() * 0xffff) | 0).toString(16)
  };
  return function () {
	var uuID = block() + s + block() + s + block() + s + block();
	return Users.uuids.indexOf(uuID) > -1 ? Users.uuid() : uuID;
  }
});

Users.prototype.add = function (user) {
  var self = this;
  var id = user.id;
  if ( !this.users[id] ) this.users[id] = user;
  user.onChangeId(function (newUser, oldId) {
    self.users[oldId] = undefined;
    self.users[newUser.id] = newUser;
  });
};
module.exports = Users;