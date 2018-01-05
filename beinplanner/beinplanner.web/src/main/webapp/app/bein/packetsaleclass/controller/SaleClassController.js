ptBossApp.controller('SaleClassController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.programClasss;
	$scope.programClass;
	$scope.members;
	$scope.staffs;
	
	$scope.saledMembers=new Array();
	
	
	$scope.disabled="disabled";
	
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
	
	
	$scope.createNewUser=function(){
		
		$scope.createUserPage="./member/createfast.html";
		$scope.showFindUser=false;
		$scope.showAddUser=false;
		$scope.showCreateUser=true;
	}
	
	
	$scope.activate=function(page){
		if(page=='user'){
			$scope.userProp=true;
		}else{
			$scope.userProp=false;
		}
	}
	
	$scope.$on("search",function(){
		$scope.search=commonService.search;
	});
	
	
	$scope.init = function(){
		$('.animate-panel').animatePanel();
		
		 $('.actions').tooltip({
		        selector: "[data-toggle=tooltip]"
		 });
		 
		 $("[data-toggle=popover]").popover();
		 
		 findPtGlobals();
		 loggedInUser();
		 commonService.pageName=$translate.instant("saleClassPage");
			commonService.pageComment=$translate.instant("saleClassPageComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
		commonService.searchBoxPH=$translate.instant("searchByProgName");
		commonService.searchBoxPHItem();
	};
	

	$scope.saleClassProg=function(){
		
		if($scope.saledMembers.length==0){
			toastr.error($translate.instant("noMemberSelectedForSale"));
			return;
		}
		
		$('.splash').css('display', '');
			
		var frmDatum = {"saleId":$scope.saleId
		   		  ,'saledMembers':$scope.saledMembers
		   		  ,'progId':$scope.programClass.progId
		   		  ,'progType':'psc'
		   		  ,'salesComment':$scope.salesComment
		   		  ,'salesDateStr':$scope.salesDateStr
		   		  ,'staffId':$scope.staffId
		   		  ,'progCount':$scope.progCount
		   		  ,'packetPrice':$scope.packetPrice
		   		  ,'mailSubject':$translate.instant("packetSaleSubject")
		   		  ,'mailContent':$translate.instant("packetSaleContent")
		   		 ,'progName':$scope.programClass.progName
		   		  
		   		}; 
		
		
		$.ajax({
			  type:'POST',
			  url: "../pt/packetsale/saleNewPacket",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res!=null){
					toastr.success($translate.instant("success"));
					
					
					var packetSales=new Array();
					$.each(res,function(i,data){
						var packetSale=new Object();
						packetSale.saleId=data.resultMessage.trim();
						packetSale.progType="psc";
						packetSales.push(packetSale);
					});
					
					getResult(packetSales);
					
					
				}
					$('.splash').css('display', 'none');
					
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
	
	
	function getResult(packetSales){
		
		
		
		$.ajax({
			  type:'POST',
			  url: "../pt/packetsale/findPacketsBySaleIds/"+globals.PROGRAM_CLASS,
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(packetSales),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				parameterService.init();
				parameterService.param1=res;
				$(location).attr("href","../bein/#/packetsale/saleclassresult");
				$('.splash').css('display', 'none');
			});
	}
	
	
	$scope.progCountChange=function(){
		$scope.packetPrice=$scope.programClass.progPrice*$scope.progCount ;
	}
	
	$scope.chooseProgram=function(programClass){
		$scope.programClass=programClass;
		
		$scope.progCount=$scope.programClass.progCount;
		$scope.packetPrice=$scope.programClass.progPrice*$scope.programClass.progCount ;
		
		
		$scope.showFindUser=true;
		$scope.showSalesAttributes=true;
		$scope.showAddUser=false;
		$scope.showChooseProgram=false;
		$scope.showSalesDetail=false;
		$scope.showConfirmSale=false;
		$scope.showCreateUser=false;
	}
	
	$scope.backToProgramFind=function(){
		$scope.showFindUser=false;
		$scope.showSalesAttributes=false;
		$scope.showAddUser=false;
		$scope.showChooseProgram=true;
		$scope.showSalesDetail=false;
		$scope.saleId=0;
		$scope.saledMembers=new Array();
		$scope.programClass="";
		$scope.showConfirmSale=false;
		$scope.showCreateUser=false;
	}
	
	$scope.addNewUserToProgram=function(programClass){
		$scope.showFindUser=true;
		$scope.showSalesAttributes=true;
		$scope.showAddUser=false;
		$scope.showChooseProgram=false;
		$scope.showSalesDetail=false;
		$scope.showConfirmSale=false;
		$scope.showCreateUser=false;
	}
	
	$scope.saleDetailUserToProgram=function(){
		$scope.showFindUser=false;
		$scope.showSalesAttributes=true;
		$scope.showAddUser=false;
		$scope.showChooseProgram=false;
		$scope.showSalesDetail=true;
		$scope.showConfirmSale=true;
		$scope.showCreateUser=false;
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
				    
				    findPrograms();
				    findAllStaff();
				}
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
	}
	
	$scope.setEnabled=function(){
		$scope.disabled="";
	};
	
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
			});
	}
	
	function findPrograms(){
		$.ajax({
			  type:'POST',
			  url: "../pt/program/findAllPrograms/"+globals.PROGRAM_CLASS,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res.length!=0){
					$scope.programClasss=res;
					$scope.noProgram=false;
				}else{
					$scope.noProgram=true;
				}
				
				$scope.$apply();
			});
	}
	
	$scope.findUser=function(){
		var frmDatum = {"userName":$scope.filterName,
				"userSurname":$scope.filterSurname,
				"userType":globals.USER_TYPE_MEMBER}; 
	   
	   
		commonService.search="";
		commonService.searchItem();
	   $.ajax({
		  type:'POST',
		  url: "../pt/ptusers/findByUserNameAndSurname",
		  contentType: "application/json; charset=utf-8",				    
		  data: JSON.stringify(frmDatum),
		  dataType: 'json', 
		  cache:false
		}).done(function(res) {
			
			//console.log(res.resultMessage);
			
			if(res!=null){
				
				$scope.members=res;
				$scope.showFindUser=false;
				
				$scope.showAddUser=true;
				commonService.searchBoxPH=$translate.instant("searchBySurname");
				commonService.searchBoxPHItem();
				
				$scope.$apply();
				
				
			}
			
			//$('#userListTable').footable();
			
		}).fail  (function(jqXHR, textStatus, errorThrown) 
		{ 
		  if(jqXHR.status == 404 || textStatus == 'error')	
			  $(location).attr("href","/beinplanner/lock.html");
		});
	}
	
	
	$scope.saleProgramToUser=function(member){
		
		var found=false;
		for(var i =0;i<$scope.saledMembers.length; i++){
			if ($scope.saledMembers[i].userId == member.userId) {
				found=true;
			}
		}
		
		if(!found){
			$scope.saledMembers.push(member);
			$scope.showAddedNewUser=true;
			toastr.success($translate.instant("memberAdded"));
		}else{
			toastr.error($translate.instant("memberAlreadyAdded"));
		}
	}
	
	
	$scope.removeProgramToUser=function(member){
		for(var i =0;i<$scope.saledMembers.length; i++){
			if ($scope.saledMembers[i].userId == member.userId) 
				$scope.saledMembers.splice(i, 1);
		}
		if($scope.saledMembers.length==0){
			$scope.showAddedNewUser=false;
		}
	}
	
	
	
		
	
});
