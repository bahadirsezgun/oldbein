ptBossApp.controller('RulesController', function($scope,$translate,parameterService,$location,homerService,commonService) {

	
	toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	$scope.isCreditCardCommission=false;
	$scope.creditCardCommissionRate=0;
	
	
	
	function createICheckEvents(){
	
		
	
	
			
		
	$('#ruleNoClassBeforePayment').on('ifChanged', function(event) {
	   
	    var ruleV=0;
	    if(event.target.checked)
	    	ruleV=1;
	    
	    
		var frmDatum={'ruleId':1,'ruleValue':ruleV,'ruleName':'noClassBeforePayment'};
	    
		   $.ajax({
			  type:'POST',
			  url: "../pt/setting/createRule",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				toastr.success($translate.instant('success'));
			});
	    
	    
	});
	
	$('#ruleNoChangeAfterBonusPayment').on('ifChanged', function(event) {
	    
	    var ruleV=0;
	    if(event.target.checked)
	    	ruleV=1;
	    
	    
		var frmDatum={'ruleId':2,'ruleValue':ruleV,'ruleName':'noChangeAfterBonusPayment'};
	    
		   $.ajax({
			  type:'POST',
			  url: "../pt/setting/createRule",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				toastr.success($translate.instant('success'));
			});
	    
	});
	 
	
	
	$('#rulePayBonusForConfirmedPayment').on('ifChanged', function(event) {
		var ruleV=0;
	    if(event.target.checked)
	    	ruleV=1;
	    
	    
		var frmDatum={'ruleId':3,'ruleValue':ruleV,'ruleName':'payBonusForConfirmedPayment'};
	    
		   $.ajax({
			  type:'POST',
			  url: "../pt/setting/createRule",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				toastr.success($translate.instant('success'));
			});
	});
	/*
	$('#ruleTaxRule').on('ifChanged', function(event) {
	   
	    var ruleV=0;
	    if(event.target.checked)
	    	ruleV=1;
	    
	    
		var frmDatum={'ruleId':4,'ruleValue':ruleV,'ruleName':'taxRule'};
	    
		   $.ajax({
			  type:'POST',
			  url: "../pt/setting/createRule",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				toastr.success($translate.instant('success'));
			});
	    
	});
	*/
	
	$('#ruleLocation').on('ifChanged', function(event) {
		   
	    var ruleV=0;
	    if(event.target.checked)
	    	ruleV=1;
	    
	    
		var frmDatum={'ruleId':5,'ruleValue':ruleV,'ruleName':'location'};
	    
		   $.ajax({
			  type:'POST',
			  url: "../pt/setting/createRule",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				toastr.success($translate.instant('success'));
			});
	    
	});
	
	$('#noticeRule').on('ifChanged', function(event) {
		   
	    var ruleV=0;
	    if(event.target.checked)
	    	ruleV=1;
	    
	    
		var frmDatum={'ruleId':6,'ruleValue':ruleV,'ruleName':'notice'};
	    
		   $.ajax({
			  type:'POST',
			  url: "../pt/setting/createRule",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				toastr.success($translate.instant('success'));
			});
	    
	});
	
	
	

	$('#creditCardCommissionRule').on('ifChanged', function(event) {
		   
	    var ruleV=0;
	    if(event.target.checked){
	    	$scope.isCreditCardCommission=true;
	    	ruleV=1;
	    }else{
	    	$scope.isCreditCardCommission=false;
	    }
	    	
	    
	    
		var frmDatum={'ruleId':7,'ruleValue':ruleV,'ruleName':'creditCardCommission'};
	    
		   $.ajax({
			  type:'POST',
			  url: "../pt/setting/createRule",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				toastr.success($translate.instant('success'));
				$scope.$apply();
			});
	    
	});
	
	
	
	$('#ruleNoSaleToPlanning').on('ifChanged', function(event) {
		var ruleV=0;
	    if(event.target.checked)
	    	ruleV=1;
	    
	    
		var frmDatum={'ruleId':9,'ruleValue':ruleV,'ruleName':'noSaleToPlanning'};
	    
		   $.ajax({
			  type:'POST',
			  url: "../pt/setting/createRule",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				toastr.success($translate.instant('success'));
			});
	});
	
	
	}
	
	
	
	$scope.saveCreditCardCommissionRate=function(){
		
		
		var frmDatum={'ruleId':8,'ruleValue':$scope.creditCardCommissionRate,'ruleName':'creditCardCommissionRate'};
	    
		   $.ajax({
			  type:'POST',
			  url: "../pt/setting/createRule",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				toastr.success($translate.instant('success'));
			});
	}
	
	$scope.init=function(){
		$('#ruleNoClassBeforePayment').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		$('#ruleNoChangeAfterBonusPayment').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		$('#rulePayBonusForConfirmedPayment').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		/*
		$('#ruleTaxRule').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		*/
		$('#ruleLocation').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		$('#noticeRule').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		$('#ruleNoSaleToPlanning').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		
		$('#creditCardCommissionRule').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		
		$.ajax({
			  type:'POST',
			  url: "../pt/setting/findRules",
			  contentType: "application/json; charset=utf-8",				    
			  //data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				
			$.each(res,function(i,data){
				
			
				if(data.ruleId==1){
					if(data.ruleValue==0){
						$("#ruleNoClassBeforePayment").iCheck('uncheck');
					}else{
						$("#ruleNoClassBeforePayment").iCheck('check');
					}
				}else if(data.ruleId==2){
					if(data.ruleValue==0){
						$("#ruleNoChangeAfterBonusPayment").iCheck('uncheck');
					}else{
						$("#ruleNoChangeAfterBonusPayment").iCheck('check');
					}
					
				}else if(data.ruleId==3){
					
					if(data.ruleValue==0){
						$("#rulePayBonusForConfirmedPayment").iCheck('uncheck');
					}else{
						$("#rulePayBonusForConfirmedPayment").iCheck('check');
					}
					
				/*}else if(data.ruleId==4){
				   if(data.ruleValue==0){
						$("#ruleTaxRule").iCheck('uncheck');
					}else{
						$("#ruleTaxRule").iCheck('check');
					}*/
				}else if(data.ruleId==5){
					   if(data.ruleValue==0){
							$("#ruleLocation").iCheck('uncheck');
						}else{
							$("#ruleLocation").iCheck('check');
						}
				}else if(data.ruleId==6){
					   if(data.ruleValue==0){
							$("#noticeRule").iCheck('uncheck');
						}else{
							$("#noticeRule").iCheck('check');
						}
				}else if(data.ruleId==7){
					   if(data.ruleValue==0){
							$("#creditCardCommissionRule").iCheck('uncheck');
							$scope.isCreditCardCommission=false;
						}else{
							$("#creditCardCommissionRule").iCheck('check');
							$scope.isCreditCardCommission=true;
						}
				}else if(data.ruleId==8){
					$scope.creditCardCommissionRate=data.ruleValue;
					$scope.$apply();
				}else if(data.ruleId==9){
					 if(data.ruleValue==0){
							$("#ruleNoSaleToPlanning").iCheck('uncheck');
						}else{
							$("#ruleNoSaleToPlanning").iCheck('check');
						}
				}
				
			});	
				createICheckEvents();
				
			});
		
		
	}

});