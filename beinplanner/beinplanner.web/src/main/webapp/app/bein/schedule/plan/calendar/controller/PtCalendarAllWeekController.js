ptBossApp.controller('PtCalendarAllWeekController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.staff;
	
	$scope.activePage='morning';
	
	$scope.upperSaleId=0;
	$scope.upperSchId=0;
	$scope.upperProgId;
	
	$scope.upperSchtStaffId=0;
	$scope.upperTime=0;
	$scope.upperDate=0;
	
	$scope.classDetailPageWeek="";
	$scope.staffIdWeek;
	
	$scope.day=0;
	
	$scope.holderShow=false;
	
	$scope.viewHolder=function(){
		if($scope.holderShow)
			$scope.holderShow=false;
		else
			$scope.holderShow=true;
	}
	
	$scope.prevDateWeek=function(){
		$scope.day=$scope.day-1;
		getTimePlansForWeek();
	}
	
	$scope.nextDateWeek=function(){
		$scope.day=$scope.day+1;
		getTimePlansForWeek();
	}
	
	$scope.closeWeek=function(){
		$scope.closeWeekFromUp();
	}
	
	$scope.individualRestriction=$rootScope.individualRestriction;
	$scope.groupRestriction=$rootScope.groupRestriction;
	$scope.membershipRestriction=$rootScope.membershipRestriction;
	
	$scope.activate=function(pn){
		$scope.activePage=pn;
	}
	
	
	
	$scope.initPTWC=function(){
		$scope.showClassDetailWeek=false;
		
	    $.ajax({
			  type:'POST',
			  url: "../pt/schedule/getCurrentTime",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				$scope.calendarDate=res.progStartTime;
				$scope.calendarDateName=res.progDayName;
				$scope.staffIdWeek=$scope.staffId;
				getTimePlansForWeek();
				$scope.$apply();
				
			});
		/*
	    $(window).scroll(function () {
		    var headerTop = $("#weekTB").offset().top-40;

		    
		    if ($(window).scrollTop() > headerTop) {
		        //when the header reaches the top of the window change position to fixed
		    	
		        $("#weekTH").css({"position": "fixed","top":40,"max-width":$("#weekTB").css("width"),"z-index":26500});
		    } else {
		        //put position back to relative
		        $("#weekTH").css({"position":"","z-index":0});
		    }
		});*/
	}

	$scope.closeDetailWeek=function(){
		$scope.showClassDetailWeek=false;
		$scope.classDetailPageWeek="";
		getTimePlansForWeek();
	}
	
	$scope.changeStaffWeek=function(){
		if($scope.staffIdWeek!="0"){
		  getTimePlansForWeek();
		}
	}
	
	
	$scope.personalBookingWeek=function(){
		 $scope.classDetailPageWeek="./schedule/plan/personal/booking.html";
		 $scope.goDirectWeek=true;
		 $scope.showClassDetailWeek=true;
	}
	
	$scope.classBookingWeek=function(){
		$scope.classDetailPageWeek="./schedule/plan/class/booking.html";
		$scope.goDirectWeek=true;
		$scope.showClassDetailWeek=true;
	}
	
	function getTimePlansForWeek(){
		
		var actualWidth = window.innerWidth ||
        document.documentElement.clientWidth ||
        document.body.clientWidth ||
        document.body.offsetWidth;
		
		var frmDatum={"calendarDate":$scope.calendarDate
			 	  	 ,"day":$scope.day
			 	  	 ,"dayDuration":6
			 	  	 ,"staffId":$scope.staffIdWeek
			 	  	 ,'actualWidth':parseInt(actualWidth)
				 	 };

		$scope.scheduledPlansForSearch="";
		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/searchTimePlansForAllStaffToWeek",
			  contentType: "application/json; charset=utf-8",				    
			  data:JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.holdTime=false;
				//$("#calendarForWeekHeader").html(res.calendarForWeekHeader);
				
				$("#calendarForWeekMorning").html(res.calendarForWeekMorning);
				$("#calendarForWeekNight").html(res.calendarForWeekNight);
				createEvents();
				/*
				for(var i=0;i<$scope.staffs.length;i++){
					if($scope.staffs[i].userId==$scope.staffIdWeek){
						$scope.staff=$scope.staffs[i];
					}
				}
				*/
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
	var instructorName;
	var dropStartDate;
	
	$scope.holdTime=false;
	
	
	
    function changeTimeTrigger(){
		
		if(schtStaffId!="0"){
		
		var textStr=$translate.instant("changeTimePlan")+" \n "
					+$translate.instant("changedTime")	+dropStartDate+' '+time+" \n "
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
   			 	  	,'planStartDateStr':dropStartDate
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
      				
      				getTimePlansForWeek();
      				$(".splash").css('display','none');
      			}).fail  (function(jqXHR, textStatus, errorThrown){ 
    				if(jqXHR.status == 404 || textStatus == 'error')	
  					  $(location).attr("href","/beinplanner/lock.html");
  				
    				$('.splash').css('display', 'none');
      			});
            
            }else{
            	getTimePlansForWeek();
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
	
	
	
	function createEvents(){
		
		var dayNames=$("p[name=dayName]");
		$.each(dayNames,function(i,data){
			var dayName=$(this).html();
			$(this).html($translate.instant(dayName));
		});
		 
		
		 
		 $( "div [name=tokenTimeWeek]" ).draggable({
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
		  
		 
		 $( "div [name=tokenTimeWeek]" ).unbind("click").bind("click",function(){
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
		 
		 $( "div [name=tokenTimeWeek]" ).unbind("mouseover").bind("mouseover",function(){
			 
			 var customInfo=$(this).find(".customInfo").eq(0);
			 
			 
			 var height=parseFloat($(this).height())/2;
			 
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
			 
			 
			  $("#infoDivWeek").css("z-index",1500);
		  });
		 
		 $( "div [name=tokenTimeWeek]" ).unbind("mouseout").bind("mouseout",function(){
			 $("#infoDiv").css("display","none"); 
			 
		  });
		 
		 $( "div [name=newTimeWeek]" ).unbind("click").bind("click",function(){
			 
			  $scope.upperSchtStaffId=$( this ).find( "span" ).eq(0).html();
			  $scope.upperTime=$( this ).find( "span" ).eq(1).html(); 
			  $scope.upperDate=$( this ).find( "span" ).eq(3).html(); ;
			  $scope.upperProgId=0;
			  $scope.upperSchId=0;
			  $scope.upperSaleId=0;
			  
			  $scope.goDirectWeek=false;
	    	  $scope.showClassDetailWeek=true;	
	    	  
	    	  
	    	  if($rootScope.uniquePacket==1){
		  			if($rootScope.whichPacket==1){
		  				 $scope.classDetailPageWeek="./schedule/plan/personal/booking.html";
		  				 $scope.goDirectWeek=true;
		  				 $scope.showClassDetailWeek=true;
		  			}else if($rootScope.whichPacket==2){
		  				$scope.classDetailPageWeek="./schedule/plan/class/booking.html";
		  				$scope.goDirectWeek=true;
		  				$scope.showClassDetailWeek=true;
		  			}
		  	  }
	    	  
	    	  
			  $scope.$apply();
	    	  
	    	  
		 });
		 
		 $( "div [name=freeTimeWeek]" ).droppable({
		      drop: function( event, ui ) {
		    	  schtStaffId=$( this ).find( "span" ).eq(0).html();
		    	  time=$( this ).find( "span" ).eq(1).html(); 
		    	  instructorName=$( this ).find( "span" ).eq(2).html(); 
		    	  dropStartDate=$( this ).find( "span" ).eq(3).html(); 
		    	
		      }
		 });
		 
		 $( "div [name=holdTimeWeek]" ).droppable({
		      drop: function( event, ui ) {
		    	  schtStaffId=$( this ).find( "span" ).eq(0).html();
		    	  time=$( this ).find( "span" ).eq(1).html(); 
		    	  instructorName=$( this ).find( "span" ).eq(2).html(); 
		    	  
		      }
		 });
		 
		 
		 $( "div [name=tokenHoldTimeWeek]" ).draggable({
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
		 
		 
		 $("button[name=infoBtnWeek]").unbind("click").bind("click",function(){
				
			 var div=$(this).parent().parent();
			 
			 var schId=$(div).find( "span" ).eq(0).html();
			 var schtId=$(div).find( "span" ).eq(1).html();
			 var progType=$(div).find( "span" ).eq(4).html();
			 var progId=$(div).find( "span" ).eq(6).html();
					
			
			 $scope.upperSchId=schId;
			 $scope.upperProgId=progId;
			
			 $scope.upperSchtStaffId=0;
			 $scope.upperTime=0;
			 $scope.upperDate=0;
			 
			 
			 if(progType==globals.PROGRAM_PERSONAL){
				 $scope.classDetailPageWeek="./schedule/plan/personal/booking.html";
			 }else if(progType==globals.PROGRAM_CLASS){
				 $scope.classDetailPageWeek="./schedule/plan/class/booking.html";
					  
			 }
			 $scope.goDirectWeek=true;
			 $scope.showClassDetailWeek=true;	
			 $scope.$apply();
		});
		 
		 $("button[name=trashBtnWeek]").unbind("click").bind("click",function(){
				
			 var div=$(this).parent().parent();
			 
			 var schId=$(div).find( "span" ).eq(0).html();
			 var schtId=$(div).find( "span" ).eq(1).html();
			 var progType=$(div).find( "span" ).eq(4).html();
			
			
			// $("#weekTH").css({"position":"","z-index":0});
			    
			   
			 
			    
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
		      					getTimePlansForWeek();
		      				}else{
		      					toastr.success($translate.instant(res.resultMessage));
		      				}
		      			
		      				/*
		      				 var headerTop = $("#weekTB").offset().top-40;
		     			    if ($(window).scrollTop() > headerTop) {
		     			        //when the header reaches the top of the window change position to fixed
		     			    	
		     			        $("#weekTH").css({"position": "fixed","top":40,"max-width":$("#weekTB").css("width"),"z-index":26500});
		     			    } else {
		     			    	$("#weekTH").css({"position":"","z-index":0});
		     			    }*/
		      			});
					}
		        });
			});
		 
		 $("button[name=cancelBtnWeek]").unbind("click").bind("click",function(){
				
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
		      			 // data:JSON.stringify(frmDatum),
		      			  dataType: 'json', 
		      			  cache:false
		      			}).done(function(res) {
		      				if(res.resultStatu==1){
		      					toastr.success($translate.instant("success"));
		      					getTimePlansForWeek();
		      				}else{
		      					toastr.success($translate.instant(res.resultMessage));
		      				}
		      			});
					}
		        });
			});
		 
		 $("button[name=postponeBtnWeek]").unbind("click").bind("click",function(){
				
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
		      					getTimePlansForWeek();
		      				}else{
		      					toastr.success($translate.instant(res.resultMessage));
		      				}
		      			});
					}
		        });
			});
		 
	};
	
	
	
	

});