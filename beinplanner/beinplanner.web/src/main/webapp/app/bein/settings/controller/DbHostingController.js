ptBossApp.controller('DbHostingController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService) {

	$scope.dbUrl;
	$scope.dbPort;
	$scope.dbUsername;
	$scope.dbPassword;
	
	
	toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	$scope.init = function(){
		$("[data-toggle=popover]").popover();
    	
		    commonService.pageName=$translate.instant("settings_dbHostTitle");
			commonService.pageComment=$translate.instant("settings_dbHostComment");
			commonService.normalHeaderVisible=false;
			commonService.setNormalHeader();
			$.ajax({
				  type:'POST',
				  url: "../pt/setting/findDbHost",
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					if(res!=null){
						$scope.dbUrl=res.dbUrl;
						$scope.dbPort=res.dbPort;
						$scope.dbUsername=res.dbUsername;
						$scope.dbPassword=res.dbPassword;
						$scope.$apply();
					}
				});
	}
	
	$scope.createDbHost =function(){
		var frmDatum = {'dbUrl':$scope.dbUrl,
				        'dbPort':$scope.dbPort,
				        'dbUsername':$scope.dbUsername,
				        'dbPassword':$scope.dbPassword
				        }; 
		
		   $.ajax({
			  type:'POST',
			  url: "../pt/setting/createDbHost",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					if(res.resultStatu=="1"){
						toastr.success($translate.instant(res.resultMessage.trim()));
					}else{
						toastr.error($translate.instant(res.resultMessage.trim()));
						findDbHost();
					}
				}
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	};
	
	function findDbHost(){
		$.ajax({
			  type:'POST',
			  url: "../pt/setting/findDbHost",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					$scope.dbUrl=res.dbUrl;
					$scope.dbPort=res.dbPort;
					$scope.dbUsername=res.dbUsername;
					$scope.dbPassword=res.dbPassword;
					$scope.$apply();
				}
			});
	}
	
	$scope.downloadFile=function(){
		$("#form2").submit();
		dataBackupStatu();
	}
	
	$scope.progressBackupValue=0;
	$scope.progressValue=0;
    $scope.fileName;
	
    
	$scope.sendFile=function(objId){
		$scope.progressValue=0;
		sendUploadFiles(objId);
	};
	
	$scope.fileOpen=function(){
		$("#dbFileUploadUrl").click();
	}
	
	
	$("#dbFileUploadUrl").bind("change",function(e){
		$scope.fileName=e.target.files[0].name;
		$scope.$apply();
	});
	
	function sendUploadFiles(objectId){
	    var url = "/beinplanner/pt/dbbackup/uploadDbBackup";
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
			request.open("POST", url+"?fileId="+fileId+"&fileName="+fileName);//+"&usrId="+usrRId+"&firmIdx="+$("#firmIdxF").val());
			//addExcelFile(fileName, fileId);
			request.send(formData);
		}
		
		window.setTimeout(function(){
			dataStatu();}, 800);
		
	}
	
    var locationChange=false;
	
	$rootScope.$on("$routeChangeStart", function (event, next, current) {
		locationChange=true;
	});
	
	function dataStatu(){
		if(!locationChange){
		$.ajax({
		  	  type:'POST',
		  	  url: "/beinplanner/pt/dbbackup/progressListener",
		  	  dataType: 'json', 
		  	  cache:true
		  	}).done(function(res) {
		  		if(res!=null){
		  			
		  			$scope.progressValue=res.loadedValue;
		  			
		  			if(res.resultStatu=="1"){
		  				$scope.$apply();
		  				window.setTimeout(function(){dataStatu();}, 100);
		  			}else{
		  				toastr.success($translate.instant("dbProcessDone"));
		  				$scope.progressValue=100;
		  				$scope.$apply();
		  			}
		  			
		  		}
		  	});
		}
	}
	
	
	function dataBackupStatu(){
		
		if(!locationChange){
		$.ajax({
		  	  type:'POST',
		  	  url: "/beinplanner/pt/dbbackup/downloadDbBackupStatu",
		  	  dataType: 'json', 
		  	  cache:true
		  	}).done(function(res) {
		  		if(res!=null){
		  			
		  			$scope.progressBackupValue=res.loadedValue;
		  			
		  			if(res.resultStatu=="2"){
		  				$scope.$apply();
		  				window.setTimeout(function(){dataBackupStatu();}, 100);
		  			}else{
		  				toastr.success($translate.instant("dbProcessDone"));
		  				$scope.progressBackupValue=100;
		  				$scope.$apply();
		  			}
		  			
		  		}
		  	});
		}
	}
	
	
});