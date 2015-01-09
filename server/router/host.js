module.exports = function (app, config, pathSettings, base, users) {
  
  var
    jwt = require("jsonwebtoken"),
    letters    = "a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð",
    validName  = new RegExp("^(((["+letters+"]+)((-["+letters+"]+)*)(('(["+letters+"]+)?)*)|())( ?)){1,4}$"),
    validEmail = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
    validCoord = /^[+-]?([0-9]|[1-9][0-9]|[1][0-7][0-9]|180)\.[0-9]{15}$/,
    validToken = /^[a-fA-F0-9]{32}$/,
    anything   = /^.+$/,
    fields     = ["name", "email", "latitude", "longitude", "location"],
    validators = [validName, validEmail, validCoord, validCoord, anything];
  
  app.get("/host/:id", function (req, res) {
    res.send(req.params.id, 200);
  });
  
  app.get("/request-token", function (req, res) {
    var profile = {
      name: req.body.name,
      email: req.body.email,
      latitude: req.body.latitude,
      longitude: req.body.longitude,
      location: req.body.location
    };
    for (var i = 0; i < fields.length; i++) {
      if (!validators[i].test(profile[ fields[i] ])) {
        res.json({ state: -1 });
        return;
      }
    }
    var token = jwt.sign(profile, app.get("secretPass"), { expiresInMinutes: 60 * 2 });
    res.json({ token: token });
  })
  
};