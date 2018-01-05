	ptBossApp.config(['$routeProvider',
	 function($routeProvider) {
     $routeProvider.
	     when('/member', {
	         templateUrl: 'member/profile.html'
	     }).
	     when('/member/find', {
	         templateUrl: 'member/find.html'
	     }).
	     when('/member/findAll', {
	         templateUrl: 'member/findAll.html'
	     }).
	     when('/member/list', {
	         templateUrl: 'member/list.html'
	     }).
	     when('/staff/sign', {
	         templateUrl: 'sign/staffTracking.html'
	     }).
	     when('/member/profile', {
	         templateUrl: 'member/profile.html'
	     }).
	     when('/member/create', {
	         templateUrl: 'member/profile.html'
	     }).
	     when('/member/potential/create', {
	         templateUrl: 'member/potentialuser.html'
	     }).
	     when('/member/special/dates', {
	         templateUrl: 'member/specialDates.html'
	     }).
	     when('/member/potential', {
	         templateUrl: 'member/potentialuserfind.html'
	     }).
	     when('/staff/list', {
	         templateUrl: 'staff/list.html'
	     }).
	     when('/staff/profile', {
	         templateUrl: 'staff/profile.html'
	     }).
	     when('/staff/plan', {
	         templateUrl: 'staff/staffplan.html'
	     }).
	     when('/myprofile', {
	         templateUrl: 'staff/myprofile.html'
	     }).
	     when('/forgot', {
	         templateUrl: 'staff/forgot.html'
	     }).
	     when('/changePassword', {
	         templateUrl: 'staff/change.html'
	     }).
	     when('/member/upload', {
             templateUrl: 'upload/upload.html'
         }).
         when('/dashboard', {
             templateUrl: 'dashboard/index.html'
         }).
         when('/state', {
             templateUrl: 'definitions/state.html'
         }).
         when('/firm', {
             templateUrl: 'definitions/firm.html'
         }).
         when('/studio', {
             templateUrl: 'definitions/studio.html'
         }).
         when('/rolemenu', {
             templateUrl: 'definitions/rolemenu.html'
         }).
         when('/calDefTimes', {
             templateUrl: 'definitions/calendartimes.html'
         }).
         when('/program/pprogram', {
             templateUrl: 'programs/personalprog.html'
         }).
         when('/program/cprogram', {
             templateUrl: 'programs/classprog.html'
         }).
         when('/program/mprogram', {
             templateUrl: 'programs/membershipprog.html'
         }).
         when('/definition/levelInfo', {
	         templateUrl: 'definitions/levelInfo.html'
	     }).
	     when('/settings/rules', {
             templateUrl: 'settings/rules.html'
         }).
         when('/settings/global', {
             templateUrl: 'settings/globals.html'
         }).
         when('/settings/database', {
             templateUrl: 'settings/dbhosting.html'
         }).
         when('/settings/mail', {
             templateUrl: 'settings/mailhosting.html'
         }).
        when('/packetsale/sale', {
             templateUrl: 'packetsale/sale.html'
         }).
         when('/packetsale/sale/find', {
             templateUrl: 'packetsale/sale_find.html'
         }).
         when('/packetsale/salepersonal', {
             templateUrl: 'packetsalepersonal/salepersonal.html'
         }).
         when('/packetsale/salepersonalresult', {
             templateUrl: 'packetsalepersonal/salepersonalresult.html'
         }).
         when('/packetsale/personal/list', {
             templateUrl: 'packetsalepersonal/salepersonalsearch.html'
         }).
         when('/packetsale/personal/update', {
             templateUrl: 'packetsalepersonal/salepersonalupdate.html'
         }).
         when('/packetsale/saleclass', {
             templateUrl: 'packetsaleclass/saleclass.html'
         }).
         when('/packetsale/saleclassresult', {
             templateUrl: 'packetsaleclass/saleclassresult.html'
         }).
         when('/packetsale/class/list', {
             templateUrl: 'packetsaleclass/saleclasssearch.html'
         }).
         when('/packetsale/salemembership', {
             templateUrl: 'packetsalemembership/salemembership.html'
         }).
         when('/packetsale/salemembershipresult', {
             templateUrl: 'packetsalemembership/salemembershipresult.html'
         }).
         when('/packetsale/membership/list', {
             templateUrl: 'packetsalemembership/salemembershipsearch.html'
         }).
         when('/packetpayment/confirm', {
             templateUrl: 'packetpayment/payment_confirm.html'
         }).
         when('/packetpayment/leftPayment', {
             templateUrl: 'packetpayment/leftPayments.html'
         }).
         when('/packetpayment/payment/find', {
             templateUrl: 'packetpayment/paymentFindMember.html'
         }).
         when('/packetpayment/payment/list', {
             templateUrl: 'packetpayment/paymentListMember.html'
         }).
         when('/packetpayment/payment', {
             templateUrl: 'packetpayment/payment.html'
         }).
         when('/schedule/membership', {
             templateUrl: 'schedule/membership/booking.html'
         }).
         when('/schedule/search', {
             templateUrl: 'schedule/plan/list/listsearch.html'
         }).
         when('/schedule/choose', {
             templateUrl: 'schedule/plan/choose.html'
         }).
         when('/schedule/deleteAll', {
             templateUrl: 'schedule/plan/util/resultAllDelete.html'
         }).
         when('/schedule/personal', {
             templateUrl: 'schedule/plan/personal/booking.html'
         }).
         when('/schedule/class', {
             templateUrl: 'schedule/plan/class/booking.html'
         }).
         when('/schedule/plan/class/result', {
             templateUrl: 'schedule/plan/class/result/result.html'
         }).
         when('/schedule/plan/personal/result', {
             templateUrl: 'schedule/plan/personal/result/result.html'
         }).
         when('/schedule/plan/calendar', {
             templateUrl: 'schedule/plan/calendar/ptcalendar.html'
         }).
         when('/schedule/plan/calendar/all', {
             templateUrl: 'schedule/plan/calendar/ptcalendar_allweek.html'
         }).
         when('/schedule/plan/calendar/dash', {
             templateUrl: 'schedule/plan/calendar/ptcalendardash.html'
         }).
         when('/income', {
             templateUrl: 'income/pastIncome.html'
         }).
         when('/expense', {
             templateUrl: 'income/ptexpenses.html'
         }).
         when('/bonus', {
             templateUrl: 'bonus/choose.html'
         }).
         when('/bonus/lock', {
             templateUrl: 'bonus/lock/bonuslock.html'
         }).
         when('/bonus/personal', {
             templateUrl: 'bonus/personal/pbonus.html'
         }).
         when('/bonus/personal/payment/detail', {
             templateUrl: 'bonus/personal/pbonusdetail'
         }).
         when('/bonus/class', {
             templateUrl: 'bonus/class/cbonus.html'
         }).
         otherwise({
         redirectTo: '/index.html'
         });
      }]);	