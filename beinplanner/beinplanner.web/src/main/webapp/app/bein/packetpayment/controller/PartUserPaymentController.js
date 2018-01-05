ptBossApp.controller('PartUserPaymentController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	
	$scope.payAmount;
	$scope.payDateStr;
	$scope.payConfirm=0;
	$scope.payType="0";
	$scope.packetPaymentDetails;
	$scope.payId=0;
	$scope.payComment="";
	$scope.packetPaymentType;
	
	$scope.onProgress=false;
	
	$scope.initPacketUserPayment=function(){
		
		$("[data-toggle=popover]").popover();
		
		if($scope.packetPaymentType=="ppm"){
			$scope.packetSale.progCount=1;
		}
		
		if($scope.packetSale.packetPaymentFactory!=null){
			$scope.payId=$scope.packetSale.packetPaymentFactory.payId;
			$scope.payAmount=$scope.packetSale.packetPrice;
		}else{
			$scope.payId=0;
			$scope.payAmount=$scope.packetSale.packetPrice;
		}
		findPaymentDetail($scope.saleId);
	};
	
	
	var findPaymentDetail=function(saleId){
		
		
		
		
		$.ajax({
			  type:'POST',
			  url: "../pt/packetpayment/findPacketPaymentBySaleId/"+$scope.saleId+"/"+$scope.packetPaymentType,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res!=null){
					
					
					var payAmount=res.payAmount;
					$scope.payId=res.payId;
					$scope.payAmount=$scope.packetSale.leftPrice;
					
					if(res.packetPaymentDetailFactories!=null){
						$scope.packetPaymentDetails=res.packetPaymentDetailFactories;
						
					}
					$scope.$apply();
				}else{
					$scope.payId=0;
					$scope.packetPaymentDetails={};
					$scope.$apply();
				}
				
				
			
				$("#payDate").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
			}).fail  (function(jqXHR, textStatus, errorThrown){ 
				  if(jqXHR.status == 404 || textStatus == 'error')	{
					  console.log("jqXHR.status "+jqXHR.status);
					  $(location).attr("href","/beinplanner/lock.html");
				  }else{
					    $scope.payId=0;
					    $scope.packetPaymentDetails={};
						$scope.$apply();
						$("#payDate").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
						  
				  }
				
				
			});
		
		
	}
	
	$scope.createPacketPayment=function(){
		
		if($scope.payType=="0"){
			toastr.error($translate.instant('noPayTypeSelected'));
			return;
		}
		
		$scope.onProgress=true;
		
		var frmDatum={	"saleId":$scope.saleId
						,"payId":$scope.payId
						,"type":$scope.packetPaymentType
						,"payAmount":$scope.payAmount
						,"payDateStr":$scope.payDateStr
						,"payConfirm":$scope.payConfirm
						,"payType":$scope.payType
						,"payComment":$scope.payComment
						,'mailSubject':$translate.instant("packetPaymentSubject")
				   		,'mailContent':$translate.instant("packetPaymentContent")
				   		,'userName':$scope.packetSale.userName
				   		,'userSurname':$scope.packetSale.userSurname
				   		,'progName':$scope.packetSale.progName
				   		  
					 }
		$.ajax({
			  type:'POST',
			  url: "../pt/packetpayment/createPacketPayment",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res.resultStatu==1){
					$scope.payId=res.resultMessage;
					$scope.packetSale.leftPrice=parseFloat($scope.packetSale.leftPrice)-parseFloat($scope.payAmount);
					
					findPaymentDetail($scope.saleId);
					toastr.success($translate.instant("success"));
				}else{
					toastr.success($translate.instant("fail"));
				}
				
			}).done(function() {
				$scope.onProgress=false;
				$scope.$apply();
			});
	};
	
	
	
	$scope.packetPaymentDetailDelete=function(payDetId,payId,payAmount){
		
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deletePacketPaymentDetailComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
            	$.ajax({
					  type:'POST',
					  url: "../pt/packetpayment/deletePacketPaymentDetail/"+payDetId+"/"+payId+"/"+$scope.packetPaymentType,
					  contentType: "application/json; charset=utf-8",				    
					  dataType: 'json', 
					  cache:false
					}).done(function(res) {
						
						if(res.resultStatu=="2"){
							toastr.error($translate.instant(res.resultMessage));
						}else{
							$scope.packetSale.leftPrice=$scope.packetSale.leftPrice+payAmount;
							findPaymentDetail($scope.saleId);
							toastr.success($translate.instant("success"));
						}
					});
             }
      });
		
	}
	

});