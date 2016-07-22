(function() {

  angular.module('app')
    .controller('PerformanceCtrl', [
      '$scope', 'columnDefinition', 'Performance',
      PerformanceCtrl
    ]);

  function PerformanceCtrl($scope, columnDefinition, Performance) {
    var perfType = $scope.$state.current.name.split('.').pop();
    $scope.app.subtitle = perfType + ' performance';

    // change site also triggers change vendor
    $scope.$watch('currentVendor', getReloadFunc(true));

    $scope.dateRange = {
      start: moment().subtract(7, 'days').format('YYYY-MM-DD'),
      end: moment().subtract(1, 'days').format('YYYY-MM-DD')
    };
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
    $scope.$watch('dateRange', getReloadFunc(false), true);

    $scope.filter = {
      options: {
        debounce: 500
      }
    };

    $scope.query = {
      q: '',
      limit: '5',
      order: 'name',
      page: 1
    };

    function success(items) {
      $scope.items = items;
    }
    function getList() {
      if (!$scope.currentVendor)
        return;

      var filters = [];
      angular.forEach($scope.filters, function(filter) {
          filters.push(filter.indicator + '-' + filter.operator + '-' + filter.value);
      });

      var params = angular.copy($scope.query);
      params.site = $scope.currentSite;
      params.dimension = perfType;
      params.vendor = $scope.currentVendor;
      params.startdate = $scope.dateRange.start;
      params.enddate = $scope.dateRange.end;
      params.filter = filters.join(',');

      $scope.promise = Performance.get(params, success).$promise;
    };

    $scope.removeFilter = function () {
      $scope.query.q = '';
      
      if ($scope.filter.form.$dirty) {
        $scope.filter.form.$setPristine();
      }
    };

    $scope.$watch('query', function (newValue, oldValue) {
      getList();
    }, true);

    function getReloadFunc(resetQ) {
      return function(newVal, oldVal) {
        console.log("new: ", newVal, ",old: ", oldVal);
        if (angular.equals(newVal, oldVal))
          return;
        if ($scope.query.page != 1 || resetQ && $scope.query.q) {
          $scope.query.page = 1;
          $scope.query.q = '';
        } else {
          getList();
        }
      }
    }

    ///////////// column customize ///////////////
    $scope.colDef = columnDefinition;
    $scope.columns = [ "keyword", "clicks", "cost", "revenue" ];
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

    ///////////// filters ////////////////
    $scope.filters = [];
    var allOperators = {
        number: ["gt", "lt", "eq"],
        string: ["eq", "like"]
    };
    $scope.openAddFilter = false;
    $scope.newFilter = {
        indicator: null,
        operator: null,
        value: ""
    };
    $scope.operators = [];
    $scope.selectFitlerIndicator = function(col) {
        $scope.newFilter.indicator = col;
        $scope.operators = $scope.colDef[col].format == "string" ?
            allOperators.string : allOperators.number;
        $scope.newFilter.operator = null;
    };
    $scope.addFilter = function() {
        // todo: validate
        $scope.filters.push($scope.newFilter);
        $scope.newFilter = {
            indicator: null,
            operator: null,
            value: ""
        };
        $scope.openAddFilter = false;
    };
    $scope.deleteFilter = function(filter) {
        var pos = $scope.filters.indexOf(filter);
        if (pos >= 0)
            $scope.filters.splice(pos, 1);
    };
    $scope.$watch('filters', getReloadFunc(false), true);
  }
})();
