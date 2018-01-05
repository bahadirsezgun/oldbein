ptBossLoginApp.controller('LoginController', function($scope,$translate) {
	
	
	$scope.version;
	$scope.logosRight=new Array();
	$scope.logosLeft=new Array();
	
	
	var user=new Object();
	user.userName=localStorage.getItem('username');
 
	if(user.userName==null){
		user.userName="";
	}
	
	user.password="";
	
	
	   toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	
	var days = ["sunday","monday","tuesday","wednesday","thursday","friday","saturday"];
	var monthNames = ["january", "february", "march", "april", "may", "june","july", "august", "september", "october", "november", "december"];
	
	var d=new Date();
	$scope.time=d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
	$scope.date=$translate.instant(days[d.getDay()])+", "+$translate.instant(monthNames[d.getMonth()+1])+" "+(d.getMonth()+1)+","+d.getFullYear();
	//Friday, February 27, 2015
	   
	$scope.user=user;
	
	$scope.userName_PlaceHolder="enterUserName";
	$scope.password_PlaceHolder="enterPassword";
	
	$scope.init=function(){
		readMyXMLForBrand("https://s3-us-west-2.amazonaws.com/beinplanner/marketBein.json");
		findGlobals();
	};
	
	function findGlobals(){
    	$.ajax({
    		  type:'POST',
    		  url: "./pt/setting/findPtGlobal",
    		  contentType: "application/json; charset=utf-8",				    
    		  dataType: 'json', 
    		  cache:false
    		}).done(function(res) {
    			if(res!=null){
    				$scope.ptTz=res.ptTz;
    				$scope.ptCurrency=res.ptCurrency;
    				$scope.ptStaticIp=res.ptStaticIp;
    				$scope.ptLang=(res.ptLang).substring(0,2);
    				$scope.ptDateFormat=res.ptScrDateFormat;
    				$translate.use($scope.ptLang);
    				$translate.refresh;
    			
    			}else{
    				$scope.ptLang = navigator.language || navigator.userLanguage; 
    				$scope.ptLang = ($scope.ptLang).substring(0,2);
    				$translate.use(ptLang);
    				$translate.refresh;
    				
    			}
    			
    			$scope.time=d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
    			$scope.date=$translate.instant(days[d.getDay()])+", "+$translate.instant(monthNames[d.getMonth()+1])+" "+(d.getMonth()+1)+","+d.getFullYear();
    			$scope.$apply();
    		
    		}).fail  (function(jqXHR, textStatus, errorThrown){ 
  			 
    			$scope.ptLang = navigator.language || navigator.userLanguage; 
				$scope.ptLang = ($scope.ptLang).substring(0,2);
				
				$translate.use($scope.ptLang);
				$translate.refresh;
				$scope.time=d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
    			$scope.date=$translate.instant(days[d.getDay()])+", "+$translate.instant(monthNames[d.getMonth()+1])+" "+(d.getMonth()+1)+","+d.getFullYear();
    			$scope.$apply();
  			});
    	};
	
    	
    	$scope.loginKey=function(keyEvent){
    		if (keyEvent.which === 13){
    			$scope.login();
    		}
    	}
    	
    
  $scope.campainCode;
    	
    	
   $scope.campain=function(){
	  if($scope.user.userName==""){
		  toastr.error($translate.instant('userNameNotFound'));
		  return;
	  }else if($scope.user.password==""){
		  toastr.error($translate.instant('passwordMustNotBeEmpty'));
		  return;
	  }
	   
	   $.ajax({
			  type:'POST',
			  url: "./pt/login/campain/"+$scope.user.userName+"/"+$scope.user.password+"/"+$scope.campainCode,
			  contentType: "application/json; charset=utf-8",				    
			 // data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res.resultStatu==1){
				toastr.success($translate.instant(res.resultMessage));
			   }else{
				   toastr.error($translate.instant(res.resultMessage));	
				}
			});
   }
    	
	$scope.login=function(){
		$.ajax({
			  type:'POST',
			  url: "./pt/login/loginUser/"+$scope.user.userName+"/"+$scope.user.password,
			  contentType: "application/json; charset=utf-8",				    
			 // data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res.resultMessage=="LOGIN_SUCCESS"){
					$(location).attr("href","./bein/#dashboard?"+$scope.version);
					localStorage.setItem('username', user.userName);

					
				}else{
					$scope.resultMessage=res.resultMessage;
					toastr.error($translate.instant(res.resultMessage));
				}
			});
	};
	
	
	$scope.reset=function(){
		
		if($scope.user.userName==""){
			toastr.error($translate.instant("noUserEntered"));
			return;
		}
		
		$.ajax({
			  type:'POST',
			  url: "./pt/ptusers/resetPassword/"+$scope.user.userName,
			  contentType: "application/json; charset=utf-8",				    
			 // data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res.resultStatu==1){
					toastr.success($translate.instant(res.resultMessage));
				}else{
					 toastr.error($translate.instant(res.resultMessage));  
				  }
			});
		
	}
	
	
	function readMyXMLForBrand(xmlName){
		$.ajax({
		type:'POST', 
		crossDomain: true,
		jsonpCallback: 'jsonCallback',
	    url: xmlName, // name of file you want to parse
	    dataType: 'jsonp',
	    success: function(json) {
	    	$(json.market).each(function(i,data){
	  		  var xmlPageId=data.pageId;
	  		  if(xmlPageId==1){
	  			   var version=data.title;
	  			   $scope.version=version;
	  			   $scope.$apply();
	  			 
	               if(version!=null){
	            	   sessionStorage.removeItem('updateObj');
	            	   var frmDatum = {"version":version}; 
	            	   $.ajax({
	     				  type:'POST',
	     				  url: "./pt/update/controlUpdate",
	     				  contentType: "application/json; charset=utf-8",
	     				  data: JSON.stringify(frmDatum),
		     			  dataType: 'json', 
	     				  cache:false
	     				}).done(function(res) {
	     					if(res.updVer!=version){
	     						updateInformation(json,version);
	     					}
	     					getBrands(json);
	     				});
	            	}
	  			}
	  		});
	     },
	     error: function(e) {
	        console.log(e.message);
	     }
	  });
 	}
	
	function getBrands(json){
		
		$(json.logosLeft).each(function(i,data){
			var logo=new Object();
			logo.src=data.src;
			logo.url=data.url;
			logo.title=data.title;
			
			
			$scope.logosLeft.push(logo);
			
		});
		
		$(json.logosRight).each(function(i,data){
			var logo=new Object();
			logo.src=data.src;
			logo.url=data.url;
			logo.title=data.title;
			
			
			$scope.logosRight.push(logo);
			
		});
		
		
		$scope.$apply();
		
		
	}
	
	function updateInformation(json,version){
		 
		  $(json.update).each(function(i,data){
		    			var updateObj = new Object();
							updateObj.updateComment =  data.comment;
							updateObj.updateTitle =  data.title;
							updateObj.updateSubTitle =  data.subTitle;
							updateObj.version=version;
							
				    	var memberfilter = new Array();
							memberfilter[0] = 'updateComment';
							memberfilter[1] = 'updateTitle';
							memberfilter[2] = 'updateSubTitle';
							memberfilter[3] = 'version';
							var updateObjToJson = JSON.stringify(updateObj, memberfilter,"\t");
							
							sessionStorage.setItem('updateObj',updateObjToJson);
		    	});
		    	
		    	
		    	var updateSqlObj = new Object();
	    		var k=0;
		    	$(json.updateSql).each(function(i,data){
		    		updateSqlObj.version =  data.version;
		    		if(updateSqlObj.version==version){
		    			if(k==0)
		    			    updateSqlObj.sql=data.sql+"#"+data.table+"#"+data.col;
		    			else
		    				updateSqlObj.sql+="@"+data.sql+"#"+data.table+"#"+data.col;
		    			
		    			k++;
		    		}
		    		
		    		var memberfilter = new Array();
					memberfilter[0] = 'version';
					memberfilter[1] = 'sql';
					
					var updateSqlObjToJson = JSON.stringify(updateSqlObj, memberfilter,"\t");
					sessionStorage.setItem('updateSqlObj',updateSqlObjToJson);
		    		
		    	});
		    
	}
	
	
	
});