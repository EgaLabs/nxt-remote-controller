module.exports = function (app, config, pathSettings, base, users) {
  
  var
    jwt = require("jsonwebtoken"),
    letters    = "a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð",
    validName  = new RegExp("^(((["+letters+"]+)((-["+letters+"]+)*)(('(["+letters+"]+)?)*)|())( ?)){1,4}$"),
    validEmail = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
    validCoord = /^[+-]?([0-9]|[1-9][0-9]|[1][0-7][0-9]|180)(\.[0-9]+)?$/,
    validToken = /^[a-fA-F0-9]{32}$/,
    anything   = /^.+$/,
    fields     = ["name", "email", "latitude", "longitude", "long_location", "short_location"],
    validators = [validName, validEmail, validCoord, validCoord, anything, anything];
  
  app.get("/host/:id", function (req, res) {
    res.send(req.params.id, 200);
  });
  
  app.post("/request-token", function (req, res) {
    var profile = {
      name: req.body.name,
      email: req.body.email,
      latitude: parseFloat(req.body.latitude),
      longitude: parseFloat(req.body.longitude),
      short_location: req.body.short_location,
      long_location: req.body.long_location,
      host: req.body.host == "true"
    };
    console.log(profile);
    if (profile.host === true) {
      validators[0] = anything;
    }
    for (var i = 0; i < fields.length; i++) {
      if (!validators[i].test(profile[ fields[i] ])) {
        res.json({ state: -1 });
        console.log("valor incorrecto: " + fields[i] + " es " + profile[ fields[i] ]);
        return;
      }
    }
    var token = jwt.sign(profile, app.get("secretPass"), { expiresInMinutes: 60 * 2 });
    res.json({ token: token });
  })
};