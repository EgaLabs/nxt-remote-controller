module.exports = function (app, config, pathSettings, base, users) {

  var
    chalk      = require("chalk"),
    https      = require("https"),
    geocoder   = require("geocoder"),
    jwt        = require("jsonwebtoken"),
    letters    = "a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð",
    validName  = new RegExp("^(((["+letters+"]+)((-["+letters+"]+)*)(('(["+letters+"]+)?)*)|())( ?)){1,4}$"),
    validEmail = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
    validCoord = /^[+-]?([0-9]|[1-9][0-9]|[1][0-7][0-9]|180)(\.[0-9]+)?$/,
    validToken = /^[a-fA-F0-9]{32}$/,
    anything   = /^.+$/,
    fields     = ["name", "email", "latitude", "longitude", "long_location", "short_location"],
    validators = [validName, validEmail, validCoord, validCoord, anything, anything],
    registerUser = function (res, profile) {
      console.info( chalk.cyan(
        "Registrado " + (profile.host ? "host" : "usuario") +
        ": " + profile.name + ", " + profile.email +
        ", " + profile.short_location +
        " (" + profile.latitude +
        " | " + profile.longitude +
        ")")
      );
      var token = jwt.sign(profile, app.get("secretPass"), { expiresInMinutes: 30 });
      res.json({ token: token });
    };

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
    if (profile.host === true) {
      validators[0] = anything;
    }
    for (var i = 0; i < fields.length; i++) {
      if (!validators[i].test(profile[ fields[i] ])) {
        res.json({ state: -1 });
        return;
      }
    }
    if (profile.host === false) {
      registerUser(res, profile);
      return;
    }
    geocoder.reverseGeocode(profile.latitude, profile.longitude, function (err, data) {
      var result, component, type, locality, province, comunity, country;
      for (var i = 0; i < data.results.length; i++) {
        result = data.results[i];
        for (var e = 0; e < result.address_components.length; e++) {
          component = result.address_components[e];
          for (var n = 0; n < component.types.length; n++) {
            type = component.types[n];
            if (type == "administrative_area_level_1") {
              comunity = component.long_name;
            } else if (type == "administrative_area_level_2") {
              province = component.long_name;
            } else if (type == "locality") {
              locality = component.long_name;
            } else if (type == "country") {
              country = component.long_name;
            }
          }
        }
      }
      profile.short_location = locality + ", " + comunity;
      profile.long_location = locality + ", " + province + ", " + comunity + ", " + country;
      registerUser(res, profile);
    }, { language: "es" });
  })
};
