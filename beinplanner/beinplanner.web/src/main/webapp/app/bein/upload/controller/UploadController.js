ptBossApp.controller('UploadController', function($scope,$translate,$rootScope,commonService) {
	
	$scope.init=function(){
		commonService.normalHeaderVisible=false;
		initExcel();
		findFirms();
	}
	
	
	
	
	$scope.progressValue=0;
	
	$scope.sendFile=function(objId){
		$scope.progressValue=0;
		
		sendExcelFiles(objId);
		
	};
	
	$scope.fileName;
	
	


	$("#excelFileUploadUrl").bind("change",function(e){
		$scope.fileName=e.target.files[0].name;
		$scope.$apply();
	});
	
	
	
	$scope.fileOpen=function(){
		$("#excelFileUploadUrl").click();
	}
	
	
	$scope.downloadFile=function(){
		$("#form2").submit();
	}
	
	
	function sendExcelFiles(objectId){
	    var url = "/beinplanner/pt/member/uploadMembers/"+$scope.firmId;
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
				
				
				$('.i-checks').iCheck({
			        checkboxClass: 'icheckbox_square-green',
			        radioClass: 'iradio_square-green'
			    });
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
	
	var locationChange=false;
	
	$rootScope.$on("$routeChangeStart", function (event, next, current) {
		locationChange=true;
	});
	
	function dataStatu(){
		if(!locationChange){
		$.ajax({
		  	  type:'POST',
		  	  url: "/beinplanner/pt/member/progressListener",
		  	  dataType: 'json', 
		  	  cache:true
		  	}).done(function(res) {
		  		if(res!=null){
		  			
		  			$scope.progressValue=res.loadedValue;
		  			$scope.$apply();
		  			if(res.resultStatu=="1"){
		  				window.setTimeout(function(){dataStatu();}, 100);
		  			}
		  			
		  		}
		  	});
		}
	}
	
	
	
	function initExcel(){
/*
	   var url = "/bein/ExcelFileUploadServlet?init=0";
	   if (window.XMLHttpRequest)        // Non-IE browsers
	   {
	      var req1 = new XMLHttpRequest();
	      try{
	         req1.open("POST", url, true);
	      }catch (e){
	            alert(e);
	      }
	      req1.send(null);
	   }
	   else if (window.ActiveXObject)    // IE Browsers
	   {
	      var req1 = new ActiveXObject("Microsoft.XMLHTTP");
	 
	      if (req1) 
	      {
	            req1.open("POST", url, true);
	            req1.send();
	      }
	   }
*/
	}
	
	
});