ptBossApp.controller('SaleClassResultController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {
	
	$scope.packetSales=new Array();
	$scope.ptCurrency;
	$scope.packetSale;
	$scope.saleId;
	$scope.saleShow=true;
	$scope.paymentPlaningShow=false;
	$scope.userPacketPaymentOrPlaningPage="";
	$scope.packetPaymentType="ppc";
	$scope.mustPay=$translate.instant('mustPay');
	$scope.okPay=$translate.instant('okPay');
	$scope.ptDateFormat;
	
	$scope.$on("search",function(){
		$scope.search=commonService.search;
	});
	
	
	
	
	$scope.init = function(){
		commonService.pageName=$translate.instant("saleClassResultPage");
		commonService.pageComment=$translate.instant("saleClassResultPageComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		commonService.searchBoxPH=$translate.instant("searchBySurname");
		commonService.searchBoxPHItem();
		
		
		findGlobals();
	};
	
	$scope.upperSaleId;
	$scope.upperUserId;	
	$scope.upperSchId=0;
	$scope.upperProgId;
	$scope.upperSchtStaffId=0;
	$scope.upperTime=0;
	$scope.upperDate=0;
	
	
$scope.planning=function(packetSale){
	
	$scope.upperSaleId=packetSale.saleId;
    $scope.upperUserId=packetSale.userId;
    $scope.upperProgId=packetSale.progId;
	
    $scope.saleShow=false;
	$scope.userPacketPaymentOrPlaningPage="./schedule/plan/class/booking.html";
	$scope.paymentPlaningShow=true;
    
	}

	$scope.upperPacketSale;

	$scope.planningUpdate=function(packetSale){
	
    
    $scope.upperPacketSale=packetSale;
    
    $scope.saleShow=false;
	$scope.userPacketPaymentOrPlaningPage="./packetsaleclass/saleclassupdate.html";
	$scope.paymentPlaningShow=true;
    
	
	}

	$scope.updatedSale=function(saledPacket){
		$.each($scope.packetSales,function(i,data){
			
			if(data.saleId==saledPacket.saleId){
				$scope.packetSales[i]=saledPacket;
				
			}
			
		});
		$scope.$apply();
		
	}
	
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
					$scope.packetSales=parameterService.param1;
					
					$scope.$apply();
					//$(".animate-panel").animatePanel();
					
				}
				parameterService.init();
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
		$scope.userPacketPaymentOrPlaningPage="./packetpayment/part_userpayments.html";
		$scope.paymentPlaningShow=true;
	}

	
	
	$scope.turnBackToSales=function(packetSale){
		$scope.userPacketPaymentOrPlaningPage="";
		$scope.saleShow=true;
		$scope.paymentPlaningShow=false;
	}
	
});
