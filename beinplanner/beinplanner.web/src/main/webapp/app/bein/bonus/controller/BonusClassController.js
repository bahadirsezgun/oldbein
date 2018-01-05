ptBossApp.controller('BonusClassController', function($scope,$translate,homerService,commonService,globals) {
	$scope.firms;
	$scope.firmId;
	$scope.staffs;
	$scope.staff;
	
	$scope.ptCurrency;
	
	
	$scope.startDate;
	$scope.endDate;
	$scope.month="0";
	$scope.year;
	
	$scope.monthly=true;
	$scope.queryType=1;
	$scope.monthlyPayment=true;
	$scope.queryTypePayment=1;
	
	$scope.leftPaymentClass="hbgred";
	
	$scope.filter=true;
	$scope.payment=true;
	
	$scope.filterClass="";
	$scope.userBonusObj;
	$scope.rateLabel="";
	$scope.rateSuffix;
	
	$scope.payedAmount=0;
	
	$scope.bonusPayment=new Object();
	$scope.bonusPayment.bonId;
	$scope.bonusPayment.userId;
	$scope.bonusPayment.bonPaymentDateStr;
	$scope.bonusPayment.bonAmount;
	$scope.bonusPayment.bonComment;
	$scope.bonusPayment.bonMonth;
	$scope.bonusPayment.bonYear;
	$scope.bonusPayment.bonStartDateStr;
	$scope.bonusPayment.bonEndDateStr;
	$scope.bonusPayment.bonType="bpc";
	$scope.bonusPayment.bonQueryType=1;
	
	
	$scope.paymentDetailPage="";
	$scope.paymentDetail=false;
	
	$scope.bonusPaymentRule;
	
	$scope.bonusPayments=new Array();
	
	
	$scope.months=new Array({value:"0",name:$translate.instant("pleaseSelect")}
		    ,{value:"1",name:$translate.instant("january")}
		   ,{value:"2",name:$translate.instant("february")}
		   ,{value:"3",name:$translate.instant("march")}
		   ,{value:"4",name:$translate.instant("april")}
		   ,{value:"5",name:$translate.instant("may")}
		   ,{value:"6",name:$translate.instant("june")}
		   ,{value:"7",name:$translate.instant("july")}
		   ,{value:"8",name:$translate.instant("august")}
		   ,{value:"9",name:$translate.instant("september")}
		   ,{value:"10",name:$translate.instant("october")}
		   ,{value:"11",name:$translate.instant("november")}
		   ,{value:"12",name:$translate.instant("december")}
		   );
		
		
	$scope.years=new Array();
		
		
	$scope.init=function(){
		findPtGlobals();
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		
		var date=new Date();
		var year=date.getFullYear();
		for(var i=-10;i<10;i++){
			$scope.years.push(year+i);
		}
		$scope.year=year;
		
		$('#inQueryCheck').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		$('#inPaymentCheck').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
    	
	}
	
	
	
	$('#inQueryCheck').on('ifChanged', function(event) {
		if(event.target.checked){
			$scope.monthly=true;
			$scope.queryType=1;
		}else{
			$scope.monthly=false;
			$scope.queryType=2;
		}
		$scope.$apply();
    });
    
    
    $('#inPaymentCheck').on('ifChanged', function(event) {
		if(event.target.checked){
			$scope.monthlyPayment=true;
			$scope.bonusPayment.bonQueryType=1;
		}else{
			$scope.monthlyPayment=false;
			$scope.bonusPayment.bonQueryType=2;
		}
		$scope.$apply();
    });
	
    
    $scope.scheduleFactories;
    
    $scope.bonusValue=0;
    $scope.staffPayment=0;
    $scope.progressPayment=0;
    
    $scope.creditCardCommissionRate;
    $scope.creditCardCommissionRule;
    
    
    $scope.editBonusDetail=function(userBonusDetailObj){
    	$.ajax({
			  type:'POST',
			  url: "../pt/userBonusPayment/findUserPaymentDetail/"+globals.BONUS_TYPE_CLASS+"/"+userBonusDetailObj.schtId,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				console.log(res);
				$scope.scheduleFactories=res.scheduleFactories;
				$scope.bonusValue=userBonusDetailObj.bonusValue;
				$scope.progressPayment=0;
				$.each($scope.scheduleFactories,function(i,schf){
					var bonusUnitAmount= 0;
					var bonusValue=0;
					var staffPayment=0;
					  if(schf.packetPaymentFactory!=null){
						 bonusUnitAmount= schf.packetPaymentFactory.payAmount/schf.saleCount;
						 bonusValue=userBonusDetailObj.bonusValue;
						if($scope.userBonusObj.bonusType==globals.BONUS_IS_TYPE_RATE){
							staffPayment=bonusUnitAmount*bonusValue/100;
						}else if($scope.userBonusObj.bonusType==globals.BONUS_IS_TYPE_STATIC){
							staffPayment=userBonusDetailObj.staffPaymentAmount;
						}else if($scope.userBonusObj.bonusType==globals.BONUS_IS_TYPE_STATIC_RATE){
							staffPayment=bonusUnitAmount*bonusValue/100;
						}
						
						
						if($scope.creditCardCommissionRule==1){
							if(schf.packetPaymentFactory.payType==2){
								staffPayment=staffPayment*((100-$scope.creditCardCommissionRate)/100);
							}
						}
						
						
						if($scope.bonusPaymentRule==1){
							if(schf.packetPaymentFactory.payConfirm==1){
								$scope.progressPayment+=staffPayment;
							}
							
						}else{
							$scope.progressPayment+=staffPayment;
						}
						
						
						
						
						
						
						
					}else{
						
						schf.packetPaymentFactory=new Object();
						schf.packetPaymentFactory.payAmount=0;
						schf.packetPaymentFactory.payConfirm=0;
						
					}
					  
					$scope.scheduleFactories[i].staffPayment=staffPayment;
					
				});
				
				
				
				$scope.paymentDetailPage="./bonus/class/cbonusdetail.html";
				$scope.paymentDetail=true;
				$scope.$apply();
				
			});
    };
    
    
	
    
    
	$scope.showFilter=function(){
		
		if($scope.paymentDetail){
			$scope.paymentDetail=false;
		}else{
			if($scope.payment){
				$scope.filterClass="animated fadeInDown";
				$scope.filter=true;
			}else{
				$scope.payment=true;
			}
		}
	}
	
	$scope.leftPayment=0;
	
	$scope.queryBonusPayment=function(){
		
		$(".splash").css("display",'');
		
		var frmDatum={"queryType":$scope.queryType
					 ,"schStaffId":$scope.staff.userId
					 ,"startDateStr":$scope.startDate
					 ,"endDateStr":$scope.endDate
					 ,"month":$scope.month
					 ,'year':$scope.year}
		
		
		  $scope.monthlyPayment=$scope.monthly;
		

		  $scope.bonusPayment.bonQueryType=$scope.queryType;
		  $scope.bonusPayment.userId=$scope.staff.userId;
		  $scope.bonusPayment.bonMonth=$scope.month;
		  $scope.bonusPayment.bonYear=$scope.year;
		  $scope.bonusPayment.bonStartDateStr=$scope.startDate;
		  $scope.bonusPayment.bonEndDateStr=$scope.endDate;
		  
		  
		 $.ajax({
			  type:'POST',
			  url: "../pt/userBonusCalculator/findStaffBonus/"+globals.BONUS_TYPE_CLASS,
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			   dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.userBonusObj=res;
				$scope.payedAmount=$scope.userBonusObj.payedAmount;
				
				$scope.bonusPayments=$scope.userBonusObj.userBonusPaymentFactories;
				$scope.leftPayment=$scope.userBonusObj.willPayAmount-$scope.payedAmount;
				
				if($scope.leftPayment>0){
					$scope.leftPaymentClass="hbgred";
				}else{
					$scope.leftPaymentClass="hbggreen";
				}
				
				
				$scope.bonusPaymentRule=$scope.userBonusObj.bonusPaymentRule;
				
				  $scope.creditCardCommissionRate=$scope.userBonusObj.creditCardCommissionRate;
				  $scope.creditCardCommissionRule=$scope.userBonusObj.creditCardCommissionRule;
				  
				
				if($scope.userBonusObj.bonusType==globals.BONUS_IS_TYPE_RATE){
					$scope.rateLabel=$translate.instant("bonusRate");
					$scope.rateSuffix="%"
				}else if($scope.userBonusObj.bonusType==globals.BONUS_IS_TYPE_STATIC){
					$scope.rateLabel=$translate.instant("bonusStatic");
					$scope.rateSuffix=$scope.ptCurrency;
				}else if($scope.userBonusObj.bonusType==globals.BONUS_IS_TYPE_STATIC_RATE){
					$scope.rateLabel=$translate.instant("bonusRateStatic");
					$scope.rateSuffix="%"
				}
				
				
				$scope.filter=false;
				$scope.$apply();
				$(".splash").css("display",'none');
			}).fail  (function(jqXHR, textStatus, errorThrown){
				$(".splash").css("display",'none');
			});
	};
	
	
	
   $scope.searchBonusPayment=function(){
		
		$(".splash").css("display",'');
		
		var frmDatum={"queryType":$scope.queryType
					 ,"schStaffId":$scope.staff.userId
					 ,"startDateStr":$scope.startDate
					 ,"endDateStr":$scope.endDate
					 ,"month":$scope.month
					 ,'year':$scope.year}
		
		
		  $scope.monthlyPayment=$scope.monthly;
		

		  if($scope.bonusPayment.bonQueryType==$scope.queryType
				  && $scope.bonusPayment.bonMonth==$scope.month
				  && $scope.bonusPayment.bonStartDateStr==$scope.startDate
				  && $scope.bonusPayment.bonYear==$scope.year
				  && $scope.bonusPayment.bonEndDateStr==$scope.endDate
				  && $scope.bonusPayment.userId==$scope.staff.userId
				  ){
			  
			  $scope.payment=false;
			  $(".splash").css("display",'none');
			  
		  }else{
			
		  
		 $.ajax({
			  type:'POST',
			  url: "../pt/userBonusCalculator/findStaffBonus/"+globals.BONUS_TYPE_CLASS,
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			   dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.userBonusObj=res;
				$scope.payedAmount=$scope.userBonusObj.payedAmount;
				$scope.bonusPayments=$scope.userBonusObj.userBonusPaymentFactories;
				
				$scope.leftPayment=$scope.userBonusObj.willPayAmount-$scope.payedAmount;
				
				if($scope.leftPayment>0){
					$scope.leftPaymentClass="hbgred";
				}else{
					$scope.leftPaymentClass="hbggreen";
				}
				
				$scope.bonusPaymentRule=$scope.userBonusObj.bonusPaymentRule;
				$scope.creditCardCommissionRate=$scope.userBonusObj.creditCardCommissionRate;
				  $scope.creditCardCommissionRule=$scope.userBonusObj.creditCardCommissionRule;
				  
				
				if($scope.userBonusObj.bonusType==globals.BONUS_IS_TYPE_RATE){
					$scope.rateLabel=$translate.instant("bonusRate");
					$scope.rateSuffix="%"
				}else if($scope.userBonusObj.bonusType==globals.BONUS_IS_TYPE_STATIC){
					$scope.rateLabel=$translate.instant("bonusStatic");
					$scope.rateSuffix=$scope.ptCurrency;
				}else if($scope.userBonusObj.bonusType==globals.BONUS_IS_TYPE_STATIC_RATE){
					$scope.rateLabel=$translate.instant("bonusRateStatic");
					$scope.rateSuffix="%"
				}
				
				$scope.payment=false;
				$scope.filter=false;
				$scope.$apply();
				$(".splash").css("display",'none');
			}).fail  (function(jqXHR, textStatus, errorThrown){
				$(".splash").css("display",'none');
			});
		  }
	};
	
	
	
	 $scope.saveBonusPayment=function(){
		 $.ajax({
			  type:'POST',
			  url: "../pt/userBonusPayment/saveBonusPayment/"+globals.BONUS_TYPE_CLASS,
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify($scope.bonusPayment),
			   dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res.resultStatu==1){
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
				
				 $scope.findBonusPayment();
			});
	}
    

	 $scope.deleteBonusPayment=function(bonusPayment){
		 
		 
		 $.ajax({
			  type:'POST',
			  url: "../pt/userBonusPayment/deleteBonusPayment/"+globals.BONUS_TYPE_CLASS,
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(bonusPayment),
			   dataType: 'json', 
			  cache:false
			}).done(function(res) {
				 $scope.findBonusPayment();
			});
	}
    
	 $scope.findBonusPayment=function(){
		
		 var frmDatum={"queryType":$scope.bonusPayment.bonQueryType
				 ,"schStaffId":$scope.bonusPayment.userId
				 ,"startDateStr":$scope.bonusPayment.bonStartDateStr
				 ,"endDateStr":$scope.bonusPayment.bonEndDateStr
				 ,"month":$scope.bonusPayment.bonMonth
				 ,'year':$scope.bonusPayment.bonYear}
		 
		 $.ajax({
			  type:'POST',
			  url: "../pt/userBonusPayment/findStaffBonusPayment/"+globals.BONUS_TYPE_CLASS,
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			   dataType: 'json', 
			  cache:false
			}).done(function(res) {
				var totalPayment=0;
				if(res!=null){
					$.each(res,function(i,data){
						totalPayment+=data.bonAmount;
					});
					$scope.payedAmount=totalPayment;
					
					$scope.leftPayment=$scope.userBonusObj.willPayAmount-$scope.payedAmount;
					
					if($scope.leftPayment>0){
						$scope.leftPaymentClass="hbgred";
					}else{
						$scope.leftPaymentClass="hbggreen";
					}
					
				}
				$scope.bonusPayments=res;
				$scope.$apply();
			});
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
				  
				    $("#startDate").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
				    $("#endDate").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
			        
				    $("#bonPaymentDate").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
			        
				    $("#bonStartDate").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
				    $("#bonEndDate").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
			        
				    
				    findFirms();
				}
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
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
				$scope.firmId=$scope.firms[0].firmId;
				$scope.$apply();
				findStaff();
				
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
	
	function findStaff(){
	  	  $.ajax({
	  		  type:'POST',
	  		  url: "../pt/ptusers/findAll/"+$scope.firmId+"/"+globals.USER_TYPE_SCHEDULAR_STAFF,
	  		  contentType: "application/json; charset=utf-8",				    
	  		  dataType: 'json', 
	  		  cache:false
	  		}).done(function(res) {
	  					if(res!=null){
	  						$scope.staffs=res;
	  						$scope.$apply();
	  					}
	  				}).fail  (function(jqXHR, textStatus, errorThrown) 
	  				{ 
	  				  if(jqXHR.status == 404 || textStatus == 'error')	
	  					  $(location).attr("href","/beinplanner/lock.html");
	  				});
	  	}
	
	
	

});