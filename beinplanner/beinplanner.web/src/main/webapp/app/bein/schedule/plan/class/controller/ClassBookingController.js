ptBossApp.controller('ClassBookingController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	
	/***********************************
	GLOBALS
	***********************************/
	$scope.ptCurrency;
	$scope.ptLang;
	$scope.ptDateFormat;
	$scope.ptTz;
	$scope.upperDateChange=false;
	
	$scope.studioDefinedForBooking=0;
	
	/**SELECTED****************/
	$scope.scheduleObj=new Object();
	//$scope.scheduleObj.schedulePlan;
	
	$scope.schedulePlan=new Object();
	$scope.schedulePlan.schId=0;
	
	$scope.scheduleTimePlanForUpdate; // EDIT ICIN BU OBJE SETLENIR.
	$scope.editFlag=false;
	
	$scope.selectedMembers=new Array();
	$scope.staff;
	$scope.programClass=null;
	
	$scope.tpComment="";
	
	$scope.staffs;
	$scope.programClasss;
	$scope.studious;
	
	$scope.studio;
	$scope.selectedStudious=new Array();
	
	$scope.timingPage="./schedule/plan/class/timing/index.html";
	$scope.usersPage="";
	$scope.showUser=false;
	$scope.hasOldClasses=false;
	$scope.continueClasses=false;
	
	$scope.resultOfBookingPage;
	$scope.showResult=false;
	$scope.selectionsShow=false;
	
	
	/************************************/
	/***************PLANNING*************/
	/************************************/
	
	 toastr.options = {
	            "debug": false,
	            "newestOnTop": false,
	            "positionClass": "toast-top-center",
	            "closeButton": true,
	            "toastClass": "animated fadeInDown",
	        };
	
	$scope.letsPlan=function(periodCount,timeType,planStartDate,days,planStartDateTime){
		 if($scope.staff==null){
			 toastr.error($translate.instant("noDefaultProgInstructor"));
			 return false;
		 }
		
		 $('.splash').css('display', '');
		$scope.schedulePlan.schStaffId=$scope.staff.userId;
		$scope.schedulePlan.progId=$scope.programClass.progId;
		$scope.schedulePlan.progType=globals.PROGRAM_CLASS;
		$scope.schedulePlan.schCount=$scope.programClass.progCount;
		
		var callMethod="createSchedule";
		if($scope.editFlag){
			callMethod="updateSchedule";
			
		}
		
		if(days.length==0){
			toastr.error($translate.instant('noTimeSelected'));
			 $('.splash').css('display', 'none');
			return;
		}
		if(planStartDate==""){
			toastr.error($translate.instant('noStartTimeSelected'));
			$('.splash').css('display', 'none');
			return;
		}
		$scope.resultOfBookingPage="";
		
		var frmDatum = {"timeType":timeType,
						"staffName":$scope.staff.staffName+" "+$scope.staff.staffSurname,
						"periodCount":periodCount,
						"planStartDateStr":planStartDate,
						"schedulePlan":$scope.schedulePlan,
						"scheduleStudios":$scope.selectedStudious,
						"users":$scope.selectedMembers,
						"scheduleTimeObjs":days,
						"scheduleTimePlanForUpdate":$scope.scheduleTimePlanForUpdate,
						"planStartDateTime":planStartDateTime,
						"tpComment":$scope.tpComment
						}; 
		console.log(frmDatum);
		console.log("URL "+"../pt/schedule/"+callMethod+"/"+globals.SCHEDULE_TYPE_CLASS);
		   $('.splash').css('display', '');
		   $.ajax({
			  type:'POST',
			  url: "../pt/schedule/"+callMethod+"/"+globals.SCHEDULE_TYPE_CLASS,
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				console.log(res);
				if(res!=null){
					$scope.scheduleObj=res;
					$scope.schedulePlan=res.schedulePlan;
					if($scope.editFlag){
						$scope.staff=res.scheduleTimePlanForUpdate.staff;
					}
					$scope.scheduleTimePlans=$scope.scheduleObj.schedulePlan.scheduleTimePlans;
					
					$scope.selectedMembers=new Array();
					
					$.each($scope.scheduleTimePlans,function(j,scheduleTimePlan){
						var users=scheduleTimePlan.users;
						if(j==0){
							$.each(users,function(i,data){
								$scope.selectedMembers.push(data);
							});
						}else{
						    $.each(users,function(i,data){
									var found=false;
									for(var i=0;i<$scope.selectedMembers.length;i++){
										var memberIn=$scope.selectedMembers[i];
										if(memberIn.userId==data.userId){
											found=true;
											break;
										}
									}
									if(!found){
										$scope.selectedMembers.push(data);
									}
							});
						}
				});
					
					
					$scope.hasOldClasses=true;
					$scope.continueClasses=false;
					
					if($scope.editFlag){
						$scope.resultOfBookingPage="";
						var updatedTimePlan=res.scheduleTimePlanForUpdate;
						if(updatedTimePlan.setPlanStatus==1){
							toastr.success($translate.instant(updatedTimePlan.planStatusComment));
						}else{
							toastr.error($translate.instant(updatedTimePlan.planStatusComment));
						}
						$scope.resultOfBookingPage="./schedule/plan/class/result/result.html";
						$scope.showResult=true;
						
					}else{
						$scope.resultOfBookingPage="./schedule/plan/class/result/result.html";
						$scope.showResult=true;
					}
					
					$scope.$apply();
				}
				$('.splash').css('display', 'none');
			}).fail  (function(jqXHR, textStatus, errorThrown){ 
				/*
				if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
				
				*/
				
				$('.splash').css('display', 'none');
				toastr.error($translate.instant('noClassGenerated'));
				
			});
		
	}
	
	$scope.timeOfPlan="0";
	$scope.planDateStr;
	
	
	$scope.editMainScheduleTimePlan=function(scheduleTimePlan){
		
		$scope.scheduleTimePlanForUpdate=scheduleTimePlan;
		$scope.editFlag=true;
		$scope.selectionsShow=true;
		$scope.showUser=false;
		$scope.usersPage="";
		$scope.continueClasses=false;
		$scope.period=false;
		
		$scope.timeOfPlan=scheduleTimePlan.planDayTime;
		$scope.planDateStr=scheduleTimePlan.planStartDateStr;
		
		$scope.upperTime=scheduleTimePlan.planDayTime;
		$scope.upperDate=$scope.scheduleTimePlanForUpdate.planStartDateStr;
		
		if($scope.upperSchtStaffId!=0){
		    for(var i=0;i<$scope.staffs.length;i++){
				if($scope.staffs[i].userId==$scope.upperSchtStaffId){
					$scope.staff=$scope.staffs[i];
					break;
				}
			}
		}else{
			
			for(var i=0;i<$scope.staffs.length;i++){
				if($scope.staffs[i].userId==$scope.scheduleTimePlanForUpdate.staff.userId){
					$scope.staff=$scope.staffs[i];
					break;
				}
			}
			
		}
		$scope.selectedMembers=$scope.scheduleTimePlanForUpdate.users;
		$scope.selectedStudious=$scope.scheduleTimePlanForUpdate.scheduleStudios;
		$scope.showResult=false;
	
		$scope.tpComment=$scope.scheduleTimePlanForUpdate.tpComment;
		
		$scope.resultOfBookingPage="";
	};
	
	$scope.newInitPlan=function(){
		$scope.editFlag=false;
		$scope.scheduleTimePlanForUpdate.schtId=0;
		$scope.resultOfBookingPage="";
	}
	
	$scope.planNewClasses=function(){
		$scope.resultOfBookingPage="";
		$scope.selectionsShow=true;
		$scope.showResult=false;
		$scope.editFlag=false;
		$scope.showUser=false;
		$scope.usersPage="";
		$scope.continueClasses=false;
		$scope.timeOfPlan="";
		$scope.upperTime="";
		$scope.upperDate="";
		$scope.tpComment="";
		
		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/getCurrentTime",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				$scope.upperDate=res.progStartTime;
				$scope.$apply();
			
				
			});
		
		
		$scope.preProgramed=true;
		
		
		
	}
	
	
	$scope.showOldClasses=function(){
		$scope.resultOfBookingPage="./schedule/plan/class/result/result.html";
		$scope.showResult=true;
		$scope.showUser=false;
		$scope.usersPage="";
		$scope.continueClasses=false;
		
	}
	
	$scope.updateSchedulePlanForUpper=function(schedulePlan){
		$scope.scheduleObj.schedulePlan=schedulePlan;
		$scope.schedulePlan=schedulePlan;
		$scope.scheduleTimePlans=schedulePlan.scheduleTimePlans;
		
	}
	
	$scope.closeFindMember=function(){
		$scope.showUser=false;
		$scope.usersPage="";
	}
	
	$scope.findMember=function(){
		$scope.usersPage="./schedule/plan/class/users/users.html";
		$scope.showUser=true;
		 /* 
		if($scope.programClass==null){
			toastr.error($translate.instant('noProgramSelectedToUserAdd'));
		}else{
		  $scope.usersPage="./schedule/plan/class/users/users.html";
		  $scope.showUser=true;
		}*/
	}
	
	
	$scope.addSelectedUser=function(member){
		
		if($scope.programClass==null){
			if(member.progId!=0){
				$.each($scope.programClasss,function(i,data){
				
					if(data.progId==member.progId){
						$scope.programClass=data;
					}
				});
			}else{
				toastr.error($translate.instant("chooseProgramFirst"));
				return;
			}
		}
		
		var maxMemberCount=$scope.programClass.maxMemberCount;
    	var memberCount=$scope.selectedMembers.length;
    	$scope.selectionsShow=true;
		
    	if(memberCount>maxMemberCount){
    		toastr.error($translate.instant("overQuataMemberSelected"));
    	}else{
    		var found=false;
			for(var i=0;i<$scope.selectedMembers.length;i++){
				var memberIn=$scope.selectedMembers[i];
				if(memberIn.userId==member.userId){
					found=true;
					break;
				}
			}
			if(!found){
				$scope.selectedMembers.push(member);
				if($scope.programClass==null){
					if(member.progId!=0){
						$.each($scope.programClasss,function(i,data){
							if(data.progId==member.progId){
								$scope.programClass=data;
							}
						});
					}
				}
				if($scope.editFlag){
					// KONTROL EDILECEK SAKIN KAFA ILE
					   //$scope.scheduleTimePlanForUpdate.users=$scope.selectedMembers; 
					   var days=new Array();
        			   var dayObj=new Object();
        			   dayObj.progStartTime=$scope.timeOfPlan;
        			   days.push(dayObj);
        			   $scope.letsPlan(0,1,$scope.planDateStr,days,$scope.timeOfPlan);
				}
				
				
				
				
			}else{
				toastr.error($translate.instant("memberAlreadyAdded"));
			}
		}
	}
	
	$scope.removeMember=function(member){
		var minMemberCount=$scope.programClass.minMemberCount;
    	
		if($scope.selectedMembers.length==minMemberCount){
			toastr.error($translate.instant("noMemberSelected"));
			return;
		}
		
		$scope.memberToOut=member;
		
		swal({
            title: $translate.instant("warning"),
            text: $translate.instant("removeUserComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesContinue"),
            cancelButtonText: $translate.instant("no"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
            	$scope.resultOfBookingPage="";
            	for(var i=0;i<$scope.selectedMembers.length;i++){
        			var memberIn=$scope.selectedMembers[i];
        			if(memberIn.userId==member.userId){
        				$scope.selectedMembers.splice(i, 1);
        				$scope.$apply();
        				break;
        			}
        		}
        		
        		if($scope.editFlag){
        			   
        			   $scope.scheduleTimePlanForUpdate.users=$scope.selectedMembers; 
        			   
        			   
        			   var frmDatum = {"suppId":member.sucpId,
       						"saleId":member.saleId,
       						"userId":member.userId,
       						"type":globals.SCHEDULE_TYPE_CLASS}; 
        			   
			       		   $('.splash').css('display', '');
			       		   
			       		   $.ajax({
			       			  type:'POST',
			       			  url: "../pt/schedule/removeMember/"+globals.PROGRAM_CLASS,
			       			  contentType: "application/json; charset=utf-8",				    
			       			  data: JSON.stringify(frmDatum),
			       			  dataType: 'json', 
			       			  cache:false
			       			}).done(function(res) {
			       				$('.splash').css('display', 'none');
			       				toastr.success($translate.instant(res.resultMessage));
			       				

			       				for(var i=0;i<$scope.schedulePlan.scheduleTimePlans.length;i++){
			       					
			       					if($scope.schedulePlan.scheduleTimePlans[i].schtId==$scope.scheduleTimePlanForUpdate.schtId){
			       						$scope.schedulePlan.scheduleTimePlans[i].users= $scope.scheduleTimePlanForUpdate.users;
			       						break;
			       					}
			       					
			       				}
			       				
			       				$scope.$apply();
			       			}).fail  (function(jqXHR, textStatus, errorThrown){ 
			       				$('.splash').css('display', 'none');
			  				  if(jqXHR.status == 404 || textStatus == 'error')	
			  					  $(location).attr("href","/beinplanner/lock.html");
			    			});
        			   
        			   
        			  /* 
        			   var days=new Array();
        			   var dayObj=new Object();
        			   dayObj.progStartTime=$scope.timeOfPlan;
        			   days.push(dayObj);
        			   $scope.letsPlan(0,1,$scope.planDateStr,days,$scope.timeOfPlan);
        			   */
        		
        			   
        			   
        		}
        		$scope.memberToOut=null;
            	
            }else{
            	$scope.memberToOut=null;
            }
         });
		
		
	};
	
	$scope.addSelectedStudio=function(){
    	 if($scope.studio==""){
    		 return;
    	 }
    	 
    	var found=false;
		for(var i=0;i<$scope.selectedStudious.length;i++){
			var studioIn=$scope.selectedStudious[i];
			if(studioIn.studioId==$scope.studio.studioId){
				found=true;
				break;
			}
		}
		if(!found){
			$scope.selectedStudious.push($scope.studio);
			if($scope.editFlag){
				   $scope.scheduleTimePlanForUpdate.scheduleStudios=$scope.selectedStudious; 
			}
		}
	}
	
	$scope.removeStudio=function(studio){
		for(var i=0;i<$scope.selectedStudious.length;i++){
			var studioIn=$scope.selectedStudious[i];
			if(studioIn.studioId==studio.studioId){
				$scope.selectedStudious.splice(i, 1);
				break;
			}
		}
		
		if($scope.editFlag){
			   $scope.scheduleTimePlanForUpdate.scheduleStudios=$scope.selectedStudious; 
		}
	};
    
    
   $scope.setStaff=function(staff){
	   if(staff==""){
		   $scope.staff;
	   }else{
		   $scope.staff=staff;
		   if($scope.editFlag){
			   $scope.scheduleTimePlanForUpdate.staff=staff; 
		   }
		   
	   }
	}
	
	
	
	
	/************************************/
	
	
	$scope.initCB = function(){
		$(".splash").css('display','');
		findGlobals();
		loggedInUser();
		$scope.first=true;
		$scope.studioDefinedForBooking=$rootScope.studioDefinedForBooking;
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
  						findClassPrograms();
  					}
  				}).fail  (function(jqXHR, textStatus, errorThrown) 
  				{ 
  				  if(jqXHR.status == 404 || textStatus == 'error')	
  					  $(location).attr("href","/beinplanner/lock.html");
  				});
  	}
    
    function findClassPrograms(){
    	$.ajax({
			  type:'POST',
			  url: "../pt/program/findAllProgramsForDefinition/"+$scope.firmId+"/"+globals.PROGRAM_CLASS,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
					$scope.programClasss=res;
					$scope.$apply();
					findStudios();
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
		
	}
    
    
    function findStudios(){
		$.ajax({
			  type:'POST',
			  url: "../pt/definition/studio/findAll/"+$scope.firmId,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.studious=res;
				$scope.$apply();
				findSalesOfPrograms();
			});
	};
	
	
	$scope.userSelectedBySaleFromUserFind=function(saleId,progId){
		$scope.upperSchId=0;
		$scope.upperProgId=progId;
		$scope.continueClasses=true;
		$scope.upperSaleId=saleId;
		
		$scope.timeOfPlan=$scope.upperTime;
		$scope.planDateStr=$scope.upperDate;
		
		findSalesOfPrograms();
	}
	
	$scope.userSelectedBySchIdFromUserFind=function(schId,progId){
		$scope.upperSchId=schId;
		$scope.upperProgId=progId;
		$scope.continueClasses=true;
		$scope.upperSaleId=0;
		$scope.timeOfPlan=$scope.upperTime;
		$scope.planDateStr=$scope.upperDate;
		
		findSalesOfPrograms();
	}
	
	
	function findSalesOfPrograms(){
		
		if($scope.upperSaleId == undefined || $scope.upperSaleId == null){
			$scope.upperSaleId=0;
		}
		if($scope.upperProgId == undefined || $scope.upperProgId == null){
			$scope.upperProgId=0;
		}
		if($scope.upperSchId == undefined || $scope.upperSchId == null){
			$scope.upperSchId=0;
		}
		
		var saleId=$scope.upperSaleId;
		var progId= $scope.upperProgId;
		var schId=$scope.upperSchId;
		
		if($scope.upperProgId!=0){
			$scope.selectionsShow=true;
			
				$.ajax({
					  type:'POST',
					  url: "../pt/program/findProgramByProgId/"+progId+"/"+globals.PROGRAM_CLASS,
					  contentType: "application/json; charset=utf-8",
					  dataType: 'json', 
					  cache:false
					}).done(function(res) {
							$scope.programClass=res;
							$scope.preProgramed=true;
							$scope.$apply();
					}).fail  (function(jqXHR, textStatus, errorThrown) 
							{ 
						$(".splash").css('display','none');
					});
				
				var callMethod;
				var callId;
				
				if(schId!=0){
					callMethod="findSchedulePlanById";
					callId=schId;
				}else if(saleId!=0){
					callMethod="findSchedulePlanBySaleId";
					callId=saleId;
				}
				
				if(callMethod!=""){
					$.ajax({
						  type:'POST',
						  url: "../pt/schedule/"+callMethod+"/"+callId+"/"+globals.PROGRAM_CLASS,
						  contentType: "application/json; charset=utf-8",				    
						  dataType: 'json', 
						  cache:false
						}).done(function(res) {
							if(res!=null){
								$scope.schedulePlan=res;
								$scope.hasOldClasses=true;
								$scope.scheduleObj.schedulePlan=res;

								
								
								if($scope.upperSchtStaffId!=0){
								    for(var i=0;i<$scope.staffs.length;i++){
										if($scope.staffs[i].userId==$scope.upperSchtStaffId){
											$scope.staff=$scope.staffs[i];
											break;
										}
									}
								}else{
									$scope.staff=$scope.scheduleObj.schedulePlan.staff;
									
								}
								
								
								
								var scheduleTimePlans=$scope.scheduleObj.schedulePlan.scheduleTimePlans;
								$.each(scheduleTimePlans,function(j,scheduleTimePlan){
										var users=scheduleTimePlan.users;
										if(j==0){
											$.each(users,function(i,data){
												$scope.selectedMembers.push(data);
											});
										}else{
										    $.each(users,function(i,data){
													var found=false;
													for(var i=0;i<$scope.selectedMembers.length;i++){
														var memberIn=$scope.selectedMembers[i];
														if(memberIn.userId==data.userId){
															found=true;
															break;
														}
													}
													if(!found){
														$scope.selectedMembers.push(data);
													}
											});
										}
								});
								if($scope.continueClasses){
									$scope.resultOfBookingPage="";
									$scope.showResult=false;
									$scope.selectionsShow=true;
									$scope.showUser=false;
									$scope.usersPage="";
									$scope.timeOfPlan=$scope.upperTime;
									$scope.planDateStr=$scope.upperDate;
									$scope.continueClasses=false;
									
									
								}else{
									$scope.resultOfBookingPage="./schedule/plan/class/result/result.html";
									$scope.showResult=true;
									$scope.continueClasses=false;
								}
								$scope.$apply();
							
							}else{
								$.ajax({
									  type:'POST',
									  url: "../pt/packetsale/findUserBySaleId/"+globals.PROGRAM_CLASS+"/"+saleId,
									  contentType: "application/json; charset=utf-8",				    
									 dataType: 'json', 
									  cache:false
									}).done(function(res) {
										$scope.selectedMembers.push(res);
										$scope.$apply();
									}).fail  (function(jqXHR, textStatus, errorThrown){ 
										if(jqXHR.status == 404 || textStatus == 'error')	
											  $(location).attr("href","/beinplanner/lock.html");
										
										
									});
							}
							$(".splash").css('display','none');
						}).fail  (function(jqXHR, textStatus, errorThrown){ 
							    $.ajax({
								  type:'POST',
								  url: "../pt/packetsale/findUserBySaleId/"+globals.PROGRAM_CLASS+"/"+saleId,
								  contentType: "application/json; charset=utf-8",				    
								 dataType: 'json', 
								  cache:false
								}).done(function(res) {
									$scope.selectedMembers.push(res);
									$(".splash").css('display','none');
									$scope.$apply();
								}).fail  (function(jqXHR, textStatus, errorThrown){ 
									$(".splash").css('display','none');
								});
							  
						});
				}
		}else{
			
			
			$scope.showResult=false;
			$scope.selectedMembers=new Array();
			$scope.preProgramed=false;
			
			for(var i=0;i<$scope.staffs.length;i++){
				if($scope.staffs[i].userId==$scope.upperSchtStaffId){
					$scope.staff=$scope.staffs[i];
					break;
				}
			}
			
			

			if($scope.upperTime==null || $scope.upperTime=='undefined' || $scope.upperTime=="" ){
				$scope.timeOfPlan="";
			}else{
				$scope.timeOfPlan=$scope.upperTime;
			}
			
			if($scope.upperDate==null || $scope.upperDate=='undefined' || $scope.upperDate=="" ){
				$("#planDateStr").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
		    }else{
				$scope.planDateStr=$scope.upperDate;
			}
			
			$scope.$apply();
			$(".splash").css('display','none');
		}
	}

	$scope.ppChange=function(){
	 if($scope.programClass!=""){
		$scope.selectionsShow=true;
	 }
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
    				}
    			*/
    			}
    		
    		}).fail  (function(jqXHR, textStatus, errorThrown) 
    				{ 
  			  if(jqXHR.status == 404 || textStatus == 'error')	
  				  $(location).attr("href","/beinplanner/lock.html");
  			});
    };
    	
    	
    $scope.showCreateUser=false;
	$scope.createUserPage="";
	
	$scope.createNewUser=function(){
		
		$scope.createUserPage="./member/createfast.html";
		$scope.showCreateUser=true;
	}
	
	$scope.closeNewUserCreate=function(){
		
		$scope.createUserPage="";
		$scope.showCreateUser=false;
	}
});