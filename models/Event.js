var mongoose = require('mongoose');
var bcrypt = require('bcrypt-nodejs');
var crypto = require('crypto');

var eventSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    location: {
        type: {
            type: String,
            required: true,
            enum: ['Point', 'Polygon'],
            default: 'Point'
        },
        coordinates: [Number]
    },
    datetime: { type: Date, default: Date.now },
    visibility: Boolean,
    guests: Array,
    comments: Array,
    picture: String
});
eventSchema.index({
    location: '2dsphere'
});
//db.events.ensureIndex( { location : "2dsphere" } )
/**
 * Hash the password for security.
 * "Pre" is a Mongoose middleware that executes before each user.save() call.
 */

eventSchema.pre('save', function(next) {
    var user = this;
    next();
});

module.exports = mongoose.model('Event', eventSchema);
