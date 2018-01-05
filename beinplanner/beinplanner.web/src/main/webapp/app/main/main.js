require.config({
   //urlArgs: "version="+(new Date()).getTime(),
   waitSeconds: 200,
   paths: {
		    'angular': '../../jslib/lib/angular/angular',
			'anSanitize': '../../jslib/lib/angular/angular-sanitize',
			'uiMask': '../../jslib/lib/mask/mask.min',
	        'ptbossapp':'./ptbossapp',
	        'anroute': '../../jslib/lib/angular/angular-route',
	        'modulloader':'./modulloader',
			'antranslate': '../../jslib/lib/angular/angular-translate',
   },
   shim: {
	   'angular': {
           exports: 'angular'
       },
      'ptbossapp': {
           deps: ['angular']
       },
      'modulloader':{
    	   deps: ['angular','main']
       },
       'anSanitize': {
           deps: ['angular']
       },
       'antranslate': {
            deps: ['angular']
       },
       'uiMask': {
           deps: ['angular']
       },
      	'anroute': {
           deps: ['angular']
       }
      
   },
    deps :[]
});

requirejs(['angular','anSanitize','ptbossapp','modulloader','antranslate','anroute','uiMask'],
  function   (angular ,anSanitize,ptbossapp,modulloader,antranslate,anroute,uiMask ) {
	 
	  angular.element(document).ready(function() {
	      angular.bootstrap(document, ['PTBossApp']);
	  });
	  
  });

