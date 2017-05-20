'use strict';

angular.module('app.task.services', ['ngResource'])
    .factory('TaskService', ['$log', '$resource', function($log, $resource) {
        var api = $resource('/api/task/:id', {"id": "@id"}, {
            get: {method: 'GET'},
            save: {method: 'POST'},
            update: {method: 'PUT'},
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
            delete: function(task, successCallback, errorCallback) {
                logger.info('Deleting task entry: %j', task);
                return api.delete(task,
                    function(deleted) {
                        logger.info('Deleted task entry: %j', deleted);
                        successCallback(deleted);
                    },
                    function(error) {
                        logger.error('Deleting the task failed because of an error: %j', error);
                        errorCallback(error);
                    }
                );
            },
            findAll: function() {
                logger.info('Finding all tasks.');
                return api.query();
            },
            findById: function(id) {
                logger.info('Finding task by id: %s', id);
                return api.get({id: id}).$promise;
            },
            update: function(task, successCallback, errorCallback) {
                logger.info('Updating task: %j', task);
                return api.update(task,
                    function(updated) {
                        logger.info('Updated the information of the task: %j', updated);
                        successCallback(updated);
                    },
                    function(error) {
                        logger.error('Updating the information of the task failed because of an error: %j', error);
                        errorCallback(error);
                    });
            }
        };
    }]);