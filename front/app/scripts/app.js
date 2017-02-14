'use strict';

/**
 * @ngdoc overview
 * @name messengerApp
 * @description
 * # messengerApp
 *
 * Main module of the application.
 */
angular
  .module('messengerApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ui.bootstrap',
    'smart-table'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .when('/administration', {
        templateUrl: 'views/administration.html',
        controller: 'AdministrationCtrl',
        controllerAs: 'administration'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
