define(['./lang-en','./lang-de','./lang-tr']);

ptBossLoginApp.config(['$translateProvider', function ($translateProvider) {
	  $translateProvider.translations('en', translationsEn);
	
	  $translateProvider.translations('de', translationsDe);

	  $translateProvider.translations('tr', translationsTr);

	  $translateProvider.useSanitizeValueStrategy('escape');
	/*
	  var userLang = navigator.language || navigator.userLanguage; 
		alert ("The language is: " + userLang);
		*/
		
	  
	  $translateProvider.preferredLanguage('tr');
}]);