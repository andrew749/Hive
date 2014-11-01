var _ = require('lodash');
var async = require('async');
var crypto = require('crypto');
var nodemailer = require('nodemailer');
var passport = require('passport');
var Event = require('../models/Event');
var secrets = require('../config/secrets');
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
        res.send(data);
    });
};

/**
 * POST /events/nearMe
 * Finds nearby events
 * requires location [longitude,latitude] and distance parameters
 */

exports.postNearMe = function(req, res) {
    if(!req.body.lng||!req.body.lat||!req.body.distance){
        //todo return errors
    }
    console.log(req.body);
    var point = {type: 'Point', coordinates: [parseFloat(req.body.lng), parseFloat(req.body.lat)]};

    Event.geoNear(point, {maxDistance: req.body.distance/ 6378137,
  distanceMultiplier: 6378137 , spherical : true }, function(err, results, stats) {
        if(err)res.send(err);
        console.log(err + " " + results+ " " + stats);
        res.send(results);
    });
};

/**
 * POST /event/create
 * Add event
 */

exports.postCreate = function(req, res) {
    //req.assert('email', 'Email is not valid').isEmail();
    //req.assert('password', 'Password must be at least 4 characters long').len(4);
    //req.assert('confirmPassword', 'Passwords do not match').equals(req.body.password);

    var errors = req.validationErrors();

    if (errors) {
    req.flash('errors', errors);
    return res.redirect('/signup');
    }
    console.log(req.body);
    var event = new Event({
        name: req.body.name,
        location: {
             coordinates: [parseFloat(req.body.lng),parseFloat(req.body.lat)]
        },
        datetime: req.body.datetime,
        visibility: req.body.visibility,
        picture: req.body.pic
    });

    /*
    User.findOne({ email: req.body.email }, function(err, existingUser) {
    if (existingUser) {
      req.flash('errors', { msg: 'Account with that email address already exists.' });
      return res.redirect('/signup');
    } */
    event.save(function(err) {
      if (!err)
        res.send("OK!");
    else
        res.send("You suck! " + err);
    });
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
