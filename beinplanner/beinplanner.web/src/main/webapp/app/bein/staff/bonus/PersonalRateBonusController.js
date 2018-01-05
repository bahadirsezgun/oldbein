ptBossApp.controller('PersonalRateBonusController', function($scope,$translate,parameterService,$location,homerService,commonService) {

	$scope.bonusValue;
	$scope.bonusCount;
	$scope.bonusId=0;
	$scope.bonusType=1;
	$scope.bonusIsType=1;
	
	$scope.defBonuses;
	
	
	$scope.initPRateBonus=function(){
		$("[data-toggle=popover]").popover();
		findUserPersonelRateBonus();
	};
	
	
	function findUserPersonelRateBonus(){
			  
			   $.ajax({
				  type:'POST',
				  url: "../pt/pbonus/personal/findPersonalRateBonus/"+$scope.userId,
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$scope.defBonuses=res;
					$scope.$apply();
				});
	}
	
	
	$scope.deletePersonalRateBonus=function(bonusId){
		    $.ajax({
				  type:'POST',
				  url: "../pt/pbonus/personal/deletePersonalRateBonus/"+bonusId,
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					if(res.resultStatu=="1"){
						findUserPersonelRateBonus();
						toastr.success($translate.instant("success"));
					}else{
						toastr.error($translate.instant(res.resultMessage));
					}
				});
		
	}
	
	$scope.addPersonalRateBonus=function(bonusId){
		   var frmDatum = {"bonusId":$scope.bonusId
				   		  ,'bonusValue':$scope.bonusValue
				   		  ,'bonusCount':$scope.bonusCount
				   		  ,'bonusType':$scope.bonusType
				   		  ,'bonusIsType':$scope.bonusIsType
				   		  ,'userId':$scope.userId
				   		  }; 
		  
		   $.ajax({
			  type:'POST',
			  url: "../pt/pbonus/personal/createPersonalRateBonus",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				
				if(res.resultStatu=="1"){
					findUserPersonelRateBonus();
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
			});
	}
	
	$scope.newPersonalRateBonus=function(bonusId){
		$scope.bonusValue="";
		$scope.bonusCount="";
		$scope.bonusId=0;
	}
	
	
	$scope.updatePersonalRateBonus=function(defBonus){
		$scope.bonusValue=defBonus.bonusValue;
		$scope.bonusCount=defBonus.bonusCount;
		$scope.bonusId=defBonus.bonusId;
	}
	
	
});