(function() {

  angular.module('app')
    .controller('OptimizationLogCtrl', [
      '$scope', 'RuleLogs',
      OptimizationLogCtrl
    ]);

  function OptimizationLogCtrl($scope, RuleLogs) {
    $scope.app.subtitle = 'Optimization Log';

    $scope.$watch('currentVendor', function(newVal, oldVal) {
      if (angular.equals(newVal, oldVal))
        return;
      if ($scope.query.page != 1) {
        $scope.query.page = 1;
      } else {
        getList();
      }
    });

    $scope.query = {
      limit: '5',
      order: '-date',
      page: 1
    };

    function success(items) {
      $scope.items = items;
    }
    function getList() {
      if (!$scope.currentSite)
        return;

      var params = angular.copy($scope.query);
      params.site = $scope.currentSite;
      params.rule = 0;
      params.vendor = $scope.currentVendor;
      $scope.promise = RuleLogs.get(params, success).$promise;
    };
    $scope.$watch('query', getList, true);
  }
})();
