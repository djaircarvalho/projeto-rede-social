$(document).ready(function(){

$.urlParam = function(name){
	 var results = new RegExp('[\?&]' + name +
	'=([^&#]*)').exec(window.location.href);
	 if (results==null){
	 return null;
	 }
	 else{
	 return results[1] || 0;
	 }
	}	

loadAllUsers();

});

// function getUserId() {
// 	var url = window.location.href;
// 	var matches = url.match(/\/users\/([^\/]+)/);
// 	if (matches) {
// 	    return matches[1];    // "whatever"
// 	} else {
// 	    return null;
// 	}
// }

function loadAllUsers() {
	  var id_user = $.urlParam("id_user");	
	  $.get("http://localhost:8080/projeto-rede-social/webapi/users", function(users, status){
	  	$(users).each(function(){
	  		var caboco = this;
			if(caboco.id == id_user){
				var nav = $("<section>");
				nav.attr("id", caboco.id);
				nav.appendTo("#dados_do_caboco");
				//TODO adicionar uma imagem com o avatar dele.
				$("<h3>").text(caboco.name).appendTo(nav);
				$("<p>").text(caboco.email).appendTo(nav);
				$("<p>").text(caboco.website).appendTo(nav);
				$("<p>").text(caboco.company.name).appendTo(nav);
				listPosts (this.posts);
			}
			else{
				var lista = $("#lista_dos_todos");
				var item = $("<li>").appendTo(lista);
				lista.attr("class", "lista_usuario");
				var link = $("<a>").text(caboco.name);
				link.attr("href", "index.html?id_user="+caboco.id);
				link.appendTo(item);
			}	
	  	});
        
        
    });


	function listPosts (posts) {
		$(posts).each(function(){
			var post = this;
			var section = $("<section>");
			section.attr("id", post.id);
			section.attr("class", "post");
			section.appendTo("#posts");
			$("<h3>").text(post.title).appendTo(section);
			$("<p>").text(post.body).appendTo(section);	
		});
		
	}
}


