
'use strict';

angular.module('app.task.directives', [])
    .controller('DeleteTaskController', ['$log', '$scope', '$uibModalInstance', '$state', 'TaskService', 'task', 'successCallback', 'errorCallback',
        function($log, $scope, $uibModalInstance, $state, TaskService, task, successCallback, errorCallback) {
            var logger = $log.getInstance('app.task.directives.DeleteTaskController');

            logger.info('Showing delete confirmation dialog for task: %j', task);
            $scope.task = task;

            $scope.cancel = function() {
                logger.info('User clicked cancel button. Task is not deleted.');
                $uibModalInstance.dismiss('cancel');
            };

            $scope.delete = function() {
                logger.info('User clicked delete button. Task is deleted.');
                $uibModalInstance.close();
                TaskService.delete(task, successCallback, errorCallback);
            };
        }])
    .directive('deleteTaskLink', ['$uibModal', '$state', 'NotificationService', function($uibModal, $state, NotificationService) {
        return {
            link: function (scope, element, attr) {
                scope.onSuccess = function() {
                    NotificationService.success('task.notifications.delete.success');
                    $state.go('task.list');
                };

                scope.onError = function() {
                    NotificationService.error('task.notifications.delete.error');
                };

                scope.showDeleteConfirmationDialog = function() {
                    $uibModal.open({
                        templateUrl: 'task/delete-task-modal.html',
                        controller: 'DeleteTaskController',
                        resolve: {
                            errorCallback: function() {
                                return scope.onError;
                            },
                            successCallback: function() {
                                return scope.onSuccess;
                            },
                            task: function () {
                                return scope.task;
                            }
                        }
                    });
                };
            },
            template: '<a id="delete-task-link" ng-click="showDeleteConfirmationDialog()" translate="directive.delete.task.link.label"></a>',
            scope: {
                task: '='
            }
        };
    }])
    .directive('taskForm', ['$log', '$state', 'NotificationService', 'TaskService', function($log, $state, NotificationService, TaskService) {
        var logger = $log.getInstance('app.task.directives.taskForm');

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
                    else if (scope.formType === 'update') {
                        TaskService.update(scope.task, onSuccess, onError);
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