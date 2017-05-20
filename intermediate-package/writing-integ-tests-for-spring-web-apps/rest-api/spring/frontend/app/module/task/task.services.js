'use strict';

angular.module('app.task.services', ['ngResource'])
    .factory('TaskService', ['$log', '$resource', function($log, $resource) {
        var api = $resource('/api/task/:id', {"id": "@id"}, {
            save: {method: 'POST'},
            query:  {method: 'GET', params: {}, isArray: true}
        });

        var logger = $log.getInstance('app.task.services.TaskService');

        return {
            create: function(task, successCallback, errorCallback) {
                logger.info('Creating a new task: %j', task);
                return api.save(task,
                    function(created) {
                        logger.info('Created a new task: %j', created);
                        successCallback(created);
                    },
                    function(error) {
                        logger.error('Creating a task failed because of an error: %j', error);
                        errorCallback(error);
                    });
            },
            findAll: function() {
                logger.info('Finding all tasks.');
                return api.query();
            }
        };
    }]);