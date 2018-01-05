ptBossApp.controller('FindUserPacketPaymentController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.filterName="";
	$scope.filterSurname="";
	
	$scope.init = function(){
		parameterService.init();
		$('.animate-panel').animatePanel();
		
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		
		
	};
	
	
	$scope.$on("search",function(){
		$scope.search=commonService.search;
	});
	
	
	
	
	$scope.find =function(){
		parameterService.param1=$scope.filterName;
		parameterService.param2=$scope.filterSurname;
		$location.path("/packetpayment/payment/list");
	};
	
	$scope.search;
	
	$scope.members;
	
	$scope.getMember=function(member){
		parameterService.param1=member.userId;
		$location.path("/packetpayment/payment");
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
		
		var frmDatum = {"userName":parameterService.param1,
						"userSurname":parameterService.param2}; 
			   
			   
			   $.ajax({
				  type:'POST',
				  url: "../pt/member/findByUserNameAndSurname",
				  contentType: "application/json; charset=utf-8",				    
				  data: JSON.stringify(frmDatum),
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					
					//console.log(res.resultMessage);
					
					if(res!=null){
						
						$scope.members=res;
						$scope.$apply();
						$('.animate-panel').animatePanel();
						
						commonService.pageName=$translate.instant("memberListPage");
						commonService.pageComment=$translate.instant("memberListPageComment");
						commonService.normalHeaderVisible=true;
						commonService.setNormalHeader();
						
					}
					
					//$('#userListTable').footable();
					
				}).fail  (function(jqXHR, textStatus, errorThrown) 
				{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
				});
		
		
		parameterService.init();
		
	}
	
	
	
	
});