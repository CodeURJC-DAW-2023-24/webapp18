async function showMessages(n){
    const button = document.getElementById("showButton");
    button.style.display = "none";
    console.log("SHOWMESSAGE ACTIVADO");
    console.log(n);
    const content = document.getElementById("messageContainer");
    let s = "<h3>Comentarios</h3>"
    content.innerHTML = s
    for(let i = 0; i<n; i++){
        let fetchpar = '/pagePartPoolMsg?id='+ i;
        const response = await fetch(fetchpar);
        const pagePart = await response.text();
        content.innerHTML += pagePart;
    }
    s = "<div class='newCommentContainer'>\n" +
    "    <form id='newMsg' action='/newMsg' method='post'>\n" +
    "        <input type='text' id='commentInput' name='commentInput' placeholder='Escribe el comentario'><br><br>\n" +
    "        <button class='btn' type='submit'>Publicar</button>\n" +
    "    </form>\n" +
    "</div>";
    content.innerHTML += s
}