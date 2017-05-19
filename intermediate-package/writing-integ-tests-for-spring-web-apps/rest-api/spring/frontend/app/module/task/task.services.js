'use strict';

angular.module('app.task.services', ['ngResource'])
    .factory('TaskService', ['$log', '$resource', function($log, $resource) {
        var api = $resource('/api/task/:id', {"id": "@id"}, {
            query:  {method: 'GET', params: {}, isArray: true}
        });

        var logger = $log.getInstance('app.task.services.TaskService');

        return {
            findAll: function() {
                logger.info('Finding all tasks.');
                return api.query();
            }
        };
    }]);