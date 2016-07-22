(function() {
  'use strict';

  angular.module('app')
    .controller('MainCtrl', [
      '$scope', '$translate', '$cookies', '$http', '$mdDialog', '$mdMedia', '$auth', 'authService', '$rootScope', 'Notifications', '$interval',
      MainCtrl
    ]);

  function MainCtrl($scope, $translate, $cookies, $http, $mdDialog, $mdMedia, $auth, authService, $rootScope, Notifications, $interval) {
    // add ie/smart classes to html body
    $scope.isIE = !!navigator.userAgent.match(/MSIE/i);
    $scope.screenGtSmall = $mdMedia('gt-sm');
    $scope.asideFolded = !$scope.screenGtSmall;

    // config
    $scope.app = {
      name: 'SEM Tool',
      version: '0.0.1',
      subtitle: 'Dashboard'
    };

    // translate
    $scope.lang = { isopen: false };
    $scope.langs = {en:'English', de_DE:'German', it_IT:'Italian'};
    $scope.selectLang = $scope.langs[$translate.proposedLanguage()] || "English";
    $scope.setLang = function(langKey, $event) {
      // set the current lang
      $scope.selectLang = $scope.langs[langKey];
      // You can change the language during runtime
      $translate.use(langKey);
      $scope.lang.isopen = !$scope.lang.isopen;
    };

    // sites / vendors
    $scope.noVendor = false;
    $scope.sites = {};
    $scope.currentSite = null;
    $scope.vendors = {};
    $scope.currentVendor = null;
    $scope.setSite = function(siteID) {
      var site = $scope.sites[siteID];
      $scope.currentSite = siteID;
      $cookies.put('site', siteID);
      $scope.$broadcast("event:site-changed", site);

      $scope.vendors = {};
      if (site.vendors.length > 0) {
        $scope.noVendor = false;
        angular.forEach(site.vendors, function(vendor) {
          $scope.vendors[vendor.id] = vendor;
        });
        $scope.currentVendor = site.vendors[0].id;
      } else {
        $scope.noVendor = true;
        $scope.currentVendor = null;
      }
    };
    $scope.setVendor = function(vid) {
      $scope.currentVendor = vid;
    };

    var stopNotify;

    // authentication and profile
    $scope.currentUser = null;
    $scope.showLogin = false;
    $scope.$on("event:auth-loginRequired", function() {
      // this event can be emitted when stateChangeStart or $http response with 401 status
      console.log("need auth: get event:auth-loginRequired");
      $scope.showLogin = true;
    });
    $scope.$on("event:auth-loginSuccess", function() {
      // this event can be emitted after user login
      $scope.showLogin = false;

      Notifications.get(null, function(res) { $scope.notifications = res});
      // setup notification check
      if (!angular.isDefined(stopNotify)) {
        stopNotify = $interval(function() {
          if ($auth.isAuthenticated()) {
            Notifications.get(null, function(res) { $scope.notifications = res});
          } else {
            $interval.cancel(stopNotify);
            stopNotify = undefined;
          }
        }, 600000);
      }

      var payload = $auth.getPayload();
      if ($scope.currentUser && $scope.currentUser.uid == payload.uid) {
        $rootScope.currentUser = payload;
        authService.loginConfirmed(payload);
      } else {
        authService.loginConfirmed(payload, function() { return false; });
        $scope.currentUser = payload;

        // load sites/vendors
        console.log("load profile");
        $http.get('/profile').then(function(response) {
          $scope.sites = {};

          if (angular.isArray(response.data.sites) && response.data.sites.length > 0) {
            $rootScope.noSite = false;

            angular.forEach(response.data.sites, function(site) {
              $scope.sites[site.id] = site;
            });
            var cookieSite = $cookies.get('site');
            if ($scope.sites[cookieSite]) {
              $scope.setSite(cookieSite);
            } else {
              $scope.setSite(response.data.sites[0].id);
            }
          } else {
            $rootScope.noSite = true;
            $scope.currentSite = null;
            $scope.currentVendor = null;
            $scope.$state.go("app.site");
          }
        }, function(response) {
          console.log("profile get failed:", response);
          // todo: what to do then?
        });
      }
    });
    // this event can be emitted when $http response with 403 status
    $scope.$on("event:auth-forbidden", function() {
        $mdDialog.show(
            $mdDialog.alert()
            //.parent(angular.element(document.body))
            .clickOutsideToClose(true)
            .title('Insufficient permission')
            .textContent('You do not have permission to access this!')
            .ok('Got it!')
        );
    });

    $scope.logout = function() {
      $auth.logout();
      $scope.$state.go('access.signin');
    };

    if ($auth.isAuthenticated()) {
        $scope.$broadcast("event:auth-loginSuccess");
    }
  }

})();
