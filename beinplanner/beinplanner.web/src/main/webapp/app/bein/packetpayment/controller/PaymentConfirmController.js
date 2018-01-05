ptBossApp.controller('PaymentConfirmController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.confirmed=0;
	$scope.unConfirmed=0;
	
	$scope.confirms;
	$scope.showQuery=true;
	$scope.filterName="";
	$scope.filterSurname="";
	
	$scope.showDetail=false;
	$scope.ptCurrency;
	
	
	$scope.init=function(){
		$('.i-checks').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		findPtGlobals();
	}
	
	$('#confirmChk').on('ifChanged', function(event) {
		if(event.target.checked){
			$scope.confirmed=1;
		}else{
			$scope.confirmed=0;
		}
		$scope.$apply();
    });
	
	$('#unConfirmChk').on('ifChanged', function(event) {
		if(event.target.checked){
			$scope.unConfirmed=1;
		}else{
			$scope.unConfirmed=0;
		}
		$scope.$apply();
    });
	

	
	
	$scope.payConfirm =function(puc){
	
			
		 $.ajax({
			  type:'POST',
			  url: "../pt/packetpayment/updatePaymentToConfirm",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(puc),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				puc.payConfirm=res.payConfirm;
				$scope.$apply();
			});
		
		
		
	}
	
	$scope.find =function(){
		   
		  var frmDatum = {"userName":$scope.filterName
			,"userSurname":$scope.filterSurname
			,"confirmed":$scope.confirmed
			,"unConfirmed":$scope.unConfirmed
			}; 
		  
		   $.ajax({
			  type:'POST',
			  url: "../pt/packetpayment/findPaymentsToConfirm/",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
					$scope.confirms=res;
				    $scope.showQuery=false;
				    $scope.$apply();
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			})
	};
	
	$scope.packetPaymentDetails;
	
	$scope.closeDetail =function(){
		$scope.showDetail=false;
		$scope.packetPaymentDetails=new Array();
		$scope.packetPayment="";
	};
	
	$scope.packetPayment;
	
	$scope.findDetail =function(packetPayment){
		$scope.packetPayment=packetPayment;
		$.ajax({
			  type:'POST',
			  url: "../pt/packetpayment/findPacketPaymentByPayId/"+packetPayment.payId+"/"+packetPayment.type,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.packetPaymentDetails=res.packetPaymentDetailFactories;
				$scope.showDetail=true;
				$scope.$apply();
			});
		
	}
	
	
	
	$scope.query =function(){
		if($scope.showDetail){
			 $scope.showDetail=false;
			 $scope.packetPayment="";
		}else{
			$scope.showQuery=true;
			 $scope.showDetail=false;
			 $scope.packetPayment="";
		}
		 
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
					$scope.ptCurrency=res.ptCurrency;
				    $scope.$apply();
				}
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
	}
	
});