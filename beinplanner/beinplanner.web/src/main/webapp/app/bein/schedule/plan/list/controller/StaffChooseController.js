ptBossApp.controller('StaffChooseController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {
	
	$scope.maskGsm="(999) 999-9999";
	
	
	
	$scope.$on("search",function(){
		$scope.search=commonService.search;
	});
	
	
	
	
	$scope.search;
	
	$scope.staffs;
	
	$scope.staffChoosed=false;
	$scope.staffId=0;
	
	
	
	//init method /member/list.html kullaniyor
	$scope.initSCC = function(){
		homerService.init();
		commonService.searchBoxPH=$translate.instant("searchBySurname");
		commonService.searchBoxPHItem();
		
		
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
						$('.animate-panel').animatePanel();
					}
					
					
					
				}).fail  (function(jqXHR, textStatus, errorThrown) 
				{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
				});
	}
	
	
	
	$scope.chooseStaff=function(staff){
		$scope.staffChoosed=true;
		$scope.staffId=staff.userId;
		$scope.setStaff(staff);
	};
	
	
	
	
	
	
});
