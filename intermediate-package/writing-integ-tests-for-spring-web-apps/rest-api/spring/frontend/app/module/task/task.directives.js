
'use strict';

angular.module('app.task.directives', [])
    .directive('taskList', [function() {
        return {
            templateUrl: 'task/task-list-directive.html',
            scope: {
                tasks: '='
            }
        };
    }]);