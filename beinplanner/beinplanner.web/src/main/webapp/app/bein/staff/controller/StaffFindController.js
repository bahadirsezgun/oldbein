ptBossApp.controller('StaffFindController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {
	
	$scope.maskGsm="(999) 999-9999";
	$scope.noStaff=false;
	
	
	$scope.init = function(){
		$('.animate-panel').animatePanel();
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
	};
	
	
	$scope.$on("search",function(){
		$scope.search=commonService.search;
	});
	
	
	
	
	$scope.search;
	
	$scope.staffs;
	
	$scope.getStaff=function(staff){
		parameterService.param1=staff.userId;
		$location.path("/staff/profile");
	}
	
	$rootScope.$on("$routeChangeStart", function (event, next, current) {
		commonService.searchBoxPH="";
		commonService.searchBoxPHItem();
	});
	
	
	//init method /member/list.html kullaniyor
	$scope.list = function(){
		homerService.init();
		commonService.searchBoxPH=$translate.instant("searchBySurname");
		commonService.searchBoxPHItem();
		
		
	   $.ajax({
		  type:'POST',
		  url: "../pt/ptusers/findAllWithPassive/0/0",
		  contentType: "application/json; charset=utf-8",				    
		  dataType: 'json', 
		  cache:false
		}).done(function(res) {
					
					//console.log(res.resultMessage);
					
					if(res!=null){
						
						$scope.staffs=res;
						if($scope.staffs.length==0){
							$scope.noStaff=true;
						}
						
						
						$scope.$apply();
						$('.animate-panel').animatePanel();
						
						commonService.pageName=$translate.instant("staffListPage");
						commonService.pageComment=$translate.instant("staffListPageComment");
						commonService.normalHeaderVisible=true;
						commonService.setNormalHeader();
						
					}else{
						$scope.noStaff=true;
						$scope.$apply();
					}
					
					//$('#userListTable').footable();
					
				}).fail  (function(jqXHR, textStatus, errorThrown) 
				{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
				});
	}
	
	
	
	$scope.deleteStaff=function(userId,userType){
		
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteStaffComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yes"),
            cancelButtonText: $translate.instant("no"),
            closeOnConfirm: true,
            closeOnCancel: true },
            function (isConfirm) {
            	if (isConfirm) {
            	  
            		var frmDatum = {"userId":userId,
            				"userType":userType}
            		
            		$.ajax({
  					  type:'POST',
  					  url: "../pt/ptusers/delete",
  					  contentType: "application/json; charset=utf-8",				    
  					  data: JSON.stringify(frmDatum),
  					  dataType: 'json', 
  					  cache:false
  					}).done(function(res) {
  						$scope.list();
  					});
            	}
            });
		
		
	};
	
	
	
	
});
