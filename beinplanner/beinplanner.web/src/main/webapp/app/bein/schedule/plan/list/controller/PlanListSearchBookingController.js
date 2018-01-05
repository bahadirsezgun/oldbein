ptBossApp.controller('PlanListSearchBookingController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.instructorPage="";
	$scope.studiosPage="";
	$scope.firmId="";
	$scope.userType;
	$scope.user;
	
	$scope.selectedStaff;
	$scope.selectedStudious=new Array();
	$scope.planDateStr;
	
	$scope.selectedStaffId=0;
	
	$scope.showTimes=false;
	$scope.startDate;
	$scope.endDate;
	$scope.staffId;
	$scope.studios;
	$scope.timesPage="";
	
	$scope.staffs;
	
	$scope.staffId="0";

	
	
	$scope.initPLSBC = function(){
		findGlobals();
		loggedInUser();
		
	}
	
	$scope.backToMain=function(){
		$scope.timesPage="";
		$scope.showTimes=false;
	}
	
	$scope.searchPlans=function(){
		
		$scope.startDate=$scope.planDateStr;
		$scope.endDate=$scope.planDateStr;
		$scope.timesPage="./schedule/plan/scheduled/calendar.html"
		$scope.showTimes=true;
	}
	
	
	
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
  			findStaff();
  			$scope.$apply();
  			
  		}).fail  (function(jqXHR, textStatus, errorThrown){ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
		});
		
		
	}
	
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
    				/*if($scope.ptLang!=""){
    					var lang=$scope.ptLang.substring(0,2);
    					$translate.use(lang);
    				}*/
    			
    			}
    		
    			$("#planDateStr").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
    		   
    			
    		}).fail  (function(jqXHR, textStatus, errorThrown) 
    				{ 
  			  if(jqXHR.status == 404 || textStatus == 'error')	
  				  $(location).attr("href","/beinplanner/lock.html");
  			});
    	};
    	
    	function findStaff(){
    		  $.ajax({
    			  type:'POST',
    			  url: "../pt/ptusers/findAll/"+$scope.firmId+"/"+globals.USER_TYPE_SCHEDULAR_STAFF,
    			  contentType: "application/json; charset=utf-8",				    
    			  dataType: 'json', 
    			  cache:false
    			}).done(function(res) {
    						if(res!=null){
    							$scope.staffs=res;
    							$scope.$apply();
    							$('.animate-panel').animatePanel();
    						}
    					}).fail  (function(jqXHR, textStatus, errorThrown) 
    					{ 
    					  if(jqXHR.status == 404 || textStatus == 'error')	
    						  $(location).attr("href","/beinplanner/lock.html");
    					});
    		}
	
});