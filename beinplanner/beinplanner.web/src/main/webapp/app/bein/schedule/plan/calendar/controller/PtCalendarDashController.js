ptBossApp.controller('PtCalendarDashController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.staffId="";
	$scope.visibleStaff=false;
	$scope.showClassDetail=false;
	
	$scope.times=new Array("06:00","06:30","07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30"
			,"12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30"
			,"19:00","19:30","20:00","20:30","21:00","21:30","22:00","22:30","23:00","23:30");

	/***********************************
	LOGGED IN USER ATTRIBUTES
	***********************************/
	$scope.user;
	$scope.firmId;
	$scope.userType;
	
	
	$scope.scheduleTimePlans;
	
	$scope.staffs;
	
	$scope.calendarDateStr;
	$scope.calendarDateNameStr;
	
	
	$scope.calendarDate;
	$scope.day=0;
	
	
	
	$scope.planDateStr;
	$scope.timeOfPlan;
	$scope.schtId;
	$scope.schId;
	$scope.progType;
	
	$scope.changeTime=function(stp){
		$scope.showClassDetail=true;
		$scope.planDateStr=stp.planStartDateStr;
		$scope.timeOfPlan=stp.planDayTime;
		$scope.schtId=stp.schtId;
		$scope.schId=stp.schId;
		$scope.progType=stp.progType;
		
	}
	
	
	$scope.prevDate=function(){
			$scope.day=$scope.day-1;
			getTimePlans();
	}
	
	$scope.nextDate=function(){
		$scope.day=$scope.day+1;
		getTimePlans();
	}
	
	
	
	
	$scope.initPTC=function(){
		findGlobals();
		
		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/getCurrentTime",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				$scope.calendarDate=res.progStartTime;
				$scope.calendarDateName=res.progDayName;
				$scope.$apply();
				loggedInUser();
				
			});
		
	}
	$scope.ptDateFormat;
	
	
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
    			}
    			
    			$("#planDateStr").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
    	    	
    		
    		}).fail  (function(jqXHR, textStatus, errorThrown) 
    				{ 
  			  if(jqXHR.status == 404 || textStatus == 'error')	
  				  $(location).attr("href","/beinplanner/lock.html");
  			});
    };
	function getTimePlans(){
		var frmDatum={	"calendarDate":$scope.calendarDate
				  		,"staffId":$scope.staffId
		 	  	 		,"day":$scope.day
			 	    };

		$scope.scheduledPlansForSearch="";
		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/searchDashTimePlansForStaff",
			  contentType: "application/json; charset=utf-8",				    
			  data:JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.holdTime=false;
				
				
				$scope.calendarDateStr=res.calendarDate;
				$scope.calendarDateNameStr=res.calendarDateName;
				$scope.scheduleTimePlans=res.scheduleTimePlans;
				
				
				$scope.$apply();
			}).fail  (function(jqXHR, textStatus, errorThrown){ 
				if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
				
  			});
	}
	var progName;
	var schId;
	var schtId;
	var schtStaffId;
	var time;
	var currentTime;
	var instructorName;
	
	$scope.holdTime=false;
	
	
	
	$scope.confirmIt=function(){
		
		if($scope.staffId!="0" || $scope.timeOfPlan==""){
		
		var textStr=$translate.instant("changeTimePlan")+" \n "
					+$translate.instant("changedTime")	+$scope.planDateStr+" "+$scope.timeOfPlan+" \n "
					+instructorName;
		
		swal({
            title: $translate.instant("warning"),
            text: textStr,
            html: true,
            type: "info",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesContinue"),
            cancelButtonText: $translate.instant("no"),
            closeOnConfirm: true,
            closeOnCancel: true,
            },
        function (isConfirm) {
            if (isConfirm) {
            	
            	$(".splash").css('display','');
            	
            	var frmDatum={"schId":$scope.schId
   			 	  	 ,"schtId":$scope.schtId
   			 	  	,'schtStaffId':$scope.staffId
   			 	  	,'planStartDateStr':$scope.planDateStr
   			 	  	,'planDayTime':$scope.timeOfPlan
   			 	  };
            	
            	$.ajax({
      			  type:'POST',
      			  url: "../pt/schedule/changeSchedule",
      			  contentType: "application/json; charset=utf-8",				    
      			  data:JSON.stringify(frmDatum),
      			  dataType: 'json', 
      			  cache:false
      			}).done(function(res) {
      				var result=res.hmiResultObjs[0];
      				if(result!=null){
	      				if(result.resultStatu=="2"){
	      					toastr.error($translate.instant(result.resultMessage));
	      				}else{
	      					$scope.showClassDetail=false;
	      					toastr.success($translate.instant("success"));
	      					getTimePlans();
	      				}
	      				
	      				if(res.hmiResultObjs.length==0){
	      					toastr.error($translate.instant(result.resultMessage));
	      				}
      				}
      				
      				
      				
      				$(".splash").css('display','none');
      			}).fail  (function(jqXHR, textStatus, errorThrown){ 
    				if(jqXHR.status == 404 || textStatus == 'error')	
  					  $(location).attr("href","/beinplanner/lock.html");
  				
    				$('.splash').css('display', 'none');
      			});
            
            }
         });
		}else{
			toaster.error($translate.instant("chooseInstructorOrTime"));
		}
	}
	
	
	
	$scope.deleteIt=function(){
		
	
		
		var textStr=$translate.instant("deleteTimePlan")+" \n "
					+$translate.instant("changedTime")	+$scope.planDateStr+" "+$scope.timeOfPlan+" \n "
					+instructorName;
		
		swal({
            title: $translate.instant("warning"),
            text: textStr,
            html: true,
            type: "info",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesContinue"),
            cancelButtonText: $translate.instant("no"),
            closeOnConfirm: true,
            closeOnCancel: true,
            },
        function (isConfirm) {
            if (isConfirm) {
            	
            	$(".splash").css('display','');
            	
            	var frmDatum={"schId":$scope.schId
   			 	  	 ,"schtId":$scope.schtId
   			 	  	,'schtStaffId':$scope.staffId
   			 	  	,'planStartDateStr':$scope.planDateStr
   			 	  	,'planDayTime':$scope.timeOfPlan
   			 	  	,'type':$scope.progType
   			 	  };
            	
            	$.ajax({
      			  type:'POST',
      			  url: "../pt/schedule/deleteTimePlan/"+$scope.progType,
      			  contentType: "application/json; charset=utf-8",				    
      			  data:JSON.stringify(frmDatum),
      			  dataType: 'json', 
      			  cache:false
      			}).done(function(res) {
      				
      				if(res!=null){
	      				if(res.resultStatu=="2"){
	      					toastr.error($translate.instant(res.resultMessage));
	      				}else{
	      					$scope.showClassDetail=false;
	      					toastr.success($translate.instant("success"));
	      					getTimePlans();
	      				}
      				}
      				
      				
      				
      				$(".splash").css('display','none');
      			}).fail  (function(jqXHR, textStatus, errorThrown){ 
    				if(jqXHR.status == 404 || textStatus == 'error')	
  					  $(location).attr("href","/beinplanner/lock.html");
  				
    				$('.splash').css('display', 'none');
      			});
            
            }
         });
		
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
  			$scope.staffId=$scope.user.userId;
  			$scope.$apply();
  			
  			if($scope.userType==globals.USER_TYPE_SCHEDULAR_STAFF){
  			  getTimePlans();
  			  $scope.visibleStaff=false;
  			}else{
  				findStaff();
  			}
  		
  		}).fail  (function(jqXHR, textStatus, errorThrown){ 
  			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
		});
	}
	
	
	$scope.changeStaff=function(){
		if($scope.staffId!=""){
			getTimePlans();
		}
		
	}
	
	function findStaff(){
		  $.ajax({
			  type:'POST',
			  url: "../pt/ptusers/findAll/"+$scope.firmId+"/"+globals.USER_TYPE_SCHEDULAR_STAFF,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
						if(res!=null){
							$scope.visibleStaff=true;
							$scope.staffs=res;
							$scope.staffId=$scope.staffs[0].userId;
							getTimePlans();
							$scope.$apply();
							
						}
						
						//$('#userListTable').footable();
						
					}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
					  if(jqXHR.status == 404 || textStatus == 'error')	
						  $(location).attr("href","/beinplanner/lock.html");
					});
	}
});