var exports = module.exports;

var Users = function () {
	this.users = {};
};

var proto = "prototype";

/*
 * Stored UUID's.
 */
Users[proto].uuids = [];
Users[proto].uuid_spliter = "-";

/*
 * Checks if UUID exists. If one UUID is destroyed it will be available. 
 */
Users[proto].exists_uuid = function (id) {
  return this.uuids.indexOf(id) > -1;
};

/*
 * Deletes a generated/saved UUID from the Array.
 */
Users[proto].remove_uuid = function (id) {
  if (this.exists_uuid(id)) this.uuids.splice(this.uuids.indexOf(id), 1);
  return this;
};

/*
 * Saves the given UUID in the Array.
 */
Users[proto].save_uuid = function (id) {
  if (!this.exists_uuid(id)) this.uuids.push(id);
  return this;
};

/*
 * Generates a new UUID unique in the Users.uuids Array.
 */
Users[proto].generate_uuid = (function () {
  var block = function () {
    return ( (Math.random() * 0xffff) | 0).toString(16)
  };
  return function () {
    var spliter = this.uuid_spliter,
	      uuID = block() + spliter + block() + spliter + block() + spliter + block();
	  return this.exists_uuid(uuID) > -1 ? this.generate_uuid() : uuID;
  };
});

/*
 * Checks if user exists.
 */
Users[proto].exists_user = function (user) {
  if (typeof user == "string") return !!this.users[user] && this.exists_uuid(user);
  return this.exists_user(user.id);
};

/*
 * Removes existing user.
 */
Users[proto].remove_user = function (user) {
  if (typeof user == "string")
  {
    delete this.users[user];
    this.remove_uuid(user);
    return this;
  }
  return this.remove_user(user.id);
};

/*
 * Saves the user.
 */
Users[proto].save_user = function (user) {
  if (!this.exists_user(user)) this.save_uuid(user.id).users[user.id] = user;
  return this;
};

/*
 * Saves the user and listens to ID changes.
 */
Users[proto].register_user = function (user) {
  if (this.exists_user(id)) return this;
  var self = this;
  this.save_user(user);
  user.onChangeId(function (client, uuid) {
    self.remove_user(uuid).save_user(client);
  });
};

/*
 * Filters over all the users.
 */
Users[proto].filter = function (fn) {
  var result = [], check = null;
  for (var user in this.users) {
    if ( this.users.hasOwnProperty(user) ) {
      check = fn(this.users[user]);
      if (typeof check == "boolean")
      {
        if (check == true) result[result.length] = this.users[user];
      }
      if (!!check) result[result.length] = check;
    }
  }
  return result;
};

/*
 * Authenticate an user.
 */
Users[proto].auth = function (email, password) {
  this.filter(function () {});
};

module.exports = Users;