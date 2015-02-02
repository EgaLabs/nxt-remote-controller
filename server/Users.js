var exports = module.exports;

var Users = function () {
  this.users = {};
}

Users.prototype = {
  
  existsById: function (id) {
    if (typeof id !== "string") return this.existsById(id.id);
    return this.users[id] !== undefined;
  },

  indexById: function (id) {
    if (typeof id !== "string") return this.indexById(id.id);
    var i = 0;
    for (var user in this.users) {
      if (this.users.hasOwnProperty(user)) {
        if (id === user) return i;
      }
      i++;
    }
    return -1;
  },

  eachById: function (fn) {
    var i = 0, user;
    for (user in this.users) {
      if (this.users.hasOwnProperty(user)) {
        fn(user, this.users[user], i);
      }
      i++;
    }
    return this;
  },

  mapById: function (fn) {
    var i = 0, user;
    for (user in this.users) {
      if (this.users.hasOwnProperty(user)) {
        this.users[user] = fn(user, this.users[user], i);
      }
      i++;
    }
    return this;
  },

  filterById: function (fn) {
    var i = 0, result = {}, user, check;
    for (user in this.users) {
      if (this.users.hasOwnProperty(user)) {
        check = fn(user, this.users[user], i);
        if (typeof check === "boolean" && check === true) result[user] = this.users[user];
      }
      i++;
    }
    return result;
  },

  findById: function (id) {
    if (typeof id === "string") return this.findById(id.id);
    return this.users[id];
  },

  saveById: function (user) {
    this.users[user.id] = user;
  },

  removeById: function (id) {
    this.users[id] = undefined;
  },


  existsByToken: function (token) {
    if (typeof token !== "string") return this.existsByToken(token.token);
    for (var user in this.users) {
      if (this.users.hasOwnProperty(user)) {
        if (this.users[user].token === token) return true;
      }
    }
    return false;
  },

  indexByToken: function (token) {
    if (typeof token !== "string") return this.indexByToken(token.token);
    var i = 0;
    for (var user in this.users) {
      if (this.users.hasOwnProperty(user)) {
        if (token === this.users[user].token) return i;
      }
      i++;
    }
    return -1;
  },

  eachByToken: function (fn) {
    var i = 0, user, us;
    for (user in this.users) {
      if (this.users.hasOwnProperty(user)) {
        us = this.users[user];
        fn(us.token, us, i);
      }
      i++;
    }
    return this;
  },

  mapByToken: function (fn) {
    var i = 0, user, us;
    for (user in this.users) {
      if (this.users.hasOwnProperty(user)) {
        us = this.users[user];
        this.users[user] = fn(us.token, us, i);
      }
      i++;
    }
    return this;
  },

  filterByToken: function (fn) {
    var i = 0, result = {}, user, check, us;
    for (user in this.users) {
      if (this.users.hasOwnProperty(user)) {
        us = this.users[user];
        check = fn(us.token, us, i);
        if (typeof check === "boolean" && check === true) result[us.token] = us;
      }
      i++;
    }
    return result;
  },

  findByToken: function (token) {
    if (typeof token !== "string") return this.findByToken(token.token);
    var user, us;
    for (user in this.users) {
      if (this.users.hasOwnProperty(user)) {
        us = this.users[user];
        if (us.token === token) return us;
      }
    } 
  },

  removeByToken: function (token) {
    if (typeof token !== "string") return this.removeByToken(token.token); 
    var user, us;
    for (user in this.users) {
      if (this.users.hasOwnProperty(user)) {
        us = this.users[user];
        if (us.token === token) {
          us = null;
          us = undefined;
          delete us;
        }
      }
    }
    return this;
  },

  originalIdByToken: function (token) {
    if (typeof token !== "string") return this.originalIdByToken(token.token); 
    for (var user in this.users) {
      if (this.users.hasOwnProperty(user)) {
        if (this.users[user].token === token) return user;
      }
    }
  },

  findByPeer: function (peer) {
    if (typeof peer !== "string") return this.findByPeer(peer.peer);
    for (var user in this.users) {
      if (this.users.hasOwnProperty(user)) {
        if (this.users[user].peer === peer) return user; 
      }
    } 
  }

};

module.exports = Users;