ptBossApp.controller('PersonelProgController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.individualRestriction=$rootScope.individualRestriction;
	
		$scope.noProgram=true;
		$scope.willProgramCreate=false;
		$scope.programPersonals;
		$scope.programInstructors;
		
		$scope.progId="0";
		$scope.progName="";
		$scope.progShortName="";
		$scope.progStatus="1";
		$scope.progDuration=60;
		$scope.progUserId="0";
		$scope.progBeforeDuration=0;
		$scope.progAfterDuration=0;
		$scope.progDescription="";
		$scope.progComment="";
		$scope.firmId;
		$scope.progCount;
		$scope.progPrice=0;
		
		$scope.restFlag="0";
		$scope.restType="0";
		$scope.restDuration=0;
		
	    toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	    $scope.init = function(){
	    	
	    	findFirms();
	    	$("[data-toggle=popover]").popover();
	    	commonService.pageName=$translate.instant("definition_personelprog");
			commonService.pageComment=$translate.instant("personelProgDefinitionComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
			
			
	    };
	    
	    /*
	    $scope.priceKey=function(keyEvent){
    		
	    	console.log(keyEvent.which);
	    	var price=$scope.progPrice;
	    	if (keyEvent.which == 44){
    			$scope.progPrice=String(price).replace(',', '.');
    			
    		}
    	}
	    */
	    
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
	    
	    function findPrograms(firmId){
			$.ajax({
				  type:'POST',
				  url: "../pt/program/findAllProgramsForDefinition/"+firmId+"/"+globals.PROGRAM_PERSONAL,
				  contentType: "application/json; charset=utf-8",
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					
					if(res.length!=0){
						$scope.programPersonals=res;
						$scope.noProgram=false;
					}else{
						$scope.programPersonals=null;
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
					findPrograms($scope.firmId);
					/*
					$("#progDuration").TouchSpin();
				    $("#progBeforeDuration").TouchSpin();
				    $("#progAfterDuration").TouchSpin();
					$("#progPrice").TouchSpin({
						postfix: $scope.firms[0].firmCurrency
					});
			    	*/
					$scope.$apply();
					
				});
		}
		
		
		$scope.addNewProgram =function(){
			$scope.progName="";
			$scope.progShortName="";
			$scope.progStatus="1";
			$scope.progId="0";
			
			$scope.progDuration=60;
			$scope.progUserId="0";
			$scope.progBeforeDuration=0;
			$scope.progAfterDuration=0;
			$scope.progDescription="";
			$scope.progComment="";
			$scope.progPrice=0;
			$scope.progCount=0;
			$scope.restFlag="0";
			$scope.restType="0";
			$scope.restDuration=0;
			
			if (!$scope.$$phase) 
				$scope.$apply()
			
			$scope.willProgramCreate=true;
			
		};
		
		
		$scope.showProgram =function(progId){
			
			
			   $.ajax({
				  type:'POST',
				  //url: "../pt/program/findProgramPersonal/"+progId,
				  url: "../pt/program/findProgramByProgId/"+progId+"/"+globals.PROGRAM_PERSONAL,
				  contentType: "application/json; charset=utf-8",
				  dataType: 'json', 
				  cache:false
				}).done(function(data) {
					
					var res=data;
					
					if(res!=null){
						
						$scope.progName=res.progName;
						$scope.progShortName=res.progShortName;
						$scope.firmId=res.firmId;
						$scope.firmName=res.firmName;
						$scope.progStatus=""+res.progStatus;
						$scope.progId=res.progId;
						$scope.willProgramCreate=true;
						$scope.progDuration=res.progDuration;
						$scope.progUserId=""+res.progUserId;
						$scope.progBeforeDuration=res.progBeforeDuration;
						$scope.progAfterDuration=res.progAfterDuration;
						$scope.progDescription=res.progDescription;
						$scope.progComment=res.progComment;
						$scope.progPrice=res.progPrice;
						$scope.progCount=res.progCount;
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
		
		
		
		$scope.createProgram =function(){
			var frmDatum = {'type':'pp',
							'progId':$scope.progId,
					        'firmId':$scope.firmId,
					        'progName':$scope.progName,
					        'progStatus':$scope.progStatus,
					        'progDuration':$scope.progDuration,
					        'progUserId':$scope.progUserId,
					        'progBeforeDuration':$scope.progBeforeDuration,
					        'progAfterDuration':$scope.progAfterDuration,
					        'progDescription':$scope.progDescription,
					        'progComment':$scope.progComment,
					        'progPrice':$scope.progPrice,
					        'progCount':$scope.progCount,
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
					
					if(res.resultStatu=="1"){
						$scope.progId=res.resultMessage.trim();
						findPrograms($scope.firmId);
						toastr.success($translate.instant('success'));
						
					}else{
						toastr.error($translate.instant(res.resultMessage));
					}
					
				}).fail  (function(jqXHR, textStatus, errorThrown) 
				{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
				});
			
			
		}
			
		
		
		$scope.deleteProgram =function(progId){
			   
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
	            	
	            	 var frmDatum = {"progId":progId,'type':'pp'}; 
	       		   $.ajax({
	       			  type:'POST',
	       			  url: "../pt/program/deleteProgram",
	     			  contentType: "application/json; charset=utf-8",				    
	     			  data: JSON.stringify(frmDatum),
	     			  dataType: 'json', 
	     			  cache:false
	     			}).done(function(res) {
	     				if(res.resultStatu=="1"){
	     					swal($translate.instant("deleted"), $translate.instant("deletedSuccessMessage"), "success");
	     					$scope.willProgramCreate=false;
	     					findPrograms($scope.firmId);
	     				}else{
	     					swal($translate.instant("nodeleted"), $translate.instant("programUsedInSales"), "error");
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