ptBossApp.controller('DashboardClassController', function($scope,$translate,homerService,commonService) {
	
	$scope.tokenClass="150";
	$scope.monthName="may";
	$scope.activeDuration="10 Month";
	$scope.activateDate="12/01/2017";
	$scope.lastUpdateDate="10/05/2017";
	$scope.messageText="You have two new messages from Monica Bolt";
	$scope.week="";
	$scope.calendar="";
	
	$scope.startDateStr="";
	$scope.endDateStr="";
	$scope.staffId=$scope.user.userId;
	$scope.studios=new Array();
	
	
	$scope.initDC=function(){
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		$('.animate-panel').animatePanel();
		//getDataToGraph();
		
		$scope.calendar="./schedule/plan/calendar/ptcalendardash.html";
	}
	/*
	function getStaffClasses(){
		var frmDatum={"startDateStr":"",
				  "endDateStr":"",
				  "staffId":$scope.user.userId,
				  "studios":$scope.studios};
	
		$scope.scheduledPlansForSearch="";
		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/searchPlans",
			  contentType: "application/json; charset=utf-8",				    
			  data:JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$("#planDateStrIn").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
				$scope.planDateStrIn=$scope.startDate;
				
				$scope.scheduledPlansForSearch=res;
				console.log(res);
				
				$scope.$apply();
			});
	}
	*/
	/*
	function getDataToGraph(){
		var data1 = [ [0, 55], [1, 48], [2, 40], [3, 36], [4, 40], [5, 60], [6, 50], [7, 51]
					 ,[8, 55], [9, 48], [10, 40], [11, 36], [12, 40], [13, 60], [14, 50], [15, 51] ];
        var data2 = [ [0, 56], [1, 49], [2, 41], [3, 38], [4, 46], [5, 67], [6, 57], [7, 59]
        			 ,[8, 58], [9, 58], [10, 20], [11, 16], [12, 10], [13, 30], [14, 20], [15, 21]];

        var chartUsersOptions = {
            series: {
                splines: {
                    show: true,
                    tension: 0.4,
                    lineWidth: 1,
                    fill: 0.4
                },
            },
            grid: {
                tickColor: "#f0f0f0",
                borderWidth: 1,
                borderColor: 'f0f0f0',
                color: '#6a6c6f'
            },
            colors: [ "#62cb31", "#efefef"],
        };

        $.plot($("#flot-line-chart"), [data1, data2], chartUsersOptions);
	}*/
	
});