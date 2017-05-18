'use strict'

angular.module('app.common.config', [])

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

        // Use HTML5 mode for routes. The base path is configured in the head-section of 'client.jsp'.
        $locationProvider.html5Mode(true);
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
    }]);