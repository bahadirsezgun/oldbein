ptBossApp.controller('StudioController', function($scope,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.studioName;
	$scope.studioShortName;
	$scope.firmId;
	$scope.firmName;
	$scope.studioStatus="1";
	$scope.studioId="";
	
	
	/**TANIMLI SYUDYOLAR*/
	$scope.studios;
	$scope.firms;
	
	$scope.noStudio=false;
	$scope.willStudioCreate=false;
	
	toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	$scope.init = function(){
		
			initTour();
			findFirms();
			
			commonService.pageName=$translate.instant("definition_studio");
			commonService.pageComment=$translate.instant("studioDefinitionComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
			
		   
	}
	
	$scope.changeFirm=function(){
		findStudios();
	}
	
	function findStudios(){
		$.ajax({
			  type:'POST',
			  url: "../pt/definition/studio/findAll/"+$scope.firmId,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res.length!=0){
					$scope.studios=res;
					$scope.noStudio=false;
				}else{
					$scope.noStudio=true;
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
				$scope.$apply();
				findStudios();
				
			});
	}
	
	$scope.addNewStudio =function(){
		$scope.studioName="";
		$scope.studioShortName="";
		$scope.studioStatus="1";
		$scope.studioId=0;
		$scope.willStudioCreate=true;
		
	};
	
	
	$scope.showStudio =function(studioId){
		
		
		   $.ajax({
			  type:'POST',
			  url: "../pt/definition/studio/findStudio/"+studioId,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					
					$scope.studioName=res.studioName;
					$scope.studioShortName=res.studioShortName;
					$scope.firmId=res.firmId;
					$scope.firmName=res.firmName;
					$scope.studioStatus=""+res.studioStatus;
					$scope.studioId=res.studioId;
					$scope.willStudioCreate=true;
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
	
	
	
	$scope.createStudio =function(city){
		var frmDatum = {'studioId':$scope.studioId,
				        'firmId':$scope.firmId,
				        'studioName':$scope.studioName,
				        'studioStatus':$scope.studioStatus,
				        'studioShortName':$scope.studioShortName}; 
		   $.ajax({
			  type:'POST',
			  url: "../pt/definition/studio/create",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res!=null){
					$scope.studioId=res.resultMessage.trim();
					$scope.noStudio=false;
					findStudios();
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
		
	
	
	$scope.deleteStudio =function(studioId){
		   
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteStudioComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: false,
            closeOnCancel: false },
        function (isConfirm) {
            if (isConfirm) {
            	
            	
            	
               var frmDatum = {"studioId":studioId}; 
     		   $.ajax({
     			  type:'POST',
     			  url: "../pt/definition/studio/delete",
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
     				$scope.studios=null;
     				$scope.studioId="";
     				$scope.willStudioCreate=false;
     				$scope.$apply();
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

                },
                {
                    element: ".tour-5",
                    title: $translate.instant("studio_help5_header"),
                    content: $translate.instant("studio_help5_comment"),
                    placement: "left"

                }
            ]});

        // Initialize the tour
        tour.init();
		
	}
	
	
	
	
	
	
	
	
});