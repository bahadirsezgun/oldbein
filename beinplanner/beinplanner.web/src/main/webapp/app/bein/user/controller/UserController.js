ptBossApp.controller('UserController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {
	
	$scope.member;
	
	$scope.packetSaled=true;
	$scope.firms;
	$scope.cities;
	$scope.states;
	$scope.ptLang;
	$scope.ptDateFormat;
	$scope.bonusType=0;
	
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
	$scope.userType=globals.USER_TYPE_MEMBER;
	$scope.userSsn="";
	$scope.userGender="0";
	$scope.userComment="";
	$scope.staffId="0";
	$scope.staffStatu=1;
	$scope.userCompletePercent=0;
	$scope.staffs;
	$scope.avatars;
	
	
	$scope.packetPaymentPage=""
	$scope.makePayment=false;
	$scope.paymentProfileShow=false;
	
	
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
	                {"url":"/beinplanner/homerlib/images/a23.PNG","value":"a23.PNG"},
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
						  url: "../pt/member/createProfileUrl",
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
		});
	}
	
	$scope.newUser=0;
	
	
	$scope.init = function(){
		
		
		commonService.pageName=$translate.instant("memberCreatePage");
		commonService.pageComment=$translate.instant("memberCreatePageComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		
		
		 if(parameterService.param1=="POT"){
			var potential=parameterService.param2;
			// $scope.firmId=""+potential.firmId;
			$scope.userName=potential.userName;
			$scope.userSurname=potential.userSurname;
			$scope.userGsm=potential.userGsm;
			$scope.userGender=""+potential.userGender;
			$scope.userEmail=""+potential.userEmail;
			$scope.newUser=0;
		    
		}else if(parameterService.param1!=""){
			$scope.userId=parameterService.param1;
		    $scope.newUser=1;
		    
		}else{
			$scope.userId=0;
			$scope.newUser=0;
		}
	
		$scope.avatars=avatarFemale;
		
		findFirms();
		
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
					$("#birthday").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat});
					
					
				}
			});
		
		parameterService.init();
		
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
				$scope.$apply();
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
	/*
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
					
					if($scope.states!=null){
						
						$scope.stateId=$scope.states[0].stateId;
						$scope.initStateId=$scope.states[0].stateId;
						$scope.initStateName=$scope.states[0].stateName;
						$scope.states[0].selected=true;
						findCities();
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
					$scope.cityId=$scope.cities[0].cityId;
				
				}
				if($scope.userId!=0){
					findUserById();
					
				}
				
				$scope.$apply();
			});
	};
	
	*/
	
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
			if($scope.userId!=0){
				findUserById();
				
			}
			//findStates();
		});
	}
		
	$scope.selectedMember;
	
	function findUserById(){
		
		$.ajax({
			  type:'POST',
			  url: "../pt/ptusers/findById/"+$scope.userId,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					$scope.selectedMember=res;
					
					$scope.packetPaymentPage="./packetpayment/payment.html"
					$scope.makePayment=true;
					
					
					$scope.userSsn=res.userSsn;
					$scope.userName=res.userName;
					$scope.userSurname=res.userSurname;
					$scope.password=res.password;
					$scope.userBirthdayStr=res.userBirthdayStr;
					$scope.userBirthday=new Date(res.userBirthday);
					
					
					$scope.firmId=res.firmId;
					$scope.stateId=res.stateId;
					$scope.cityId=res.cityId;
					$scope.userAddress=res.userAddress;
					$scope.userPhone=res.userPhone;
					$scope.userGsm=res.userGsm;
					$scope.userEmail=res.userEmail;
					$scope.userType=res.userType;
					$scope.userId=res.userId;
					$scope.userGender=""+res.userGender;
					$scope.userComment=res.userComment;
					$scope.staffId=""+res.staffId;
					$scope.staffStatu=""+res.staffStatu;
					$scope.staffName=res.staffName;
					$scope.createTimeStr=res.createTimeStr;
					
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
					
					
					$scope.profileUrl=defaultUrlPath
					
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
					
					$scope.$apply();
					$scope.$digest()
				}
			});
		
		
		
	}
	
	$scope.changeBirthday = function(){
		//$("#birthday").datepicker('close');
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
	
	
	
	
	
	$scope.maskWorkPhone="(999) 999-9999 ext. 9999";
	$scope.maskGsm="(999) 999-9999";
	
	
	
	$scope.createMemberFromMobile =function(){
				convertMobileToWeb();
		
	}
	
	function convertMobileToWeb(){
		$("#birthday").datepicker("setDate", $scope.userBirthday);
		$scope.createMember();
	}
	
	$scope.createMember =function(){
		  
		
		if(controlElements()){
		   var frmDatum = {"userSsn":$scope.userSsn,
			"userName":$scope.userName,
			"userSurname":$scope.userSurname,
			"password":$scope.password,
			"userBirthdayStr":$scope.userBirthdayStr,
			"firmId":$scope.firmId,
		   // "stateId":$scope.stateId,
		   // "cityId":$scope.cityId,
		    "stateId":0,
		    "cityId":0,
		    "userAddress":$scope.userAddress,
			"userPhone":$scope.userPhone,
			"userGsm":$scope.userGsm,
			"userEmail":$scope.userEmail,
			"userType":$scope.userType,
			"userId":$scope.userId,
			"userGender":$scope.userGender,
			"userComment":$scope.userComment,
			"staffId":$scope.staffId,
			"staffStatu":$scope.staffStatu
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
