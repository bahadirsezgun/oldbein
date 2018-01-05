ptBossApp.controller('ResultClassBookingController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	/***********************************
	GLOBALS
	***********************************/
	$scope.ptCurrency;
	$scope.ptLang;
	$scope.ptDateFormat;
	$scope.ptTz;
	
	$scope.plannedClassCount;
	$scope.programClassCount;
	$scope.willPlanClassCount;
	
	$scope.timeResult=new Array("06:00","06:30","07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30"
			,"12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30"
			,"19:00","19:30","20:00","20:30","21:00","21:30","22:00","22:30","23:00","23:30");

	
	
	$scope.firmId;
	
	$scope.attendies=new Array();
	
	/*PLAN DELETE ATTR*/
	$scope.deleteAuth=false;
	
	$scope.schedulePlan;
	$scope.scheduleTimePlans;
	$scope.scheduleTimePlan;
	
	
	$scope.initType;
	$scope.read="col-md-12";
	
	$scope.staffId="0";
	$scope.memberName="";
	$scope.memberSurname="";
	
	/****************************************
	* PAGES
	 ************************************/
	
	$scope.generalInfo=true;
	
	
	$scope.usersPage="";
	$scope.showUsers=false;
	
	$scope.showInstructor=false;
	$scope.instructorPage="";
	
	$scope.studiousPage="";
	$scope.showStudios=false;
	
	$scope.showMainResult=true;
	$scope.showDetail=false;
	
	
	$scope.deletePlanOk=false;
	
	$scope.showBookingContinue=false;
	$scope.bookingContinuePage="";
	
	$scope.programClass;
	
	/*************************************
	 * SCHEDULE PLAN CHANGE ATTRIBUTES
	 ************************************/
	$scope.selectedMembers;
	$scope.planStartDateTime;
	
	$scope.selectedProgramClass=new Object();
	
	/*************************************
	 * TIME PLAN QUERY ATTRIBUTES
	 ************************************/
	$scope.startDate;
	$scope.endDate;
	$scope.staffId;
	$scope.studios;
	/*************************************/
	
	$scope.search;
	
	$scope.$on("search",function(){
		$scope.search=commonService.search;
	});
	
	
	function setAttendies(){
		$scope.attendies=new Array();
	  for(var k=0;k<$scope.schedulePlan.scheduleTimePlans.length;k++){	
		 var scheduleTimePlan=$scope.schedulePlan.scheduleTimePlans[k];
		 for(var i=0;i<scheduleTimePlan.users.length;i++){
			var user=scheduleTimePlan.users[i];
			var uf=false;
			for(var j=0;j<$scope.attendies.length;j++){
				var at=$scope.attendies[j];
				if(at.userId==user.userId){
					uf=true;
					break;
				}
			}
			if(!uf){
				$scope.attendies.push(user);
			}
			
		}
	  }
		
	}
	
	$scope.hideGeneralInfo=function(){
		if($scope.generalInfo){
			$scope.generalInfo=false;
		}else{
			$scope.generalInfo=true;
		}
	}
	
	
	
	
	function controlScheduleTimePlans(){
		var malert=false;
		$.each($scope.scheduleTimePlans,function(i,tp){
			if(!malert){
				if(tp.planStatus!=1){
					
					malert=true;
					swal({
			            title: $translate.instant("plansHaveAProblems"),
			            text: $translate.instant("plansHaveAProblemsComment"),
			            type: "warning",
			            showConfirmButton: true,
			            confirmButtonText:$translate.instant("close"),
			            closeOnConfirm:true,
			            timer: 3000
			            });
				 }
			}
		});
		malert=false;
	};
	
	$scope.initResCB=function(){
		
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		
				
		$scope.schedulePlan=$scope.scheduleObj.schedulePlan;
		$scope.scheduleTimePlans=$scope.scheduleObj.schedulePlan.scheduleTimePlans;
		$scope.selectedProgramClass.progId=$scope.scheduleObj.schedulePlan.progId;
		$scope.selectedProgramClass.progType=$scope.scheduleObj.schedulePlan.progType;
		
		
		$scope.plannedClassCount=$scope.schedulePlan.plannedCount;//$scope.scheduleTimePlans.length;
		$scope.programClassCount=$scope.schedulePlan.schCount;
		$scope.willPlanClassCount=$scope.schedulePlan.willPlanCount;
		
		
		setAttendies();
		controlScheduleTimePlans();
		
		$scope.read="col-md-12";
		$('.splash').css('display', 'none');
		loggedInUser();
		findGlobals();
		
	};
	
	function getProgramClass(progId){
		$.ajax({
			  type:'POST',
			  url: "../pt/program/findProgramByProgId/"+progId+"/"+globals.PROGRAM_CLASS,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.programClass=res;
				$scope.selectedProgramClass=res;
				$scope.$apply();
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
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
  			var userLogged=res;
  			var userType=res.userType;
  			
  			
  			$scope.firmId=res.firmId;
  			
  			if(userType==globals.USER_TYPE_SCHEDULAR_STAFF){
  				if(userLogged.userId==$scope.schedulePlan.schStaffId){
  					$scope.deleteAuth=true;
  				}
  			}else if(userType!=globals.USER_TYPE_MEMBER && userType!=globals.USER_TYPE_SCHEDULAR_STAFF){
  				$scope.deleteAuth=true;
  			}
  			
  			$scope.$apply();
  			
  			
  			
  		}).fail  (function(jqXHR, textStatus, errorThrown){ 
  			
  			parameterService.init();
  			
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
		});
	}
	
	
    
    
	
	
	
	$scope.deleteClasss=function(){
		swal({
            title: $translate.instant("deleteAllPlan"),
            text: $translate.instant("deleteAllPlanComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesContinue"),
            cancelButtonText: $translate.instant("no"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
            	
      		$scope.scheduledPlansForSearch="";
      		$.ajax({
      			  type:'POST',
      			  url: "../pt/schedule/deletePlan/"+$scope.schedulePlan.progType,
      			  contentType: "application/json; charset=utf-8",				    
      			  data:JSON.stringify($scope.schedulePlan),
      			  dataType: 'json', 
      			  cache:false
      			}).done(function(res) {
      				if(res.resultStatu==1){
      					toastr.success($translate.instant("success"));
      					
      					$(location).attr("href", "/beinplanner/bein/#/schedule/deleteAll" );
      					
      					
      					$scope.showDetail=false;
      					$scope.usersPage="";
      					$scope.showUsers=false;
      					$scope.studiousPage="";
      					$scope.showStudios=false;
      					
      					$scope.showMainResult=false;
      					$scope.deletePlanOk=true;
      					
      				}else{
      					toastr.success($translate.instant(res.resultMessage));
      				}
      			});
			}
        });
    }
    
	
	function getTimePlan(){
		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/getSchedule/"+globals.PROGRAM_CLASS+"/"+$scope.schedulePlan.schId,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					$scope.schedulePlan=res;
					$scope.scheduleTimePlans=res.scheduleTimePlans;
					$scope.updateSchedulePlanForUpper($scope.schedulePlan);
					/*
					$scope.plannedClassCount=$scope.scheduleTimePlans.length;
					$scope.programClassCount=$scope.schedulePlan.schCount;
					*/
					
					$scope.plannedClassCount=$scope.schedulePlan.plannedCount;//$scope.scheduleTimePlans.length;
					$scope.programClassCount=$scope.schedulePlan.schCount;
					$scope.willPlanClassCount=$scope.schedulePlan.willPlanCount;
					
					setAttendies();
					controlScheduleTimePlans();
					$scope.$apply();
				}else{
					$(location).attr("href", "/beinplanner/bein/#/schedule/deleteAll" );
				}
				
			}).fail  (function(jqXHR, textStatus, errorThrown){ 
	  			
	  			parameterService.init();
	  			
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
				  else
					  $(location).attr("href", "/beinplanner/bein/#/schedule/deleteAll" );
			});
	}
	
	
	$scope.deleteTimePlan=function(scheduleTimePlan){
		
		swal({
            title: $translate.instant("warning"),
            text: $translate.instant("deleteTimePlan"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesContinue"),
            cancelButtonText: $translate.instant("no"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
            	
      		$scope.scheduledPlansForSearch="";
      		$.ajax({
      			  type:'POST',
      			  url: "../pt/schedule/deleteTimePlan/"+scheduleTimePlan.progType,
      			  contentType: "application/json; charset=utf-8",				    
      			  data:JSON.stringify(scheduleTimePlan),
      			  dataType: 'json', 
      			  cache:false
      			}).done(function(res) {
      				if(res.resultStatu==1){
      					toastr.success($translate.instant("success"));
      					getTimePlan();
      				}else{
      					toastr.success($translate.instant(res.resultMessage));
      				}
      			});
			}
        });
	}
	
	
	
	$scope.editScheduleTimePlan=function(scheduleTimePlan){
		$scope.editMainScheduleTimePlan(scheduleTimePlan);
	};
	
	
	
	
	
	$scope.continueClass=function(){
		$scope.planNewClasses();
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
    				
    			
    				$('.clockpicker').clockpicker({autoclose: true, placement: 'left',align: 'top'});
    		    	$("#planDateStr").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
    		    
    			}
    		
    		}).fail  (function(jqXHR, textStatus, errorThrown) 
    		{ 
  			  if(jqXHR.status == 404 || textStatus == 'error')	
  				  $(location).attr("href","/beinplanner/lock.html");
  			});
    };
    
    
    
    
   
    
});