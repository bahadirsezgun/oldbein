	
	ptBossApp.controller('CommonController', function($scope,commonService,$element) {

		 $scope.commonLoaderValue=false;
		 
		 $scope.expression={"position":"absolute"};
		 
		 
		 $scope.closeLoader =   function (){
			 $scope.commonLoaderValue=false;
				
		 };
		 
		 
		 
		 
		 
		 $scope.$on("handlebroadcast",function(){
			 $scope.commonLoaderValue=commonService.loaderValue;
			 var loaderDiv=$("#loaderDiv",$element);
				
			 if(commonService.loaderValue=="true"){
				 $(loaderDiv).modal();
				 
			 }else{
				 $(loaderDiv).modal('hide'); 
			 }
			 
			 
			 var top=Math.max(0, (($(window).height() -100 ) / 2) + 
                     $(window).scrollTop()) + "px";
			 var left=Math.max(0, (($(window).width()) / 2) + 
                     $(window).scrollLeft()) + "px";
			 
			 var topModal=$(window).scrollTop()+"px";
			 
			 var minHeight=$(document).height();
			 var minWidth=$(document).width();
			 
			 
			 $scope.expression={"position":"absolute","top":top,"left":left,"margin-top":"0px","z-index":"2500"};
			 $scope.expressionLayer={"position":"absolute","top":topModal,"z-index":"499","min-height":minHeight,"min-width":minWidth};
			 
			 
			 
		 });
	});