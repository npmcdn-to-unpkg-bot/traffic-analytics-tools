(function() {

  angular.module('app')
    .controller('SiteCtrl', [
      '$scope', '$mdDialog', 'Sites',
      SiteCtrl
    ]);

  function SiteCtrl($scope, $mdDialog, Sites) {
    $scope.app.subtitle = 'Site Management';
    function success(items) {
      $scope.items = items;
    }
    function getList() {
      $scope.promise = Sites.get(null, success).$promise;
    };
    getList();

    ///// modal /////
    $scope.editItem = function (ev, item) {
      $mdDialog.show({
        clickOutsideToClose: false,
        controller: ['$scope', '$mdDialog', 'Sites', editItemCtrl],
        controllerAs: 'ctrl',
        focusOnOpen: false,
        parent: angular.element(document.body),
        locals: { item: item },
        bindToController: true,
        targetEvent: ev,
        templateUrl: 'tpl/site-edit-dialog.html'
      }).then(getList);
    };

    $scope.deleteItem = function (ev, item) {
      var toDel;
      if (item) {
        toDel = [item];
      } else if ($scope.selected.length > 0) {
        toDel = $scope.selected;
      } else {
        return;
      }
      $mdDialog.show({
        clickOutsideToClose: true,
        controller: ['$mdDialog', '$q', 'Sites', deleteCtrl],
        controllerAs: 'ctrl',
        focusOnOpen: false,
        parent: angular.element(document.body),
        locals: { items: toDel },
        bindToController: true,
        targetEvent: ev,
        templateUrl: 'tpl/delete-confirm-dialog.html'
      }).then(getList);
    };
  }

  function editItemCtrl($scope, $mdDialog, Sites) {
    if (this.item) {
      this.title = "Edit Site";
      $scope.item = angular.copy(this.item);
    } else {
      this.title = "Add Site";
      $scope.item = {};
    }
    this.cancel = $mdDialog.cancel;

    this.onSave = false;
    function success(item) {
      this.onSave = false;
      $mdDialog.hide(item);
    }

    this.save = function () {
      $scope.editForm.$setSubmitted();

      if ($scope.editForm.$valid) {
        this.onSave = true;
        Sites.save($scope.item, success);
      }
    };
  }

  function deleteCtrl($mdDialog, $q, Sites) {
    this.title = "Delete Sites";
    this.content = 'Are you sure to delete this site? The operation is not revocable!';

    this.cancel = $mdDialog.cancel;

    function deleteItem(item, index) {
      var deferred = Sites.remove({id: item.id});

      // update deleted in case we can retry on failure
      //deferred.$promise.then(function () {
      //  this.items.splice(index, 1);
      //});
      
      return deferred.$promise;
    }

    this.onprocess = false;
    this.ok = function() {
      this.onprocess = true;
      $q.all(this.items.map(deleteItem)).then(success, error);
    };

    function success() {
      console.log("success delete");
      this.onprocess = false;
      $mdDialog.hide();
    }

    function error() {
      this.error = 'Error occured when delete.';
      this.onprocess = false;
    }
  }
})();
