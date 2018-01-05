ptBossApp.controller('GlobalController', function($scope,$translate,parameterService,$location,homerService,commonService) {

	$scope.glbId=1;
	$scope.ptTz="Europe/Istanbul";
	$scope.ptCurrency="$";
	$scope.ptStaticIp="";
	$scope.ptLang="tr_TR";
	$scope.ptDateFormat="%d/%m/%y";
	
	
	
	toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	$scope.init = function(){
		
		   
			initTour();
			
			commonService.pageName=$translate.instant("settings_globalTitle");
			commonService.pageComment=$translate.instant("settings_globalComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
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
							commonService.changeLang(lang);
						}
						$scope.$apply();
					}
				});
	}
	
	
	
	
	
	
	$scope.createPtGlobal =function(){
		var frmDatum = {'glbId':$scope.glbId,
				        'ptLang':$scope.ptLang,
				        'ptCurrency':$scope.ptCurrency,
				        'ptStaticIp':$scope.ptStaticIp,
				        'ptDateFormat':$scope.ptDateFormat,
				        'ptTz':$scope.ptTz
				        
						}; 
		
		   $.ajax({
			  type:'POST',
			  url: "../pt/setting/createPtGlobal",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					if(res.resultStatu=="1"){
						toastr.success($translate.instant(res.resultMessage.trim()));
						
						if($scope.ptLang!=""){
							var lang=$scope.ptLang.substring(0,2);
							  $translate.use(lang);
							  $scope.$apply();
							  localStorage.setItem('lang', lang);
							  commonService.changeLang(lang);	
						}
						
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