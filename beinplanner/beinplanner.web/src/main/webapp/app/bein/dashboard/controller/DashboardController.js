ptBossApp.controller('DashboardController', function($rootScope,$scope,$translate,homerService,commonService,globals,restrictionService,calendarTimesService) {
	
	/***********************************
	GLOBALS
	***********************************/
	
	$scope.ptCurrency;
	$scope.ptLang;
	$scope.ptDateFormat;
	$scope.ptTz;
	$scope.user;
	
	$scope.dashboardPage=""
	
	$rootScope.calendarTimes=new Object();
	
	$rootScope.calendarTimes.morningTimes;
	$rootScope.calendarTimes.afternoonTimes;
	$rootScope.calendarTimes.nightTimes;
	$rootScope.calendarTimes.allDayTimes;
		
	$scope.init=function(){
		restrictionService.init();
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		$('.animate-panel').animatePanel();
		findGlobals();
		$rootScope.calendarTimes.morningTimes=calendarTimesService.getMorningTimes();
		$rootScope.calendarTimes.afternoonTimes=calendarTimesService.getAfternoonTimes();
		$rootScope.calendarTimes.nightTimes=calendarTimesService.getNightTimes();
		$rootScope.calendarTimes.allDayTimes=calendarTimesService.getNightTimes();
	}
	
	$scope.$on('$viewContentLoaded', function(event) {
		
	});
	
	function findGlobals(){
    	$.ajax({
    		  type:'POST',
    		  url: "../pt/setting/findPtGlobal",
    		  contentType: "application/json; charset=utf-8",				    
    		  dataType: 'json', 
    		  cache:false
    		}).done(function(res) {
    			if(res!=null){
    				$scope.ptTz=res.ptTz;
    				$scope.ptCurrency=res.ptCurrency;
    				$scope.ptStaticIp=res.ptStaticIp;
    				$scope.ptLang=(res.ptLang).substring(0,2);
    				$scope.ptDateFormat=res.ptScrDateFormat;
    				loggedInUser();
    				
    				
    				$translate.use($scope.ptLang);
    				$translate.refresh;
    				commonService.changeLang($scope.ptLang);
    				$scope.$apply();
    				
    			}else{
    				$scope.ptLang = navigator.language || navigator.userLanguage; 
    				$scope.ptLang = ($scope.ptLang).substring(0,2);
    				if(ptLang=="tr"){
    					$translate.use($scope.ptLang);
    				}else{
    					$translate.use("en");
    				}
    				$translate.refresh;
    				commonService.changeLang($scope.ptLang);
    				$scope.$apply();
    			}
    		
    		}).fail  (function(jqXHR, textStatus, errorThrown) 
    				{ 
  			  if(jqXHR.status == 404 || textStatus == 'error')	
  				  $(location).attr("href","/beinplanner/lock.html");
  			  
  			  
	  			$scope.ptLang = navigator.language || navigator.userLanguage; 
				$scope.ptLang = ($scope.ptLang).substring(0,2);
				if(ptLang=="tr"){
					$translate.use($scope.ptLang);
				}else{
					$translate.use("en");
				}
				$translate.refresh;
				commonService.changeLang($scope.ptLang);
				$scope.$apply();
  			});
    	};
  
    function loggedInUser(){
		
		$.ajax({
  		  type:'POST',
  		  url: "../pt/ptusers/getSessionUser",
  		  contentType: "application/json; charset=utf-8",				    
  		  dataType: 'json', 
  		  cache:false
  		}).done(function(res) {
  			$scope.user=res;
  			$scope.firmId=res.firmId;
  			$scope.userType=res.userType;
  			controlUpdate();
  			$scope.dashboardPage="./dashboard/"+$scope.user.dashBoardMenu.menuLink;
  			$scope.$apply();
  		
  		}).fail  (function(jqXHR, textStatus, errorThrown){ 
  			
  			parameterService.init();
  			
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
		});
	}
	
    
    
    
    function controlUpdate(){
    	
    	if($scope.userType==globals.USER_TYPE_ADMIN){
    	var updateObj=jQuery.parseJSON(sessionStorage.getItem("updateObj"));
		var updateSqlObj=jQuery.parseJSON(sessionStorage.getItem("updateSqlObj"));
			if(updateObj!=null){
				var version=updateObj.version;
				var updateSql="";
	            if(updateSqlObj!=null){
	            	updateSql=updateSqlObj.sql;
	            }
	           
	            
	            
				
				swal({html:true,
		            title: $translate.instant(updateObj.updateTitle),
		            text: $translate.instant("version")+":"+updateObj.version+". "+$translate.instant(updateObj.updateSubTitle),
		            type: "warning",
		            showCancelButton: true,
		            confirmButtonColor: "#DD6B55",
		            confirmButtonText: $translate.instant("yes"),
		            cancelButtonText: $translate.instant("no"),
		            closeOnConfirm: false,
		            closeOnCancel: true },
		        function (isConfirm) {
		            if (isConfirm) {
		            	
		            	
		            	
		               var frmDatum = {"version":version,'updateSql':updateSql}; 
		     		   $.ajax({
		     			  type:'POST',
		     			  url: "../pt/update/makeUpdate",
		     			  contentType: "application/json; charset=utf-8",				    
		     			  data: JSON.stringify(frmDatum),
		     			  dataType: 'json', 
		     			  cache:false
		     			}).done(function(res) {
		     				
		     				if(res.resultStatu="1"){
		     					swal($translate.instant("updateWillActive"), $translate.instant("updateWillActiveMessage"), "success");
		     					sessionStorage.removeItem("updateObj");
		     					sessionStorage.removeItem("updateSqlObj");
		     				
		     				}else{
		     					swal($translate.instant("updateWillNotActive"), $translate.instant("updateWillNotActiveMessage"), "fail");
		     				}
		     				
		     			}).fail  (function(jqXHR, textStatus, errorThrown) 
		     			{ 
		     			  if(jqXHR.status == 404 || textStatus == 'error')	
		     				  $(location).attr("href","/beinplanner/lock.html");
		     			});
		            	
		                
		            } else {
		                swal($translate.instant("updateCanceled"), "");
		                sessionStorage.removeItem("updateObj");
		                sessionStorage.removeItem("updateSqlObj");
		            }
		        });
	            
	            
	            
			}
    	
    	}else{
    		sessionStorage.removeItem("updateObj");
    		sessionStorage.removeItem("updateSqlObj");
    	}
    }
    
    
});
