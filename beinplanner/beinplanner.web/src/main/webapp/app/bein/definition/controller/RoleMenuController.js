ptBossApp.controller('RoleMenuController', function($scope,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.USER_TYPE_SUPER_MANAGER=5;
	$scope.USER_TYPE_MANAGER = 4;
	$scope.USER_TYPE_SCHEDULAR_STAFF  = 3;
	$scope.USER_TYPE_STAFF = 2;
	
	   $scope.menus;
	   $scope.topmenus;
	   $scope.dashboardPages=[{"name":$translate.instant("finance"),"value":800}
	    					,{"name":$translate.instant("classes"),"value":801}
	    					,{"name":$translate.instant("special"),"value":802}];
	   
	   $scope.dashmenu;
	   
	   $scope.userType=$scope.USER_TYPE_SUPER_MANAGER;
	   
	   
	   toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	
	   $scope.init = function(){
		   
		    commonService.pageName=$translate.instant("definition_rolmenu");
			commonService.pageComment=$translate.instant("rolMenuDefinitionComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
		   
			$.ajax({
				  type:'POST',
				  url: "../pt/definition/menu/findRolMenu/"+$scope.USER_TYPE_SUPER_MANAGER,
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$scope.menus=res;
					console.log(res);
					$scope.$apply();
				});
			
			$.ajax({
				  type:'POST',
				  url: "../pt/definition/menu/findTopRolMenu/"+$scope.USER_TYPE_SUPER_MANAGER,
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$scope.topmenus=res;
					console.log(res);
					$scope.$apply();
				});
		   
			 
		       $.ajax({
					  type:'POST',
					  url: "../pt/definition/menu/findDashboardMenu/"+$scope.USER_TYPE_SUPER_MANAGER,
					  contentType: "application/json; charset=utf-8",				    
					  dataType: 'json', 
					  cache:false
					}).done(function(res) {
						$scope.dashmenu=res;
						console.log(res);
						$scope.$apply();
					});
	   }
	   
	   $scope.changeUserType=function(userType){
		   $scope.userType=userType;
		   $scope.findMenus(userType);
	   }
	   
	   
	   
	   $scope.findMenus=function(userType){
		       $.ajax({
				  type:'POST',
				  url: "../pt/definition/menu/findRolMenu/"+userType,
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$scope.menus=res;
					$scope.$apply();
				});
		       
		       $.ajax({
					  type:'POST',
					  url: "../pt/definition/menu/findTopRolMenu/"+userType,
					  contentType: "application/json; charset=utf-8",				    
					  dataType: 'json', 
					  cache:false
					}).done(function(res) {
						$scope.topmenus=res;
						console.log(res);
						$scope.$apply();
					});
		       
		       
		       $.ajax({
					  type:'POST',
					  url: "../pt/definition/menu/findDashboardMenu/"+userType,
					  contentType: "application/json; charset=utf-8",				    
					  dataType: 'json', 
					  cache:false
					}).done(function(res) {
						$scope.dashmenu=res;
						console.log(res);
						$scope.$apply();
					});
	   }
	   
	   $scope.changeMenuAuth=function(menuId,authority,menuName){
		   
		   if(authority==1){
			   removeMenuAuth(menuId,menuName);
		   }else{
			   addMenuAuth(menuId,menuName);
		   }
		   
	   }
	   
	   function getRoleName(userType){
		   if(userType==$scope.USER_TYPE_SUPER_MANAGER){
			   return "SMANAGER";
		   }else if(userType==$scope.USER_TYPE_MANAGER){
			   return "MANAGER";
		   }else if(userType==$scope.USER_TYPE_SCHEDULAR_STAFF){
			   return "SSTAFF";
		   }else if(userType==$scope.USER_TYPE_STAFF){
			   return "STAFF";
		   }
		}
	   
	   
	   
	   $scope.changeDashboard=function(menuId){
		   var frmDatum = {'roleId':$scope.userType,
			        'menuId':menuId,
			        'roleName':getRoleName($scope.userType),
			        }; 
		   $.ajax({
				  type:'POST',
				  url: "../pt/definition/menu/changeDashboard/"+$scope.userType,
				  contentType: "application/json; charset=utf-8",				    
				  data: JSON.stringify(frmDatum),
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$scope.findMenus($scope.userType);
				});
	   };
	   
	   var addMenuAuth=function(menuId,menuName){
		   
		   var frmDatum = {'roleId':$scope.userType,
			        'menuId':menuId,
			        'roleName':getRoleName($scope.userType),
			        'menuName':menuName
			        }; 
		   
		        $.ajax({
				  type:'POST',
				  url: "../pt/definition/menu/addRolMenu",
				  contentType: "application/json; charset=utf-8",				    
				  data: JSON.stringify(frmDatum),
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$scope.findMenus($scope.userType);
				});
	   }
	   
	   var removeMenuAuth=function(menuId,menuName){
		   
		   var frmDatum = {'roleId':$scope.userType,
			        'menuId':menuId,
			        'roleName':getRoleName($scope.userType),
			        'menuName':menuName
			        }; 
		   
		   $.ajax({
				  type:'POST',
				  url: "../pt/definition/menu/removeRolMenu",
				  contentType: "application/json; charset=utf-8",				    
				  data: JSON.stringify(frmDatum),
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$scope.findMenus($scope.userType);
				});
	   }
});
