ptBossApp.controller('UserAllFindNoLimit', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {
	
	$scope.filterName="";
	$scope.filterSurname="";
	
	$scope.noMember=false;
	
	$scope.search;
	
	$scope.members;
	$scope.selectedMember;
	$scope.packetPaymentPage="";
	$scope.makePayment=false;
	$scope.paymentProfileShow=true;
	
	$scope.firmId="0";
	
	$scope.init = function(){
		
		
		parameterService.init();
		$('.animate-panel').animatePanel();
		
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		
		
	};
	
	
	$scope.$on("search",function(){
		$scope.search=commonService.search;
	});
	
	$scope.hidePackets=function(){
		$scope.packetPaymentPage=""
		$scope.makePayment=false;
	}
	
	$scope.showPackets=function(member){
		$scope.selectedMember=member;
		$scope.packetPaymentPage="./packetpayment/payment.html"
		$scope.makePayment=true;
	}
	
	
	$scope.findKey=function(keyEvent){
		if (keyEvent.which === 13){
			$scope.find();
		}
	}
	
	$scope.find =function(){
		parameterService.param1=$scope.filterName;
		parameterService.param2=$scope.filterSurname;
		$location.path("/member/list");
	};
	
	
	$scope.getMember=function(member){
		parameterService.param1=member.userId;
		$location.path("/member/profile");
	}
	
	$rootScope.$on("$routeChangeStart", function (event, next, current) {
		commonService.searchBoxPH="";
		commonService.searchBoxPHItem();
	});
	
	
	$scope.deleteUser =function(user){
		
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteMemberComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
		
		var frmDatum = {"userId":user.userId,
				"userType":globals.USER_TYPE_MEMBER};
		
		$.ajax({
			  type:'POST',
			  url: "../pt/ptusers/delete",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res.resultStatu==1){
					toastr.success($translate.instant("success"));
					$scope.list();
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
				
				
			})
			.fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
					  if(jqXHR.status == 404 || textStatus == 'error')	
						  $(location).attr("href","/beinplanner/lock.html");
					});
            }
            });
	};
	
	
	$scope.initFind=function(){
		findFirms();
		findGlobals();
	}
	$scope.ptCurrency;
	$scope.ptDateFormat;
	
	function findGlobals(){
		$.ajax({
			  type:'POST',
			  url: "../pt/setting/findPtGlobal",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					$scope.ptCurrency=res.ptCurrency;
					$scope.ptDateFormat=res.ptScrDateFormat;
					
					$scope.$apply();
					//$(".animate-panel").animatePanel();
					
				}
				
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
				$scope.$apply();
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
	
	
	$scope.research=function(){
		$scope.noMember=false;
		$location.path("/member/find");
	}
	
	$scope.queryShow=false;
	//init method /member/list.html kullaniyor
	$scope.list = function(){
		
		commonService.searchBoxPH=$translate.instant("searchBySurname");
		commonService.searchBoxPHItem();
		
			   
			   $.ajax({
				  type:'POST',
				  url: "../pt/ptusers/findAllToMemberWithNoLimit/"+$scope.firmId,
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					
					//console.log(res.resultMessage);
					
					if(res!=null){
						
						$scope.members=res;
						
						$scope.queryShow=true;
					
						
						$scope.$apply();
						$('.animate-panel').animatePanel();
						
						commonService.pageName=$translate.instant("memberListPage");
						commonService.pageComment=$translate.instant("memberListPageComment");
						commonService.normalHeaderVisible=true;
						commonService.setNormalHeader();
						
					}else{
						$scope.noMember=true;
						$scope.$apply();
					}
					
					//$('#userListTable').footable();
					
				})
				.fail  (function(jqXHR, textStatus, errorThrown) 
				{ 
				  
					console.log(jqXHR);
					console.log(textStatus);
					console.log(errorThrown);
					
					
					
					/*
					if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
					  */
				});
		
		
		parameterService.init();
		
	}
	
	
	
	
});