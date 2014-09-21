(function () {
    'use strict';

    var storage = { lowPrice: 0, highPrice: 500, bids: 3, category: 0, closeDay: null, title: ""};

    var ProductService = function (Restangular) {
        this.Restangular = Restangular;
    };

    ProductService.$inject = ['Restangular'];

    ProductService.prototype = {

        getProducts: function () {
            var p = this.Restangular.all('product').getList();
            return p;
        },

        find: function (params) {
            return this.Restangular.all('product').getList(params);
        },

        getProductById: function (productId) {
            return this.Restangular.one('product', productId).get();
        },

        get lowPrice() {
            return this._lowPrice;
        },
        set lowPrice(newVal) {
            this._lowPrice = parseFloat(newVal);
        },

        get highPrice() {
            return this._highPrice;
        },
        set highPrice(newVal) {
            this._highPrice = parseFloat(newVal);
        },

        get bids() {
            return this._bids;
        },
        set bids(newVal) {
            this._bids = parseInt(newVal);
        },

        get category() {
            return this._category;
        },
        set category(newVal) {
            this._category = parseInt(newVal);
        },

        exportParams: function () {
            return getParams(this);
        },

        importParams: function (params) {
            var params = getParams(params);
            _.assign(this, params);
            return params;
        }
    };


    var getParams = function (obj) {

        return _.reduce(obj, function (params, value, key) {

            if (key.indexOf('_') === 0) {
                key = key.substring(1);
            }

            if (_.isString(value) && _.isEmpty(value.trim())) {
                return params;
            }

            if (key == 'Restangular') {
                return params;
            }

            if (!_.isNull(value) &&
                !_.isNaN(value) &&
                value !== storage[key]) {
                params[key] = value;
            }
            return params;

        }, {});
    };

    angular.module('auction').service('ProductService', ProductService);
}());
