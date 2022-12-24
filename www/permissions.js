var exec = require('cordova/exec');
var PLUGIN_NAME = 'permissions';

var permissions = {

	check : function (permissions, success, error ) {
		exec(success, error, PLUGIN_NAME, 'check', permissions);
	},
	ask: function (permissions,success, error ) {
		exec(success, error, PLUGIN_NAME, 'ask', permissions);
	}
};

module.exports = permissions;
