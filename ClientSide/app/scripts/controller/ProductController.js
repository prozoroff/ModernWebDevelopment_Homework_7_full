(function () {
  'use strict';

  var ProductController = function (product,Restangular,userService, bidService) {
      var _this = this;
      _this.product = product;
      _this.Restangular = Restangular;
      _this.ws = new WebSocket('ws://localhost:8080/auction_jaxrs-1.0/ws');
      _this.WS_SESSION_ID = null;

      _this.placeBid =function(amount){

          bidService.placeBid(_this.product.id, amount, this.WS_SESSION_ID)
              .then(function (data)
              {
                  alert(data[0].message);
              });


      }

      _this.ws.onmessage = function (event) {
          if (_this.WS_SESSION_ID === null) _this.WS_SESSION_ID = event.data;
          else {
              _this.product.reservedPrice = event.data.split(':')[1];
          }
      };

  };




  ProductController.$inject = ['product','Restangular', "UserService", "BidService"];
  angular.module('auction').controller('ProductController', ProductController);
}());
