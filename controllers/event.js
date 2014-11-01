var _ = require('lodash');
var async = require('async');
var crypto = require('crypto');
var nodemailer = require('nodemailer');
var passport = require('passport');
var Event = require('../models/Event');
var secrets = require('../config/secrets');
var User = require('../models/User');
var jwt = require('jwt-simple');

/**
 * GET /events/info
 * Sends info about a given event
 * requires event ID
 */

exports.getInfo = function(req, res) {
    if(!req.params.id){
        //todo return errors
    }
    console.log(req.params);

    var _ObjectId = require('mongoose').Types.ObjectId;
    query = new _ObjectId(req.params.id);

    Event.find({_id: query})
    .exec(function(err,data) {
        if(err)res.send(err);
        console.log(err);
        //console.log(data[0].datetime_start);
        //console.log(new Date(data[0].datetime_start).getTime());
        var response = data[0].toObject();
        //convert to epoch time
        response.datetime_start_unix = new Date(response.datetime_start).getTime();
        response.datetime_end_unix = new Date(response.datetime_end).getTime();
        res.json(response);
    });
};

/**
 * POST /events/nearMe
 * Finds nearby events
 * Requires location [longitude,latitude] and distance parameters
 */

exports.postNearMe = function(req, res) {
  //Ensure user is logged in
  if (!req.user){
    console.log("can't");
    return res.json({error:"Not logged in"});
  }
  else {
    //check to see if parameters are all there
    if(!req.body.lng||!req.body.lat||!req.body.distance){
      console.log("Missing parameters");
    }
    else{
      var point = {type: 'Point', coordinates: [parseFloat(req.body.lng), parseFloat(req.body.lat)]};

    Event.geoNear(point, {maxDistance: req.body.distance/ 6378137,
    distanceMultiplier: 6378137 , spherical : true }, function(err, results, stats) {
      if(err)res.send(err);
      console.log(err + " " + results+ " " + stats);
        var response = results;
        var resTotal = [];
        for (index = 0; index < response.length; ++index) {
            //console.log(a[index]);
            //convert to epoch time
            var reso = response[index].toObject();
            reso.datetime_start_unix = new Date(reso.datetime_start).getTime();
            reso.datetime_end_unix = new Date(reso.datetime_end).getTime();
            resTotal.push(reso);
        }

        res.json(resTotal);
    });
  }
}
};

/**
 * POST /events/byUser
 * Finds nearby events
 * requires location [longitude,latitude] and distance parameters
 */

exports.postByUser = function(req, res) {
    var decoded = jwt.decode(req.body.access_token, req.app.get('jwtTokenSecret'));
    if (decoded.exp <= Date.now()) {
      return res.json({msg:'Access token has expired'});
    }
    var _ObjectId = require('mongoose').Types.ObjectId;
    query_id =  new _ObjectId(decoded.iss);
    User.findOne({ _id: query_id }, function(err, user) {
      req.user = user;
    if (!req.user){
        console.log("can't");
        return res.json({error:"Not logged in"});
    }
    else {
        if(!req.body.lng||!req.body.lat||!req.body.distance){
            //todo return errors
        }
        // var _ObjectId = require('mongoose').Types.ObjectId;
        // userQuery = new _ObjectId(req.body.user);
        Event.find({createdBy: req.user})
        .exec(function(err,data) {
            if(err)res.send(err);
            //console.log(err);
            var response = data;
            var resTotal = [];
            for (index = 0; index < response.length; ++index) {
                //console.log(a[index]);
                //convert to epoch time
                var reso = response[index].toObject();
                reso.datetime_start_unix = new Date(reso.datetime_start).getTime();
                reso.datetime_end_unix = new Date(reso.datetime_end).getTime();
                resTotal.push(reso);
            }

            res.json(resTotal);
        });
    }
    });

};


/**
 * POST /event/create
 * Add event
 */

