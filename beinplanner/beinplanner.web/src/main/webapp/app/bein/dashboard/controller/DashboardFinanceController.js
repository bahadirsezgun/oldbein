ptBossApp.controller('DashboardFinanceController', function($scope,$translate,homerService,commonService) {
	
	$scope.tokenClass="150";
	$scope.monthName="may";
	$scope.activeDuration="10 Month";
	$scope.activateDate="12/01/2017";
	$scope.lastUpdateDate="10/05/2017";
	$scope.messageText="You have two new messages from Monica Bolt";
	$scope.week="";
	
	$scope.activeMemberCount=""; //Aktif Üye Sayısı
	$scope.updateVersion=""; //Aktif Üye Sayısı
	$scope.leftPaymentInfo="";  // Eksik ödeme bilgisi
	$scope.income;
	$scope.incomes;
	$scope.prevIncomes;
	$scope.totalMemberCount;
	
	$scope.plannedClassInfos;
	$scope.prevPlannedClassInfos;
	
	
	$scope.month;
	
	$scope.packetSales;
	$scope.packetPayments;
	$scope.specialUsers;
	
	$scope.showMain=true;
	$scope.detailPage="";
	
	
	$scope.months=new Array({value:"0",name:$translate.instant("pleaseSelect")}
    ,{value:"1",name:$translate.instant("january")}
   ,{value:"2",name:$translate.instant("february")}
   ,{value:"3",name:$translate.instant("march")}
   ,{value:"4",name:$translate.instant("april")}
   ,{value:"5",name:$translate.instant("may")}
   ,{value:"6",name:$translate.instant("june")}
   ,{value:"7",name:$translate.instant("july")}
   ,{value:"8",name:$translate.instant("august")}
   ,{value:"9",name:$translate.instant("september")}
   ,{value:"10",name:$translate.instant("october")}
   ,{value:"11",name:$translate.instant("november")}
   ,{value:"12",name:$translate.instant("december")}
   );


	$scope.years=new Array();
	$scope.prevYear;
	$scope.year;
	
	
	$scope.initDFC=function(){
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		$('.animate-panel').animatePanel();
		
		var date=new Date();
		var year=date.getFullYear();
		
		for(var i=-10;i<10;i++){
			$scope.years.push(year+i);
		}
		
		$scope.year=year;
		$scope.month=date.getMonth()+1;
		$scope.prevYear=year-1;
		
		findPtGlobals()
		
		$scope.week=""
	}
	
	
	$scope.turnBackToMain=function(){
		getLeftPayments();
		getTodayPayments();
		getSaledPackets();
		getPacketPayments();
		findPastForYear();
		
		$scope.showMain=true;
		$scope.showDetailPage=false;
		$scope.showLastClass=false;
		
	}
	
	$scope.showDetail=function(){
		$scope.showMain=false;
		$scope.showDetailPage=true;
		$scope.detailPage="../bein/income/pastIncome.html";
		
	};
	
	$scope.showLeftPaymentDetail=function(){
		$scope.showMain=false;
		$scope.showDetailPage=true;
		$scope.detailPage="../bein/packetpayment/leftPayments.html";
		
	};
	
	function findSpecialDates(){
		 $.ajax({
			  type:'POST',
			  url: "../pt/ptusers/specialDates",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
					$scope.specialUsers=res;
				    $scope.$apply();
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
			})
	}
	
	function findPtGlobals(){
		 $.ajax({
			  type:'POST',
			  url: "../pt/setting/findPtGlobal",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					$scope.ptLang=(res.ptLang).substring(0,2);
				    $scope.ptDateFormat=res.ptScrDateFormat;
				    $scope.ptCurrency=res.ptCurrency;
				    $scope.$apply();
				  
				    loggedInUser();
				    getLeftPayments();
				    getActiveMembers();
					getPtUpdateVersion();
					getTodayPayments();
					getSaledPackets();
					getPacketPayments();
					findTotalMemberInSystem();
				}
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
	}
	
	function loggedInUser(){
		
		$.ajax({
  		  type:'POST',
  		  url: "../pt/ptusers/getSessionUser",
  		  contentType: "application/json; charset=utf-8",				    
  		  dataType: 'json', 
  		  cache:false
  		}).done(function(res) {
  			$scope.user=res;
  			$scope.firmId=res.firmId;
  			$scope.userType=res.userType;
  			$scope.$apply();
  			findPastForYear();
  			findPastForYearCount();
  			findSpecialDates();
  			lastClasses();
  		}).fail  (function(jqXHR, textStatus, errorThrown){ 
  			 if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beinplanner/lock.html");
		});
	}
	
	$scope.lastOfClasses;
	$scope.lastOfCountThisWeek;
	$scope.lastOfCountNextWeek;
	$scope.totalOfLastCount;
	
	
	function lastClasses(){
		$.ajax({
	  		  type:'POST',
	  		  url: "../pt/dashboard/lastOfClasses",
	  		  contentType: "application/json; charset=utf-8",				    
	  		  dataType: 'json', 
	  		  cache:false
	  		}).done(function(res) {
	  			$scope.lastOfClasses=res;
	  			
	  			console.log(res);
	  			
	  			var cotwPC=res.stpTW.length;
	  			var cotwM=res.stpMTW.length;
	  			var cotnwPC=res.stpNW.length;
	  			var cotnwM=res.stpMNW.length;
	  			
	  			$scope.lastOfCountThisWeek=parseInt(cotwPC)+parseInt(cotwM);
	  			$scope.lastOfCountNextWeek=parseInt(cotnwPC)+parseInt(cotnwM);
	  			$scope.totalOfLastCount=$scope.lastOfCountThisWeek+$scope.lastOfCountNextWeek;
	  			$scope.$apply();
	  		});
	}
	
	$scope.showLastClassDetail=function(){

		$scope.showMain=false;
		$scope.showLastClass=true;
	}
	
	function findTotalMemberInSystem(){
		$.ajax({
			  type:'POST',
			  url: "../pt/dashboard/findTotalMemberInSystem",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.totalMemberCount=res;
				console.log("$scope.totalMemberCount : "+$scope.totalMemberCount);
				$scope.$apply();
			});
		
	}
	
	function getActiveMembers(){
		
		$.ajax({
			  type:'POST',
			  url: "../pt/dashboard/activeMembers",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.activeMemberCount=res.activeMemberCount;
				
				$scope.$apply();
			});
		
	}
	
   function getPtUpdateVersion(){
		
		$.ajax({
			  type:'POST',
			  url: "../pt/dashboard/updateVersion",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.activateDate=res.updDateStr;
				$scope.updateVersion=res.updVer;
				$scope.$apply();
			});
		
	}
	
   
   function getLeftPayments(){
		
		$.ajax({
			  type:'POST',
			  url: "../pt/dashboard/leftPayment",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.leftPaymentInfo=res;
				$scope.$apply();
			});
		
	}
   
   
   
   $scope.todayPayment;
   
   function getTodayPayments(){
		
		$.ajax({
			  type:'POST',
			  url: "../pt/dashboard/todayIncomeExpense",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.todayPayment=res;
				$scope.$apply();
			});
		
	}
	
   
    
   function getPacketPayments(){
		
		$.ajax({
			  type:'POST',
			  url: "../pt/dashboard/packetPayments",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.packetPayments=res;
				$scope.$apply();
				todayInlineChart();
			});
		
	}
   
   
   
    function getSaledPackets(){
    	
    	$.ajax({
			  type:'POST',
			  url: "../pt/dashboard/packetSales",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.packetSales=res;
				$scope.$apply();
			});
    	
    }
   
   
	$scope.lastMonthName;
	$scope.lastMonthEarn=0;
	$scope.lastMonthExpense=0;
	$scope.lastMonthIncome=0;
	$scope.lastMonthEarnRate=0;
	
	$scope.thisYearEarn=0;
	$scope.thisYearExpense=0;
	$scope.thisYearIncome=0;
	
	function findPastForYear(){
		$.ajax({
			  type:'POST',
			  url: "../pt/incomeController/findPastForYear/"+$scope.year+"/"+$scope.firmId,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.lastMonthName=0;
		    	$scope.lastMonthEarn=0;
		    	$scope.lastMonthIncome=0;
		    	$scope.lastMonthExpense=0;
		    	$scope.thisYearEarn=0;
		    	$scope.thisYearExpense=0;
		    	$scope.thisYearIncome=0;
		    	
				$scope.incomes=res;
				var maxEarn=0;
				$.each($scope.incomes,function(i,income){
				    if($scope.month==1){
				    	$scope.lastMonthName=$scope.incomes[i].pimMonthName;
				    	$scope.lastMonthEarn=$scope.incomes[i].totalEarn;
				    	$scope.lastMonthIncome=$scope.incomes[i].totalIncome;
				    	$scope.lastMonthExpense=$scope.incomes[i].totalExpense;
				    	
				    }else if(income.pimMonth==$scope.month){
				    	$scope.lastMonthName=$scope.incomes[i-1].pimMonthName;
				    	$scope.lastMonthEarn=$scope.incomes[i-1].totalEarn;
				    	$scope.lastMonthIncome=$scope.incomes[i-1].totalIncome;
				    	$scope.lastMonthExpense=$scope.incomes[i-1].totalExpense;
				    	
					}
				    
				    $scope.thisYearExpense=$scope.thisYearExpense+income.totalExpense;
			    	$scope.thisYearIncome=$scope.thisYearIncome+income.totalIncome;
				    
				    if(maxEarn<$scope.lastMonthEarn){
				    	maxEarn=$scope.lastMonthEarn;
				    }
				    
				   
				    	
				    
				});
				
				$scope.thisYearEarn=$scope.thisYearIncome-$scope.thisYearExpense;
		    	
				 if(maxEarn!=0){
				    	$scope.lastMonthEarnRate=$scope.lastMonthEarn/maxEarn;
				 }else{
					 $scope.lastMonthEarnRate=0;
				 }
				 findPrevForYear();
				$scope.$apply();
				getDataToGraph();
				
			}).fail  (function(jqXHR, textStatus, errorThrown) {
				$scope.$apply();
			});
	}
	
	
	function findPrevForYear(){
		
	
		$.ajax({
			  type:'POST',
			  url: "../pt/incomeController/findPastForYear/"+$scope.prevYear+"/"+$scope.firmId,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.prevIncomes=res;
				
			
				    if($scope.month==1){
				    	$scope.lastMonthName=$scope.prevIncomes[12].pimMonthName;
				    	$scope.lastMonthEarn=$scope.prevIncomes[12].totalEarn;
				    	$scope.lastMonthIncome=$scope.prevIncomes[12].totalIncome;
				    	$scope.lastMonthExpense=$scope.prevIncomes[12].totalExpense;
				    	
				    }
				
				    $scope.$apply();
				
			}).fail  (function(jqXHR, textStatus, errorThrown) {
				$scope.$apply();
			});
	}
	
	
	
	
	
	
   function todayInlineChart(){
		
	   if($scope.todayPayment==null){
		   $scope.todayPayment=new Object();
		   $scope.todayPayment.incomeAmount=0;
		   $scope.todayPayment.expenseAmount=0;
		   
	   }
	   
		var data = [
		             {
		                 label: "bar",
		                 data: [ [1, $scope.todayPayment.incomeAmount], [2, $scope.todayPayment.expenseAmount] ]
		             }
		         ];
			
		var chartUsersOptions = {
	            series: {
	                bars: {
	                    show: true,
	                    barWidth: 0.8,
	                    fill: true,
	                    fillColor: {
	                        colors: [ {  opacity: 0.6,color:'#ffffff' }, { opacity: 0.6,color:'#62cb31' } ]
	                    },
	                    lineWidth: 1
	                }
	            },
	            xaxis: {
	                tickDecimals: 0
	            },
	            colors: ["#62cb31","#ffffff"],
	            grid: {
	                color: "#ffffff",
	                hoverable: true,
	                clickable: false,
	                tickColor: "#ffffff",
	                borderWidth: 0,
	                borderColor: 'ffffff',
	            },
	            legend: {
	                show: false
	            },
	            tooltip: false
	        };

           $.plot($("#flot-bar-chart"), data, chartUsersOptions);
    }
	
	
	
	function getDataToGraph(){
		
		var inOut=$scope.incomes;
		
		var data1 = [ [1, inOut[0].totalIncome]
					, [2, inOut[1].totalIncome]
					, [3, inOut[2].totalIncome]
					, [4, inOut[3].totalIncome]
					, [5, inOut[4].totalIncome]
					, [6, inOut[5].totalIncome]
					, [7, inOut[6].totalIncome]
					, [8, inOut[7].totalIncome]
					, [9, inOut[8].totalIncome]
					, [10, inOut[9].totalIncome]
					, [11, inOut[10].totalIncome]
					, [12, inOut[11].totalIncome] ];
		
		var data2 = [ [1, inOut[0].totalExpense]
					, [2, inOut[1].totalExpense]
					, [3, inOut[2].totalExpense]
					, [4, inOut[3].totalExpense]
					, [5, inOut[4].totalExpense]
					, [6, inOut[5].totalExpense]
					, [7, inOut[6].totalExpense]
					, [8, inOut[7].totalExpense]
					, [9, inOut[8].totalExpense]
					, [10, inOut[9].totalExpense]
					, [11, inOut[10].totalExpense]
					, [12, inOut[11].totalExpense] ];
		
	
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
            colors: [ "#62cb31", "#fedde4"],
        };

        $.plot($("#flot-line-chart"), [data1, data2], chartUsersOptions);
        
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
				$scope.firmName=$scope.firms[0].firmName;
				$scope.$apply();
				findPastForYear();
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
	
	
	
	$scope.lastMonthName;
	$scope.lastMonthCount1=0;
	$scope.lastMonthCount2=0;
	
	
	
	$scope.lastYearCount1=0;
	$scope.lastYearCount2=0;
	
	
	
	
	function findPastForYearCount(){
		$.ajax({
			  type:'POST',
			  url: "../pt/dashboard/plannedClassInfo/"+$scope.year,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.plannedClassInfos=res;
				
				
				findPrevForYearCount();
				$scope.$apply();
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) {
				$scope.$apply();
			});
	}
	
	$scope.lastYearRate=0;
	$scope.lastMonthRate=0;
	$scope.countMonthName;
	
	function findPrevForYearCount(){
		$.ajax({
			  type:'POST',
			  url: "../pt/dashboard/plannedClassInfo/"+$scope.prevYear,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.prevPlannedClassInfos=res;
				
				
				
				
				$.each($scope.prevPlannedClassInfos,function(i,ppci){
				    if(ppci.month==$scope.month){
				    	$scope.countMonthName=ppci.monthName;
				    	$scope.lastMonthCount1=ppci.classCount;
				     }
				    
				    $scope.lastYearCount1=$scope.lastYearCount1+ppci.classCount;
				});
				
				$.each($scope.plannedClassInfos,function(i,pci){
				    if(pci.month==$scope.month){
				    	$scope.countMonthName=pci.monthName;
				    	$scope.lastMonthCount2=pci.classCount;
				     }
				    $scope.lastYearCount2=$scope.lastYearCount2+pci.classCount;
				});
				
				
				
			
				if($scope.lastYearCount1>0){
					$scope.lastYearRate=Math.ceil(parseFloat($scope.lastYearCount2/$scope.lastYearCount1)*100);
					
					if($scope.lastYearRate>100){
						$scope.lastYearRate=100;
					}
				}else{
					$scope.lastYearRate=100;
				}
				
				
				if($scope.lastMonthCount1>0){
					$scope.lastMonthRate=Math.ceil(parseFloat($scope.lastMonthCount2/$scope.lastMonthCount1)*100);
					
					if($scope.lastMonthRate>100){
						$scope.lastMonthRate=100;
					}
				}else{
					$scope.lastMonthRate=100;
				}
				
				
				$scope.$apply();
				getDataToGraphCount();
			}).fail  (function(jqXHR, textStatus, errorThrown) {
				$scope.$apply();
			});
	}
	
	
	function todayInlineChartCount(){
		var inOut=$scope.plannedClassInfos;
		var dp=[];
		$.each(inOut,function(i,pc){
			dp.push([i,pc.classCount]);
			
			
		});
			
		var chartIncomeData = [
               {
                   label: "line",
                   data : dp
               }
           ];

           var chartIncomeOptions = {
               series: {
                   lines: {
                       show: true,
                       lineWidth: 0,
                       fill: true,
                       fillColor: "#64cc34"

                   }
               },
               colors: ["#62cb31"],
               grid: {
                   show: false
               },
               legend: {
                   show: false
               }
           };

           $.plot($("#flot-income-chart-count"), chartIncomeData, chartIncomeOptions);
    }
	
	function getDataToGraphCount(){
		
		var inOut=$scope.plannedClassInfos;
		var inOutPrev=$scope.prevPlannedClassInfos;
		
		var data1 = [ [1, inOut[0].classCount]
					, [2, inOut[1].classCount]
					, [3, inOut[2].classCount]
					, [4, inOut[3].classCount]
					, [5, inOut[4].classCount]
					, [6, inOut[5].classCount]
					, [7, inOut[6].classCount]
					, [8, inOut[7].classCount]
					, [9, inOut[8].classCount]
					, [10, inOut[9].classCount]
					, [11, inOut[10].classCount]
					, [12, inOut[11].classCount] ];
		
		var data2 = [ [1, inOutPrev[0].classCount]
					, [2, inOutPrev[1].classCount]
					, [3, inOutPrev[2].classCount]
					, [4, inOutPrev[3].classCount]
					, [5, inOutPrev[4].classCount]
					, [6, inOutPrev[5].classCount]
					, [7, inOutPrev[6].classCount]
					, [8, inOutPrev[7].classCount]
					, [9, inOutPrev[8].classCount]
					, [10, inOutPrev[9].classCount]
					, [11, inOutPrev[10].classCount]
					, [12, inOutPrev[11].classCount] ];
		
	
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
            colors: [ "#62cb31", "#fedde4"],
        };

        $.plot($("#flot-line-chart-count"), [data1, data2], chartUsersOptions);
        
	}
	
	
	
});