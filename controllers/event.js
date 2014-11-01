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
 * requires location [longitude,latitude] and distance parameters
 */

exports.postNearMe = function(req, res) {
  if (!req.user){
    console.log("can't");
    return res.json({error:"Not logged in"});
  } 
  else {
    if(!req.body.lng||!req.body.lat||!req.body.distance){
        //todo return errors
    }
    console.log(req.body);
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
    });
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
};


/**
 * POST /event/create
 * Add event
 */

exports.postCreate = function(req, res) {
    //req.assert('email', 'Email is not valid').isEmail();
    //req.assert('password', 'Password must be at least 4 characters long').len(4);
    //req.assert('confirmPassword', 'Passwords do not match').equals(req.body.password);

    // var errors = req.validationErrors();

    // if (errors) {
    // //req.flash();
    // return res.json({'errors': errors});
    // }

    console.log(req.body);

    if (!req.user){
      console.log("can't");
      return res.json({error:"Not logged in"});
    } 
    else {
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

      /*
      User.findOne({ email: req.body.email }, function(err, existingUser) {
      if (existingUser) {
        req.flash('errors', { msg: 'Account with that email address already exists.' });
        return res.redirect('/signup');
      } */

      event.save(function(err) {
      if (!err)
        res.json({status:'ok'});
      else
        res.json({status:err});
    });   
  }  
};

/**
 * POST /event/edit
 * Edit event
 */

exports.postEdit = function(req, res, next) {
  if (!req.user){
    console.log("can't");
    return res.json({error:"Not logged in"});
  } 
  else {
    var _ObjectId = require('mongoose').Types.ObjectId;
    query_id =  new _ObjectId(req.body.id);
    var query = Event.where({_id: query_id});
    query.findOne(function(err, event){
      //console.log(err);
      if (err) return next(err);

      event.name = req.body.name || event.name;
      event.location.coordinates = [ parseFloat(req.body.lng || event.location.coordinates[0]), parseFloat(req.body.lat || event.location.coordinates[1])];
      event.datetime = req.body.datetime || event.datetime;
      event.visibility = req.body.visibility || event.visibility;
      event.description = req.body.description || event.description;
      event.picture = req.body.pic || event.picture;

      event.save(function(err) {
        if (err) return next(err);
        req.flash('success', { msg: 'Event information updated.' });
        //res.redirect('/event');
        res.json({status:"You da real mvp"});
      });
    });
  }
};

/**
 * POST /event/delete
 * Delete event.
 */

exports.postDeleteEvent = function(req, res, next) {
  if (!req.user){
    console.log("can't");
    return res.json({error:"Not logged in"});
  } 
  else {
    Event.remove({ _id: req.body.id }, function(err) {
      //console.log(err);
      if (err) return next(err);
      res.json({ msg: 'Your event has been deleted.' });
    });
  }
};

/**
 * POST /event/comment
 * Comment on an event
 */

exports.postComment = function(req, res) {
  console.log(req.body);

  if (!req.user){
    console.log("can't");
    return res.json({error:"Not logged in"});
  } 
  else {
    var _ObjectId = require('mongoose').Types.ObjectId;
    query_id =  new _ObjectId(req.body.id);
    var query = Event.where({_id: query_id});
    query.findOne(function(err, event){
      if (err) return next(err);

      event.comments.push({user: req.user.email, comment: req.body.commentMsg, datetime: new Date()});

      event.save(function(err) {
      if (!err)
        res.json({status:'ok'});
      else
        res.json({status:err});
      });   
    });
  }      
};

/**
 * POST /login
 * Sign in using email and password.
 * @param email
 * @param password
 */

exports.postLogin = function(req, res, next) {
  req.assert('email', 'Email is not valid').isEmail();
  req.assert('password', 'Password cannot be blank').notEmpty();

  var errors = req.validationErrors();

  if (errors) {
    req.flash('errors', errors);
    return res.redirect('/login');
  }

  passport.authenticate('local', function(err, user, info) {
    if (err) return next(err);
    if (!user) {
      req.flash('errors', { msg: info.message });
      return res.redirect('/login');
    }
    req.logIn(user, function(err) {
      if (err) return next(err);
      req.flash('success', { msg: 'Success! You are logged in.' });
      res.redirect(req.session.returnTo || '/');
    });
  })(req, res, next);
};
