async function showMessages(id, n) {
    const button = document.getElementById("showButton");
    button.style.display = "none";

    loadMsgs(id, n);
}

async function deleteMsg(idP, idM) {
    const response = await fetch('/pool/message/delete?idP=' + idP + '&idM=' + idM, {
        method: 'POST'
    });
    let answer = await response.text();
    let number = parseInt(answer);

    showMessages(idP, number);
}

async function addComment(id) {
    const message = document.getElementById("commentInput").value;
    const response = await fetch('/pool/message/add?msg=' + message + '&id=' + id, {
        method: 'POST'
    });
    let answer = await response.text();
    let number = parseInt(answer);

    showMessages(id, number);
}

async function loadMsgs(id, n) {
    let pagePart = "";

    for (let i = 0; i < n; i++) {
        let fetchpar = '/pool/message/load?idP=' + id + '&idM=' + i;
        const response = await fetch(fetchpar);
        pagePart += await response.text();
    }

    const content = document.getElementById("messageContainer");
    content.innerHTML = pagePart;
}