ptBossApp.controller('PotentialUserController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

$scope.maskGsm="(999) 999-9999";
	
	$scope.userId=0;
	$scope.firmId;
	$scope.firms;
	$scope.userGsm;
	$scope.userGender="1";
	$scope.pStatu="0";
	$scope.pComment="";
	$scope.userEmail="";
	$scope.staffId="0";
	$scope.userName="";
	$scope.userSurname="";
	
	$scope.initPUC=function(){
		findFirms();
	}
	
	$scope.createMember =function(){
		
		var potential=new Object();
		potential.firmId=$scope.firmId;
		potential.userGsm=$scope.userGsm;
		potential.userGender=$scope.userGender;
		potential.userEmail=$scope.userEmail;
		potential.userName=$scope.userName;
		potential.userSurname=$scope.userSurname;
		
		parameterService.param1="POT";
		parameterService.param2=potential;
		$location.path("/member/create")
	}
	
	$scope.createPotentialMember =function(){
		   
		 var mailContent=$scope.pComment+"\n"+$translate.instant("potentialMailContent")+"  \n "+$scope.userName+" "+$scope.userSurname+" \n "+$scope.userGsm;
		 var mailSubject=$translate.instant("potentialMailSubject")
		
		  var frmDatum = {"userName":$scope.userName,
			"userSurname":$scope.userSurname,
			"firmId":$scope.firmId,
		    "userGsm":$scope.userGsm,
			"userEmail":$scope.userEmail,
			"userGender":$scope.userGender,
			"userId":$scope.userId,
			"staffId":$scope.staffId,
			"pStatu":$scope.pStatu,
			"pComment":$scope.pComment,
			"mailContent":mailContent,
			"mailSubject":mailSubject
			}; 
		  
		   $.ajax({
			  type:'POST',
			  url: "../pt/potential/create",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res.resultStatu=="1"){
					$scope.userId=parseInt(res.resultMessage);
					toastr.success($translate.instant("success"));
					$scope.$apply();
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			})
			
			
		
	};
	
	function findFirms(){
		$.ajax({
			  type:'POST',
			  url: "../pt/definition/firm/findFirms",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.firms=res;
				$scope.firmId=$scope.firms[0].firmId;
				
				findAllStaff();
				
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
	
	function findAllStaff(){
		 $.ajax({
			  type:'POST',
			  url: "../pt/ptusers/findAll/"+$scope.firmId+"/0",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.staffs=res;
				$scope.$apply();
				
				if(parameterService.param1!=""){
					var potential=parameterService.param1;
					$scope.userId=potential.userId;
					$scope.firmId=""+potential.firmId;
					$scope.userName=potential.userName;
					$scope.userSurname=potential.userSurname;
					$scope.userGsm=potential.userGsm;
					$scope.userGender=""+potential.userGender;
					$scope.pStatu=""+potential.pStatu;
					$scope.pComment=potential.pComment;
					$scope.userEmail=""+potential.userEmail;
					$scope.staffId=""+potential.staffId;
					$scope.$apply();
				}
				
			});
		}
	
});