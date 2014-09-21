(function () {
  'use strict';

  var ProductController = function (product,Restangular,userService, bidService) {
      var _this = this;
      _this.product = product;
      _this.Restangular = Restangular;

      _this.placeBid =function(amount){

          bidService.placeBid(_this.product.id, amount, _this.user.userId)
              .then(function (data)
              {
                  alert(data[0].message);
              });


      }

      userService.getUserId()
          .then(function (data) { _this.user = data[0]; });
  };




  ProductController.$inject = ['product','Restangular', "UserService", "BidService"];
  angular.module('auction').controller('ProductController', ProductController);
}());
