async function showMessages(id) {
    const button = document.getElementById("showButton");
    button.style.display = "none";

    loadMsgs(id);
}

async function deleteMsg(idP, idM) {
    const response = await fetch('/pool/message/delete?idP=' + idP + '&idM=' + idM, {
        method: 'POST'
    });

    showMessages(idP);
}

async function addComment(id) {
    const message = document.getElementById("commentInput").value;
    const response = await fetch('/pool/message/add?msg=' + message + '&id=' + id, {
        method: 'POST'
    });

    showMessages(id);
}

async function loadMsgs(id) {
    let fetchpar = '/pool/message/load?id=' + id;
    const response = await fetch(fetchpar);
    pagePart = await response.text();

    const content = document.getElementById("messageContainer");
    content.innerHTML = pagePart;
}