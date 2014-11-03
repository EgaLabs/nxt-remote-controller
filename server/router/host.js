module.exports = function (app, config, pathSettings, base, users) {
  
  var jwt = require("jsonwebtoken");
  
  app.get("/host/:id", function (req, res) {
    res.send("siiii el id es: " + req.params.id, 200);
  });
  
  app.get("/request-token", function (req, res) {
    res.send();
  })
  
};