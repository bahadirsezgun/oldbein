ptBossApp.controller('UserTokenMembershipController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.ptCurrency;
	
	
	$scope.saleShow=true;
	$scope.paymentShow=false;
	$scope.bookingShow=false;
	$scope.userBookingPage="";
	$scope.userPacketPaymentPage="";
	
	$scope.packetPaymentType="ppm";
	$scope.mustPay=$translate.instant('mustPay');
	$scope.okPay=$translate.instant('okPay');
	$scope.ptDateFormat;
	$scope.ptLang;
	/**
	 *selected membership booking id
	 */
	$scope.smpId;
	
	
	$scope.$on("search",function(){
		$scope.search=commonService.search;
	});
	
	
	
	$scope.initUTMC = function(){
		commonService.pageName=$translate.instant("saleMembershipResultPage");
		commonService.pageComment=$translate.instant("saleMembershipResultPageComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		commonService.searchBoxPH=$translate.instant("searchBySurname");
		commonService.searchBoxPHItem();
		
		findGlobals();
	};
	
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
					$scope.ptLang=(res.ptLang).substring(0,2);
					$scope.$apply();
					$(".animate-panel").animatePanel();
					
				}
			});
	}
	
	$scope.packetDelete=function(packetSale){
		
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deletePacketComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
            	var frmDatum = {'saleId':packetSale.saleId
	      		   		,'progType':packetSale.progType
	      		   	   }; 
				$.ajax({
					  type:'POST',
					  url: "../pt/packetsale/deleteSalePacket",
					  contentType: "application/json; charset=utf-8",				    
					  data:JSON.stringify(frmDatum),
					  dataType: 'json', 
					  cache:false
					}).done(function(res) {
						
						if(res.resultStatu=="2"){
							toastr.error($translate.instant(res.resultMessage));
						}else{
							
							for(var i = $scope.packetSales.length - 1; i >= 0; i--) {
							    if($scope.packetSales[i].saleId === packetSale.saleId) {
							    	$scope.packetSales.splice(i, 1);
							    }
							}
							$scope.$apply();
							toastr.success($translate.instant("success"));
						}
						
					});
          }
      });
		
	}
	
	$scope.makePayment=function(packetSale){
		$scope.saleId=packetSale.saleId;
		$scope.packetSale=packetSale;
		
		$scope.saleShow=false;
		$scope.userPacketPaymentPage="./packetpayment/part_userpayments.html";
		$scope.paymentShow=true;
		
		$scope.userBookingPage="";
		$scope.bookingShow=false;
		
		
		
	}

	$scope.showBooking=function(packetSale){
		$scope.smpId=packetSale.smpId;
		$scope.packetSale=packetSale;
		
		$scope.saleShow=false;
		$scope.userPacketPaymentPage="";
		$scope.paymentShow=false;
		
		$scope.userBookingPage="./schedule/membership/booking.html";
		$scope.bookingShow=true;
		
	}
	
	$scope.turnToUp=function(){
		$scope.turnFromUp();
	}
	$scope.turnBackToSales=function(packetSale){
		$scope.userPacketPaymentPage="";
		$scope.saleShow=true;
		$scope.paymentShow=false;
		$scope.bookingShow=false;
		
	}

});
