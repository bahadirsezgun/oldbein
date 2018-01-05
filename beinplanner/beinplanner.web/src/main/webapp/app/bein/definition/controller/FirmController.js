ptBossApp.controller('FirmController', function($scope,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.firmName="";
	$scope.firmPhone="";
	$scope.firmId="";
	$scope.firmAddress="";
	$scope.firmEmail="";
	
	$scope.firmCityId="";
	$scope.firmStateId="";
	
	$scope.initStateId="";
	$scope.initStateName="";
	
	$scope.selectedFirmName="";
	
	/**TANIMLI FIRMALAR*/
	$scope.firms;
	$scope.langs=new Array();
	$scope.cities;
	$scope.states;
	
	$scope.firmDetail=false;
	$scope.addFirmName="";
	
	toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	$scope.init = function(){
		
		    
			initTour();
			findStates();
			findFirms();
			
			$("#firmPhone").mask("(999) 999-9999");
			
		    if($scope.firmId!=""){
		    	
		    	$scope.showFirm($scope.firmId);
		    }
		
			commonService.pageName=$translate.instant("definition_firm");
			commonService.pageComment=$translate.instant("firmDefinitionComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
			
		   
	}
	
	
	
	$scope.stateChange=function(){
		findCities();
		
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
				$scope.$apply();
			});
	}
	
	function findStates(){
		$.ajax({
			  type:'POST',
			  url: "../pt/definition/state/findStates",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					$scope.states=res;
					$scope.$apply();
					
					if($scope.states!=null){
						
						$scope.firmStateId=$scope.states[0].stateId;
						$scope.initStateId=$scope.states[0].stateId;
						$scope.initStateName=$scope.states[0].stateName;
						$scope.states[0].selected=true;
						findCities();
					}
					
					
				}else{
					toastr.error($translate.instant('noProcessDone'));
				}
				
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			});
	};
	
	function findCities(){
		   $.ajax({
			  type:'POST',
			  url: "../pt/definition/city/findCities/"+$scope.firmStateId,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.cities=res;
				if(res!=null){
					$scope.cityId=$scope.cities[0].cityId;
					$scope.cities[0].selected=true;
				}
				$scope.$apply();
			});
	};
	
	
	
	
	$scope.showFirm =function(firmId){
		
		   $.ajax({
			  type:'POST',
			  url: "../pt/definition/firm/findFirm/"+firmId,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					$scope.firmId=res.firmId;
					$scope.selectedFirmName=res.firmName;
					
					
					$scope.firmId=res.firmId;
					$scope.firmName=res.firmName;
					$scope.firmPhone=res.firmPhone;
					$scope.firmAddress=res.firmAddress;
					$scope.firmEmail=res.firmEmail;
					$scope.firmCityId=res.firmCityId;
					
					if(res.firmStateId==0){
						$scope.firmStateId=$scope.states[0].stateId;	
					}else{
						$scope.firmStateId=res.firmStateId;
					}
					
					$scope.firmDetail=true;
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
	
	
	
	
	$scope.createFirm =function(){
		
		if($scope.addFirmName==""){
			toastr.error($translate.instant("noFirmNameWrited"));
			return;
		}
		
		$scope.firmId=0;
		
		var frmDatum = {'firmName':$scope.addFirmName,
				        'firmId':$scope.firmId,
				        'firmPhone':$scope.firmPhone,
				        'firmAddress':$scope.firmAddress,
				        'firmEmail':$scope.firmEmail,
				        'firmCityId':$scope.firmCityId,
				        'firmStateId':$scope.firmStateId
				        
						}; 
		
		   $.ajax({
			  type:'POST',
			  url: "../pt/definition/firm/create",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					if(res.resultStatu=="1"){
						var firmId=res.resultMessage.trim();
						$scope.showFirm(firmId);
						 findFirms();
					}else{
						toastr.error($translate.instant(res.resultMessage.trim()));
					}
				}
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	};
	
	
		
	$scope.updateFirm =function(){
		var frmDatum = {'firmName':$scope.selectedFirmName,
				        'firmId':$scope.firmId,
				        'firmPhone':$scope.firmPhone,
				        'firmAddress':$scope.firmAddress,
				        'firmEmail':$scope.firmEmail,
				        'firmCityId':$scope.firmCityId,
				        'firmStateId':$scope.firmStateId
				        };
		
		   $.ajax({
			  type:'POST',
			  url: "../pt/definition/firm/update",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					if(res.resultStatu=="1"){
					
						toastr.success($translate.instant('success'));
						findFirms();
					}else{
						toastr.error($translate.instant('fail'));
					}
				}
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  //if(jqXHR.status == 404 || textStatus == 'error')	
				  //$(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
	
	$scope.deleteFirm =function(firmId){
		   
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteStateComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: false,
            closeOnCancel: false },
        function (isConfirm) {
            if (isConfirm) {
            	
            	$scope.firmDetail=false;
            	var frmDatum = {"firmId":firmId}; 
     		   $.ajax({
     			  type:'POST',
     			  url: "../pt/definition/firm/delete",
     			  contentType: "application/json; charset=utf-8",				    
     			  data: JSON.stringify(frmDatum),
     			  dataType: 'json', 
     			  cache:false
     			}).done(function(res) {
     				if(res.resultStatu==1){
     					swal($translate.instant("deleted"), $translate.instant("deletedSuccessMessage"), "success");
     				}else{
     					swal($translate.instant("warning"), $translate.instant(res.resultMessage), "error");
     				}
     				$scope.init();
     				
     			}).fail  (function(jqXHR, textStatus, errorThrown) 
     			{ 
     			  if(jqXHR.status == 404 || textStatus == 'error')	
     				  $(location).attr("href","/beinplanner/lock.html");
     			});
            	
                
            } else {
                swal($translate.instant("deleteCanceled"), "error");
            }
        });
		
		   
	};
	
	
	
    $scope.$on("help",function(){
    	tour.restart();
    });
    
	var tour;
	function initTour(){
		
		
		
		tour = new Tour({
            backdrop: true,
            template: "<div class='popover tour'>" +
		            		" <div class='arrow'></div><h3 class='popover-title'></h3>" +
		            		"<div class='popover-content'></div>" +
		            		"<div class='popover-navigation'>" +
		            		  	"<div class='btn-group'>" +
		                    		"<button class='btn btn-default' data-role='prev'>"+$translate.instant('prev')+"</button>" +
		                    		"<button class='btn btn-default' data-role='next'>"+$translate.instant('next')+"</button>" +
		                    	"</div>" +
		                    	"<button class='btn btn-default' data-role='end'>"+$translate.instant('endTour')+"</button>" +
		                    "</div>"+
		              "</div>",
            onShown: function(tour) {

                // ISSUE    - https://github.com/sorich87/bootstrap-tour/issues/189
                // FIX      - https://github.com/sorich87/bootstrap-tour/issues/189#issuecomment-49007822

                // You have to write your used animated effect class
                // Standard animated class
                $('.animated').removeClass('fadeIn');
                // Animate class from animate-panel plugin
                $('.animated-panel').removeClass('zoomIn');

            },
            steps: [
                {
                    element: ".tour-1",
                    title: $translate.instant("studio_help1_header"),
                    content: $translate.instant("studio_help1_comment"),
                    placement: "bottom"
                },
                {
                    element: ".tour-2",
                    title: $translate.instant("studio_help2_header"),
                    content: $translate.instant("studio_help2_comment"),
                    placement: "top"

                },
                {
                    element: ".tour-3",
                    title: $translate.instant("studio_help3_header"),
                    content: $translate.instant("studio_help3_comment"),
                    placement: "right"

                },
                {
                    element: ".tour-4",
                    title: $translate.instant("studio_help4_header"),
                    content: $translate.instant("studio_help4_comment"),
                     placement: "left"

                }
            ]});

        // Initialize the tour
        tour.init();
		
	}
	
	
	
	
	
	
	
	
});