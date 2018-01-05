ptBossApp.controller('StaffTrackingController', function($scope,$translate,homerService,commonService,globals ) {

	$scope.firms;
	$scope.firmId="";
	$scope.staffs;
	$scope.staff=new Object();
	$scope.staff.userId="";
	
	$scope.query=true;
	$scope.startDate;
	$scope.endDate;
	
		
		
	$scope.initPIC=function(){
		findPtGlobals();
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		
		
    	
	}
	
	
	
	
    
    $scope.deleteTracking=function(staffTracking){
    	
    	
		
    	
    	
    	swal({
            title: $translate.instant("warning"),
            text: $translate.instant("areYouSureToCancelInOut"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yes"),
            cancelButtonText: $translate.instant("no"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
            	$(".splash").css("display",'');
		    	$.ajax({
					  type:'POST',
					  url: "../pt/stafftracking/deleteStaffInOut/"+staffTracking.ptIdx,
					  contentType: "application/json; charset=utf-8",				    
					  dataType: 'json', 
					  cache:false
					}).done(function(res) {
						toastr.success($translate.instant(res.resultMessage));
						$scope.queryPtInCome();
						
					});
		            }
        });
    };
    
    
    $scope.staffTrackings;
    
	
	$scope.queryPtInCome=function(){
		
		$(".splash").css("display",'');
		
		if($scope.firmId==""){
			toastr.error($translate.instant("noFirmNameWrited"));
			return;
		}
		
		
		var frmDatum={"userId":$scope.staff.userId
					 ,"startDateStr":$scope.startDate
					 ,"endDateStr":$scope.endDate
					 ,"firmId":$scope.firmId
					 }
		
		
		   
		  
		 $.ajax({
			  type:'POST',
			  url: "../pt/stafftracking/findStaffInOut",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			   dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.staffTrackings=res;
				$scope.query=false;
				$scope.$apply();
				$(".splash").css("display",'none');
			}).fail  (function(jqXHR, textStatus, errorThrown){
				$(".splash").css("display",'none');
			});
	};
	
	
	
	 
	
	function findPtGlobals(){
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
				    $scope.ptCurrency=res.ptCurrency;
				    $scope.$apply();
				  
				    $("#startDate").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
				    $("#endDate").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
			        
				    
				    findFirms();
				}
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
	}
	
	
	
	$scope.changeFirm=function(){
		findStaff();
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
				findStaff();
				
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
	
	function findStaff(){
	  	  $.ajax({
	  		  type:'POST',
	  		  url: "../pt/ptusers/findAll/"+$scope.firmId+"/"+globals.USER_TYPE_SCHEDULAR_STAFF,
	  		  contentType: "application/json; charset=utf-8",				    
	  		  dataType: 'json', 
	  		  cache:false
	  		}).done(function(res) {
	  					if(res!=null){
	  						$scope.staffs=res;
	  						$scope.staff.userId="";
	  						$scope.$apply();
	  					}
	  				}).fail  (function(jqXHR, textStatus, errorThrown) 
	  				{ 
	  				  if(jqXHR.status == 404 || textStatus == 'error')	
	  					  $(location).attr("href","/beinplanner/lock.html");
	  				});
	  	}
	
});