'use strict';

var App = angular.module('app', [
    'angular-logger',
    'angular-growl',
    'angularMoment',
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngMessages',
    'pascalprecht.translate',
    'ui.bootstrap',
    'ui.router',
    'http-auth-interceptor',
    'spring-security-csrf-token-interceptor',

    //Partials
    'templates',

    //Common
    'app.common.config', 'app.common.services',

    //Account
    'app.account.config', 'app.account.directives', 'app.account.services', 'app.account.controllers',

    //Tasks
    'app.task.controllers', 'app.task.directives', 'app.task.services'

]);

App.run(['$log', '$rootScope', '$state', 'AUTH_EVENTS', 'AuthenticatedUser', 'authService', 'AuthenticationService',
    function ($log, $rootScope, $state, AUTH_EVENTS, AuthenticatedUser, authService, AuthenticationService) {

        var logger = $log.getInstance('app');

        //This function retries all requests that were failed because of
        //the 401 response.
        function listenAuthenticationEvents() {
            var confirmLogin = function() {
                authService.loginConfirmed();
            };

            $rootScope.$on(AUTH_EVENTS.loginSuccess, confirmLogin);

            var viewLogInPage = function() {
                logger.info('User is not authenticated. Rendering login view.');
                $state.go('task.login');
            };

            $rootScope.$on(AUTH_EVENTS.notAuthenticated, viewLogInPage);

            var viewTaskListPage = function() {
                logger.info("User logged out. Rendering task list view.");
                $state.go('task.list', {}, {reload: true});
            };

            $rootScope.$on(AUTH_EVENTS.logoutSuccess, viewTaskListPage);

            var viewForbiddenPage = function() {
                logger.info('Permission was denied for user: %j', AuthenticatedUser);
                $state.go('task.forbidden');
            };

            $rootScope.$on(AUTH_EVENTS.notAuthorized, viewForbiddenPage);
        }

        //This function ensures that anonymous users cannot access states
        //that marked as protected (i.e. the value of the authenticated
        //property is set to true).
        function secureProtectedStates() {
            $rootScope.$on('$stateChangeStart', function (event, toState, toParams) {
                logger.trace('Moving to state: %s', toState.name);
                AuthenticationService.authorizeStateChange(event, toState, toParams);
            });
        }

        $rootScope.currentUser = AuthenticatedUser;

        listenAuthenticationEvents();
        secureProtectedStates();
    }]);