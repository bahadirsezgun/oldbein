ptBossApp.factory("globals",function($rootScope){
	
	var programs=new Object();
	
	
	programs.PROGRAM_PERSONAL=1;
	programs.PROGRAM_CLASS=2;
	programs.PROGRAM_MEMBERSHIP=3;
	
	
	programs.SCHEDULE_TYPE_PERSONAL="supp";
	programs.SCHEDULE_TYPE_CLASS="sucp";
	programs.SCHEDULE_TYPE_MEMBERSHIP="smp";
	
	
	programs.USER_TYPE_MEMBER  = 1;
	programs.USER_TYPE_STAFF = 2;
	programs.USER_TYPE_SCHEDULAR_STAFF  = 3;
	programs.USER_TYPE_MANAGER = 4;
	programs.USER_TYPE_SUPER_MANAGER = 5;
	programs.USER_TYPE_ADMIN = 6;
	programs.USER_TYPE_PASIVE = 7;
	
	programs.ALL_STAFF=0;
	
	programs.BONUS_TYPE_PERSONAL = 1;
	programs.BONUS_TYPE_CLASS = 2;
	
	programs.BONUS_IS_TYPE_RATE=1;
	programs.BONUS_IS_TYPE_STATIC=2;
	programs.BONUS_IS_TYPE_STATIC_RATE=3;
	
	return programs;
});
