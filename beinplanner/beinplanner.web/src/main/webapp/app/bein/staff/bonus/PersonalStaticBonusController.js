ptBossApp.controller('PersonalStaticBonusController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.bonusValue;
	$scope.bonusProgId="0";
	$scope.bonusId=0;
	$scope.bonusType=1;
	$scope.bonusIsType=2;
	
	$scope.defBonuses;
	$scope.programPersonals;
	$scope.noProgram;
	
	$scope.initPStaticBonus=function(){
		$("[data-toggle=popover]").popover();
		findPrograms();
		findUserPersonelStaticBonus();
		
		
		
	};
	
	
	function findUserPersonelStaticBonus(){
			  
			   $.ajax({
				  type:'POST',
				  url: "../pt/pbonus/personal/findPersonalStaticBonus/"+$scope.userId,
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$scope.defBonuses=res;
					
					$scope.$apply();
				});
	}
	
	
	$scope.deletePersonalStaticBonus=function(bonusId){
		    $.ajax({
				  type:'POST',
				  url: "../pt/pbonus/personal/deletePersonalStaticBonus/"+bonusId,
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					if(res.resultStatu=="1"){
						findUserPersonelStaticBonus();
						toastr.success($translate.instant("success"));
					}else{
						toastr.error($translate.instant(res.resultMessage));
					}
				});
		
	}
	
	$scope.addPersonalStaticBonus=function(bonusId){
		   var frmDatum = {"bonusId":$scope.bonusId
				   		  ,'bonusValue':$scope.bonusValue
				   		  ,'bonusProgId':$scope.bonusProgId
				   		  ,'bonusType':$scope.bonusType
				   		  ,'bonusIsType':$scope.bonusIsType
				   		  ,'userId':$scope.userId
				   		  }; 
		  
		   $.ajax({
			  type:'POST',
			  url: "../pt/pbonus/personal/createPersonalStaticBonus",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				
				if(res.resultStatu=="1"){
					findUserPersonelStaticBonus();
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
			});
	}
	
	$scope.newPersonalStaticBonus=function(bonusId){
		$scope.bonusValue="";
		$scope.bonusProgId="0";
		$scope.bonusId=0;
		$scope.$apply();
	}
	
	
	$scope.updatePersonalStaticBonus=function(defBonus){
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