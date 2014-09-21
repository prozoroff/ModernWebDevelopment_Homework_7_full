(function () {
    'use strict';

    var SearchController = function (
        $location,
        productService) {

        this.$location         = $location;
        this.productService    = productService;

        var params = productService.importParams($location.search());
        if (!_.isEmpty(params)) {
            this.find();
        }
    };

    SearchController.prototype = {
        find: function () {
            var params = this.productService.exportParams();
            this.$location.search(params);
            this.productService.find(params).then(function (data) {
                this.products = data;
            }.bind(this));
        }
    };

    SearchController.$inject = [
        '$location',
        'ProductService'
    ];
    angular.module('auction').controller('SearchController', SearchController);
}());
