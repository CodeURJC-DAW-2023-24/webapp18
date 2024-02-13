async function showMessages(n){
    const button = document.getElementById("showButton");
    button.style.display = "none";
    console.log("SHOWMESSAGE ACTIVADO");
    console.log(n);
    loadMsgs(n)
}
async function deleteMsg(id){
    const response = await fetch('/deleteMsg?id='+id, {
        method: 'POST'
    });
    let answer = await response.text();
    const number = answer.size;
    console.log(answer);
    loadMsgs(number);
}

async function addComment(){
    const s = document.getElementById("commentInput").value;
    const response = await fetch('/newMsg?msg='+s, {
        method: 'POST'
    });
    let answer = await response.text;
    let number = parseInt(answer);
    console.log(number);
    console.log(answer);
    loadMsgs(number);
}
async function loadMsgs(n){
    const content = document.getElementById("messageContainer");
    let s = "<h3>Comentarios</h3>"
    content.innerHTML = s
    console.log(n)
    for(let i = 0; i<n; i++){
        console.log("relalizando rutina de pagepart")
        let fetchpar = '/pagePartPoolMsg?id='+ i;
        const response = await fetch(fetchpar);
        const pagePart = await response.text();
        content.innerHTML += pagePart;
    }
    s = "<div class='newCommentContainer'>\n" +
    "    <form id='newMsg'>\n" +
    "        <input type='text' id='commentInput' name='commentInput' placeholder='Escribe el comentario'><br><br>\n" +
    "        <button class='btn' onclick = 'addComment()'>Publicar</button>\n" +
    "    </form>\n" +
    "</div>";
    content.innerHTML += s
}