'use strict';

angular.module('app.search.services', ['ngResource'])
    .factory('TaskSearchService', ['$log', '$resource', function($log, $resource) {
        var api = $resource('/api/task/search', {}, {
            'query':  {method:'GET', isArray:true}
        });

        var logger = $log.getInstance('app.search.services.TaskSearchService');

        return {
            findBySearchTerm: function(searchTerm) {
                logger.info('Searching tasks with search term: %s', searchTerm);
                return api.query({
                    searchTerm: searchTerm
                }).$promise;
            }
        };
    }]);