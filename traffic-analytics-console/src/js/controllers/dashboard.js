(function() {

  angular.module('app')
    .controller('DashboardCtrl', [
      '$scope', 'columnDefinition', 'Performance',
      DashboardCtrl
    ]);

  function DashboardCtrl($scope, columnDefinition, Performance) {
    $scope.app.subtitle = 'Dashboard';

    $scope.$watch('currentVendor', reloadData);

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

    $scope.$watch('dateRange', reloadData, true);

    function success(items) {
      $scope.items = items;
      feedChartData(items.rows);
    }
    function getList() {
      if (!$scope.currentVendor)
        return;

      var params = {};
      params.site = $scope.currentSite;
      params.dimension = 'date';
      params.vendor = $scope.currentVendor;
      params.startdate = $scope.dateRange.start;
      params.enddate = $scope.dateRange.end;

      $scope.promise = Performance.get(params, success).$promise;
    };
    function reloadData(newVal, oldVal) {
      if (!angular.equals(newVal, oldVal))
        getList();
    }
    getList();

    $scope.chart = {
      datasetOverride: [{ yAxisID: 'y-axis-1' }, { yAxisID: 'y-axis-2' }],
      options: {
        maintainAspectRatio: false,
        responsive: true,
        scales: {
          xAxes: [{
            gridLines: {
              display: false
            }
          }],
          yAxes: [{
            id: 'y-axis-1',
            type: 'linear',
            display: true,
            position: 'left',
            gridLines: {
              display: false
            }
          },
          {
            id: 'y-axis-2',
            type: 'linear',
            display: true,
            position: 'right',
            gridLines: {
              display: false
            }
          }]
        },
        tooltips: {
          mode: 'label',
          multiKeyBackground: '#000'
        }
      }
    };

    function feedChartData(data) {
      $scope.chart.labels = [];
      $scope.chart.series = [];
      $scope.chart.data = [];
      if (data.length > 0) {
        var row = data[0];
        var cols = [];
        if (row.hasOwnProperty('clicks')) {
            $scope.chart.series.push('Clicks');
            cols.push('clicks')
        }
        if (row.hasOwnProperty('revenue')) {
            $scope.chart.series.push('Revenue');
            cols.push('revenue')
        } else if (row.hasOwnProperty('cost')) {
            $scope.chart.series.push('Cost');
            cols.push('cost')
        }
        data.forEach(function(row) {
          $scope.chart.labels.push(row.date);
          for (var i = 0; i < cols.length; i++) {
            if ($scope.chart.data[i])
              $scope.chart.data[i].push(row[cols[i]]);
            else
              $scope.chart.data[i] = [row[cols[i]]];
          }
        });
      }
    }

    $scope.colDef = columnDefinition;
    $scope.columns = [ "date", "clicks", "cost", "revenue", "cpc" ];
  }
})();
