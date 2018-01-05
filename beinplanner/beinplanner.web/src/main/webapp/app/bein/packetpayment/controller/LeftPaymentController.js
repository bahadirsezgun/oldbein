ptBossApp.controller('LeftPaymentController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.initLPC=function(){
		findPtGlobals();
	}
	
	$scope.leftPacketPayment;
	$scope.ptCurrency;
	$scope.paymentShow=false;
	$scope.packetSale;
	$scope.packetPaymentType;
	
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
				    $scope.leftPaymentDetail();
				    $scope.$apply();
				   
				    
				    
				}
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
	}
	
	$scope.editPayment=function(lpp){
		$scope.saleId=lpp.saleId;
		
		var progType=globals.PROGRAM_PERSONAL;
		if(lpp.progType=="psp"){
			progType=globals.PROGRAM_PERSONAL;
			$scope.packetPaymentType="ppp";
		}else if(lpp.progType=="psc"){
			progType=globals.PROGRAM_CLASS;
			$scope.packetPaymentType="ppc";
		}if(lpp.progType=="psm"){
			progType=globals.PROGRAM_MEMBERSHIP;
			$scope.packetPaymentType="ppm";
		}
		
		
		$.ajax({
			  type:'POST',
			  url: "../pt/packetsale/findSaledPacketsById/"+$scope.saleId+"/"+progType,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.packetSale=res;
				$scope.userPacketPaymentPage="./packetpayment/part_userpayments.html";
				$scope.paymentShow=true;
				$scope.$apply();
			});
		
		
		
		
		
		
	}
	
	
	
	
	$scope.leftPaymentDetail=function(){
		 
		
		$.ajax({
			  type:'POST',
			  url: "../pt/dashboard/leftPaymentDetail",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.paymentShow=false;
				$scope.userPacketPaymentPage="";
				$scope.leftPacketPayment=res;
				$scope.$apply();
			});
	}
	
});