exports.postCreate = function(req, res) {
    if (!req.user){
      console.log("can't");
      return res.json({error:"Not logged in"});
    }
    else {
      //creating an event from parameters
      var event = new Event({
          name: req.body.name,
          location: {
            coordinates: [parseFloat(req.body.lng),parseFloat(req.body.lat)]
          },
          datetime: req.body.datetime,
          visibility: req.body.visibility,
          description: req.body.description,
          picture: req.body.pic,
          createdBy: req.user
      });

      event.save(function(err) {
      if (!err)
        res.json({status:'Successfully reated a new event!'});
      else
        res.json({error: err});
    });
  }
};

/**
 * POST /event/edit
 * Edit event
 */

exports.postEdit = function(req, res) {
  if (!req.user){
    console.log("can't");
    return res.json({error:"Not logged in"});
  }
  else {
    //Find event and replace values with those in parameters
    var _ObjectId = require('mongoose').Types.ObjectId;
    query_id =  new _ObjectId(req.body.id);
    var query = Event.where({_id: query_id});
    query.findOne(function(err, event){
      //console.log(err);
      if (err) return res.json({error: err});

      event.name = req.body.name || event.name;
      event.location.coordinates = [ parseFloat(req.body.lng || event.location.coordinates[0]), parseFloat(req.body.lat || event.location.coordinates[1])];
      event.datetime = req.body.datetime || event.datetime;
      event.visibility = req.body.visibility || event.visibility;
      event.description = req.body.description || event.description;
      event.picture = req.body.pic || event.picture;

      event.save(function(err) {
        if (err) return res.json({error:err});
        req.flash('success', { msg: 'Event information updated.' });
      });
    });
  }
};

/**
 * POST /event/delete
 * Delete event.
 */

exports.postDeleteEvent = function(req, res, next) {
    var decoded = jwt.decode(req.body.access_token, req.app.get('jwtTokenSecret'));
    if (decoded.exp <= Date.now()) {
      return res.json({msg:'Access token has expired'});
    }
    var _ObjectId = require('mongoose').Types.ObjectId;
    query_id =  new _ObjectId(decoded.iss);
    User.findOne({ _id: query_id }, function(err, user) {
      req.user = user;
    if (!req.user){
    console.log("can't");
    return res.json({error:"Not logged in"});
    }
    else {
    Event.remove({ _id: req.body.id }, function(err) {
      //console.log(err);
      if (err) return res.json({error:err});
      res.json({ msg: 'Your event has been deleted.' });
    });
    }
    });
}



/**
 * POST /event/comment
 * Comment on an event
 */

exports.postComment = function(req, res) {

  if (!req.user){
    console.log("can't");
    return res.json({error:"Not logged in"});
  }
  else {
    //Find event on which you want to comment
    var _ObjectId = require('mongoose').Types.ObjectId;
    query_id =  new _ObjectId(req.body.id);
    var query = Event.where({_id: query_id});
    query.findOne(function(err, event){
      if (err) return next(err);
      //Add a comment with the date/time
      event.comments.push({user: req.user.email, comment: req.body.commentMsg, datetime: new Date()});

      event.save(function(err) {
      if (!err)
        res.json({status:'Pushed a comment to the event'});
      else
        res.json({error:err});
      });
    });
  }
};

/**
 * POST /event/deleteComment
 * Delete a comment on an event
 */
//_.find(event.comments, function(e){
      //return e.where(
// exports.postDeleteComment = function(req, res) {
//   if (!req.user){
//     console.log("can't");
//     return res.json({error:"Not logged in"});
//   }
//   else {
//     var _ObjectId = require('mongoose').Types.ObjectId;
//     query_id =  new _ObjectId(req.body.id);
//     var query = Event.where({_id: query_id});
//     var item = {
//     user: req.user.email,
//     comment: req.body.msg,
//     datetime: req.body.datetime
//    };
//     query.findOne({comment: item},function(err, event){

//       });
//       for (var i=0; i < event.comments.length; i++){
//         if (event.comments[i] == item){
//           array.splice(i,1);
//         }
//       }
//     });
//   }
// };
