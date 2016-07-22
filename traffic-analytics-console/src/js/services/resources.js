angular.module('app')

.factory('Performance', ['$resource', function ($resource) {
  return $resource('/sites/:site/performance/:dimension');
}])

.factory('Bindings', ['$resource', function ($resource) {
  return $resource('/sites/:site/bindings');
}])

.factory('Vendors', ['$resource', function ($resource) {
  return $resource('/sites/:site/vendors/:id', { site: '@site', id: '@id' });
}])

.factory('Rules', ['$resource', function ($resource) {
  return $resource('/sites/:site/rules/:id', { site: '@site', id: '@id' });
}])

.factory('RuleLogs', ['$resource', function ($resource) {
  return $resource('/sites/:site/rules/:rule/logs');
}])

.factory('RuleResult', ['$resource', function ($resource) {
  return $resource('/sites/:site/rules/:rule/result');
}])

.factory('Notifications', ['$resource', function ($resource) {
  return $resource('/notifications');
}])

.factory('Sites', ['$resource', function ($resource) {
  return $resource('/sites/:id', { id: '@id' });
}])
;
