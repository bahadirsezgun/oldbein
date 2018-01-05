ptBossApp.controller('StaffController', function($scope,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.member;
	
	$scope.packetSaled=true;
	$scope.firms;
	$scope.cities;
	$scope.states;
	$scope.ptLang;
	$scope.ptDateFormat;
	
	$scope.userId=0;
	$scope.userName="";
	$scope.userSurname="";
	$scope.password="0000";
	$scope.userBirthdayStr="";
	$scope.firmId;
	$scope.stateId;
	$scope.cityId;
	$scope.userAddress="";
	$scope.userPhone="";
	$scope.userGsm="";
	$scope.userEmail="";
	$scope.profileUrl="/beinplanner/homerlib/images/profile.png";
	$scope.userType="3";
	$scope.userSsn="";
	$scope.userGender="0";
	$scope.userComment="";
	$scope.staffId="0";
	$scope.staffStatu="0";
	$scope.userCompletePercent=0;
	$scope.staffs;
	$scope.avatars;
	$scope.showBonus=0;
	$scope.bonusTypeP="0";
	$scope.bonusTypeC="0";
	
	var bonusTypeP=0;
	var bonusTypeC=0;
	
	$scope.ptTz;
	$scope.ptCurrency;
	$scope.ptStaticIp;
	$scope.ptLang;
	$scope.ptDateFormat;
	
	var avatarMale=[{"url":"/beinplanner/homerlib/images/a1.PNG","value":"a1.PNG"},
	                {"url":"/beinplanner/homerlib/images/a11.PNG","value":"a11.PNG"},
	                {"url":"/beinplanner/homerlib/images/a12.PNG","value":"a12.PNG"},
	                {"url":"/beinplanner/homerlib/images/a13.PNG","value":"a13.PNG"},
	                {"url":"/beinplanner/homerlib/images/a14.PNG","value":"a14.PNG"},
	                {"url":"/beinplanner/homerlib/images/a15.PNG","value":"a15.PNG"},
	                {"url":"/beinplanner/homerlib/images/a16.PNG","value":"a16.PNG"},
	                {"url":"/beinplanner/homerlib/images/a18.PNG","value":"a18.PNG"},
	                {"url":"/beinplanner/homerlib/images/a26.PNG","value":"a26.PNG"},
	                {"url":"/beinplanner/homerlib/images/a27.PNG","value":"a27.PNG"},
	                {"url":"/beinplanner/homerlib/images/a28.PNG","value":"a28.PNG"},
	                {"url":"/beinplanner/homerlib/images/a29.PNG","value":"a29.PNG"},
	                {"url":"/beinplanner/homerlib/images/a30.PNG","value":"a30.PNG"},
	                {"url":"/beinplanner/homerlib/images/a31.PNG","value":"a31.PNG"},
	                {"url":"/beinplanner/homerlib/images/a32.PNG","value":"a32.PNG"},
	                {"url":"/beinplanner/homerlib/images/m1.jpg","value":"m1.jpg"},
	                {"url":"/beinplanner/homerlib/images/m2.jpg","value":"m2.jpg"},
	                {"url":"/beinplanner/homerlib/images/m3.jpg","value":"m3.jpg"}];
	
	
	var avatarFemale=[{"url":"/beinplanner/homerlib/images/a2.PNG","value":"a2.PNG"},
	                {"url":"/beinplanner/homerlib/images/a3.PNG","value":"a3.PNG"},
	                {"url":"/beinplanner/homerlib/images/a4.PNG","value":"a4.PNG"},
	                {"url":"/beinplanner/homerlib/images/a5.PNG","value":"a5.PNG"},
	                {"url":"/beinplanner/homerlib/images/a6.PNG","value":"a6.PNG"},
	                {"url":"/beinplanner/homerlib/images/a7.PNG","value":"a7.PNG"},
	                {"url":"/beinplanner/homerlib/images/a8.PNG","value":"a8.PNG"},
	                {"url":"/beinplanner/homerlib/images/a9.PNG","value":"a9.PNG"},
	                {"url":"/beinplanner/homerlib/images/a10.PNG","value":"a10.PNG"},
	                {"url":"/beinplanner/homerlib/images/a17.PNG","value":"a17.PNG"},
	                {"url":"/beinplanner/homerlib/images/a19.PNG","value":"a19.PNG"},
	                {"url":"/beinplanner/homerlib/images/a20.PNG","value":"a20.PNG"},
	                {"url":"/beinplanner/homerlib/images/a21.PNG","value":"a21.PNG"},
	                {"url":"/beinplanner/homerlib/images/a22.PNG","value":"a22.PNG"},
	                {"url":"/beinplanner/homerlib/images/a23.PNG","value":"23.PNG"},
	                {"url":"/beinplanner/homerlib/images/a24.PNG","value":"a24.PNG"},
	                {"url":"/beinplanner/homerlib/images/a25.PNG","value":"a25.PNG"},
	                {"url":"/beinplanner/homerlib/images/f1.jpg","value":"f1.jpg"},
	                {"url":"/beinplanner/homerlib/images/f2.jpg","value":"f2.jpg"},
	                {"url":"/beinplanner/homerlib/images/f3.jpg","value":"f3.jpg"},
	                {"url":"/beinplanner/homerlib/images/f4.jpg","value":"f4.jpg"},
	                {"url":"/beinplanner/homerlib/images/f5.jpg","value":"f5.jpg"},
	                {"url":"/beinplanner/homerlib/images/f6.jpg","value":"f6.jpg"},
	                {"url":"/beinplanner/homerlib/images/f7.jpg","value":"f7.jpg"}];
	
	function createAvatarUrlEvent(){
		$('.i-checks').on('ifChanged', function(event) {
			if(event.target.checked){
				$scope.profileUrl=event.target.value;
			   
			   var frmDatum = {"userId":$scope.userId,
						"profileUrl":$scope.profileUrl
						}; 
					  
					   $.ajax({
						  type:'POST',
						  url: "../pt/staff/createProfileUrl",
						  contentType: "application/json; charset=utf-8",				    
						  data: JSON.stringify(frmDatum),
						  dataType: 'json', 
						  cache:false
						}).done(function(res) {
							if(res.resultStatu=="1"){
								$scope.userId=parseInt(res.resultMessage);
								findUserById();
								toastr.success($translate.instant("success"));
								$scope.$apply();
							}else{
								toastr.error($translate.instant(res.resultMessage));
							}
							
							
						}).fail  (function(jqXHR, textStatus, errorThrown) 
						{ 
						  if(jqXHR.status == 404 || textStatus == 'error')	
							  $(location).attr("href","/beinplanner/lock.html");
						});
			   
			}
		});
	}
	
	$scope.newUser=0;
	
	
	$scope.init = function(){
		
		
		commonService.pageName=$translate.instant("staffCreatePage");
		commonService.pageComment=$translate.instant("staffCreatePageComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		
		
		if(parameterService.param1!=""){
			$scope.userId=parameterService.param1;
		    $scope.newUser=1;
		    
		}else{
			$scope.userId=0;
			$scope.newUser=0;
		}
	
		$scope.avatars=avatarFemale;
		
		findFirms();
		findGlobals();
		
		parameterService.init();
		
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
				$scope.ptTz=res.ptTz;
				$scope.ptCurrency=res.ptCurrency;
				$scope.ptStaticIp=res.ptStaticIp;
				$scope.ptLang=res.ptLang;
				$scope.ptDateFormat=res.ptDateFormat;
				if($scope.ptLang!=""){
					var lang=$scope.ptLang.substring(0,2);
					$translate.use(lang);
					$scope.ptLang=(res.ptLang).substring(0,2);
					$scope.ptDateFormat=res.ptScrDateFormat;
					
					$("#birthday").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat});
					
				}
			}
		});
	}
	
	$scope.bonusPage="";
	$scope.bonusClassPage="";
	
	$scope.bonusCTypeChange=function(){
		
		if(bonusTypeC!="0"){
			
			
			swal({
	            title: $translate.instant("areYouSureToChange"),
	            text: $translate.instant("changeBonusStateComment"),
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: $translate.instant("yes"),
	            cancelButtonText: $translate.instant("no"),
	            closeOnConfirm: true,
	            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	
	            
						  
						   $.ajax({
							  type:'POST',
							  url: "../pt/staff/setClassBonusType/"+$scope.userId+"/"+$scope.bonusTypeC,
							  contentType: "application/json; charset=utf-8",				    
							  dataType: 'json', 
							  cache:false
							}).done(function(res) {
								if(res.resultStatu=="1"){
									if($scope.bonusTypeC=="1"){
										$scope.bonusClassPage="./staff/bonus/classRateBonus.html";
									}else if($scope.bonusTypeC=="2"){
										$scope.bonusClassPage="./staff/bonus/classStaticBonus.html";
									}else if($scope.bonusTypeC=="3"){
										$scope.bonusClassPage="./staff/bonus/classStaticRateBonus.html";
									}
									toastr.success($translate.instant("success"));
									bonusTypeC=$scope.bonusTypeC;
									
									$scope.$apply();
								}else{
									toastr.error($translate.instant(res.resultMessage));
								}
								
								
							}).fail  (function(jqXHR, textStatus, errorThrown) 
							{ 
							  if(jqXHR.status == 404 || textStatus == 'error')	
								  $(location).attr("href","/beinplanner/lock.html");
							});
	            	
	            	
	            	
	            	
	            	
	            }else {
	            	$scope.bonusTypeC=bonusTypeC;
	               // swal($translate.instant("changeCanceled"), $translate.instant("changeBonusTypeCanceled"));
	            }
	            });
			
			
			}else{
				
				
				$.ajax({
					  type:'POST',
					  url: "../pt/staff/setClassBonusType/"+$scope.userId+"/"+$scope.bonusTypeC,
					  contentType: "application/json; charset=utf-8",				    
					  dataType: 'json', 
					  cache:false
					}).done(function(res) {
						if(res.resultStatu=="1"){
							
							
							if($scope.bonusTypeC=="1"){
								$scope.bonusClassPage="./staff/bonus/classRateBonus.html";
							}else if($scope.bonusTypeC=="2"){
								$scope.bonusClassPage="./staff/bonus/classStaticBonus.html";
							}else if($scope.bonusTypeC=="3"){
								$scope.bonusClassPage="./staff/bonus/classStaticRateBonus.html";
							}
							
							bonusTypeC=$scope.bonusTypeC;
							
							$scope.$apply();
						}else{
							toastr.error($translate.instant(res.resultMessage));
						}
						
						
					}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
					  if(jqXHR.status == 404 || textStatus == 'error')	
						  $(location).attr("href","/beinplanner/lock.html");
					})
					
					
				
				
			}
		
		
		
	}
	
	$scope.bonusCTapShow=function(){
	    if($scope.bonusTypeC=="1"){
			$scope.bonusClassPage="./staff/bonus/classRateBonus.html";
		}else if($scope.bonusTypeC=="2"){
			$scope.bonusClassPage="./staff/bonus/classStaticBonus.html";
		}else if($scope.bonusTypeC=="3"){
			$scope.bonusClassPage="./staff/bonus/classStaticRateBonus.html";
		}
	};
	
	
	$scope.bonusPTapShow=function(){
	    if($scope.bonusTypeP=="1"){
			$scope.bonusPage="./staff/bonus/personalRateBonus.html";
		}else if($scope.bonusTypeP=="2"){
			$scope.bonusPage="./staff/bonus/personalStaticBonus.html";
		}else if($scope.bonusTypeP=="3"){
			$scope.bonusPage="./staff/bonus/personalStaticRateBonus.html";
		}
	};
	
	
	
	$scope.bonusPTypeChange=function(){
		
		
		if(bonusTypeP!="0"){
		
		
		swal({
            title: $translate.instant("areYouSureToChange"),
            text: $translate.instant("changeBonusStateComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yes"),
            cancelButtonText: $translate.instant("no"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
            	
            
					  
					   $.ajax({
						  type:'POST',
						  url: "../pt/staff/setPersonalBonusType/"+$scope.userId+"/"+$scope.bonusTypeP,
						  contentType: "application/json; charset=utf-8",				    
						  dataType: 'json', 
						  cache:false
						}).done(function(res) {
							if(res.resultStatu=="1"){
								if($scope.bonusTypeP=="1"){
									$scope.bonusPage="./staff/bonus/personalRateBonus.html";
								}else if($scope.bonusTypeP=="2"){
									$scope.bonusPage="./staff/bonus/personalStaticBonus.html";
								}else if($scope.bonusTypeP=="3"){
									$scope.bonusPage="./staff/bonus/personalStaticRateBonus.html";
								}
								toastr.success($translate.instant("success"));
								bonusTypeP=$scope.bonusTypeP;
								
								$scope.$apply();
							}else{
								toastr.error($translate.instant(res.resultMessage));
							}
							
							
						}).fail  (function(jqXHR, textStatus, errorThrown) 
						{ 
						  if(jqXHR.status == 404 || textStatus == 'error')	
							  $(location).attr("href","/beinplanner/lock.html");
						});
            	
            	
            	
            	
            	
            }else {
            	$scope.bonusTypeP=bonusTypeP;
               // swal($translate.instant("changeCanceled"), $translate.instant("changeBonusTypeCanceled"));
            }
            });
		
		
		}else{
			
			
			$.ajax({
				  type:'POST',
				  url: "../pt/staff/setPersonalBonusType/"+$scope.userId+"/"+$scope.bonusTypeP,
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					if(res.resultStatu=="1"){
						
						
						if($scope.bonusTypeP=="1"){
							$scope.bonusPage="./staff/bonus/personalRateBonus.html";
						}else if($scope.bonusTypeP=="2"){
							$scope.bonusPage="./staff/bonus/personalStaticBonus.html";
						}else if($scope.bonusTypeP=="3"){
							$scope.bonusPage="./staff/bonus/personalStaticRateBonus.html";
						}
						
						bonusTypeP=$scope.bonusTypeP;
						
						$scope.$apply();
					}else{
						toastr.error($translate.instant(res.resultMessage));
					}
					
					
				}).fail  (function(jqXHR, textStatus, errorThrown) 
				{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
				})
				
				
			
			
		}
		
		
	}

	
	
	$scope.genderChange=function(){
		if($scope.userGender=="1"){
			$scope.avatars=avatarFemale;
		}else{
			$scope.avatars=avatarMale;
		}
		setTimeout(function(){
			$('.i-checks').iCheck({
		        checkboxClass: 'icheckbox_square-green',
		        radioClass: 'iradio_square-green'
		    });
			createAvatarUrlEvent();
		},1000);
		
	}
	
	
	$scope.stateChange=function(){
		findCities();
		
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
				
				findAllStaff();
				
				$('.i-checks').iCheck({
			        checkboxClass: 'icheckbox_square-green',
			        radioClass: 'iradio_square-green'
			    });
				createAvatarUrlEvent();
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
	
	function findStates(){
		$.ajax({
			  type:'POST',
			  url: "../pt/definition/state/findStates",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					$scope.states=res;
					$scope.$apply();
					
					
					if(res.length>0){
						if($scope.states!=null){
							
							$scope.stateId=$scope.states[0].stateId;
							$scope.initStateId=$scope.states[0].stateId;
							$scope.initStateName=$scope.states[0].stateName;
							$scope.states[0].selected=true;
							findCities();
						}
					}
					
				}else{
					toastr.error($translate.instant('noProcessDone'));
				}
				
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			});
	};
	
	function findCities(){
		   $.ajax({
			  type:'POST',
			  url: "../pt/definition/city/findCities/"+$scope.stateId,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.cities=res;
				if(res!=null){
					if(res.length>0){
					  $scope.cityId=$scope.cities[0].cityId;
					}else{
						$scope.cityId=0;
					}
				}
				if($scope.userId!=0){
					findUserById();
					
				}
				
				$scope.$apply();
			});
	};
	
	
	function findAllStaff(){
		$.ajax({
			  type:'POST',
			  url: "../pt/ptusers/findAll/"+$scope.firmId+"/0",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.staffs=res;
				
				$scope.$apply();
				findStates();
				
			});
	}
		
	
	
	
	
	function findUserById(){
		
		$.ajax({
			  type:'POST',
			  url: "../pt/ptusers/findById/"+$scope.userId,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					
					$scope.userSsn=res.userSsn;
					$scope.userName=res.userName;
					$scope.userSurname=res.userSurname;
					$scope.password=res.password;
					$scope.userBirthdayStr=res.userBirthdayStr;
					$scope.firmId=res.firmId;
					$scope.stateId=res.stateId;
					$scope.cityId=res.cityId;
					$scope.userAddress=res.userAddress;
					$scope.userPhone=res.userPhone;
					$scope.userGsm=res.userGsm;
					$scope.userEmail=res.userEmail;
					$scope.userType=""+res.userType;
					$scope.userId=res.userId;
					$scope.userGender=""+res.userGender;
					$scope.userComment=res.userComment;
					$scope.staffId=""+res.staffId;
					$scope.staffStatu=""+res.staffStatu;
					$scope.staffName=res.staffName;
					$scope.createTimeStr=res.createTimeStr;
					$scope.bonusTypeP=""+res.bonusTypeP;
					$scope.bonusTypeC=""+res.bonusTypeC;
					
					bonusTypeP=""+res.bonusTypeP;
					bonusTypeC=""+res.bonusTypeC;
					
					
					var defaultUrlPath="/beinplanner/homerlib/images/"+$scope.profileUrl;
					if(res.urlType==1){
						defaultUrlPath="../pt/member/get/profile/"+$scope.userId+"/"+(Math.random() * (1000 - 0));
					}else{
						if(res.profileUrl==null){
							defaultUrlPath="/beinplanner/homerlib/images/profile.png";
						}else{
							defaultUrlPath="/beinplanner/homerlib/images/"+res.profileUrl;
						}
					}
					
					if($scope.userType=="3"){
						$scope.showBonus=1;
					}else{
						$scope.showBonus=0;
					}
					
					if($scope.userGender=="1"){
						$scope.avatars=avatarFemale;
					}else{
						$scope.avatars=avatarMale;
					}
					setTimeout(function(){
						$('.i-checks').iCheck({
					        checkboxClass: 'icheckbox_square-green',
					        radioClass: 'iradio_square-green'
					    });
						createAvatarUrlEvent();
					},1000);
					
					$scope.profileUrl=defaultUrlPath
					$scope.$apply();
				}
			});
		
		
		
	}
	
	$scope.userTypeChange = function(){
		if($scope.userType=="3"){
			$scope.showBonus=1;
		}else{
			$scope.showBonus=0;
		}
	};
	
	
	
	
	
	function controlElements(){
		if($scope.userEmail==""){
			toastr.error($translate.instant('userEmailNotFound'));
			return false;
		}
		if($scope.userName==""){
			toastr.error($translate.instant('userNameNotFound'));
			return false;
		}
		if($scope.userSurname==""){
			toastr.error($translate.instant('userSurnameNotFound'));
			return false;
		}
		if($scope.userGender=="0"){
			toastr.error($translate.instant('userGenderNotSelected'));
			return false;
		}
		
		if($scope.userBirthdayStr==""){
			toastr.error($translate.instant('userBirtdateNotFound'));
			return false;
		}
		
		
		
		if($scope.userGsm==""){
			toastr.error($translate.instant('userGsmNotFound'));
			return false;
		}
		return true;
	}
	
	
	$scope.statuChange=function(){
		if($scope.userId!=0){
		 var statuUrl="../pt/ptusers/";
		 if($scope.staffStatu=="1"){
			 statuUrl+="setStaffToPassiveMode/"+$scope.userId;
		 }else{
			 statuUrl+="setStaffToActiveMode/"+$scope.userId;
		 }
		
		 $.ajax({
			  type:'POST',
			  url: statuUrl,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res.resultStatu=="1"){
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			})
		}
		
	}
	
	
	$scope.maskWorkPhone="(999) 999-9999 ext. 9999";
	$scope.maskGsm="(999) 999-9999";
	$scope.maskCurrency="(999) 999-9999";
	
	$scope.createStaff =function(){
		   if(controlElements()){
		   var frmDatum = {"userSsn":$scope.userSsn,
			"userName":$scope.userName,
			"userSurname":$scope.userSurname,
			"password":$scope.password,
			"userBirthdayStr":$scope.userBirthdayStr,
			"firmId":$scope.firmId,
		    "stateId":$scope.stateId,
		    "cityId":$scope.cityId,
		    "userAddress":$scope.userAddress,
			"userPhone":$scope.userPhone,
			"userGsm":$scope.userGsm,
			"userEmail":$scope.userEmail,
			"userType":$scope.userType,
			"userId":$scope.userId,
			"userGender":$scope.userGender,
			"userComment":$scope.userComment,
			"staffId":$scope.staffId,
			"staffStatu":$scope.staffStatu,
			"bonusTypeP":$scope.bonusTypeP,
			"bonusTypeC":$scope.bonusTypeC
			   
		   
		   }; 
		  
		   $.ajax({
			  type:'POST',
			  url: "../pt/ptusers/create",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res.resultStatu=="1"){
					$scope.userId=parseInt(res.resultMessage);
					findUserById();
					toastr.success($translate.instant("success"));
					$scope.$apply();
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			})
			
			
		}
	};
	
	
	$scope.deleteStaff=function(){
		
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteStaffComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yes"),
            cancelButtonText: $translate.instant("no"),
            closeOnConfirm: true,
            closeOnCancel: true },
            function (isConfirm) {
            	if (isConfirm) {
            	  
            		var frmDatum = {"userId":$scope.userId,
            				"userType":$scope.userType}
            		
            		$.ajax({
  					  type:'POST',
  					  url: "../pt/ptusers/delete",
  					  contentType: "application/json; charset=utf-8",				    
  					  data: JSON.stringify(frmDatum),
  					  dataType: 'json', 
  					  cache:false
  					}).done(function(res) {
  						if(res.resultStatu=="1"){
  							
  							$location.path("/staff/list");
  							
  						}else{
  							toastr.error($translate.instant(res.resultMessage));
  						}
  					});
            	}
            });
		
		
	};
	
	/***********************************************************************************/
	
	$scope.sendFile=function(objId){
		$scope.progressValue=0;
		
		sendFiles(objId);
		
	};
	
	$scope.fileName;
	
	$("#fileUploadUrl").bind("change",function(e){
		$scope.fileName=e.target.files[0].name;
		$scope.$apply();
	});
	
	$scope.fileOpen=function(){
		$("#fileUploadUrl").click();
	}
	
	
	function sendFiles(objectId){
	    var url = "../pt/member/uploadProfileFile";
	    $scope.progressValue=10;
	    var fileInputElement=document.getElementById(objectId);
		// HTML file input user's choice...
		//formData.append("fileId", fileInputElement.files[0].name+"-ali");
		var files = fileInputElement.files;
		
		for(var i=0;i<files.length;i++){
			var formData = new FormData();
			formData.append("userfile", files[i]);
			var fileId=Math.floor((Math.random() * 1000000) + 1)
			var fileName=files[i].name;
			var request = new XMLHttpRequest();
			request.open("POST", url+"?userId="+$scope.userId+"&fileName="+fileName);
			//addExcelFile(fileName, fileId);
			request.send(formData);
		}
		
		window.setTimeout(function(){
			findUserById();
			
		}, 1000);
		
	}
	
	
});
