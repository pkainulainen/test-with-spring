'use strict';

angular.module('app.task.controllers', [])
    .config(['$stateProvider',
        function ($stateProvider) {
            $stateProvider
                .state('task', {
                    url: '/',
                    abstract: true,
                    template: '<ui-view/>'
                })
                .state('task.list', {
                    authenticate: true,
                    url: '',
                    controller: 'TaskListController',
                    templateUrl: 'task/task-list-view.html'
                });
        }
    ])
    .controller('TaskListController', ['$log', '$scope', function($log) {
        var logger = $log.getInstance('app.task.controllers.TaskListController');
        logger.info('Rendering task list view');
    }]);