(function (W, D, M, md5, undefined) {
  var id = function (a) {
    return D.getElementById(a);
  },
  letters    = "a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð",
  validName  = new RegExp("^(((["+letters+"]+)((-["+letters+"]+)*)(('(["+letters+"]+)?)*)|())( ?)){1,4}$"),
  validEmail = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
  validCoord = /^[+-]?([0-9]|[1-9][0-9]|[1][0-7][0-9]|180)(\.[0-9]+)?$/,
  validToken = /^[a-fA-F0-9]{32}$/,
  anything   = /^.+$/,
  validators = {
    "name": validName,
    "email": validEmail,
    "latitude": validCoord,
    "longitude": validCoord,
    "short_location": anything,
    "long_location": anything
  },
  userdata = depot("userdata"),
  storage = function (data) {
    if (!data) return userdata.get(userdata.ids[0]);
    return userdata.update(userdata.ids[0], data);
  },
  validate = function (id) {
    var element = D.getElementById(id);
    if(!validators[id].test(storage()[id])) {
      element.style.color = "red"
    } else {
      element.style.color = "inherit";
      if (id == "email") {
        gravatar.src = "https://www.gravatar.com/avatar/" + md5(storage().email) + ".png?s=150&d=blank";
        gravatar.title = storage().email;
      }
    }
    return ;
  },
  updateAllData = function () {
    data = storage();
    if (data.name != "") {
      name.innerHTML = data.name;
      name.title = data.name;
      validate("name");
    }
    if (data.email != "") {
      email.innerHTML = data.email;
      email.title = data.email;
      gravatar.title = data.email;
      gravatar.src = "https://gravatar.com/avatar/" + md5(data.email) + ".png?s=150&d=blank";
      validate("email");
    }
    if (data.latitude != "") {
      latitude.innerHTML = _fixed(data.latitude, 5);
      latitude.title = data.latitude;
      validate("latitude");
    }
    if (data.longitude != "") {
      longitude.innerHTML = _fixed(data.longitude, 5);
      longitude.title = data.longitude;
      validate("longitude");
    }
    if (data.short_location != "") {
      geolocation.innerHTML = data.short_location;
      geolocation.title = data.long_location;
      geolocation.style.height = coords.offsetHeight + "px";
      geolocation.style.width = 240 - coords.offsetWidth + "px";
      validate("short_location");
    }
  },
  gravatar = id("gravatar"),
  name = id("name"),
  email = id("email")
  geolocation = id("short_location"),
  coords = id("coords"),
  latitude = id("latitude"),
  longitude = id("longitude"),
  info = id("info");
  var replaceByInput = function (id) {
    var input = D.createElement("input");
    input.value = this.innerHTML;
    input.className = "inline-input";
    input.id = id + "-input";
    input.style.width = this.offsetWidth + "px";
    input.style.height = this.offsetHeight + "px";
    return input;
  },
  removeInput = function (id) {
    D.getElementById(id).innerHTML = D.getElementById(id + "-input").value;
  }
  _fixed = function (num, round) {
    return (num * M.pow(10, round) | 0) / M.pow(10, round);
  };
  var mouseEnter = function (id) {
    return function () {
      if (D.getElementById(id + "-input")) {
        return;
      }
      var input = replaceByInput.call(this, id);
      input.addEventListener("keyup", function () {
        var data = {};
        data[id] = this.value;
        storage(data);
        validate(id);
      });
      input.addEventListener("blur", mouseBlur(id));
      this.innerHTML = "";
      this.appendChild(input);
    };
  },
  mouseExit = function (id) {
    return function () {
      var input = D.getElementById(id + "-input");
      if (input != D.activeElement) {
        removeInput(id);
      }
    };
  },
  mouseBlur = function (id) {
    return function () {
      removeInput(id);
      validate(id);
    };
  };
  updateAllData();
  name.addEventListener("mouseenter", mouseEnter("name"));
  name.addEventListener("mouseout", mouseExit("name"));
  email.addEventListener("mouseenter", mouseEnter("email"));
  email.addEventListener("mouseout", mouseExit("email"));
  info.addEventListener("click", function () {
    navigator.geolocation.getCurrentPosition(function (pos) {
      var positionData = {
        longitude: pos.coords.longitude,
        latitude: pos.coords.latitude,
        timestamp: pos.timestamp
      };
      marmottajax.get({
        url: "https://maps.googleapis.com/maps/api/geocode/json",
        options: {
          latlng: positionData.latitude + "," + positionData.longitude,
          sensor: false
        },
        json: true
      }).then(function (data) {
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
        positionData.short_location = locality + ", " + comunity;
        positionData.long_location = locality + ", " + province + ", " + comunity + ", " + country;
        storage(positionData);
        updateAllData();
      });
    });
  });
})(window, document, Math, md5);