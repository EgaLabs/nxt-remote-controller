/**
 * Main script for client page.
 * @author Esaú García (EgaTuts).
 * @version 1.0.0
 */
(function (root, undefined) {
  "use strict";

  /*
   * Little fallback when user media requires to be browser prefixed.
   */
  navigator.getMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia,
  
  /*
   * We get the sources and try to get first the camera that's facing the environment (rear).
   */
  MediaStreamTrack.getSources(function (sources) {
    var rear = [],
        front = [],
        i = 0,
        e = 0,
        source;
    for (; i < sources.length; i++) {
      source = sources[i];
      if (source.kind !== "video") continue;

      if (source.facing === "environment") {
        rear.push(source);
      } else if (source.facing === "user") {
        front.push(source);
      }
    }
    for (; e < front.length; e++) {
      rear.push(front[e]);
    }
    startStreaming(rear);
  });

  /*
   * Splits the queries of the actual URL and returns it's values based on it's ids.
   */
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
  },

  /*
   * Returns a new PeerJS instance with ICE servers configured.
   */
  getPeerInstance = function (id) {
    return new Peer(id, {
      host: "localhost",
      port: 9000,
      path: "/",
      debug: true,
      config: {
        "iceServers": [
          { url: "stun:stun01.sipphone.com"      }, { url: "stun:stun.ekiga.net"           },
          { url: "stun:stun.fwdnet.net"          }, { url: "stun:stun.ideasip.com"         },
          { url: "stun:stun.iptel.org"           }, { url: "stun:stun.rixtelecom.se"       },
          { url: "stun:stun.schlund.de"          }, { url: "stun:stun.l.google.com:19302"  },
          { url: "stun:stun1.l.google.com:19302" }, { url: "stun:stun2.l.google.com:19302" },
          { url: "stun:stun3.l.google.com:19302" }, { url: "stun:stun4.l.google.com:19302" },
          { url: "stun:stunserver.org"           }, { url: "stun:stun.softjoys.com"        },
          { url: "stun:stun.voiparound.com"      }, { url: "stun:stun.voipbuster.com"      },
          { url: "stun:stun.voipstunt.com"       }, { url: "stun:stun.voxgratia.org"       },
          { url: "stun:stun.xten.com"            },
          
          { url: "turn:numb.viagenie.ca",                 credential: "muazkh",                       username: "webrtc@live.com"     },
          { url: "turn:192.158.29.39:3478?transport=udp", credential: "JZEOEt2V3Qb0y27GRntt2u2PAYA=", username: "28224511:1379330808" },
          { url: "turn:192.158.29.39:3478?transport=tcp", credential: "JZEOEt2V3Qb0y27GRntt2u2PAYA=", username: "28224511:1379330808" }
        ]
      }
    });
  },

  /*
   * Starts the streaming.
   */
  startStreaming = function (cameras) {
    navigator.getMedia({
      video: {
        mandatory: {
          minFrameRate: 30
        },
        optional: [
          { sourceId: cameras[0].id   },
          { facingMode: "environment" },
          { frameRate: 60 }
        ]
      },
      audio: true
    }, function (stream) {

      var peer = getPeerInstance(GET("from")),
          call = peer.call(GET("to"), stream);
      /*
       * When the remote stream is received.
       */
      call.on("stream", function (remote) {
        // Do something here with the stream like attaching it to a video source attribute.
      });

    }, function () {
      alert("An error ocurred while accessing the camera.");
    });
  };

})(this);