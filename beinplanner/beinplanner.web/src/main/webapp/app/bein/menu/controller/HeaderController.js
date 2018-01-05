ptBossApp.controller('HeaderController', function($rootScope,$scope,$translate,commonService,homerService) {
	
	
	$scope.menuList="";
	$scope.menuTop="";
	$scope.mobileMenu="";
	
	$scope.searchBox;
	$scope.searchBoxPH;
	
	$scope.$on("searchBoxPH",function(){
		$scope.searchBoxPH=commonService.searchBoxPH;
	});
	
	$scope.$on("search",function(){
		$scope.searchBox=commonService.search;
	});
	
	$scope.$on("changeLang",function(){
		 $translate.use($rootScope.lang);
		 $translate.refresh;
		 $scope.$apply();
		
	});
	
	$scope.searchBoxChange=function(){
		commonService.search=$scope.searchBox;
		commonService.searchItem();
	}
	
	$scope.callHelp=function(){
		commonService.helpItem();
	}
	
	function initHomerHeader(){
		 
		
		 $('.animate-panel').animatePanel();
		
		 $('.closebox').unbind('click').bind('click', function (event) {
		        event.preventDefault();
		        var hpanel = $(this).closest('div.hpanel');
		        hpanel.remove();
		        if($('body').hasClass('fullscreen-panel-mode')) { $('body').removeClass('fullscreen-panel-mode');}
		    });
		 
		 
		 $('.showhide').unbind('click').bind('click', function (event) {
		        event.preventDefault();
		        var hpanel = $(this).closest('div.hpanel');
		        var icon = $(this).find('i:first');
		        var body = hpanel.find('div.panel-body');
		        var footer = hpanel.find('div.panel-footer');
		        body.slideToggle(300);
		        footer.slideToggle(200);

		        icon.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
		        hpanel.toggleClass('').toggleClass('panel-collapse');
		        setTimeout(function () {
		            hpanel.resize();
		            hpanel.find('[id^=map-]').resize();
		        }, 50);
		    });
		 
		 	$('.hide-menu').unbind('click').bind('click', function(event){
		        event.preventDefault();
		        if ($(window).width() < 769) {
		            $("body").toggleClass("show-sidebar");
		        } else {
		            $("body").toggleClass("hide-sidebar");
		        }
		    });

		 	$('.hide-menu').click();
			
		    
		    $('.fullscreen').unbind('click').bind('click', function() {
		        var hpanel = $(this).closest('div.hpanel');
		        var icon = $(this).find('i:first');
		        $('body').toggleClass('fullscreen-panel-mode');
		        icon.toggleClass('fa-expand').toggleClass('fa-compress');
		        hpanel.toggleClass('fullscreen');
		        setTimeout(function() {
		            $(window).trigger('resize');
		        }, 100);
		    });

		  
		
		    
		    $('.small-header-action').unbind('click').bind('click', function(event){
		        event.preventDefault();
		        var icon = $(this).find('i:first');
		        var breadcrumb  = $(this).parent().find('#hbreadcrumb');
		        $(this).parent().parent().parent().toggleClass('small-header');
		        breadcrumb.toggleClass('m-t-lg');
		        icon.toggleClass('fa-arrow-up').toggleClass('fa-arrow-down');
		    });
		    
		
		 // Open close right sidebar
		    $('.right-sidebar-toggle').unbind('click').bind('click', function () {
		        $('#right-sidebar').toggleClass('sidebar-open');
		    });
		    
		    
	};
	
	
	$scope.init=function(){
		$.ajax({
  		  type:'POST',
  		  url: "../pt/setting/findPtGlobal",
  		  contentType: "application/json; charset=utf-8",				    
  		  dataType: 'json', 
  		  cache:false
  		}).done(function(res) {
  			if(res!=null){
  				$scope.ptTz=res.ptTz;
  				$scope.ptCurrency=res.ptCurrency;
  				$scope.ptStaticIp=res.ptStaticIp;
  				$scope.ptLang=(res.ptLang).substring(0,2);
  				$scope.ptDateFormat=res.ptScrDateFormat;
  				
  				$translate.use($scope.ptLang);
				$translate.refresh;
				$scope.$apply();
  			}
  			$scope.initMenu();
  		});
		
	}
	
	
	$scope.initMenu=function(){
		
		
		 // Open close right sidebar
	
		
	    
		
		$.ajax({
			  type:'POST',
			  url: "../pt/menu/gettopmenu",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				var menuHtml="";
				var mobileMenuHtml="<li style='display: inline-block;'><a class='' href='../index.html'><i class='pe-7s-power fa-2x text-danger pull-right'></i> "+$translate.instant("logout")+"</a></li>";
				
				menuHtml+="<table>";
				
				
				
				
				var sizeOfMenu=parseInt(res.length);
				
				
				
				
				$.each(res,function(i,menu){
					if(menu.menuName=="calendar"){
						mobileMenuHtml+="<li style='display: inline-block;'><a class='' href='#/schedule/plan/calendar/dash'><i class='"+menu.menuClass+" fa-2x pull-right'></i>"+$translate.instant(menu.menuName)+"</a>";
						
					}else if(menu.menuName!="create_member" && menu.menuName!="find_member"  ){
						mobileMenuHtml+="<li style='display: inline-block;'><a class='' href='"+menu.menuLink+"'><i class='"+menu.menuClass+" fa-2x pull-right'></i>"+$translate.instant(menu.menuName)+"</a>";
					}
					  
					
				 if(i==0){	
					menuHtml+="<tr>";
				 }
					menuHtml+="<td>";
					menuHtml+="    <a href='"+menu.menuLink+"'>";
					menuHtml+="        <i class='"+menu.menuClass+"'></i>";
					menuHtml+="        <h5>"+$translate.instant(menu.menuName)+"</h5>";
					menuHtml+="    </a>";
					menuHtml+="</td>";
				 if(i==2){	
						menuHtml+="</tr><tr>";
				 }		
				 if(i==5){	
						menuHtml+="</tr><tr>";
				 } 
				 if(i==8){	
						menuHtml+="</tr>";
				 }	
				 
				 if(sizeOfMenu==(i+1) && (i!=2) && i!=5 && i!=8){
					 menuHtml+="</tr>";
				 }
				 
				});
				menuHtml+="</table>";
				
				
				
				$scope.mobileMenu=mobileMenuHtml;
				$scope.menuTop=menuHtml;
				$scope.$apply();
				
				//$('#side-menu').metisMenu();
			    
				
				//initHomer();
				
				initHomerHeader();
				createEventsForMobileMenu();
			});
	};
		
	
	function createEventsForMobileMenu(){
		$("#mobile-collapse").find("li").unbind("click").bind("click",function(){
			$("#mobileMenuToggle").click();
		});
	}
	
	
	
});
