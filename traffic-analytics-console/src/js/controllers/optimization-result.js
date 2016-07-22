(function() {

  angular.module('app')
    .controller('OptimizationResultCtrl', [
      '$scope', 'columnDefinition', 'Rules', 'RuleResult',
      OptimizationResultCtrl
    ]);

  function OptimizationResultCtrl($scope, columnDefinition, Rules, RuleResult) {
    $scope.app.subtitle = 'Optimization Result';
    var rule = $scope.$stateParams.rule;

    $scope.$watch('currentSite', function(newVal, oldVal) {
      if (angular.equals(newVal, oldVal))
        return;
      if (oldVal) {
        $scope.$state.go("app.optimization.log");
      } else {
        $scope.rule = Rules.get({ site: newVal, id: rule });
        getList();
      }
    });

    if ($scope.currentSite)
      $scope.rule = Rules.get({ site: $scope.currentSite, id: rule });

    if ($scope.$stateParams.date) {
      $scope.dateRange = { start: $scope.$stateParams.date, end: $scope.$stateParams.date };
    } else {
      // default to yesterday
      var yesterday = moment().subtract(1, 'days').format('YYYY-MM-DD');
      $scope.dateRange = { start: yesterday, end: yesterday };
    }
    $scope.dateRangeOptions = {
      ranges: {
        Yesterday: [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
        'Last 7 Days': [moment().subtract(6, 'days'), moment()],
        'Last 30 Days': [moment().subtract(29, 'days'), moment()],
        'This Month': [moment().startOf('month'), moment().endOf('month')],
        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
      },
      //timePicker: false,
      //timePickerIncrement: 30,
      opens: 'left',
      locale: {
        format: 'YYYY-MM-DD',
        applyLabel: '确认',
        cancelLabel: '取消',
        fromLabel: '从',
        toLabel: '到',
        weekLabel: 'W',
        customRangeLabel: '选择时间',
        daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
        monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
      }
    };
    $scope.$watch('dateRange', function(newVal, oldVal) {
      if (angular.equals(newVal, oldVal))
        return;
      if ($scope.query.page != 1) {
        $scope.query.page = 1;
      } else {
        getList();
      }
    }, true);

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
      params.rule = rule;
      params.startdate = $scope.dateRange.start;
      params.enddate = $scope.dateRange.end;
      $scope.promise = RuleResult.get(params, success).$promise;
    };
    $scope.$watch('query', getList, true);

    ///////////// column customize ///////////////
    $scope.colDef = columnDefinition;
    $scope.columns = [ "date", "keyword", "clicks", "cost", "revenue", "cpc" ];
    $scope.tmpColumns = angular.copy($scope.columns);

    $scope.sortableOptions = {
        update: function(e, ui) {},
        stop: function(e, ui) {}
    };

    $scope.add_col = function(col) {
        if ($scope.tmpColumns.indexOf(col) < 0) {
            console.log("add", col);
            $scope.tmpColumns.push(col);
        }
    };
    $scope.del_col = function(col) {
        var pos = $scope.tmpColumns.indexOf(col);
        console.log("del", col, pos);
        if (pos >= 0)
            $scope.tmpColumns.splice(pos, 1);
        console.log($scope.tmpColumns);
    };

    $scope.col_sel_open = false;
    $scope.cancelColSelect = function() {
        $scope.tmpColumns = angular.copy($scope.columns);
        $scope.col_sel_open = false;
    };
    $scope.applyColSelect = function() {
        $scope.columns = angular.copy($scope.tmpColumns);
        //console.log($scope.columns);
        $scope.col_sel_open = false;
    };
  }
})();
