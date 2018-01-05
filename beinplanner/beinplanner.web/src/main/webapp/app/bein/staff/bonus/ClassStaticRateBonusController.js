ptBossApp.controller('ClassStaticRateBonusController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.bonusValue;
	$scope.bonusProgId="0";
	$scope.bonusId=0;
	$scope.bonusType=2;
	$scope.bonusIsType=3;
	
	$scope.defBonuses;
	$scope.programClasses;
	$scope.noProgram;
	
	$scope.initCStaticRateBonus=function(){
		$("[data-toggle=popover]").popover();
		findClassPrograms();
		findUserClassStaticRateBonus();
		
		
		
	};
	
	
	function findUserClassStaticRateBonus(){
			  
			   $.ajax({
				  type:'POST',
				  url: "../pt/cbonus/class/findClassStaticRateBonus/"+$scope.userId,
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$scope.defBonuses=res;
					
					$scope.$apply();
				});
	}
	
	
	$scope.deleteClassStaticRateBonus=function(bonusId){
		    $.ajax({
				  type:'POST',
				  url: "../pt/cbonus/class/deleteClassStaticRateBonus/"+bonusId,
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					if(res.resultStatu=="1"){
						findUserClassStaticRateBonus();
						toastr.success($translate.instant("success"));
					}else{
						toastr.error($translate.instant(res.resultMessage));
					}
				});
		
	}
	
	$scope.addClassStaticRateBonus=function(bonusId){
		   var frmDatum = {"bonusId":$scope.bonusId
				   		  ,'bonusValue':$scope.bonusValue
				   		  ,'bonusProgId':$scope.bonusProgId
				   		  ,'bonusType':$scope.bonusType
				   		  ,'bonusIsType':$scope.bonusIsType
				   		  ,'userId':$scope.userId
				   		  }; 
		  
		   $.ajax({
			  type:'POST',
			  url: "../pt/cbonus/class/createClassStaticRateBonus",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				
				if(res.resultStatu=="1"){
					findUserClassStaticRateBonus();
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
			});
	}
	
	$scope.newClassStaticRateBonus=function(bonusId){
		$scope.bonusValue="";
		$scope.bonusProgId="0";
		$scope.bonusId=0;
		$scope.$apply();
	}
	
	
	$scope.updateClassStaticRateBonus=function(defBonus){
		$scope.bonusValue=defBonus.bonusValue;
		$scope.bonusProgId=""+defBonus.bonusProgId;
		$scope.bonusId=defBonus.bonusId;
		$scope.$apply();
	}
	
	
	function findClassPrograms(){
		$.ajax({
			  type:'POST',
			  url: "../pt/program/findAllProgramsForStaff/"+$scope.firmId+"/"+globals.PROGRAM_CLASS,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res.length!=0){
					$scope.programClasses=res;
					$scope.bonusProgId="0";
					$scope.noProgram=false;
				}else{
					$scope.noProgram=true;
				}
				
				$scope.$apply();
			});
		
	}
	
});