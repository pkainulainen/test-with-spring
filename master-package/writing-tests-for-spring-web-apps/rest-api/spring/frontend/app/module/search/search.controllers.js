'use strict';

angular.module('app.search.controllers', [])
    .config(['$stateProvider',
        function ($stateProvider) {
            $stateProvider
                .state('task.search', {
                    authenticate: true,
                    url: 'task/search/:searchTerm',
                    controller: 'SearchResultController',
                    templateUrl: 'search/search-result-view.html',
                    resolve: {
                        searchResults: ['TaskSearchService', '$stateParams', function(TaskSearchService, $stateParams) {
                            if ($stateParams.searchTerm) {
                                return TaskSearchService.findBySearchTerm($stateParams.searchTerm);
                            }

                            return null;
                        }],
                        searchTerm: ['$stateParams', function($stateParams) {
                            return $stateParams.searchTerm;
                        }]
                    }
                });
        }
    ])
    .controller('SearchResultController', ['$log', '$scope', '$state', 'searchResults', 'searchTerm',
        function($log, $scope, $state, searchResults, searchTerm) {
            var logger = $log.getInstance('app.search.controllers.SearchResultController');
            logger.info('Rendering search results page for search term: %s with search results: %j', searchTerm, searchResults);
            $scope.searchResults = searchResults;
            $scope.searchTerm = searchTerm;
        }]);