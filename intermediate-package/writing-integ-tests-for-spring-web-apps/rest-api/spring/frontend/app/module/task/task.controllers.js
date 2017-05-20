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
                .state('task.create', {
                    authenticate: true,
                    url: 'task/create',
                    controller: 'CreateTaskController',
                    templateUrl: 'task/create-task-view.html'
                })
                .state('task.list', {
                    authenticate: true,
                    url: '',
                    controller: 'TaskListController',
                    templateUrl: 'task/task-list-view.html',
                    resolve: {
                        tasks: ['TaskService', function(TaskService) {
                            return TaskService.findAll();
                        }]
                    }
                })
                .state('task.update', {
                    authenticate: true,
                    url: 'task/:id/update',
                    controller: 'UpdateTaskController',
                    templateUrl: 'task/update-task-view.html',
                    resolve: {
                        task: ['$stateParams', 'TaskService', function($stateParams, TaskService) {
                            return TaskService.findById($stateParams.id);
                        }]
                    }
                })
                .state('task.view', {
                    authenticate: true,
                    url: 'task/:id',
                    controller: 'ViewTaskController',
                    templateUrl: 'task/view-task-view.html',
                    resolve: {
                        task: ['$stateParams', 'TaskService', function($stateParams, TaskService) {
                            return TaskService.findById($stateParams.id);
                        }]
                    }
                });
        }
    ])
    .controller('CreateTaskController', ['$log', '$scope', function($log, $scope) {
        var logger = $log.getInstance('app.task.controllers.CreateTaskController');
        logger.info('Rendering create task view');
        $scope.task = {};
    }])
    .controller('TaskListController', ['$log', '$scope', 'tasks', function($log, $scope, tasks) {
        var logger = $log.getInstance('app.task.controllers.TaskListController');
        logger.info('Rendering task list view with %s tasks', tasks.length);
        $scope.tasks = tasks;
    }])
    .controller('UpdateTaskController', ['$log', '$scope', 'task', function($log, $scope, task) {
        var logger = $log.getInstance('app.todo.controllers.UpdateTaskController');
        logger.info('Rendering update task view for task: %j', task);
        $scope.task = task;
    }])
    .controller('ViewTaskController', ['$log', '$scope', 'task', function($log, $scope, task) {
        var logger = $log.getInstance('app.todo.controllers.ViewTaskController');
        logger.info('Rendering view task view ');
        $scope.task = task;
    }]);