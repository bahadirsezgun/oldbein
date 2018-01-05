ptBossApp.controller('UserClassBookingController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.members;
	
	$scope.searchUserView="./schedule/plan/class/users/search.html";
	
	
	$scope.searchUser=true;
	$scope.resultUser=false;
	
	
	$scope.viewResult=function(members){
		$scope.searchUser=false;
		$scope.resultUser=true;
		$scope.members=members;
		$scope.$apply();
		
	};
	
	$scope.addMoreUser=function(){
		$scope.searchUser=true;
		$scope.resultUser=false;
	}
	
	
	$scope.chooseStudio=function(){
		 $scope.nextChooseStudio();
	}
	
	$scope.selectMember=function(member){
		if(member.saleStatu==1 || member.saleStatu==5 || member.saleStatu==0){
			if($scope.programClass!=null && member.progId!=0){
				if($scope.programClass.progId!=member.progId){
					toastr.error($translate.instant("userBoughtDifferentPacket"));
				}else{
					$scope.addSelectedUser(member);
				}
			}else{
				$scope.addSelectedUser(member);
			}
		}else{
			swal({
	            title: $translate.instant("continueOldClasses"),
	            text: $translate.instant("continueOldClassesComment"),
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: $translate.instant("yesContinue"),
	            cancelButtonText: $translate.instant("no"),
	            closeOnConfirm: true,
	            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	
	            	if($scope.programPersonal!=null){
	            		$scope.userSelectedBySchIdFromUserFind(member.schId,$scope.programClass.progId);
	            	}else{
	            		$scope.userSelectedBySaleFromUserFind(member.saleId,member.progId);
		            	
	            	}
	            
	            }
	        });
		}
		
		
	};
	
	
	
});