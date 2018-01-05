ptBossApp.controller('SalePersonalUpdateController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.programPersonals;
	$scope.programPersonal;
	$scope.members;
	$scope.staffs;
	
	
	$scope.userType;
	$scope.disabled="disabled";
	$scope.setEnabled=function(){
		$scope.disabled="";
	};
	
	$scope.showChooseProgram=true;
	$scope.showSalesAttributes=false;
	$scope.showFindUser=false;
	$scope.showAddUser=false;
	$scope.showConfirmSale=false;
	
	
	$scope.showSalesDetail=false;
	
	$scope.ptLang="tr_TR";
	$scope.ptDateFormat="dd/MM/yyyy";
	$scope.ptCurrency;
	
	$scope.filterName=""
	$scope.filterSurname=""
	
	$scope.saleId=0;
	$scope.staffId="0";
	$scope.progCount;
	$scope.packetPrice;
	$scope.salesComment="";
	$scope.salesDateStr;
	
	$scope.userProp=true;
	$scope.showCreateUser=false;
	$scope.createUserPage="";
	
	
	$scope.saledPacket;
	
	
	
	$scope.initSPU = function(){
		
		 $('.actions').tooltip({
		        selector: "[data-toggle=tooltip]"
		 });
		 
		 $("[data-toggle=popover]").popover();
		 
		findPtGlobals();
		loggedInUser();
		    commonService.pageName=$translate.instant("salePersonalPage");
			commonService.pageComment=$translate.instant("salePersonalPageComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
		    commonService.searchBoxPH=$translate.instant("searchByProgName");
		    commonService.searchBoxPHItem();
	};
	

	$scope.saleUpdatePersonalProg=function(){
		
		
		
		$('.splash').css('display', '');
			
		var frmDatum = {"saleId":$scope.saledPacket.saleId
		   		  ,'progId':$scope.saledPacket.progId
		   		  ,'progType':'psp'
		   			,'userId':$scope.saledPacket.userId
			   	   ,'salesComment':$scope.saledPacket.salesComment
		   		  ,'salesDateStr':$scope.saledPacket.salesDateStr
		   		  ,'staffId':$scope.saledPacket.staffId
		   		  ,'progCount':$scope.saledPacket.progCount
		   		  ,'packetPrice':$scope.saledPacket.packetPrice
		   		,'saleStatu':$scope.saledPacket.saleStatu
			   	}; 
		
		
		$.ajax({
			  type:'POST',
			  url: "../pt/packetsale/saleUpdatePacket",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res!=null){
					if(res.resultStatu==1){
						toastr.success($translate.instant("success"));
						
						$scope.saledPacket.leftPrice=$scope.saledPacket.packetPrice-$scope.saledPacket.payAmount;						
						
						$scope.updatedSale($scope.saledPacket);
					}else{
						toastr.error($translate.instant(res.resultMessage));
					}
					
					
				}
					$('.splash').css('display', 'none');
					
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
	
	
	
	
	
	$scope.progCountChange=function(){
		$scope.saledPacket.packetPrice=$scope.programPersonal.progPrice*$scope.saledPacket.progCount ;
	}
	
	
	
	
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
				    
				    $("#salesDate").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
				    
				    findAllStaff();
				}
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
	}
	
	function findAllStaff(){
		$.ajax({
			  type:'POST',
			  url: "../pt/ptusers/findAll/0/"+globals.USER_TYPE_SCHEDULAR_STAFF,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.staffs=res;
				$scope.$apply();
				saledPacketFunction();
				
			});
	}
	
	function findProgram(progId){
		$.ajax({
			  type:'POST',
			  url: "../pt/program/findProgramByProgId/"+progId+"/"+globals.PROGRAM_PERSONAL,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.programPersonal=res;
				$scope.$apply();
			});
	}
	
	function saledPacketFunction(){
		$scope.saledPacket=$scope.upperPacketSale;
		
		$scope.saledPacket.staffId=""+$scope.saledPacket.staffId;
		
		findProgram($scope.upperPacketSale.progId);
		
	}
	
	
function loggedInUser(){
		
		$.ajax({
  		  type:'POST',
  		  url: "../pt/ptusers/getSessionUser",
  		  contentType: "application/json; charset=utf-8",				    
  		  dataType: 'json', 
  		  cache:false
  		}).done(function(res) {
  			$scope.userType=res.userType;
  			$scope.$apply();
  		
  		}).fail  (function(jqXHR, textStatus, errorThrown){ 
  			
  			
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
		});
	}
	
});
