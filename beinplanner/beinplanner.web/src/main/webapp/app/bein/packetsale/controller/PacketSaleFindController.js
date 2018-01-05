ptBossApp.controller('PacketSaleFindController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.individualRestriction=$rootScope.individualRestriction;
	$scope.groupRestriction=$rootScope.groupRestriction;
	$scope.membershipRestriction=$rootScope.membershipRestriction;
	
	$scope.init = function(){
		$('.animate-panel').animatePanel();
		
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		
		if($rootScope.uniquePacket==1){
			if($rootScope.whichPacket==1){
				$(location).attr("href","/beinplanner/bein/#/packetsale/personal/list");
			}else if($rootScope.whichPacket==2){
				$(location).attr("href","/beinplanner/bein/#/packetsale/class/list");
			}else if($rootScope.whichPacket==3){
				$(location).attr("href","/beinplanner/bein/#/packetsale/membership/list");
			}
		}
		
		
		
	};
});