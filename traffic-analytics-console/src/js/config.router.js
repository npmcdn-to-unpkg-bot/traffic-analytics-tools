(function() {
  'use strict';

  angular.module('app')
    .run(['$rootScope', '$state', '$stateParams', '$auth', '$mdDialog', run])
    .config(['$stateProvider', '$urlRouterProvider', config]);

  function run($rootScope, $state, $stateParams, $auth, $mdDialog) {
    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;        

    $rootScope.noSite = false;
    function alertNoSite() {
      $mdDialog.show(
        $mdDialog.alert()
        .clickOutsideToClose(false)
        .title('No site')
        .textContent('You must create at least one site!')
        .ok('ok!')
      );
    };

    $rootScope.$on('$stateChangeStart', function (ev, next) {
      var current_name = $state.current.name;
      console.log("state change start, current:", current_name, "change to:", next.name);

      if (!next.data)
        return;

      if (next.data.needAuth && !$auth.isAuthenticated()) {
        if (!current_name || current_name == "access.signup") {
          // landing or in signup
          $state.go('access.signin');
        } else {
          if (current_name != 'access.signin') {
            $rootScope.$broadcast('event:auth-loginRequired');
          }
          $rootScope.$broadcast('$stateChangeSuccess');
        }
        ev.preventDefault();
        return;
      }

      if (next.data.needSite && $rootScope.noSite) {
        if (current_name == "app.site") {
          $rootScope.$broadcast('$stateChangeSuccess');
        } else {
          $state.go('app.site');
        }
        alertNoSite();
        ev.preventDefault();
        return;
      }
    });
  }

  function config($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/app/dashboard');

    $stateProvider
      .state('app', {
          abstract: true,
          url: '/app',
          templateUrl: "tpl/app.html"
      })
      .state('app.dashboard', {
          url: '/dashboard',
          templateUrl: 'tpl/dashboard.html',
          controller: 'DashboardCtrl',
          data: { needAuth: true, needSite: true }
      })
      .state('app.performance', {
          abstract: true,
          url: '/performance',
          template: '<div ui-view class="fade-in-up"></div>',
          data: { needAuth: true, needSite: true }
      })
      .state('app.performance.keyword', {
          url: '/keyword',
          templateUrl: 'tpl/performance.html',
          controller: 'PerformanceCtrl'
      })
      .state('app.performance.adgroup', {
          url: '/keyword',
          templateUrl: 'tpl/performance.html',
          controller: 'PerformanceCtrl'
      })
      .state('app.performance.campaign', {
          url: '/keyword',
          templateUrl: 'tpl/performance.html',
          controller: 'PerformanceCtrl'
      })
      .state('app.optimization', {
          abstract: true,
          url: '/optimization',
          template: '<div ui-view class="fade-in-up"></div>',
          data: { needAuth: true, needSite: true }
      })
      .state('app.optimization.list', {
          url: '/list',
          templateUrl: 'tpl/optimization-list.html',
          controller: 'OptimizationCtrl'
      })
      .state('app.optimization.log', {
          url: '/log',
          templateUrl: 'tpl/optimization-log.html',
          controller: 'OptimizationLogCtrl'
      })
      .state('app.optimization.result', {
          url: '/result?rule&date',
          templateUrl: 'tpl/optimization-result.html',
          controller: 'OptimizationResultCtrl'
      })
      .state('app.account', {
          url: '/account',
          templateUrl: 'tpl/account.html',
          controller: 'AccountCtrl',
          data: { needAuth: true, needSite: true }
      })
      .state('app.binding', {
          abstract: true,
          url: '/binding',
          template: '<div ui-view class="fade-in-up"></div>',
          data: { needAuth: true, needSite: true }
      })
      .state('app.binding.conversion', {
          url: '/conversion',
          templateUrl: 'tpl/binding-conversion.html',
          controller: 'BindingConversionCtrl'
      })
      .state('app.binding.adwords', {
          url: '/adwords/:id',
          templateUrl: 'tpl/binding-adwords.html',
          controller: 'BindingVendorCtrl'
      })
      .state('app.binding.bingads', {
          url: '/bingads/:id',
          templateUrl: 'tpl/binding-bingads.html',
          controller: 'BindingVendorCtrl'
      })
      .state('app.site', {
          url: '/site',
          templateUrl: 'tpl/site.html',
          controller: 'SiteCtrl',
          data: { needAuth: true }
      })
      .state('access', { 
          url: '/access',
          template: '<div ui-view class="fade-in-right-big smooth"></div>'
      })
      .state('access.signin', {
          url: '/signin',
          templateUrl: 'tpl/signin.html',
          controller: 'SigninCtrl'
      })
      .state('access.signup', {
          url: '/signup',
          templateUrl: 'tpl/signup.html',
          controller: 'SignupCtrl'
      });
  }

})();
