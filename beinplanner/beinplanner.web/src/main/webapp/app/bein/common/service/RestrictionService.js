ptBossApp.factory("restrictionService",function($rootScope){
	
	var restrictionService=new Object();
	
	$rootScope.individualRestriction=0;
	$rootScope.groupRestriction=0;
	$rootScope.membershipRestriction=0;
	$rootScope.uniquePacket=2;
	$rootScope.whichPacket=2;
	$rootScope.studioDefinedForBooking=1;
	restrictionService.init=function(){
		 $.ajax({
			  type:'POST',
			  url: "../pt/setting/findRestrictionForPacket",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				$rootScope.individualRestriction=res.individualRestriction;
				$rootScope.groupRestriction=res.groupRestriction;
				$rootScope.membershipRestriction=res.membershipRestriction;
				$rootScope.uniquePacket=res.uniquePacket;
				$rootScope.whichPacket=res.whichPacket;
				$rootScope.studioDefinedForBooking=res.studioDefinedForBooking;
				$rootScope.$apply();
				
			});
		
		
			
	}
	
	return restrictionService;
});