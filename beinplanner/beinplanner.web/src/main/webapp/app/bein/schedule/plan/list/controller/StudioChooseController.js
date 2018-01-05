ptBossApp.controller('StudioChooseController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.studious;
	$scope.chkVal=false;
	
	
	$scope.initSCC = function(){
		
		findStudios();
	}
	
	function findStudios(){
		$.ajax({
			  type:'POST',
			  url: "../pt/definition/studio/find",
			  contentType: "application/json; charset=utf-8",
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				if(res.length!=0){
					$scope.studious=res;
					
				}else{
					$scope.noStudio=true;
				}
				
				$scope.$apply();
			});
	};
	
	
	$scope.chooseStudio=function(studio){
		studio.selected=true;	
		$scope.addSelectedStudio(studio);
	};
	
	$scope.releaseStudio=function(studio){
		
		$scope.removeStudio(studio);
	}
	
	
});