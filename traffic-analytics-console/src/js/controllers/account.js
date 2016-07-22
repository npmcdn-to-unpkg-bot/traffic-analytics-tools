'use strict';

app.controller('AccountCtrl', ['$scope', 'Bindings', function($scope, Bindings) {
  $scope.app.subtitle = 'Account Binding';

  // supported vendor types
  $scope.vendorTypes = ['adwords', 'bingads'];

  $scope.vendors = null;
  $scope.conversion = null;

  $scope.$watch('currentSite', function(newVal, oldVal) {
    if (newVal) {
      Bindings.get({site: newVal}, function(data) {
        $scope.vendors = data.vendors;
        $scope.conversion = data.conversion;
      });
    }
  });
}]);
