async function showMessages(){
    const button = document.getElementById("showButton");
    button.style = "none";
    n = button.getAttribute("nMsg");
    console.log("SHOWMESSAGE ACTIVADO");
    console.log(n);
    const content = document.getElementById("messageContainer");
    s = "<h3>Comentarios</h3>"
    content.innerHTML = s
    for(let i = 0; i<n; i++){
        let fetchpar = '/pagePartPoolMsg?id='+ i;
        const response = await fetch(fetchpar);
        const pagePart = await response.text();
        content.innerHTML += pagePart;
    }
}


    const button = document.getElementById("showButton");
    button.style = block;
    button.onclick = showMessages(mensajes);
    