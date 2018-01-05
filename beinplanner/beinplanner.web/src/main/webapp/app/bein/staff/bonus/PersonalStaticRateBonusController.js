ptBossApp.controller('PersonalStaticRateBonusController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.bonusValue;
	$scope.bonusProgId="0";
	$scope.bonusId=0;
	$scope.bonusType=1;
	$scope.bonusIsType=3;
	
	$scope.defBonuses;
	$scope.programPersonals;
	$scope.noProgram;
	
	$scope.initPStaticRateBonus=function(){
		$("[data-toggle=popover]").popover();
		findPrograms();
		findUserPersonelStaticRateBonus();
		
		
		
	};
	
	
	function findUserPersonelStaticRateBonus(){
			  
			   $.ajax({
				  type:'POST',
				  url: "../pt/pbonus/personal/findPersonalStaticRateBonus/"+$scope.userId,
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$scope.defBonuses=res;
					
					$scope.$apply();
				});
	}
	
	
	$scope.deletePersonalStaticRateBonus=function(bonusId){
		    $.ajax({
				  type:'POST',
				  url: "../pt/pbonus/personal/deletePersonalStaticRateBonus/"+bonusId,
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					if(res.resultStatu=="1"){
						findUserPersonelStaticRateBonus();
						toastr.success($translate.instant("success"));
					}else{
						toastr.error($translate.instant(res.resultMessage));
					}
				});
		
	}
	
	$scope.addPersonalStaticRateBonus=function(bonusId){
		   var frmDatum = {"bonusId":$scope.bonusId
				   		  ,'bonusValue':$scope.bonusValue
				   		  ,'bonusProgId':$scope.bonusProgId
				   		  ,'bonusType':$scope.bonusType
				   		  ,'bonusIsType':$scope.bonusIsType
				   		  ,'userId':$scope.userId
				   		  }; 
		  
		   $.ajax({
			  type:'POST',
			  url: "../pt/pbonus/personal/createPersonalStaticRateBonus",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				
				if(res.resultStatu=="1"){
					findUserPersonelStaticRateBonus();
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
			});
	}
	
	$scope.newPersonalStaticRateBonus=function(bonusId){
		$scope.bonusValue="";
		$scope.bonusProgId="0";
		$scope.bonusId=0;
		$scope.$apply();
	}
	
	
	$scope.updatePersonalStaticRateBonus=function(defBonus){
		$scope.bonusValue=defBonus.bonusValue;
		$scope.bonusProgId=""+defBonus.bonusProgId;
		$scope.bonusId=defBonus.bonusId;
		$scope.$apply();
	}
	
	
	function findPrograms(){
		$.ajax({
			  type:'POST',
			  url: "../pt/program/findAllProgramsForStaff/"+$scope.firmId+"/"+globals.PROGRAM_PERSONAL,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res.length!=0){
					$scope.programPersonals=res;
					$scope.bonusProgId="0";
					$scope.noProgram=false;
				}else{
					$scope.noProgram=true;
				}
				
				$scope.$apply();
			});
		
	}
	
});