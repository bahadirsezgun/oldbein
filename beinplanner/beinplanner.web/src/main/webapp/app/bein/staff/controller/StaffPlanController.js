ptBossApp.controller('StaffPlanController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.staffs;
	$scope.year;
	$scope.month="0";
	$scope.firmId;
	$scope.staffId="0";
	$scope.showQuery=true;
	
	$scope.monthly=true;
	
	$scope.staffPlans;
	
	$scope.startDate;
	$scope.endDate;
	
	$scope.months=new Array({value:"0",name:$translate.instant("pleaseSelect")}
    ,{value:"1",name:$translate.instant("january")}
   ,{value:"2",name:$translate.instant("february")}
   ,{value:"3",name:$translate.instant("march")}
   ,{value:"4",name:$translate.instant("april")}
   ,{value:"5",name:$translate.instant("may")}
   ,{value:"6",name:$translate.instant("june")}
   ,{value:"7",name:$translate.instant("july")}
   ,{value:"8",name:$translate.instant("august")}
   ,{value:"9",name:$translate.instant("september")}
   ,{value:"10",name:$translate.instant("october")}
   ,{value:"11",name:$translate.instant("november")}
   ,{value:"12",name:$translate.instant("december")}
   );


	$scope.years=new Array();
	
	
	$scope.showPlanDetail=function(staffPlan){
		
	}
	
	$scope.queryPersonal=function(){
		var frmDatum={"month":$scope.month,"year":$scope.year,"staffId":$scope.staffId,"typeOfSchedule":globals.PROGRAM_PERSONAL}
		$.ajax({
	  		  type:'POST',
	  		  url: "../pt/scheduleStaff/getSchStaffPlan",
	  		  data:JSON.stringify(frmDatum),
	  		  contentType: "application/json; charset=utf-8",				    
	  		  dataType: 'json', 
	  		  cache:false
	  		}).done(function(res) {
	  			$scope.staffPlans=res;
	  			$scope.showQuery=false;
	  			$scope.$apply();
	  		});
		
		
	}
	
	$scope.queryClass=function(){
		var frmDatum={"month":$scope.month,"year":$scope.year,"staffId":$scope.staffId,"typeOfSchedule":globals.PROGRAM_CLASS}
		$.ajax({
	  		  type:'POST',
	  		  url: "../pt/scheduleStaff/getSchStaffPlan",
	  		  data:JSON.stringify(frmDatum),
	  		  contentType: "application/json; charset=utf-8",				    
	  		  dataType: 'json', 
	  		  cache:false
	  		}).done(function(res) {
	  			$scope.staffPlans=res;
	  			$scope.showQuery=false;
	  			$scope.$apply();
	  		});
		
		
	}
	
	
	
	$scope.queryPersonalDate=function(){
		var frmDatum={"startDateStr":$scope.startDate,"endDateStr":$scope.endDate,"staffId":$scope.staffId,"typeOfSchedule":globals.PROGRAM_PERSONAL}
		$.ajax({
	  		  type:'POST',
	  		  url: "../pt/scheduleStaff/getSchStaffPlanByDate",
	  		  data:JSON.stringify(frmDatum),
	  		  contentType: "application/json; charset=utf-8",				    
	  		  dataType: 'json', 
	  		  cache:false
	  		}).done(function(res) {
	  			$scope.staffPlans=res;
	  			$scope.showQuery=false;
	  			$scope.$apply();
	  		});
		
		
	}
	
	$scope.queryClassDate=function(){
		var frmDatum={"startDateStr":$scope.startDate,"endDateStr":$scope.endDate,"staffId":$scope.staffId,"typeOfSchedule":globals.PROGRAM_CLASS}
		$.ajax({
	  		  type:'POST',
	  		  url: "../pt/scheduleStaff/getSchStaffPlanByDate",
	  		  data:JSON.stringify(frmDatum),
	  		  contentType: "application/json; charset=utf-8",				    
	  		  dataType: 'json', 
	  		  cache:false
	  		}).done(function(res) {
	  			$scope.staffPlans=res;
	  			$scope.showQuery=false;
	  			$scope.$apply();
	  		});
		
		
	}
	
	$scope.initSPC=function(){
		commonService.pageName=$translate.instant("staffPlanTitle");
		commonService.pageComment=$translate.instant("staffPlanTitleComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		var date=new Date();
		var year=date.getFullYear();
		
		for(var i=-10;i<10;i++){
			$scope.years.push(year+i);
		}
		
		$scope.year=year;
		$scope.month=""+(date.getMonth()+1);
		$('#inQueryCheck').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		loggedInUser();
		findPtGlobals();
	}
	
	$('#inQueryCheck').on('ifChanged', function(event) {
		if(event.target.checked){
			$scope.monthly=true;
		}else{
			$scope.monthly=false;
		}
		$scope.$apply();
    });
	
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
			        
				    
				    
				}
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
	}
	
	function loggedInUser(){
		
		$.ajax({
  		  type:'POST',
  		  url: "../pt/ptusers/getSessionUser",
  		  contentType: "application/json; charset=utf-8",				    
  		  dataType: 'json', 
  		  cache:false
  		}).done(function(res) {
  			$scope.user=res;
  			$scope.firmId=res.firmId;
  			$scope.userType=res.userType;
  			findStaff();
  			
  			$scope.$apply();
  		
  		}).fail  (function(jqXHR, textStatus, errorThrown){ 
  			
  			parameterService.init();
  			
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
							if($scope.staffs.length>0)
							$scope.staffId=$scope.staffs[0].userId;
							
							$scope.$apply();
						}
					}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
					  if(jqXHR.status == 404 || textStatus == 'error')	
						  $(location).attr("href","/beinplanner/lock.html");
					});
		}
	
	
	
	
});