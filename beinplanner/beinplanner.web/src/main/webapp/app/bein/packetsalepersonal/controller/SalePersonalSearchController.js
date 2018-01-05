ptBossApp.controller('SalePersonalSearchController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.filterName="";
	$scope.filterSurname="";
	$scope.filterDate="";
	
	$scope.init = function(){
		parameterService.init();
		$('.animate-panel').animatePanel();
		 findGlobals();
		 
		commonService.pageName=$translate.instant("salePersonalSearchPage");
		commonService.pageComment=$translate.instant("salePersonalSearchPageComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		
	};
	
	$scope.searchSale=function(){
		  
		var frmDatum = {"userName":$scope.filterName
		   		  ,'userSurname':$scope.filterSurname
		   		  ,'salesDateStr':$scope.filterDate
		   		  ,'progType':'psp'}; 
		
		$.ajax({
			  type:'POST',
			  url: "../pt/packetsale/searchSaledPackets",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				parameterService.init();
				parameterService.param1=res;
				$(location).attr("href","../bein/#/packetsale/salepersonalresult");
				$('.splash').css('display', 'none');
			}).fail  (function(jqXHR, textStatus, errorThrown){ 
				if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
				
				
			});
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
					$scope.ptLang=(res.ptLang).substring(0,2);
					$scope.ptDateFormat=res.ptScrDateFormat;
					$("#filterDate").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat});
					
					
				}
			}).fail  (function(jqXHR, textStatus, errorThrown){ 
				if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
				
				
			});
	}
	

});