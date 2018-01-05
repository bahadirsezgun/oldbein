ptBossApp.controller('BonusLockController', function($scope,$translate,homerService,commonService) {

	
	var days = ["sunday","monday","tuesday","wednesday","thursday","friday","saturday"];
	var monthNames = ["january", "february", "march", "april", "may", "june","july", "august", "september", "october", "november", "december"];
	
	var d=new Date();
	$scope.time=d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
	$scope.date=$translate.instant(days[d.getDay()])+", "+$translate.instant(monthNames[d.getMonth()+1])+" "+(d.getMonth()+1)+","+d.getFullYear();
	
	
	$scope.lockClass="";
	$scope.lockBtn="";
	
	$scope.init=function(){
		
		
		$.ajax({
			  type:'POST',
			  url: "../pt/bonusLock/finBonusLock",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res.resultStatu=="1"){
					$scope.lockClass="pe-7s-unlock text-success";
					$scope.lockBtn=$translate.instant("bonus_lock");
				}else{
					$scope.lockClass="pe-7s-lock text-danger";
					$scope.lockBtn=$translate.instant("bonus_unlock");
				}
				
				$scope.$apply();
				
			});
		
		
		
	}
	
	$scope.saveLock=function(){
		$.ajax({
			  type:'POST',
			  url: "../pt/bonusLock/lock",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res.resultStatu=="1"){
					$scope.lockClass="pe-7s-unlock text-success";
					$scope.lockBtn=$translate.instant("bonus_lock");
				}else{
					$scope.lockClass="pe-7s-lock text-danger";
					$scope.lockBtn=$translate.instant("bonus_unlock");
				}
				$scope.$apply();
				
			});
	}
	

});