/**
 * Created by alex_prozorov on 20.09.2014.
 */
(function () {
    'use strict';

    var UserService = function (Restangular) {
        this.Restangular = Restangular;
    };

    UserService.$inject = ['Restangular'];

    UserService.prototype = {

        getUserId: function (){
            var u = this.Restangular.all('user').getList();
            return u;
        }
    };

    angular.module('auction').service('UserService', UserService);
}());
