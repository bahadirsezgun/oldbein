ptBossApp.controller('CreateFastUserController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.maskGsm="(999) 999-9999";
	
	$scope.userId=0;
	$scope.firmId;
	$scope.firms;
	$scope.userGsm;
	$scope.userGender="1";
	
	
	$scope.initCFUC=function(){
		findFirms();
	}
	
	
	$scope.newMemberCreate=function(){
		$scope.userId=0;
		$scope.userGsm="";
		$scope.userGender="1";
		$scope.userSurname="";
		$scope.userName="";
	}
	
	$scope.createMember =function(){
		   
		  var frmDatum = {"userName":$scope.userName,
			"userSurname":$scope.userSurname,
			"firmId":$scope.firmId,
		    "userGsm":$scope.userGsm,
			"userType":globals.USER_TYPE_MEMBER,
			"userGender":$scope.userGender,
			"userId":$scope.userId,
			"userBirthdayStr":"",
			"userEmail":"",
			"userSsn":"",
			"userAddress":"",
			"userPhone":"",
			"userComment":""
			}; 
		  
		   $.ajax({
			  type:'POST',
			  url: "../pt/ptusers/create",
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
				
				$scope.$apply();
				
				$('.i-checks').iCheck({
			        checkboxClass: 'icheckbox_square-green',
			        radioClass: 'iradio_square-green'
			    });
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
	
});