'use strict'

angular.module('app.common.config', [])
    .constant('COMMON_EVENTS', {
        notFound: 'event:not-found'
    })

    .config(['growlProvider', function(growlProvider) {
        growlProvider.globalPosition('bottom-right');
        growlProvider.globalTimeToLive(5000);
    }])

    .config(['logEnhancerProvider', function (logEnhancerProvider) {
        logEnhancerProvider.datetimePattern = 'DD.MM.YYYY HH:mm:ss';
        logEnhancerProvider.prefixPattern = '%s::[%s]> ';
        logEnhancerProvider.logLevels = {
            '*': logEnhancerProvider.LEVEL.DEBUG
        };
    }])

    .config(['$urlRouterProvider', '$locationProvider', function ($urlRouterProvider, $locationProvider) {
        $urlRouterProvider.otherwise( function($injector) {
            var $state = $injector.get("$state");
            $state.go("task.list");
        });

        $locationProvider.html5Mode(false);
    }])

    .config(['$translateProvider', function ($translateProvider) {
        // Initialize angular-translate
        $translateProvider.useStaticFilesLoader({
            prefix: '/i18n/',
            suffix: '.json'
        });

        $translateProvider.preferredLanguage('en');
        $translateProvider.fallbackLanguage('en');
        $translateProvider.useCookieStorage();
        $translateProvider.useMissingTranslationHandlerLog();
        $translateProvider.useSanitizeValueStrategy('escaped');
    }])

    .config(['$httpProvider', function ($httpProvider) {
        $httpProvider.interceptors.push([
            '$injector',
            function ($injector) {
                return $injector.get('404Interceptor');
            }
        ]);
    }])

    .factory('404Interceptor', ['$rootScope', '$q', 'COMMON_EVENTS', function ($rootScope, $q, COMMON_EVENTS) {
        return {
            responseError: function(response) {
                if (response.status === 404) {
                    $rootScope.$broadcast(COMMON_EVENTS.notFound);
                }
                return $q.reject(response);
            }
        };
    }]);