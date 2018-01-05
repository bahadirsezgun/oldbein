ptBossApp.controller('NormalHeaderController', function($rootScope,$scope,$translate,commonService,homerService) {
	
	$scope.pageName;
	$scope.pageComment;
	$scope.normalHeaderVisible=false;
	
	$scope.$on("setNormalHeader",function(){
		$scope.pageName=commonService.pageName;
		$scope.pageComment=commonService.pageComment;
		$scope.normalHeaderVisible=commonService.normalHeaderVisible;
		
	});
	
});
