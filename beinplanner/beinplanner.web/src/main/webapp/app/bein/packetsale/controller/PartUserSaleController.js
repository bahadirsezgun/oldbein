ptBossApp.controller('PartUserSaleController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.packetSales;
	$scope.bookingPage="";
	$scope.showBookingPage=false;
	
	$scope.saleIdforBooking;
	$scope.progTypeforBooking;
	$scope.packetSaleforBooking;
	
	$scope.upperSaleId=0;
	$scope.upperSchId=0;
	$scope.upperProgId;
	
	
	$scope.initPacketUserSale=function(){
		findUserBoughtPackets();
		
	}
	
	$scope.payForPacket=function(packetSale){
		$scope.selectSaleToPay(packetSale);
	}
	
	
	$scope.turnFromUp=function(){
		$scope.showBookingPage=false;
		$scope.bookingPage="";
	}
	
	$scope.bookingForPacket=function(packetSale){
		$scope.showBookingPage=true;
		$scope.saleIdforBooking=packetSale.saleId;
		$scope.packetSaleforBooking=packetSale;
		
		$scope.upperSaleId=packetSale.saleId;
		$scope.upperSchId=0;
		$scope.upperProgId=packetSale.progId;
		
		
		if(packetSale.progType=="psp"){
			$scope.progTypeforBooking=1;
			$scope.bookingPage="./schedule/plan/personal/booking.html";
		}else if(packetSale.progType=="psc"){
			$scope.progTypeforBooking=2;
			$scope.bookingPage="./schedule/plan/class/booking.html";
		}else if(packetSale.progType=="psm"){
			$scope.showBookingPage=true;
			$scope.bookingPage="./schedule/detail/user_token_membership.html";
		}
		
		
	}
	
	
	
	function findUserBoughtPackets(){
		
		
		 $.ajax({
			  type:'POST',
			  url: "../pt/packetsale/findAllUserBoughtPackets/"+$scope.selectedMember.userId,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
			  if(res!=null){
				  
				  if(res.length==0){
					  $scope.backToSaleToNoFound();
					  $scope.$apply();
				  }else{
					  $scope.packetSales=res;
					  $scope.$apply(); 
				  }
				  
				 
			  }else{
				  $scope.backToSaleToNoFound();
				  $scope.$apply(); 
			  }
				
				
				
			});
		
	}
	
});