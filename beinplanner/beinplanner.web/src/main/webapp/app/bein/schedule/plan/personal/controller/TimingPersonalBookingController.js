ptBossApp.controller('TimingPersonalBookingController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals,calendarTimesService) {

/*
	$scope.times=new Array("06:00","06:30","07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30"
								,"12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30"
								,"19:00","19:30","20:00","20:30","21:00","21:30","22:00","22:30","23:00","23:30");
    
*/	
	$scope.times=null;
	
	/*
	
	$scope.planDateStr;
   
	$scope.timeOfPlan="";
	*/
	$scope.monday="0";
    $scope.tuesday="0";
    $scope.wednesday="0";
    $scope.thursday="0";
    $scope.friday="0";
    $scope.saturday="0";
    $scope.sunday="0";
    
    
    $scope.periodCount=1;
    $scope.period=false;
    
    $scope.initTCB=function(){
    	$('.i-checks').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
    	
    	if($scope.times==null){
    		
    		$.ajax({
				  type:'POST',
				  url: "../pt/schedule/getAllDayTimes",
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$rootScope.calendarTimes.allDayTimes=res;
					$scope.times=res;
					
					
					setTimeout(function(){
						$("#planDateStr").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
				    	 if($scope.upperDate!=0 && $scope.upperDate!=""){
				        	$scope.planDateStr=$scope.upperDate;
				        	$scope.timeOfPlan=$scope.upperTime;
				        }else{
				        	$scope.timeOfPlan="";
				        }
				        
				        console.log("timeOfPlan "+$scope.timeOfPlan);
				        
				        $scope.$apply();
					 },2000);
					
					
				});
    		
    		
    	}
    	
    		
    	/*
    	$('.clockpicker').clockpicker({autoclose: true, placement: 'right',align: 'top'});
    	
    	setTimeout(function(){
        $("#planDateStr").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
    	
        if($scope.upperDate!=0 && $scope.upperDate!=""){
        	$scope.planDateStr=$scope.upperDate;
        	$scope.timeOfPlan=$scope.upperTime;
        }else{
        	$scope.timeOfPlan="";
        }
        
        console.log("timeOfPlan "+$scope.timeOfPlan);
        
        $scope.$apply();
    	
    	},2000);*/
    };
    
    
    $('.i-checks').on('ifChanged', function(event) {
		if(event.target.checked){
			$scope.period=false;
		}else{
			$scope.period=true;
		}
		$scope.$apply();
    });
    
    
    $scope.setPlanStartDate=function(psd){
    	$scope.planDateStr=psd;
    }
    
    $scope.letsPlanMore=function(){
    	var periodCount=$scope.periodCount;
    	var timeType=2;
    	var planStartDate=$scope.planDateStr;
		var days=generatePersonalDetailProgram();
		if($scope.selectedMembers.length==0){
    		toastr.error($translate.instant("noMemberSelected"));
    		return;
    	}
		
		$scope.letsPlan(periodCount,timeType,planStartDate,days);
    }
    
    $scope.letsPlanOne=function(){
    	var periodCount=0;
    	var timeType=1;
    	var days=new Array();
    	var dayObj=new Object();
    	dayObj.progStartTime=$scope.timeOfPlan;
    	if( $scope.timeOfPlan=='undefined' || $scope.timeOfPlan=="0" || $scope.timeOfPlan==null || $scope.timeOfPlan==""){
    		toastr.error($translate.instant("noTimeSelected"));
    		$scope.timeOfPlan="";
    		return;
    	}
    	
    	if($scope.selectedMembers.length==0){
    		toastr.error($translate.instant("noMemberSelected"));
    		return;
    	}
    	
    	days.push(dayObj);
    	var planStartDate=$scope.planDateStr;
    	
    	if($scope.editFlag){
    		$scope.letsPlan(periodCount,timeType,planStartDate,days,$scope.timeOfPlan);
    	}else{
    		$scope.letsPlan(periodCount,timeType,planStartDate,days);
    	}
    }
    /*
    $scope.$watch('showResult', function() {
        if (!$scope.showResult) {
           if($scope.editFlag){
        	   $scope.period=false;
        	   $scope.planDateStr=$scope.scheduleTimePlanForUpdate.planStartDateStr;
           }
        }
    });
    */
    
    
    $scope.$watch('scheduleTimePlanForUpdate', function() {
    	$scope.period=false;
    });
    	   
    
    $scope.$watch('upperDate', function() {
    	
    	if($scope.upperDate!="" || $scope.upperDate!="0"){
    		$scope.planDateStr=$scope.upperDate;	
    	}
    	$scope.timeOfPlan=$scope.upperTime;
    });
    
    $scope.$watch('continueClasses', function() {
        
        setTimeout(function(){
        	$scope.planDateStr=$scope.upperDate;
        	 $scope.$apply();
        },100);
       
   });
   
    
   
   $scope.newPlan=function(){
	   $scope.newInitPlan();
   }
    
    
  function generatePersonalDetailProgram(){
	   var days=new Array();
		if($scope.monday!="0"){
			var mondayObj=new Object();
			mondayObj.progDay=1;
			mondayObj.progStartTime=$scope.monday;
			days.push(mondayObj);
		}
		if($scope.tuesday!="0"){
			var tuesdayObj=new Object();
			tuesdayObj.progDay=2;
			tuesdayObj.progStartTime=$scope.tuesday;
			
			days.push(tuesdayObj);
		}
		if($scope.wednesday!="0"){
			var wednesdayObj=new Object();
			wednesdayObj.progDay=3;
			wednesdayObj.progStartTime=$scope.wednesday;
			days.push(wednesdayObj);
		}
		if($scope.thursday!="0"){
			var thursdayObj=new Object();
			thursdayObj.progDay=4;
			thursdayObj.progStartTime=$scope.thursday;
			days.push(thursdayObj);
		}
		if($scope.friday!="0"){
			var fridayObj=new Object();
			fridayObj.progDay=5;
			fridayObj.progStartTime=$scope.friday;
			days.push(fridayObj);
		}
		if($scope.saturday!="0"){
			var saturdayObj=new Object();
			saturdayObj.progDay=6;
			saturdayObj.progStartTime=$scope.saturday;
			
			days.push(saturdayObj);
		}
		if($scope.sunday!="0"){
			var sundayObj=new Object();
			sundayObj.progDay=7;
			sundayObj.progStartTime=$scope.sunday;
			days.push(sundayObj);
		}
		
		return days;
		
	}
    
});