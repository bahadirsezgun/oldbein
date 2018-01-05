ptBossApp.config(['$translateProvider', function ($translateProvider) {
	  $translateProvider.translations('en', translationsEn);
	
	  $translateProvider.translations('de', translationsDe);

	  $translateProvider.translations('tr', translationsTr);

	  $translateProvider.useSanitizeValueStrategy('escape');
	
	  var lang=localStorage.getItem('lang');  
	  if(lang==null)
		  lang="tr";
	  
	  $translateProvider.preferredLanguage(lang);
}]);