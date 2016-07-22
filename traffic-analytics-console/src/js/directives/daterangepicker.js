app.directive('daterangepicker', function() {
  return {
    restrict: 'A',
    scope: {
      options: '=daterangepicker',
      dateRange: '='
    },
    link: function(scope, element) {
      scope.options.startDate = scope.dateRange.start;
      scope.options.endDate   = scope.dateRange.end;

      element.daterangepicker(scope.options, function(start, end) {
        scope.dateRange.start = start.format(scope.options.locale.format);
        scope.dateRange.end = end.format(scope.options.locale.format);
        //scope.$parent.startMoment = start;
        //scope.$parent.endMoment = end;
        scope.$apply();
      });
    }
  };
});
