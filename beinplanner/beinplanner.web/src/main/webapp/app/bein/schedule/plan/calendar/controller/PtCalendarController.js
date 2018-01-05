ptBossApp.controller('PtCalendarController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals,calendarTimesService) {

	$scope.activePage='morning';
	$scope.staffId="0";
	$scope.showClassDetail=false;
	$scope.classDetailPage="";
	$scope.showClassDetailWeek=false;
	/***********************************
	LOGGED IN USER ATTRIBUTES
	***********************************/
	$scope.user;
	$scope.firmId;
	$scope.userType;
	
	$scope.upperSaleId=0;
	$scope.upperSchId=0;
	$scope.upperProgId;
	
	$scope.upperSchtStaffId=0;
	$scope.upperTime=0;
	$scope.upperDate=0;
	
	$scope.morningTimes=calendarTimesService.getMorningTimes();
	$scope.afternoonTimes=calendarTimesService.getAfternoonTimes();
	$scope.nightTimes=calendarTimesService.getNightTimes();
	
	
	$scope.individualRestriction=$rootScope.individualRestriction;
	$scope.groupRestriction=$rootScope.groupRestriction;
	$scope.membershipRestriction=$rootScope.membershipRestriction;
	
	
	$scope.staffs;
	
	$scope.holderShow=false;
	
	$scope.viewHolder=function(){
		if($scope.holderShow)
			$scope.holderShow=false;
		else
			$scope.holderShow=true;
	}
	
	$scope.activate=function(pn){
		$scope.activePage='pn';
	}
	$scope.calendarDateStr;
	$scope.calendarDateNameStr;
	
	
	$scope.calendarDate;
	$scope.day=0;
	
	$scope.calendarForMorning;
	$scope.testStr;
	
	$scope.closeWeekFromUp=function(){
		$scope.week=false;
		$scope.showClassDetail=false;
		$scope.weekPage="";
		getTimePlans();
	}
	
	$scope.prevDate=function(){
			$scope.day=$scope.day-1;
			getTimePlans();
	}
	
	$scope.nextDate=function(){
		$scope.day=$scope.day+1;
		getTimePlans();
	}
	
	
	$scope.closeDetail=function(){
		$scope.showClassDetail=false;
		$scope.week=false;
		$scope.showClassDetailWeek=false;
		$scope.classDetailPage="";
		$scope.weekPage="";
		getTimePlans();
	}
	
	$scope.initPTC=function(){
	    
		
		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/getCurrentTime",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				$scope.calendarDate=res.progStartTime;
				$scope.calendarDateName=res.progDayName;
				
				
				$scope.morningTimes=calendarTimesService.getMorningTimes();
				$scope.afternoonTimes=calendarTimesService.getAfternoonTimes();
				$scope.nightTimes=calendarTimesService.getNightTimes();
				
				console.log($scope.morningTimes);
				console.log($scope.afternoonTimes);
				console.log($scope.nightTimes);
				
				
				$scope.$apply();
				loggedInUser();
				
			});
		getTimePlans();
	}
	
	

	
	$scope.activate=function(daySection){
		if(daySection=="morning"){
			$scope.activePage='morning';
		}else if(daySection=="afternoon"){
			$scope.activePage='afternoon';
		}else if(daySection=="night"){
			$scope.activePage='night';
		}
	}
	
	
	
	
	function getTimePlans(){
		var frmDatum={"calendarDate":$scope.calendarDate
			 	  	 ,"day":$scope.day
			 	    };

		$scope.scheduledPlansForSearch="";
		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/searchTimePlansForStaff",
			  contentType: "application/json; charset=utf-8",				    
			  data:JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.holdTime=false;
				
				
				$scope.calendarDateStr=res.calendarDate;
				$scope.calendarDateNameStr=res.calendarDateName;
				
				
				$("#calendarForMorning").html(res.calendarForMorning);
				$("#calendarForAfternoon").html(res.calendarForAfternoon);
				$("#calendarForNight").html(res.calendarForNight);
				
				createEvents();
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
	
	function changeTimeTrigger(){
		
		if(schtStaffId!="0"){
		
		var textStr=$translate.instant("changeTimePlan")+" \n "
					+$translate.instant("changedTime")	+time+" \n "
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
            	
            	var frmDatum={"schId":schId
   			 	  	 ,"schtId":schtId
   			 	  	,'schtStaffId':schtStaffId
   			 	  	,'planStartDateStr':$scope.calendarDateStr
   			 	  	,'planDayTime':time
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
	      				}
      				}
      				getTimePlans();
      				$(".splash").css('display','none');
      			}).fail  (function(jqXHR, textStatus, errorThrown){ 
    				if(jqXHR.status == 404 || textStatus == 'error')	
  					  $(location).attr("href","/beinplanner/lock.html");
  				
    				$('.splash').css('display', 'none');
      			});
            
            }else{
            	getTimePlans();
            }
         });
		}else{
			$("#holdBox").find("p").eq(0).html(progName);
			$("#holdBox").find("span").eq(0).html(schId);
			$("#holdBox").find("span").eq(1).html(schtId);
			$("#holdBox").find("span").eq(2).html(schtStaffId);
			$("#holdBox").find("span").eq(3).html(time);
			$scope.holdTime=true;
			$scope.$apply();
		}
	}
	
	
	$scope.personalBooking=function(){
		 $scope.classDetailPage="./schedule/plan/personal/booking.html";
		 $scope.goDirect=true;
		 $scope.showClassDetail=true;
	}
	
	$scope.classBooking=function(){
		$scope.classDetailPage="./schedule/plan/class/booking.html";
		$scope.goDirect=true;
		$scope.showClassDetail=true;
	}
	
	
	function createEvents(){
		var weekBtns=$("span[name=week]");
		$.each(weekBtns,function(i,data){
			$(this).html($translate.instant("weekly"));
			
		});
		 
		
		
		$("#calendarForMorning").find("button").unbind("click").bind("click",function(){
			
			$scope.staffId=$(this).find("span").eq(1).html();
			$scope.week=true;
			$scope.showClassDetail=false;
			$scope.showClassDetailWeek=false;
			$scope.weekPage="./schedule/plan/calendar/ptcalendar_week.html";
			$scope.$apply();				
		});
		
		$("#calendarForAfternoon").find("button").unbind("click").bind("click",function(){
			$scope.staffId=$(this).find("span").eq(1).html();
			$scope.week=true;
			$scope.showClassDetail=false;
			$scope.showClassDetailWeek=false;
			$scope.weekPage="./schedule/plan/calendar/ptcalendar_week.html";
			$scope.$apply();			
		});
		
		$("#calendarForNight").find("button").unbind("click").bind("click",function(){
			$scope.staffId=$(this).find("span").eq(1).html();
			$scope.week=true;
			$scope.showClassDetail=false;
			$scope.showClassDetailWeek=false;
			$scope.weekPage="./schedule/plan/calendar/ptcalendar_week.html";
			$scope.$apply();				
		});
		 
		 $( "div [name=tokenTime]" ).draggable({
			 revert:  function(event,ui){
				 //alert(event.id);  
				 if(event && event[0].id!='holdTime'){
					   changeTimeTrigger();
				 }else{
					    $("#holdBox").find("p").eq(0).html(progName);
						$("#holdBox").find("span").eq(0).html(schId);
						$("#holdBox").find("span").eq(1).html(schtId);
						$("#holdBox").find("span").eq(2).html(schtStaffId);
						$("#holdBox").find("span").eq(3).html(time);
						$scope.holdTime=true;
						$scope.$apply();
					 return true;
				 }
				 
				 
				 
				   return !event;
		        } ,
			 drag:function(event,ui){
				 schId=$( this ).find( "span" ).eq(0).html();
				 schtId=$( this ).find( "span" ).eq(1).html();
				 progName=$( this ).find( "p" ).eq(0).html();
			 }
		 });
		 
		 
		 $( "div [name=tokenTime]" ).unbind("click").bind("click",function(){
			 var customButtons=$(this).find(".customButtons").eq(0);
				
			 var dipAttr=$(customButtons).css("display"); 
			 if(dipAttr=="block"){
				 $(customButtons).css("display","none"); 
			 }else{
			 var width=$(this).width();
			 $(customButtons).css("left","0px");
			 $(customButtons).css("min-width",width);	
			 $(customButtons).css("display",""); 
			 }
			 
			 
			
		 });
		 
		 $( "div [name=tokenTime]" ).unbind("mouseover").bind("mouseover",function(){
			 
			 var customInfo=$(this).find(".customInfo").eq(0);
			 
			 
			 var height=parseFloat($(this).height());
			 
			 var top=parseFloat($(this).offset().top)-parseFloat(height);
			 var width=$(this).width();
			 var dw=$( document ).width()/2;
			 
			 
			 var left=parseFloat($(this).offset().left)+parseFloat(width);
			 
			 if(left>dw){
				 left=parseFloat($(this).offset().left)-parseFloat($("#infoDiv").width())-10; 
			 }
			 
			 
			 $("#infoDiv").css("position","absolute");
			 $("#infoDiv").html($(customInfo).html());
			 $("#infoDiv").css("top",top);
			 $("#infoDiv").css("left",left);
			 $("#infoDiv").css("text-align","center");
			 
			  $("#infoDiv").css("display","");
			 
			 
			  $("#infoDiv").css("z-index",1500);
		  });
		 
		 $( "div [name=tokenTime]" ).unbind("mouseout").bind("mouseout",function(){
			 $("#infoDiv").css("display","none"); 
			 
		  });
		 
		 $( "div [name=newTime]" ).unbind("click").bind("click",function(){
		 
			  $scope.upperSchtStaffId=$( this ).find( "span" ).eq(0).html();
			  $scope.upperTime=$( this ).find( "span" ).eq(1).html(); 
			  $scope.upperDate=$scope.calendarDateStr;
			  $scope.upperProgId=0;
			  $scope.upperSchId=0;
			  $scope.upperSaleId=0;
			  
			  $scope.goDirect=false;
	    	  $scope.showClassDetail=true;	
	    	  
	    	  
	    	  
	    	  
	    	  if($rootScope.uniquePacket==1){
	  			if($rootScope.whichPacket==1){
	  				  $scope.classDetailPage="./schedule/plan/personal/booking.html";
	  				  $scope.goDirect=true;
	  				  $scope.showClassDetail=true;
	  			}else if($rootScope.whichPacket==2){
	  				$scope.classDetailPage="./schedule/plan/class/booking.html";
	  				$scope.goDirect=true;
	  				$scope.showClassDetail=true;
	  			}
	  		   }
	    	  
	    	  $scope.$apply();
			 
	    	  
	    	  
		 });
		   
		 $( "div [name=freeTime]" ).droppable({
		      drop: function( event, ui ) {
		    	  schtStaffId=$( this ).find( "span" ).eq(0).html();
		    	  time=$( this ).find( "span" ).eq(1).html(); 
		    	  instructorName=$( this ).find( "span" ).eq(2).html(); 
		      }
		 });
		 
		 $( "div [name=holdTime]" ).droppable({
		      drop: function( event, ui ) {
		    	  schtStaffId=$( this ).find( "span" ).eq(0).html();
		    	  time=$( this ).find( "span" ).eq(1).html(); 
		    	  instructorName=$( this ).find( "span" ).eq(2).html(); 
		      }
		 });
		 
		 
		 $( "div [name=tokenHoldTime]" ).draggable({
			 revert:  function(event,ui){
				changeTimeTrigger();
				 return true;
				} ,
			 drag:function(event,ui){
				 schId=$( this ).find( "span" ).eq(0).html();
				 schtId=$( this ).find( "span" ).eq(1).html();
				 progName=$( this ).find( "p" ).eq(0).html();
			 }
		 });
		 
		 $("button[name=infoBtn]").unbind("click").bind("click",function(){
				
			 var div=$(this).parent().parent();
			 
			 var schId=$(div).find( "span" ).eq(0).html();
			 var schtId=$(div).find( "span" ).eq(1).html();
			 var progType=$(div).find( "span" ).eq(4).html();
			 var progId=$(div).find( "span" ).eq(5).html();
				
			 parameterService.param1="OLD";
			 parameterService.param2=schId;
			 parameterService.param3=schtId;
			
			 
			 $scope.upperSchId=schId;
			 $scope.upperProgId=progId;
			 $scope.upperSchtStaffId=0;
			 $scope.upperTime=0;
			 $scope.upperDate=0;
			 
			 
			 if(progType==globals.PROGRAM_PERSONAL){
				 $scope.classDetailPage="./schedule/plan/personal/booking.html";
			 }else if(progType==globals.PROGRAM_CLASS){
				 $scope.classDetailPage="./schedule/plan/class/booking.html";
					  
			 }
			 $scope.goDirect=true;
			 $scope.showClassDetail=true;	
			 $scope.$apply();
		});
		 
		 
		 
		 $("button[name=trashBtn]").unbind("click").bind("click",function(){
				
			 var div=$(this).parent().parent();
			 
			 var schId=$(div).find( "span" ).eq(0).html();
			 var schtId=$(div).find( "span" ).eq(1).html();
			 var progType=$(div).find( "span" ).eq(4).html();
				
			 var frmDatum={"schtId":schtId,
					       "schId":schId};
			 
			 
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
		      			  url: "../pt/schedule/deleteTimePlan/"+progType,
		      			  contentType: "application/json; charset=utf-8",				    
		      			  data:JSON.stringify(frmDatum),
		      			  dataType: 'json', 
		      			  cache:false
		      			}).done(function(res) {
		      				if(res.resultStatu==1){
		      					toastr.success($translate.instant("success"));
		      					getTimePlans();
		      				}else{
		      					toastr.success($translate.instant(res.resultMessage));
		      				}
		      			});
					}
		        });
			});
		 
		 
		 $("button[name=cancelBtn]").unbind("click").bind("click",function(){
				
			 var div=$(this).parent().parent();
			 
			 var schtId=$(div).find( "span" ).eq(1).html();
			
			 var title="cancelTimePlan";
			 if($(this).find("em").eq(0).html().trim()=="1"){
				 title="cancelCancelTimePlan";
			 }
			 
				swal({
		            title: $translate.instant("warning"),
		            text: $translate.instant(title),
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
		      			  url: "../pt/schedule/cancelTimePlan/"+schtId,
		      			  contentType: "application/json; charset=utf-8",				    
		      			  //data:JSON.stringify(frmDatum),
		      			  dataType: 'json', 
		      			  cache:false
		      			}).done(function(res) {
		      				if(res.resultStatu==1){
		      					toastr.success($translate.instant("success"));
		      					getTimePlans();
		      				}else{
		      					toastr.success($translate.instant(res.resultMessage));
		      				}
		      			});
					}
		        });
			});
		 
		 $("button[name=postponeBtn]").unbind("click").bind("click",function(){
				
			 var div=$(this).parent().parent();
			 
			 var schtId=$(div).find( "span" ).eq(1).html();
			 var progType=$(div).find( "span" ).eq(4).html();
			 
			 var title="postponeTimePlan";
			 if($(this).find("em").eq(0).html().trim()=="2"){
				 title="cancelPostponeTimePlan";
			 }
			 
				swal({
		            title: $translate.instant("warning"),
		            text: $translate.instant(title),
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
		      			  url: "../pt/schedule/postPoneTimePlan/"+schtId+"/"+progType,
		      			  contentType: "application/json; charset=utf-8",				    
		      			  //data:JSON.stringify(frmDatum),
		      			  dataType: 'json', 
		      			  cache:false
		      			}).done(function(res) {
		      				if(res.resultStatu==1){
		      					toastr.success($translate.instant("success"));
		      					getTimePlans();
		      				}else{
		      					toastr.success($translate.instant(res.resultMessage));
		      				}
		      			});
					}
		        });
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
  			$scope.$apply();
  			findStaff();
  			getTimePlans();
  		
  		}).fail  (function(jqXHR, textStatus, errorThrown){ 
  			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
		});
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
							$scope.staffs=res;
							$scope.$apply();
							$('.animate-panel').animatePanel();
						}
						
						//$('#userListTable').footable();
						
					}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
					  if(jqXHR.status == 404 || textStatus == 'error')	
						  $(location).attr("href","/beinplanner/lock.html");
					});
	}
});