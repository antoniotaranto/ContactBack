try {
	jQuery.noConflict();
	$j = jQuery;
}catch(e) {
	alert("jQuery não está disponível");
}



function getNewRndNumber(){
	return Math.round(Math.random()*99999999999);
}


var Content_Show = function(){

	$j("div.outer")
		.css("opacity",0)
		.animate({opacity: 1},550, function(){
			$j(this).css("opacity","auto");
		});
}


		  
var InitMain = function() {
	
	try{
		Pop();	
	}catch(e){}
	
	try{
		if($j.browser.msie && $j.browser.version < 7)
			DD_belatedPNG.fix(".fixpng");
	}catch(e) {}

	
	try{
		
		$j("table.grid tr").each(function(i){
			
			if (i % 2) {
				$j(this).addClass("odd");
				
			}
			else {
				
				if($j(this).find("th").length <=0){
					
					$j(this).addClass("even");	
				}
				
			}
		});
	}catch(e) {}
	
	try{
		InitDomain();
	}catch(e){}
	
	
	
	$j("body").css("visibility","visible");
	
	
	try{
		$j(".expand").each(
			function(){
				this.onclick = function(){
					var metadados = $j(this).metadata();
					var target = metadados.target;
					
					if($j(target).is(".hidden")){
						$j(target).show("normal");
						$j(target).removeClass("hidden");
						$j(this).addClass("active");
						
					}else{
						$j(target).hide("normal");
						$j(target).addClass("hidden");
						$j(this).removeClass("active");
					}
						
					return false;	
				}
			}
		);
		
	}catch(e){}
	
	try{
		$j(".collapse").each(
			function(){
				this.onclick = function(){
					var metadados = $j(this).metadata();
					var target = metadados.target;
					
					if ($j(target).is(".hidden")) {
						$j(target).show("normal");
						$j(target).removeClass("hidden");
						$j(this).removeClass("active");
						
					}
					else {
						$j(target).hide("normal");
						$j(target).addClass("hidden");
						$j(this).addClass("active");
					}
					
					return false;
				}
			}
		);
	}catch(e){}

}

var hidePopLoadingMessage = function(){
				
	$j(".pop-container").find("div.loading").remove();
	var iframe = $j(".pop-container").find("iframe:first");
	
	var iframe_height_dim = ($j.browser.msie)?76:58;
	
	iframe.height($j(".pop-container").height()-iframe_height_dim);
	
	var iDoc = iframe[0].contentDocument;
	if (iDoc == undefined || iDoc == null)
		iDoc = iframe[0].contentWindow.document;
		
	var title = iDoc.title;
	var popTitle = $j(".pop-container").find(".pop-title span:first");
	
	if(!popTitle.text()){
		popTitle.text(title);
	}
}

var Pop = function() {
	
	$j("input.blockUI, a.blockUI").bind("click", function(){
		
		var metadata = $j(this).metadata();
		
		var height = 630;
		var width = 793;
		
		var top = Math.floor(($j(window).height()-height)/2);
		
		if(top<0){
			top = 20;
		}
			
		var left = Math.floor(($j(window).width()-width)/2);
		
		if(metadata.url.indexOf("_rnd") == -1) {
			var signal = (metadata.url.indexOf("?")>-1)?"&":"?";
			metadata.url += signal + "_rnd=" + getNewRndNumber();
		}
		
		var container = document.createElement("div");
		$j(container).addClass("pop-container");

		var header = document.createElement("h6");
			$j(header).html("<span>"+(metadata.title || "")+"</span>");
			$j(header).addClass("pop-title");
	
		var cancel_button = document.createElement("span");
			$j(cancel_button).addClass("button");
			$j(cancel_button).addClass("fechar");
			
			
		var cancel = document.createElement("a");
			$j(cancel).addClass("cancel");
			$j(cancel).attr("href","#");
			$j(cancel).attr("title","Fechar");
			$j(cancel).append("fechar");
			
			$j(cancel).bind("click", function(){
				$j.unblockUI();
				return false;
			});
		
		$j(cancel_button).append(cancel);
	
		$j(header).append(cancel_button);
		
		
		
		$j(container).append(header);
		
		var loading = document.createElement("div");
		$j(loading).addClass("loading");
		$j(loading).append("Carregando conteúdo, por favor aguarde...");
		
		$j(container).append(loading);
		
		if(metadata.iframe){
			
			var divWidth = 748;
			var divHeight = ($j(window).height() * 0.7);
			
			$j(container).width(divWidth);
			
			$j(container).height(divHeight);
			
			var divLeft = (($j(window).width()-divWidth)/2);
			var divTop = (($j(window).height()-divHeight)/2);
			
			$j(container).append("<iframe src='"+metadata.url+"' class='inside' onload='hidePopLoadingMessage();' frameborder='0'></iframe>");
			
			$j.blockUI({message: container, css:{height: '100%', width: '100%', opacity: 0}});
							
			var block = $j(container).parent();		
			block.css("left",divLeft+"px");
			block.css("top",divTop+"px");
			
			if($j.browser.msie){
				divWidth = divWidth+6;
				divHeight = divHeight+6;
			}else{
				divWidth = divWidth+2;
				divHeight = divHeight+2;
			}
		
			block.animate({width: divWidth, height: divHeight, opacity: 1, borderWidth: '1px'}, 300);
			
					
		}else{
			$j.get(metadata.url, "html", function(data){
				
				hidePopLoadingMessage();
				
				var inside = document.createElement("div");
				$j(inside).addClass("inside");
				
				$j(inside).append(data);
				
				$j(container).append(inside);
				
				$j.blockUI({message: container, css:{height: '100%', width: '100%', opacity: 0}});
						
				var block = $j(container).parent();		
				
				var divWidth = 750;
				var divHeight = ($j(window).height() * 0.7);
				
				$j(container).height(divHeight-2);
				
				var divLeft = (($j(window).width()-divWidth)/2);
				var divTop = (($j(window).height()-divHeight)/2);
				
				block.css("left",divLeft+"px");
				block.css("top",divTop+"px");
				
				block.animate({width: divWidth, height: divHeight, opacity: 1, borderWidth: '1px'}, 300);
				
			});
		}
		
					
		return false;
	});
	
}

var showBlockUI = function(block){
	var divWidth = 750;
	var divHeight = ($j(window).height() * 0.7);
	
	$j(iframe).width(divWidth-15);
	
	$j(iframe).height(divHeight-45);
	
	var divLeft = (($j(window).width()-divWidth)/2);
	var divTop = (($j(window).height()-divHeight)/2);
	
	block.css("left",divLeft+"px");
	block.css("top",divTop+"px");
	
	block.animate({width: divWidth, height: divHeight, opacity: 1, borderWidth: '1px'}, 300);
}


var ExecuteAfterLoaded = function(f) {
	var loadedImagesCount = 0;
	var contentImages = $j("div.content").find("img");
	var contentImagesCount = contentImages.length;
	
	var exec = function(){
		eval(f);
	}
	
	if(contentImagesCount>0)
		contentImages.bind("load", 
			function(){
				loadedImagesCount++;
				if(loadedImagesCount>=contentImagesCount)
					exec();
					return false;
			}
		);
		
	exec();
	
}


var title = "";

$j(document).ready(function(){
	ExecuteAfterLoaded("InitMain()");
	
	title = document.title;
	window.onresize = function(){
		if($j(window).width()<=1024){
			//$j(".sidebar.last").hide();
		}else{
			//$j(".sidebar.last").show();
		}
	}
})
