ptBossApp.controller('SpecialDatesUserFindController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	
	$scope.users;
	$scope.mailSend=false;
	$scope.user;
	$scope.sendMailPerson;
	
	$scope.init=function(){
		 $.ajax({
			  type:'POST',
			  url: "../pt/ptusers/specialDates",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
					$scope.users=res;
				    $scope.$apply();
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			})
	}
	
	
	$scope.closeMail=function(){
		$scope.mailSend=false;
	}
	
	
	$scope.openMail=function(pu){
		if(pu.gender==1){
			$scope.sendMailPerson=$translate.instant("wTitle")+" "+pu.userName+" "+pu.userSurname;
		}else{
			$scope.sendMailPerson=$translate.instant("mTitle")+" "+pu.userName+" "+pu.userSurname;
		}
		$scope.user=pu;
		$scope.mailSend=true;
	}
	
	$scope.sendMail=function(){
		
		var frmDatum={"mailSubject":$translate.instant("birthdateSubject")
				      ,"mailContent":$scope.sendMailPerson+", "+$scope.mailContent
				      ,"userEmail":$scope.user.userEmail};
		
		
		
		$.ajax({
			  type:'POST',
			  url: "../pt/mail/sendMailForSpecialDates",
			  contentType: "application/json; charset=utf-8",				    
			  data:JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.mailSend=false;
				toastr.success($translate.instant("mailSended"));
				$scope.$apply();
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  console.log(textStatus);
				  console.log(errorThrown);
			});
		
		
	}
	
	
	
	
	
});