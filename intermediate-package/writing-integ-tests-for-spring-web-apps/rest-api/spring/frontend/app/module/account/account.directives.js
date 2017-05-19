'use strict';

angular.module('app.account.directives', [])
    .directive('loginForm', ['$log', 'AUTH_EVENTS', 'AuthenticationService', function ($log, AUTH_EVENTS, AuthenticationService) {

        var logger = $log.getInstance('app.account.directives.loginForm');

        return {
            link: function (scope, element, attr) {
                scope.login = {};
                scope.loginFailed = false;

                scope.$on(AUTH_EVENTS.loginFailed, function() {
                    logger.info('Received login failed event.');
                    scope.loginFailed = true;
                });

                scope.submitLoginForm = function() {
                    logger.info('Submitting log in form.');
                    AuthenticationService.logIn(scope.login.username, scope.login.password);
                };
            },
            templateUrl: 'account/login-form-directive.html',
            scope: {
            }
        };
    }]);