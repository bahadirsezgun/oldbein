ptBossApp.controller('RightSideBarController', function($rootScope,$scope,$translate,commonService,homerService) {
	
	
	$scope.menuList="";
	$scope.menuTop="";
	
	$scope.searchBox;
	$scope.searchBoxPH;
	
	
	$scope.init=function(){
		$("#sidebar-close").on('click', function () {
	    	$('#right-sidebar').toggleClass('sidebar-open');
	    });
		
		readMyXMLForBrand("https://s3-us-west-2.amazonaws.com/beinplanner/marketBein.json");
	};
	
	
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
	  		  if(xmlPageId==2){
	  			   var version=data.title;
	  			   $scope.version=version;
	  			   $scope.$apply();
	  			}
	  		});
	     },
	     error: function(e) {
	        console.log(e.message);
	     }
	  });
	}
	
	
});
