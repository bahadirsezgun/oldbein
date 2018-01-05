ptBossApp.controller('PtExpensesController', function($scope,$translate,parameterService,$location,homerService,commonService) {

	$scope.year;
	$scope.month="0";
	$scope.firmId="0";
	$scope.firms;
	$scope.ptExpenseses;
	$scope.firmName="";
	$scope.firmIdQuery="";
	$scope.ptCurrency;
	
	$scope.ptExpenses=new Object();
	$scope.ptExpenses.peId=0;
	$scope.ptExpenses.peAmount=0;
	$scope.ptExpenses.peComment="";
	$scope.ptExpenses.peDate="";
	$scope.ptExpenses.peDateStr="";
	$scope.ptExpenses.firmId;
	$scope.ptExpenses.peInOut="0";
	
	$scope.query=true;
	$scope.newExpense=false;
	
	$scope.totalExpenseAmount=0;
	$scope.totalIncomeAmount=0;
	
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
	
	
	
	$scope.initPEC=function(){
		commonService.pageName=$translate.instant("ptExpensesTitle");
		commonService.pageComment=$translate.instant("ptExpensesTitleComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		var date=new Date();
		var year=date.getFullYear();
		
		for(var i=-10;i<10;i++){
			$scope.years.push(year+i);
		}
		
		$scope.year=year;
		$scope.month=""+(date.getMonth()+1);
		findPtGlobals();
		
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
				  
				    $("#peDateStr").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
			        
				    findFirms();
				}
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
	}
	
	
	$scope.editExpense=function(ptExpenses){
		$scope.ptExpenses=ptExpenses;
		$scope.ptExpenses.peInOut=""+ptExpenses.peInOut;
		$scope.query=false;
		$scope.newExpense=true;
	}
	
	$scope.showQuery=function(){
		$scope.query=true;
		$scope.newExpense=false;
	}
	$scope.hideQuery=function(){
		$scope.query=false;
		$scope.newExpense=false;
	}
	
	$scope.enterExpense=function(){
		$scope.query=false;
		$scope.newExpense=true;
		$scope.ptExpenses.peId=0;
		$scope.ptExpenses.peAmount=0;
		$scope.ptExpenses.peComment="";
		$scope.ptExpenses.peInOut="0";
		
	}
	
	$scope.queryPtExpenses=function(){
		 $.ajax({
			  type:'POST',
			  url: "../pt/incomeController/findPtExpensesForMonth/"+$scope.year+"/"+$scope.month+"/"+$scope.firmIdQuery,
			  contentType: "application/json; charset=utf-8",				    
			   dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.ptExpenseses=res;
				$scope.query=false;
				$scope.newExpense=false;
				$scope.$apply();
			})
		 
		 $.ajax({
			  type:'POST',
			  url: "../pt/incomeController/findPtTotalExpensesByMonth/"+$scope.year+"/"+$scope.month+"/"+$scope.firmIdQuery,
			  contentType: "application/json; charset=utf-8",				    
			   dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.monthName=res.monthName;
				$scope.totalExpenseAmount=res.totalExpense;
				$scope.totalIncomeAmount=res.totalIncome;
				$scope.query=false;
				$scope.newExpense=false;
				$scope.$apply();
			});
		
	}
	
	

	$scope.createPtExpenses=function(income){
	
		$.ajax({
			  type:'POST',
			  url: "../pt/incomeController/createPtExpenses",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify($scope.ptExpenses),
			   dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				toastr.success($translate.instant("incomeExpenseRecorded"));
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) {
				$scope.$apply();
			});
	}
	
	$scope.cancelPtExpenses=function(expense){
		
		
		swal({
            title: $translate.instant("warning"),
            text: $translate.instant("deleteExpenses"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesContinue"),
            cancelButtonText: $translate.instant("no"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
		
		         $.ajax({
					  type:'POST',
					  url: "../pt/incomeController/deletePtExpenses",
					  contentType: "application/json; charset=utf-8",				    
					  data: JSON.stringify(expense),
					   dataType: 'json', 
					  cache:false
					}).done(function(res) {
						
						toastr.success($translate.instant("expenseDeleted"));
						$scope.ptExpenseses=null;
						$scope.totalExpenseAmount=0;
						$scope.totalIncomeAmount=0;
						$scope.$apply();
						$scope.queryPtExpenses();
						
					}).fail  (function(jqXHR, textStatus, errorThrown) {
						$scope.$apply();
					});
            }
       });
	}
	
	
	
	function findPtExpensesByDate(){
		$.ajax({
			  type:'POST',
			  url: "../pt/incomeController/findPtExpensesByDate",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify($scope.ptExpenses),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.ptExpenseses=res;
				$scope.$apply();
			}).fail  (function(jqXHR, textStatus, errorThrown) {
				$scope.$apply();
			});
		
		
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
				$scope.ptExpenses.firmId=$scope.firms[0].firmId;
				$scope.firmIdQuery=$scope.firms[0].firmId;
				$scope.firmName=$scope.firms[0].firmName;
				findPtExpensesByDate();
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  if(jqXHR.status == 404 || textStatus == 'error')	
					  $(location).attr("href","/beinplanner/lock.html");
			});
		
		
	}
	
	
});