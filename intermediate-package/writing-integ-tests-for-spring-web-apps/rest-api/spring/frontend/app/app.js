'use strict';

var App = angular.module('app', [
    'angular-logger',
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngMessages',
    'pascalprecht.translate',
    'ui.bootstrap',
    'ui.router',

    //Partials
    'templates',

    //Common
    'app.common.config',

    //Tasks
    'app.task.controllers'

]);