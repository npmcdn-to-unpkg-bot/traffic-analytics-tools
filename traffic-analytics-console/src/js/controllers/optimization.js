(function() {

  angular.module('app')
    .controller('OptimizationCtrl', [
      '$scope', 'columnDefinition', '$mdDialog', 'Rules',
      OptimizationCtrl
    ]);

  function OptimizationCtrl($scope, columnDefinition, $mdDialog, Rules) {
    $scope.app.subtitle = 'Optimization Rules';

    $scope.$watch('currentSite', function(newVal, oldVal) {
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
      order: 'name',
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
      $scope.promise = Rules.get(params, success).$promise;
    };
    $scope.$watch('query', getList, true);

    $scope.colDef = columnDefinition;

    ///// modal /////
    $scope.editItem = function (ev, item) {
      $mdDialog.show({
        clickOutsideToClose: false,
        controller: ['$scope', '$mdDialog', 'columnDefinition', 'Rules', editItemCtrl],
        controllerAs: 'ctrl',
        focusOnOpen: false,
        parent: angular.element(document.body),
        locals: { item: item, vendors: $scope.vendors, site: $scope.currentSite },
        bindToController: true,
        targetEvent: ev,
        templateUrl: 'tpl/rule-edit-dialog.html'
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
        controller: ['$mdDialog', '$q', 'Rules', deleteCtrl],
        controllerAs: 'ctrl',
        focusOnOpen: false,
        parent: angular.element(document.body),
        locals: { items: toDel, site: $scope.currentSite },
        bindToController: true,
        targetEvent: ev,
        templateUrl: 'tpl/delete-confirm-dialog.html'
      }).then(getList);
    };
  }

  function editItemCtrl($scope, $mdDialog, columnDefinition, Rules) {
    if (this.item) {
      this.title = "Edit Rule";
      $scope.item = angular.copy(this.item);
      $scope.item.vendor = '' + $scope.item.vendor;  // select value can't be number
      this.inputBid = ["notify", "pause"].indexOf($scope.item.action) == -1;
    } else {
      this.title = "Add Rule";
      $scope.item = { filters: [] };
      this.inputBid = false;
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
        $scope.item.site = this.site;
        Rules.save($scope.item, success);
      }
    };

    ///// dialog logic
    this.colDef = columnDefinition;

    // filters
    var allOperators = {
      number: ["gt", "lt", "eq"],
      string: ["eq", "like"]
    };
    this.openAddFilter = false;
    $scope.newFilter = {
      indicator: null,
      days: null,
      operator: null,
      value: ""
    };
    this.availableDays = [1,3,7,10];
    this.daysDisabled = false;
    this.operators = [];
    this.selectFitlerIndicator = function(col) {
      $scope.newFilter.indicator = col;
      if (this.colDef[col].type == "string") {
        this.operators = allOperators.string;
        this.daysDisabled = true;
      } else {
        this.operators = allOperators.number;
        this.daysDisabled = false;
      }
      $scope.newFilter.days = null;
      $scope.newFilter.operator = null;
      $scope.newFilter.value = "";
    };
    this.addFilter = function() {
      // todo: validate
      $scope.item.filters.push($scope.newFilter);
      $scope.newFilter = {
        indicator: null,
        days: null,
        operator: null,
        value: ""
      };
      //$scope.openAddFilter = false;  // fixme: why not works
    };
    this.deleteFilter = function(filter) {
      var pos = $scope.item.filters.indexOf(filter);
      if (pos >= 0)
        $scope.item.filters.splice(pos, 1);
    };

    this.changeAction = function() {
      if (["notify", "pause"].indexOf($scope.item.action) >= 0)
        this.inputBid = false;
      else
        this.inputBid = true;
      $scope.item.bidby = "";
    };
  }

  function deleteCtrl($mdDialog, $q, Rules) {
    this.title = "Delete Rules";
    this.content = 'Are you sure to delete this rule? The operation is not revocable!';

    this.cancel = $mdDialog.cancel;

    var site = this.site;
    function deleteItem(item, index) {
      var deferred = Rules.remove({site: site, id: item.id});

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
