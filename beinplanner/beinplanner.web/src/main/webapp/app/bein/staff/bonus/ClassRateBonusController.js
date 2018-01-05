ptBossApp.controller('ClassRateBonusController', function($scope,$translate,parameterService,$location,homerService,commonService) {

	$scope.bonusValue;
	$scope.bonusCount;
	$scope.bonusId=0;
	$scope.bonusType=2;
	$scope.bonusIsType=1;
	
	$scope.defBonuses;
	
	
	$scope.initCRateBonus=function(){
		$("[data-toggle=popover]").popover();
		findUserPersonelRateBonus();
	};
	
	
	function findUserPersonelRateBonus(){
			  
			   $.ajax({
				  type:'POST',
				  url: "../pt/cbonus/class/findClassRateBonus/"+$scope.userId,
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$scope.defBonuses=res;
					$scope.$apply();
				});
	}
	
	
	$scope.deleteClassRateBonus=function(bonusId){
		    $.ajax({
				  type:'POST',
				  url: "../pt/cbonus/class/deleteClassRateBonus/"+bonusId,
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
	
	$scope.addClassRateBonus=function(bonusId){
		   var frmDatum = {"bonusId":$scope.bonusId
				   		  ,'bonusValue':$scope.bonusValue
				   		  ,'bonusCount':$scope.bonusCount
				   		  ,'bonusType':$scope.bonusType
				   		  ,'bonusIsType':$scope.bonusIsType
				   		  ,'userId':$scope.userId
				   		  }; 
		  
		   $.ajax({
			  type:'POST',
			  url: "../pt/cbonus/class/createClassRateBonus",
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
	
	$scope.newClassRateBonus=function(bonusId){
		$scope.bonusValue="";
		$scope.bonusCount="";
		$scope.bonusId=0;
	}
	
	
	$scope.updateClassRateBonus=function(defBonus){
		$scope.bonusValue=defBonus.bonusValue;
		$scope.bonusCount=defBonus.bonusCount;
		$scope.bonusId=defBonus.bonusId;
	}
	
	
});