'use strict';

angular.module('app.account.controllers', [])
    .config(['$stateProvider',
        function ($stateProvider) {
            $stateProvider
                .state('task.login', {
                    url: 'user/login',
                    controller: 'LoginController',
                    templateUrl: 'account/login-view.html'
                });
        }
    ])
    .controller('LoginController', ['$log', function($log) {
        var logger = $log.getInstance('app.account.controllers.LoginController');
        logger.info('Rendering login view.');
    }]);