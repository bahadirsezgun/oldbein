ptBossApp.controller('DefinitionController', function($scope,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.cities;
	$scope.states;
	$scope.stateName;
	
	$scope.selectedStateName="";
	$scope.selectedStateId="";
	$scope.cityName="";
	
	toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	$scope.init = function(){
		
			initTour();
		
		    if($scope.selectedStateId!=""){
		    	
		    	var state=new Object();
		    	state.stateId=$scope.selectedStateId;
		    	state.stateName=$scope.selectedStateName;
		    	
		    	
		    	$scope.showCities(state);
		    }
		
			commonService.pageName=$translate.instant("definition_state_city");
			commonService.pageComment=$translate.instant("stateDefinitionComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
			
			
			
			
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
						
					}else{
						toastr.error($translate.instant('noProcessDone'));
					}
					
					
					
				}).fail  (function(jqXHR, textStatus, errorThrown) 
				{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
				});
	}
	
	
	
	
	
	$scope.showCities =function(state){
		
		
		
		
		$scope.selectedStateName=state.stateName;
		$scope.selectedStateId=state.stateId;
		
		/*
		$('#stateId').editable({
            type: 'text',
            pk: 1,
            prepend:$scope.selectedStateName,
            title: $translate.instant("changeCityName"),
            showbuttons: 'right',
            success: function(response, newValue) {
            	$scope.selectedStateName=newValue;
            	updateState();
             }
        });*/
		
		 $.ajax({
			  type:'POST',
			  url: "../pt/definition/city/findCities/"+state.stateId,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.cities=res;
				$scope.$apply();
			});
	};
	
	
	
	$scope.createCity =function(city){
		var frmDatum = {"stateId":$scope.selectedStateId,'cityName':$scope.cityName}; 
		   $.ajax({
			  type:'POST',
			  url: "../pt/definition/city/create",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				var state=new Object();
				state.stateId=$scope.selectedStateId;
				state.selectedStateName=$scope.selectedStateName;
				
				$scope.showCities(state);
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
		
	
	$scope.updateState = function(){
		var frmDatum = {"stateName":$scope.selectedStateName,'stateId':$scope.selectedStateId}; 
		   $.ajax({
			  type:'POST',
			  url: "../pt/definition/state/update",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				$scope.init();
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			});
	}
	
	$scope.createState =function(){
		   var frmDatum = {"stateName":$scope.stateName}; 
		   $.ajax({
			  type:'POST',
			  url: "../pt/definition/state/create",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				$scope.init();
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			});
	};
	
	$scope.deleteState =function(state){
		   
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
            	
            	
            	var frmDatum = {"stateName":state.stateName,'stateId':state.stateId}; 
     		   $.ajax({
     			  type:'POST',
     			  url: "../pt/definition/state/delete",
     			  contentType: "application/json; charset=utf-8",				    
     			  data: JSON.stringify(frmDatum),
     			  dataType: 'json', 
     			  cache:false
     			}).done(function(res) {
     				swal($translate.instant("deleted"), $translate.instant("deletedSuccessMessage"), "success");
     				$scope.init();
     				
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
	
	
	$scope.deleteCity =function(city){
		   
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteCityComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: false,
            closeOnCancel: false },
        function (isConfirm) {
            if (isConfirm) {
            	
            	
            	var frmDatum = {"cityName":city.stateName,'cityId':city.cityId}; 
     		   $.ajax({
     			  type:'POST',
     			  url: "../pt/definition/city/delete",
     			  contentType: "application/json; charset=utf-8",				    
     			  data: JSON.stringify(frmDatum),
     			  dataType: 'json', 
     			  cache:false
     			}).done(function(res) {
     				swal($translate.instant("deleted"), $translate.instant("deletedSuccessMessage"), "success");
     				$scope.init();
     				
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
                    title: $translate.instant("state_help1_header"),
                    content: $translate.instant("state_help1_comment"),
                    placement: "bottom"
                },
                {
                    element: ".tour-2",
                    title: $translate.instant("state_help2_header"),
                    content: $translate.instant("state_help2_comment"),
                    placement: "top"

                },
                {
                    element: ".tour-3",
                    title: $translate.instant("state_help3_header"),
                    content: $translate.instant("state_help3_comment"),
                    placement: "right"

                },
                {
                    element: ".tour-4",
                    title: $translate.instant("state_help4_header"),
                    content: $translate.instant("state_help4_comment"),
                     placement: "left"

                },
                {
                    element: ".tour-5",
                    title: $translate.instant("state_help5_header"),
                    content: $translate.instant("state_help5_comment"),
                    placement: "left"

                }
            ]});

        // Initialize the tour
        tour.init();
		
	}
	
	
	
	
	
	
	
	
});
