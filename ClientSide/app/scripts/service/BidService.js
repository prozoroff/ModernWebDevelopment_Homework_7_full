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

        wsconfig: function () {
            var ws = new WebSocket('ws://localhost:8080/auction_jaxrs-1.0/ws');
            var WS_SESSION_ID = null;

            ws.onmessage = function (event) {
                if (WS_SESSION_ID === null) WS_SESSION_ID = event.data;
                else {
                    var message = event.data.split(':', 2);
                    alert(message);
                }
            };
        },

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
