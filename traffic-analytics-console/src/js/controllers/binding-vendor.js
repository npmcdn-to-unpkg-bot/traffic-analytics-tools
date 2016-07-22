(function() {

  angular.module('app')
    .controller('BindingVendorCtrl', [
      '$scope', 'Vendors',
      BindingVendorCtrl
    ]);

  function BindingVendorCtrl($scope, Vendors) {
    var bindType = $scope.$state.current.name.split('.').pop();
    $scope.app.subtitle = 'Bind ' + bindType + ' Account';

    var vendorId = $scope.$stateParams.id;

    var unwatch = $scope.$watch('currentSite', function(newVal, oldVal) {
      if (!newVal)
        return;

      if (vendorId) {
        Vendors.get({site: newVal, id: vendorId}, function(vendor) {
          $scope.account = vendor.data;
        });
      } else {
        var tmpName = bindType + moment().format(' YYYY-MM-DD');
        $scope.account = { site: newVal, type: bindType, name: tmpName };
      }
      unwatch();
      unwatch = null;
    });

    $scope.activeStep = 0;
    $scope.goStep = function(dr) {
      $scope.activeStep += dr;
      if ($scope.activeStep < 0)
        $scope.activeStep = 0;
      else if ($scope.activeStep > $scope.totalSteps-1)
        $scope.activeStep = $scope.totalSteps - 1;
    };

    $scope.isSaving = false;
    $scope.errorMsg = null;
    function success(item) {
      $scope.isSaving = false;
    }
    $scope.save = function() {
      $scope.isSaving = true;
      $scope.errorMsg = null;
      Vendors.save($scope.account, success);
    };
  }
})();
