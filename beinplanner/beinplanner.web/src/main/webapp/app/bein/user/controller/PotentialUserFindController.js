ptBossApp.controller('PotentialUserFindController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

$scope.maskGsm="(999) 999-9999";
	
   $scope.filterName="";
   $scope.filterSurname="";
   $scope.showQuery=true;
   $scope.potentials;
   $scope.potential;
	
	$scope.initPUC=function(){
		findFirms();
	}
	
	$scope.query =function(){
		 $scope.showQuery=true;
	}
	
	$scope.newPotential =function(){
		$location.path("/member/potential/create");
	}
	
	$scope.edit =function(potential){
		parameterService.param1=potential;
		$location.path("/member/potential/create");
	}
	$scope.find =function(){
		   
		  var frmDatum = {"userName":$scope.filterName,
			"userSurname":$scope.filterSurname
			}; 
		  
		   $.ajax({
			  type:'POST',
			  url: "../pt/potential/findByName",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
					$scope.potentials=res;
				    $scope.showQuery=false;
				    $scope.$apply();
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			})
	};
	
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
	
	
});