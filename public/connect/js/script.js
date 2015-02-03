/**
 * Main script for client page.
 * @author Esaú García (EgaTuts).
 * @version 1.0.0
 */
(function (root, undefined) {
  "use strict";

  var configData = [
    { url:'stun:stun01.sipphone.com' },
    { url:'stun:stun.ekiga.net' },
    { url:'stun:stun.fwdnet.net' },
    { url:'stun:stun.ideasip.com' },
    { url:'stun:stun.iptel.org' },
    { url:'stun:stun.rixtelecom.se' },
    { url:'stun:stun.schlund.de' },
    { url:'stun:stun.l.google.com:19302' },
    { url:'stun:stun1.l.google.com:19302' },
    { url:'stun:stun2.l.google.com:19302' },
    { url:'stun:stun3.l.google.com:19302' },
    { url:'stun:stun4.l.google.com:19302' },
    { url:'stun:stunserver.org' },
    { url:'stun:stun.softjoys.com' },
    { url:'stun:stun.voiparound.com' },
    { url:'stun:stun.voipbuster.com' },
    { url:'stun:stun.voipstunt.com' },
    { url:'stun:stun.voxgratia.org' },
    { url:'stun:stun.xten.com' },
    {
      url: 'turn:numb.viagenie.ca',
      credential: 'muazkh',
      username: 'webrtc@live.com'
    },
    {
      url: 'turn:192.158.29.39:3478?transport=udp',
      credential: 'JZEOEt2V3Qb0y27GRntt2u2PAYA=',
      username: '28224511:1379330808'
    },
    {
      url: 'turn:192.158.29.39:3478?transport=tcp',
      credential: 'JZEOEt2V3Qb0y27GRntt2u2PAYA=',
      username: '28224511:1379330808'
    }
  ];

  var GET = function (variable) {
    var query = window.location.search.substring(1),
        vars = query.split("&"),
        i = 0,
        pair;
    for (; i < vars.length; i++) {
        pair = vars[i].split("=");
        if(pair[0] == variable) return pair[1];
    }
    return false ;
  };
  var peer = new Peer(GET("from"),
    {
      host: "localhost",
      port: 9000,
      path: "/",
      debug: true,
      config: {
        "iceServers": configData
      }
    }
  ),
  attachStream = function (video, stream) {
    if (navigator.mozGetUserMedia) {
      video.mozSrcObject = stream;
    } else {
      var vendor = window.URL || window.webkitURL;
      video.src = vendor.createObjectURL(stream);
    }
    video.play();
  };
  navigator.getMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia,
  peer.on("connection", function () {
    console.log(arguments);
  });
  peer.on("stream", function (stream) {
    attachStream(document.getElementById("you"), stream);
  });
  navigator.getMedia({ "video": true, "audio": true }, function (stream) {
    var call = peer.call(GET("to"), stream);
    attachStream(document.getElementById("me"), stream);
    call.on("stream", function (remoteStream) {
      attachStream(document.getElementById("you"), remoteStream);
    });
  }, function () {
    alert("TU NAVEGADOR NO SOPORTA VIDEO CONFERENCIAS CON HTML 5");
  });

})(this);