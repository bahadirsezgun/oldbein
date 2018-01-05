ptBossApp.controller('PacketPaymentController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	
	
	$scope.ptLang="tr_TR";
	$scope.ptDateFormat="dd/MM/yyyy";
	$scope.ptCurrency;
	
	
	
	
	
	$scope.userPacketSalePage="./packetsale/part_usersales.html";
	$scope.userPacketPaymentPage="";
	
	$scope.showSale=true;
	$scope.noSaleFound=false;
	
	$scope.saleId;
	$scope.packetSale;
	$scope.packetPaymentType;
	
	
	$scope.backToSale=function(){
		$scope.userPacketSalePage="./packetsale/part_usersales.html";
		$scope.showSale=true;
		$scope.userPacketPaymentPage="";
		
	}
	
	$scope.backToSaleToNoFound=function(){
		$scope.userPacketSalePage="";
		$scope.showSale=false;
		$scope.userPacketPaymentPage="";
		$scope.noSaleFound=true;
	}
	
	$scope.selectSaleToPay=function(packetSale){
		$scope.packetSale=packetSale;
		$scope.saleId=$scope.packetSale.saleId;
		
		if($scope.packetSale.progType=="psc"){
        	$scope.packetPaymentType="ppc";
        }else if($scope.packetSale.progType=="psm"){
        	$scope.packetPaymentType="ppm";
        }else if($scope.packetSale.progType=="psp"){
        	$scope.packetPaymentType="ppp";
        }
		
		$scope.showSale=false;
		$scope.userPacketPaymentPage="./packetpayment/part_userpayments.html";
		$scope.userPacketSalePage="";
	}
	
	$scope.init=function(){
		findPtGlobals();
		
			
			
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
				    $scope.$apply();
				   // $("#payDate").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
				   // findUserBoughtPackets();
				    
				}
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
	}
	
	
	
});