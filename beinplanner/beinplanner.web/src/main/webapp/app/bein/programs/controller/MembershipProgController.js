ptBossApp.controller('MembershipProgController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.membershipRestriction=$rootScope.membershipRestriction;
	
	$scope.noProgram=true;
	$scope.willProgramCreate=false;
	$scope.programMemberships;
	$scope.programInstructors;
	
	$scope.progId="0";
	$scope.progName="";
	$scope.progShortName="";
	$scope.progPrice=0;
	$scope.progDuration=1;
	$scope.progDurationType="2";
	$scope.progBeforeDuration=10;
	$scope.progAfterDuration=5;
	$scope.progUserId="0";
	$scope.progDescription="";
	$scope.progComment="";
	$scope.firmId;
	$scope.progStatus="1";
	
	$scope.progDetails;
	$scope.dformat="dd/mm/yyyy";
	
	$scope.freezeDurationType="0";
	$scope.freezeDuration="0";
	$scope.maxFreezeCount="0";
	
	
	
	$scope.progRestriction="0";
	
	
    
    $scope.monday="";
    $scope.tuesday="";
    $scope.wednesday="";
    $scope.thursday="";
    $scope.friday="";
    $scope.saturday="";
    $scope.sunday="";
    
    

	$scope.ptTz;
	$scope.ptCurrency;
	$scope.ptStaticIp;
	$scope.ptLang;
	$scope.ptDateFormat;
    
	
	$scope.progRestrictionMonday="-1";
	$scope.progRestrictionTuesday="-1";
	$scope.progRestrictionWednesday="-1";
	$scope.progRestrictionThursday="-1";
	$scope.progRestrictionFriday="-1";
	$scope.progRestrictionSaturday="-1";
	$scope.progRestrictionSunday="-1";
	
	
	
    
    toastr.options = {
        "debug": false,
        "newestOnTop": false,
        "positionClass": "toast-top-center",
        "closeButton": true,
        "toastClass": "animated fadeInDown",
    };

    $scope.init = function(){
    	$('.clockpicker').clockpicker({autoclose: true, placement: 'left',align: 'top'});
    	$("[data-toggle=popover]").popover();
    	
    	findGlobals();
    	
		
    	commonService.pageName=$translate.instant("definition_membershipprog");
		commonService.pageComment=$translate.instant("membershipProgDefinitionComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		
    };
    
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
    				$scope.ptLang=res.ptLang;
    				$scope.ptDateFormat=res.ptScrDateFormat;
    				if($scope.ptLang!=""){
    					var lang=$scope.ptLang.substring(0,2);
    					$translate.use(lang);
    				}
    			
    			}
    			findFirms();
    		}).fail  (function(jqXHR, textStatus, errorThrown) 
    				{ 
  			  if(jqXHR.status == 404 || textStatus == 'error')	
  				  $(location).attr("href","/beinplanner/lock.html");
  			});
    	};
   
    
    function findInstructors(firmId){
    	$.ajax({
			  type:'POST',
			  url: "../pt/ptusers/findAll/"+firmId+"/"+globals.USER_TYPE_SCHEDULAR_STAFF,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.programInstructors=res;
				$scope.$apply();
			});
    }
    
    function findMembershipPrograms(){
		$.ajax({
			  type:'POST',
			  url: "../pt/program/findAllProgramsForDefinition/"+$scope.firmId+"/"+globals.PROGRAM_MEMBERSHIP,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res.length!=0){
					$scope.programMemberships=res;
					$scope.noProgram=false;
				}else{
					$scope.programMemberships=null;
					$scope.noProgram=true;
				}
				
				$scope.$apply();
			});
		
	}
   
	function findFirms(){
		$.ajax({
			  type:'POST',
			  url: "../pt/definition/firm/findFirms",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.firms=res;
				$scope.firmId=$scope.firms[0].firmId;
				findInstructors($scope.firmId);
				findMembershipPrograms();
				var lang=$scope.ptLang.substring(0, 2);
				if(lang!="tr"){
					$scope.dformat='mm/dd/yyyy';
				}
				$scope.$apply();
			});
	}
	
	
	$scope.addNewMembershipProgram =function(){
		$scope.progId="0";
		$scope.progName="";
		$scope.progShortName="";
		$scope.progPrice=0;
		$scope.progUserId="0";
		$scope.progDescription="";
		$scope.progComment="";
		$scope.progStatus="1";
		
		$scope.progRestriction="0";
		$scope.progRestrictionMonday="-1";
		$scope.progRestrictionTuesday="-1";
		$scope.progRestrictionWednesday="-1";
		$scope.progRestrictionThursday="-1";
		$scope.progRestrictionFriday="-1";
		$scope.progRestrictionSaturday="-1";
		$scope.progRestrictionSunday="-1";
		$scope.progDuration=0;
		$scope.progDurationType="0";
		
		$scope.freezeDurationType="0";
		$scope.freezeDuration="5";
		$scope.maxFreezeCount="3";
		
		
		if (!$scope.$$phase) 
			$scope.$apply();
		
		$scope.willProgramCreate=true;
		
	};
	
	
	$scope.showProgram =function(progId){
		
		
		   $.ajax({
			  type:'POST',
			  //url: "../pt/program/findProgram/"+progId,
			  url: "../pt/program/findProgramByProgId/"+progId+"/"+globals.PROGRAM_MEMBERSHIP,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					$scope.progId=res.progId;
					$scope.progName=res.progName;
					$scope.progShortName=res.progShortName;
					$scope.progPrice=res.progPrice;
					$scope.progUserId=""+res.progUserId;
					$scope.progDescription=res.progDescription;
					$scope.progComment=res.progComment;
					$scope.firmId=res.firmId;
					$scope.progStatus=""+res.progStatus;
					$scope.willProgramCreate=true;
					$scope.progRestriction=""+res.progRestriction;
					$scope.progDuration=""+res.progDuration;
					$scope.progDurationType=""+res.progDurationType;
					
					$scope.freezeDurationType=""+res.freezeDurationType;
					$scope.freezeDuration=res.freezeDuration;
					$scope.maxFreezeCount=res.maxFreezeCount;
					
					
					var programMembershipDetails=res.programMembershipDetails;
					$.each(programMembershipDetails,function(i,data){
						
						if(data.progRestrictedDay==1){
							$scope.progRestrictionMonday=""+data.progRestrictedTime;
						}else if(data.progRestrictedDay==2){
							$scope.progRestrictionTuesday=""+data.progRestrictedTime;
						}else if(data.progRestrictedDay==3){
							$scope.progRestrictionWednesday=""+data.progRestrictedTime;
						}else if(data.progRestrictedDay==4){
							$scope.progRestrictionThursday=""+data.progRestrictedTime;
						}else if(data.progRestrictedDay==5){
							$scope.progRestrictionFriday=""+data.progRestrictedTime;
						}else if(data.progRestrictedDay==6){
							$scope.progRestrictionSaturday=""+data.progRestrictedTime;
						}else if(data.progRestrictedDay==7){
							$scope.progRestrictionSunday=""+data.progRestrictedTime;
						}
						
					});
					
					
					$scope.$apply();
					
				}else{
					toastr.error($translate.instant('noProcessDone'));
				}
				
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			});
		
	};
	
	
	
	$scope.createMembershipProgram =function(){
		
		if($scope.progStartDate==""){
			toastr.error($translate.instant('fillRequiredFields'));
			return;
		}
		else if($scope.progEndDate==""){
			$scope.progEndDate=$scope.progStartDate;
		}
		
		var membershipDetails=generateMembershipDetailProgram($scope.progId);
		
		
		var frmDatum = {'type':'pm',
						'progId':$scope.progId,
				        'firmId':$scope.firmId,
				        'progName':$scope.progName,
				        'progStatus':$scope.progStatus,
				        'progRestriction':$scope.progRestriction,
						'progUserId':$scope.progUserId,
				        'progDescription':$scope.progDescription,
				        'progComment':$scope.progComment,
				        'progPrice':$scope.progPrice,
				        'dateFormat':$scope.dformat,
				        'programMembershipDetails':membershipDetails,
				        'progDuration':$scope.progDuration,
				        'progDurationType':$scope.progDurationType,
				        'freezeDurationType':$scope.freezeDurationType,
				        'freezeDuration':$scope.freezeDuration,
				        'maxFreezeCount':$scope.maxFreezeCount,
				        'progShortName':$scope.progShortName
					       }; 
		   $.ajax({
			  type:'POST',
			  url: "../pt/program/createProgram",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res.resultStatu==1){
					$scope.progId=res.resultMessage.trim();
					findMembershipPrograms();
					toastr.success($translate.instant('success'));
				}else{
					toastr.error($translate.instant('noProcessDone'));
				}
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
		
		
	}
	
	function generateMembershipDetailProgram(progId){
		
		
		var days=new Array();
		if($scope.progRestrictionMonday!="-1"){
			var mondayObj=new Object();
			mondayObj.progId=progId;
			mondayObj.progRestrictedDay=1;
			mondayObj.progRestrictedTime=$scope.progRestrictionMonday;
			
			days.push(mondayObj);
		}
		if($scope.progRestrictionTuesday!="-1"){
			var tuesdayObj=new Object();
			tuesdayObj.progId=progId;
			tuesdayObj.progRestrictedDay=2;
			tuesdayObj.progRestrictedTime=$scope.progRestrictionTuesday;
			
			days.push(tuesdayObj);
		}
		if($scope.progRestrictionWednesday!="-1"){
			var wednesdayObj=new Object();
			wednesdayObj.progId=progId;
			wednesdayObj.progRestrictedDay=3;
			wednesdayObj.progRestrictedTime=$scope.progRestrictionWednesday;
			
			days.push(wednesdayObj);
		}
		if($scope.progRestrictionThursday!="-1"){
			var thursdayObj=new Object();
			thursdayObj.progId=progId;
			thursdayObj.progRestrictedDay=4;
			thursdayObj.progRestrictedTime=$scope.thursday;
			
			days.push(thursdayObj);
		}
		if($scope.progRestrictionFriday!="-1"){
			var fridayObj=new Object();
			fridayObj.progId=progId;
			fridayObj.progRestrictedDay=5;
			fridayObj.progRestrictedTime=$scope.progRestrictionFriday;
			
			days.push(fridayObj);
		}
		if($scope.progRestrictionSaturday!="-1"){
			var saturdayObj=new Object();
			saturdayObj.progId=progId;
			saturdayObj.progRestrictedDay=6;
			saturdayObj.progRestrictedTime=$scope.progRestrictionSaturday;
			
			days.push(saturdayObj);
		}
		if($scope.progRestrictionSunday!="-1"){
			var sundayObj=new Object();
			sundayObj.progId=progId;
			sundayObj.progRestrictedDay=7;
			sundayObj.progRestrictedTime=$scope.progRestrictionSunday;
			
			days.push(sundayObj);
		}
		
		return days;
		
	}
	
	
	
	$scope.deleteMembershipProgram =function(progId){
		   
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteMembershipComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: false,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
            	var frmDatum = {"progId":progId,'type':'pm'}; 
     		   $.ajax({
     			  type:'POST',
     			  url: "../pt/program/deleteProgram",
     			  contentType: "application/json; charset=utf-8",				    
     			  data: JSON.stringify(frmDatum),
     			  dataType: 'json', 
     			  cache:false
     			}).done(function(res) {
     				
     				if(res.resultStatu="1"){
     					swal($translate.instant("deleted"), $translate.instant("deletedSuccessMessage"), "success");
     					$scope.willProgramCreate=false;
     					findMembershipPrograms();
     				}else{
     					swal($translate.instant("nodeleted"), $translate.instant("programUsedInSales"), "fail");
     				}
     				
     			}).fail  (function(jqXHR, textStatus, errorThrown) 
     			{ 
     			  if(jqXHR.status == 404 || textStatus == 'error')	
     				  $(location).attr("href","/beinplanner/lock.html");
     			});
            	
                
            } else {
                swal($translate.instant("deleteCanceled"), "");
            }
        });
	};
	
	
	
	
	
    
    

});