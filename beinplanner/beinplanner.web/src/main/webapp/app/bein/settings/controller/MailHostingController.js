ptBossApp.controller('MailHostingController', function($scope,$translate,parameterService,$location,homerService,commonService) {

	$scope.hostName;
	$scope.fromName;
	$scope.mailUsername;
	$scope.mailPassword;
	$scope.smtpPort;
	$scope.smtpAuth="0";
	$scope.useSsl="0";
	$scope.toWhom;
	
	toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	$scope.init = function(){
		$("[data-toggle=popover]").popover();
    	
		    commonService.pageName=$translate.instant("settings_dbMailTitle");
			commonService.pageComment=$translate.instant("settings_dbMailComment");
			commonService.normalHeaderVisible=false;
			commonService.setNormalHeader();
			$.ajax({
				  type:'POST',
				  url: "../pt/setting/findDbMail",
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					if(res!=null){
						$scope.hostName=res.hostName;
						$scope.fromName=res.fromName;
						$scope.mailUsername=res.mailUsername;
						$scope.mailPassword=res.mailPassword;
						$scope.smtpPort=res.smtpPort;
						$scope.smtpAuth=""+res.smtpAuth;
						$scope.useSsl=""+res.useSsl;
						$scope.$apply();
						
						
					}
				});
	}
	
	$scope.sendTestMail=function(){
		var toWho=new Array();
		toWho.push($scope.toWhom);
		
		
		var frmDatum = {'toWho':toWho,
		        'subject':$translate.instant("testSubject"),
		        'content':$translate.instant("testContent"),
		        }; 
		
		 $.ajax({
			  type:'POST',
			  url: "../pt/setting/sendTestMail",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					if(res.resultStatu=="1"){
						toastr.success($translate.instant(res.resultMessage.trim()));
					}else{
						toastr.error($translate.instant(res.resultMessage.trim()));
					}
				}
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			});

	}
	
	$scope.createDbMail =function(){
		var frmDatum = {'hostName':$scope.hostName,
				        'fromName':$scope.fromName,
				        'mailUsername':$scope.mailUsername,
				        'mailPassword':$scope.mailPassword,
				        'smtpPort':$scope.smtpPort,
				        'smtpAuth':$scope.smtpAuth,
				        'useSsl':$scope.useSsl
				        }; 
		
		   $.ajax({
			  type:'POST',
			  url: "../pt/setting/createDbMail",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					if(res.resultStatu=="1"){
						toastr.success($translate.instant(res.resultMessage.trim()));
					}else{
						toastr.error($translate.instant(res.resultMessage.trim()));
					}
				}
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	};
	
});