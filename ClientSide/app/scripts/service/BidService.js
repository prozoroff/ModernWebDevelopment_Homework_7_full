/**
 * Created by alex_prozorov on 20.09.2014.
 */
(function () {
    'use strict';

    var BidService = function (Restangular) {
        this.Restangular = Restangular;
    };

    BidService.$inject = ['Restangular'];

    BidService.prototype = {

        placeBid: function (productId, amount, userId){

            var payload = {"productId": + productId
             ,"amount":  amount
             , "desiredQuantity":  1
             , "userId": userId  };
            //var payloadJson =  JSON.parse(payload);
            var response = this.Restangular.all("bid").post(payload);
            return response;
        }
    };

    angular.module('auction').service('BidService', BidService);
}());
