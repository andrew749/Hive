// @file jwtverify.js
//Checks if the token is valid
 
var User = require('../models/User');
var jwt = require('jwt-simple');
 
module.exports = function(req, res, next) {
    var token = (req.body && req.body.access_token) || (req.query && req.query.access_token) || req.headers['x-access-token'];
    if (token) {
      try {
        var decoded = jwt.decode(token, app.get('jwtTokenSecret'));
        if (decoded.exp <= Date.now()) {
          return res.json({msg:'Access token has expired'});
        }
        var _ObjectId = require('mongoose').Types.ObjectId;
        query_id =  new _ObjectId(decoded.iss);
        User.findOne({ _id: query_id }, function(err, user) {
          req.user = user;
        });
      } catch (err) {
        return next();
      }
    } else {
      next();
    }
};