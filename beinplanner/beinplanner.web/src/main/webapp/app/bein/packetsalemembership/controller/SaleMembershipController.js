ptBossApp.controller('SaleMembershipController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.programMemberships;
	$scope.programMembership;
	$scope.members;
	$scope.staffs;
	
	$scope.saledMembers=new Array();
	
	
	
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
	
	$scope.smpStartDateStr;
	
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
		 commonService.pageName=$translate.instant("saleMembershipPage");
			commonService.pageComment=$translate.instant("saleMembershipPageComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
		commonService.searchBoxPH=$translate.instant("searchByProgName");
		commonService.searchBoxPHItem();
	};
	

	$scope.saleMembershipProg=function(){
		
		if($scope.saledMembers.length==0){
			toastr.error($translate.instant("noMemberSelectedForSale"));
			return;
		}
		
		$('.splash').css('display', '');
			
		var frmDatum = {"saleId":$scope.saleId
		   		  ,'saledMembers':$scope.saledMembers
		   		  ,'progId':$scope.programMembership.progId
		   		  ,'progType':'psm'
		   		  ,'salesComment':$scope.salesComment
		   		  ,'salesDateStr':$scope.salesDateStr
		   		  ,'staffId':$scope.staffId
		   		  ,'packetPrice':$scope.packetPrice
		   		  ,'smpStartDateStr':$scope.smpStartDateStr
		   		  ,'mailSubject':$translate.instant("packetSaleSubject")
		   		  ,'mailContent':$translate.instant("packetSaleContent")
		   		   ,'progName':$scope.programMembership.progName
		   		  
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
						packetSale.progType="psm";
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
			  url: "../pt/packetsale/findPacketsBySaleIds/"+globals.PROGRAM_MEMBERSHIP,
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(packetSales),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				parameterService.init();
				parameterService.param1=res;
				$(location).attr("href","../bein/#/packetsale/salemembershipresult");
				$('.splash').css('display', 'none');
			});
	}
	
	
	
	
	$scope.chooseProgram=function(programMembership){
		$scope.programMembership=programMembership;
		
		$scope.packetPrice=$scope.programMembership.progPrice ;
		
		
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
		$scope.programMembership="";
		$scope.showConfirmSale=false;
		$scope.showCreateUser=false;
	}
	
	$scope.addNewUserToProgram=function(programMembership){
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
				    $("#smpStartDateStr").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
				    
				    findPrograms();
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
			});
	}
	
	function findPrograms(){
		$.ajax({
			  type:'POST',
			  url: "../pt/program/findAllPrograms/"+globals.PROGRAM_MEMBERSHIP,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res.length!=0){
					$scope.programMemberships=res;
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
