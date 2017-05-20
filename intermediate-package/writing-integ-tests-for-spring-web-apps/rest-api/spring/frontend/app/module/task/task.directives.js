
'use strict';

angular.module('app.task.directives', [])
    .directive('taskForm', ['$log', '$state', 'NotificationService', 'TaskService', function($log, $state, NotificationService, TaskService) {
        var logger = $log.getInstance('app.todo.directives.taskForm');

        return {
            link: function (scope, element, attr) {
                scope.saveTask = function() {
                    logger.info('Saving task: %j', scope.task);

                    var onSuccess = function(saved) {
                        NotificationService.success(scope.successMessageKey);
                        $state.go('task.view', {id: saved.id});
                    };

                    var onError = function() {
                        NotificationService.error(scope.errorMessageKey);
                    };

                    if (scope.formType === 'create') {
                        TaskService.create(scope.task, onSuccess, onError);
                    }
                    else {
                        logger.error('Unknown form type: %s', scope.formType);
                    }
                };
            },
            templateUrl: 'task/task-form-directive.html',
            scope: {
                errorMessageKey: '@',
                formType: '@',
                task: '=',
                successMessageKey: '@'
            }
        };
    }])
    .directive('taskList', [function() {
        return {
            templateUrl: 'task/task-list-directive.html',
            scope: {
                tasks: '='
            }
        };
    }]);