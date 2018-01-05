ptBossApp.controller('DefinitionLevelInfoController', function($scope,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.levelName;
	$scope.levelId;
	$scope.levelDetail=false;
	$scope.addLevelInfoName;
	
	/**TANIMLI SYUDYOLAR*/
	$scope.levelInfos;
	$scope.levelInfo;
	
	$scope.noLevels=true;
	
	toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	$scope.init = function(){
		
		findlevelInfos();
		$scope.levelDetail=false;
			commonService.pageName=$translate.instant("definition_studio");
			commonService.pageComment=$translate.instant("studioDefinitionComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
			
		   
	}
	
	
	
	function findlevelInfos(){
		$.ajax({
			  type:'POST',
			  url: "../pt/definition/level/findLevel",
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				$scope.levelInfos=res;
				$scope.$apply();
			});
		
	}
	
	
	$scope.addNewLevelInfo =function(){
		$scope.levelName="";
		$scope.levelId=0;
		
	};
	
	
	$scope.showLevelInfo =function(levelId){
		
		
		   $.ajax({
			  type:'POST',
			  url: "../pt/definition/level/findLevelById/"+levelId,
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					$scope.levelDetail=true;
					$scope.levelId=res.levelId;
					$scope.levelName=res.levelName;
					
					$scope.$apply();
					
				}else{
					toastr.error($translate.instant('noProcessDone'));
				}
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			});
		
	};
	
	
	
	$scope.createLevelInfo =function(){
		var frmDatum = {'levelName':$scope.addLevelInfoName}; 
		   $.ajax({
			  type:'POST',
			  url: "../pt/definition/level/create",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res!=null){
					findlevelInfos();
					toastr.success($translate.instant('success'));
					
				}else{
					toastr.error($translate.instant('noProcessDone'));
				}
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
	
	$scope.updateLevelInfo =function(){
		var frmDatum = {'levelName':$scope.levelName,'levelId':$scope.levelId}; 
		$.ajax({
			  type:'POST',
			  url: "../pt/definition/level/create",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res!=null){
					findlevelInfos();
					toastr.success($translate.instant('success'));
					
				}else{
					toastr.error($translate.instant('noProcessDone'));
				}
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
		
	
	
	$scope.deleteLevelInfo =function(levelId){
		   
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteLevelComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
            	
            	
            	
               var frmDatum = {"levelId":levelId}; 
     		   $.ajax({
     			  type:'POST',
     			  url: "../pt/definition/level/delete",
     			  contentType: "application/json; charset=utf-8",				    
     			  data: JSON.stringify(frmDatum),
     			  dataType: 'json', 
     			  cache:false
     			}).done(function(res) {
     				$scope.init();
     				
     			}).fail  (function(jqXHR, textStatus, errorThrown) 
     			{ 
     			  if(jqXHR.status == 404 || textStatus == 'error')	
     				  $(location).attr("href","/beinplanner/lock.html");
     			});
            	
                
            } else {
                swal($translate.instant("deleteCanceled"), "");
            }
        });
		
		   
	};
	
	
	
	
	
	
	
	
	
	
});