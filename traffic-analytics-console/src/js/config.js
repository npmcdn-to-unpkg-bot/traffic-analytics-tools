// config
var app =
angular.module('app')
  .config(['$translateProvider', function($translateProvider) {
      // Register a loader for the static files
      // So, the module will search missing translation tables under the specified urls.
      // Those urls are [prefix][langKey][suffix].
      $translateProvider.useStaticFilesLoader({
        prefix: 'assets/l10n/',
        suffix: '.js'
      });

      // Tell the module what language to use by default
      $translateProvider.preferredLanguage('en');

      // Tell the module to store the language in the local storage
      $translateProvider.useLocalStorage();

      // Enable escaping of HTML
      $translateProvider.useSanitizeValueStrategy('escape');
  }])

  .config(['ChartJsProvider', function(ChartJsProvider) {
    // Configure all charts
    ChartJsProvider.setOptions({
      chartColors: ['#FF7F0E', '#2CA02C', '#7777FF'],
      responsive: false
    });

    // Configure all line charts
    ChartJsProvider.setOptions('line', {
      showLines: true
    });
  }]);
