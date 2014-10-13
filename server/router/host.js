module.exports = function (app, config, pathSettings, base) {
  
  app.get("/host/:id", function (req, res) {
    res.send("siiii el id es: " + req.params.id, 200);
  });
  
};