function sendExcelFiles(objectId){
    var url = "/ptboss/ExcelFileUploadServlet";
    
    var fileInputElement=document.getElementById(objectId);
	// HTML file input user's choice...
	//formData.append("fileId", fileInputElement.files[0].name+"-ali");
	var files = fileInputElement.files;
	
	for(var i=0;i<files.length;i++){
		var formData = new FormData();
		formData.append("userfile", files[i]);
		var fileId=Math.floor((Math.random() * 1000000) + 1)
		var fileName=files[i].name;
		var request = new XMLHttpRequest();
		request.open("POST", url+"?fileId="+fileId+"&fileName="+fileName+"&usrId="+usrRId+"&firmIdx="+$("#firmIdxF").val());
		addExcelFile(fileName, fileId);
		request.send(formData);
	}
	window.setTimeout("ajaxExcelFunction();", 800);
	
}


function ajaxExcelFunction()
{  
   var url = "/AbaxStudio/ExcelFileUploadServlet";
 
   if (window.XMLHttpRequest)        // Non-IE browsers
   {
      req = new XMLHttpRequest();
      req.onreadystatechange = processExcelStateChange;
 
      try{
         req.open("GET", url, true);
      }catch (e){
            alert(e);
      }
      req.send(null);
   }
   else if (window.ActiveXObject)    // IE Browsers
   {
      req = new ActiveXObject("Microsoft.XMLHTTP");
 
      if (req) 
      {
            req.onReadyStateChange = processExcelStateChange;
            req.open("GET", url, true);
            req.send();
      }
   }
}


function processExcelStateChange()
{
   if (req.readyState == 4)
   {
      if (req.status == 200) // OK response
      {
         var xml = req.responseXML;
 		 if(xml==null){
 		 	window.setTimeout("ajaxExcelFunction();", 100);
 		 	return;
 		 }
         
         var files = xml.getElementsByTagName("file");
         var sendFlag=false;
         if(files.length==0){
         	sendFlag=true;
         }
         
         for(var i=0;i<files.length;i++){
         	var file = files[i];
			var fileId =file.getElementsByTagName("fileId")[0].firstChild.data;
			var fileName =file.getElementsByTagName("fileName")[0].firstChild.data;
			
			var _id=fileId;
			
			if(document.getElementById(_id)==null){
				continue;
			}
			var isNotFinished =file.getElementsByTagName("finished")[0];
			var myBytesRead =file.getElementsByTagName("bytes_read")[0];
			var myContentLength =file.getElementsByTagName("content_length")[0];
			var myPercent =file.getElementsByTagName("percent_complete")[0];
			   
			// Check to see if it's even started yet
			if ((isNotFinished == null) && (myPercent == null)){
				sendFlag=true;
			}else{
				// It's started, get the status of the upload
				if (myPercent != null){
					myPercent = myPercent.firstChild.data;
					//document.getElementById(_id).style.width = myPercent + "%";
					document.getElementById(_id).style.width = myPercent + "%";
					$("#"+_id).attr('aria-valuenow',myPercent);
					sendFlag=true;
				}else{
					
					document.getElementById(_id).style.width ="100%";
					//document.getElementById(_id).style.width ="100%";
					$("#"+_id+"_Div").remove();
					$("#excelFileUploadUrl").val("");
					$(".bootstrap-filestyle").find("input").eq(0).val("");
					window.setTimeout(endofExcelUpload(), 5000);
				}
			}
			
		}
        if(sendFlag){
			window.setTimeout("ajaxExcelFunction();", 100);
	
		}
         
      }
      else
      {
            alert(req.statusText);
      }
   }
}


function endofExcelUpload(){
	$("#excelFileUploadUrl").val("");
	dataStatu();
}

function dataStatu(){
	
	$.ajax({
	  	  type:'POST',
	  	  url: "/ptboss/ModulExcelSessionServlet",
	  	  dataType: 'json', 
	  	  cache:true
	  	}).done(function(res) {
	  		if(res.result!=null){
	  			
	  			if(res.result.errorStatu=="0"){
	  				window.setTimeout("dataStatu();", 100);
	  			}
	  			if(res.result.loadStatu!=null)
	  			    $("#fileLog").html(res.result.excelStatu+"    "+res.result.loadStatu);
	  			else
	  				$("#fileLog").html(res.result.excelStatu);	
	  			
	  		}
	  	});
	
}



function addExcelFile(fileName,fileId){
	var _id=fileId;
	var str=""+
	"	<div id='"+_id+"_Div'  style='width:500px;height:40px;vertical-align: middle;border: 1px rgb(220,220,200) solid;background-color: rgb(245,245,245);display: table-cell;'>"+
	"		<table cellpadding='0' cellspacing='0' border='0' width=100% style='padding: 0px; margin: 0px;'>"+
	"			<tr>"+
	"				<td style='padding: 0px; margin: 0px;'>"+
	"				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+fileName+
	"				</td>"+
	"				<td style='width:150px;padding: 0px; margin: 0px;'>"+
	"				"+
	/*"					<div style='padding:2px;width:95%;height:10px;vertical-align: middle;border: 1px rgb(153,153,153) solid;background-color: white;display: table;'>"+
	"						<div id='"+_id+"' style='width:0px;height:100%;vertical-align: middle;background-color: rgb(143,175,255)'>"+
	"						"+
	"						</div>"+
	"					</div>"+
	*/
	"					<div class='progress'>"+
	"					<div id='"+_id+"' class='progress-bar progress-bar-info' role='progressbar' aria-valuenow='0' aria-valuemin='0' aria-valuemax='100' style='width: 0%'>"+
	"						<span class='sr-only'>0%</span>"+
	"					</div>"+
	"				</div>"+
	
	
	"				</td>"+
	"			</tr>"+
	"		</table>"+
	
	"	</div> <br>  "+
	
	"";
	
	/*
	 * 
	 * <div class="progress">
							<div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%">
								<span class="sr-only">40%</span>
							</div>
						</div>
	 */
	
	document.getElementById("fileExcelList").innerHTML=document.getElementById("fileExcelList").innerHTML+str;

}

 
 
function initExcel(){

	var url = "/ptboss/ExcelFileUploadServlet?init=0&userName="+userName+"&password="+password+"&usrId="+usrRId;
 
   if (window.XMLHttpRequest)        // Non-IE browsers
   {
      var req1 = new XMLHttpRequest();
      
 
      try{
         req1.open("POST", url, true);
      }catch (e){
            alert(e);
      }
      req1.send(null);
   }
   else if (window.ActiveXObject)    // IE Browsers
   {
      var req1 = new ActiveXObject("Microsoft.XMLHTTP");
 
      if (req1) 
      {
            req1.open("POST", url, true);
            req1.send();
      }
   }

}
window.setTimeout("initExcel();", 1000);