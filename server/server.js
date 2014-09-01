/*
 * @user https://github.com/Egatuts
 * @repo https://github.com/Egatuts/nxt-remote-controller.git
 * @file https://github.com/Egatuts/nxt-remote-controller/blob/master/server/server.js
 * @license MIT (http://www.opensource.org/licenses/MIT)
 */

/*
 * Shortcuts.
 */
var c        = console || {};
var nope     = function () {};
var chaining = function (a, b) {
  return function () {
	b.apply(this, arguments);
	return a;  
  };
};

var Log  = {
  d  : chaining(Log, c.log     || nope),
  e  : chaining(Log, c.error   || nope),
  w  : chaining(Log, c.warn    || nope),
  i  : chaining(Log, c.info    || nope),
  t  : chaining(Log, c.time    || nope),
  te : chaining(Log, c.timeEnd || nope)
};
var quit = function () {
	Log.e.apply(this, arguments);
	process.exit(1);
};