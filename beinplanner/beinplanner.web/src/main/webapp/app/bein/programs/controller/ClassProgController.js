ptBossApp.controller('ClassProgController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	
	$scope.groupRestriction=$rootScope.groupRestriction;
	
	$scope.noProgram=true;
	$scope.willProgramCreate=false;
	$scope.programClasses;
	$scope.programInstructors;
	
	$scope.progId="0";
	$scope.progName="";
	$scope.progShortName="";
	$scope.progCount=0;
	$scope.minMemberCount=0;
	$scope.maxMemberCount=10;
	$scope.progPrice=0;
	$scope.progDuration=60;
	$scope.progBeforeDuration=0;
	$scope.progAfterDuration=0;
	$scope.progUserId="0";
	$scope.progDescription="";
	$scope.progComment="";
	$scope.firmId;
	$scope.progStatus="1";
	
	$scope.restFlag="0";
	$scope.restType="0";
	$scope.restDuration=0;
	
	$scope.progDetails;
	$scope.dformat="dd/mm/yyyy";
	
	
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
		
    	commonService.pageName=$translate.instant("definition_personelprog");
		commonService.pageComment=$translate.instant("personelProgDefinitionComment");
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
    				$scope.ptDateFormat=res.ptDateFormat;
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
    	}
   
    
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
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			    { 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
				});
    }
    
    function findClassPrograms(firmId){
    	$.ajax({
			  type:'POST',
			  url: "../pt/program/findAllProgramsForDefinition/"+firmId+"/"+globals.PROGRAM_CLASS,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res.length!=0){
					$scope.programClasses=res;
					$scope.noProgram=false;
				}else{
					$scope.programClasses=null;
					$scope.noProgram=true;
				}
				
				$scope.$apply();
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
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
				findClassPrograms($scope.firmId);
				
				var lang=$scope.ptLang.substring(0, 2);
				if(lang!="tr"){
					$scope.dformat='mm/dd/yyyy';
				}
				
				/*$("#progDuration").TouchSpin();
			    $("#progBeforeDuration").TouchSpin();
			    $("#progAfterDuration").TouchSpin();
				*/
				$scope.$apply();
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
				});
	}
	
	
	$scope.addNewClassProgram =function(){
		$scope.progId="0";
		$scope.progName="";
		$scope.progShortName="";
		$scope.progCount=0;
		$scope.progPrice=0;
		$scope.progDuration=60;
		$scope.progBeforeDuration=0;
		$scope.progAfterDuration=0;
		$scope.progUserId="0";
		$scope.progDescription="";
		$scope.progComment="";
		$scope.progStatus="1";
		$scope.minMemberCount=0;
		$scope.maxMemberCount=10;
		
		$scope.restFlag="0";
		$scope.restType="0";
		$scope.restDuration=0;	
		
		$scope.monday="";
	    $scope.tuesday="";
	    $scope.wednesday="";
	    $scope.thursday="";
	    $scope.friday="";
	    $scope.saturday="";
	    $scope.sunday="";
		
		if (!$scope.$$phase) 
			$scope.$apply();
		
		$scope.willProgramCreate=true;
		
	};
	
	
	
	$scope.showProgram =function(progId){
		
		
		   $.ajax({
			  type:'POST',
			  //url: "../pt/program/findProgramClass/"+progId,
			  url: "../pt/program/findProgramByProgId/"+progId+"/"+globals.PROGRAM_CLASS,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					$scope.progId=res.progId;
					$scope.progName=res.progName;
					$scope.progShortName=res.progShortName;
					$scope.progCount=res.progCount;
					$scope.progStartDate=res.progStartDateStr;
					$scope.progEndDate=res.progEndDateStr;
					$scope.progEndless=""+res.progEndless;
					$scope.progPrice=res.progPrice;
					$scope.progDuration=res.progDuration;;
					$scope.progBeforeDuration=res.progBeforeDuration;
					$scope.progAfterDuration=res.progAfterDuration;
					$scope.progUserId=""+res.progUserId;
					$scope.progDescription=res.progDescription;
					$scope.progComment=res.progComment;
					$scope.firmId=res.firmId;
					$scope.progStatus=""+res.progStatus;
					$scope.willProgramCreate=true;
					$scope.minMemberCount=res.minMemberCount;
					$scope.maxMemberCount=res.maxMemberCount;
					
					$scope.restFlag=""+res.restFlag;
					$scope.restType=""+res.restType;
					$scope.restDuration=res.restDuration;
					
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
	
	
	
	$scope.createClassProgram =function(){
		
		if($scope.progStartDate==""){
			toastr.error($translate.instant('fillRequiredFields'));
			return;
		}
		else if($scope.progEndDate==""){
			$scope.progEndDate=$scope.progStartDate;
		}
		
		//var classDetails=generateClassDetailProgram($scope.progId);
		
		var frmDatum = {'type':'pc',
						'progId':$scope.progId,
				        'firmId':$scope.firmId,
				        'progName':$scope.progName,
				        'progStatus':$scope.progStatus,
				        'progCount':$scope.progCount,
						'progDuration':$scope.progDuration,
				        'progUserId':$scope.progUserId,
				        'progBeforeDuration':$scope.progBeforeDuration,
				        'progAfterDuration':$scope.progAfterDuration,
				        'progDescription':$scope.progDescription,
				        'progComment':$scope.progComment,
				        'progPrice':$scope.progPrice,
				        'dateFormat':$scope.dformat,
				        'minMemberCount':$scope.minMemberCount,
						'maxMemberCount':$scope.maxMemberCount,
						'progShortName':$scope.progShortName,
						'restFlag':$scope.restFlag,
				        'restType':$scope.restType,
				        'restDuration':$scope.restDuration
		
					}; 
		   $.ajax({
			  type:'POST',
			  url: "../pt/program/createProgram",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res!=null){
					$scope.progId=res.resultMessage.trim();
					findClassPrograms($scope.firmId);
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
	
	
	
	
	$scope.deleteClassProgram =function(progId){
		   
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteProgramComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: false,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
            	
            	
            	
               var frmDatum = {"progId":progId,'type':'pc'}; 
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
     					findClassPrograms($scope.firmId);
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