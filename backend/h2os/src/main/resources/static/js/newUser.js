async function checkPassword(){
    let passInput = document.getElementById("pass");
    let pass = passInput.value;
    let message = "";
    let color = "";
    if (!pass){
        message = "Introduzca una contraseña"
        color = "red"
    }else if (pass.length < 8){
        message = "Contraseña débil"
        color = "orange"
    }else{
        message = "Contraseña segura"
        color = "green"
    }

    const messageDiv = document.getElementById("passContent");
    messageDiv.innerHTML = message;
    messageDiv.style.color = color;
}

async function checkAge(){
    let ageInput = document.getElementById("age");
    let age = ageInput.value;
    let ageNum = parseInt(age);
    let message="";
    if (isNaN(ageNum)){
        message = "La edad debe ser un número"
    }else if(!Number.isInteger(ageNum)){
        message = "La edad debe ser un número entero"
    }else if(ageNum < 0){
        message = "La edad debe ser un número positivo"
    }
    
    const messageDiv = document.getElementById("ageContent");
    messageDiv.innerHTML = message;
    
}

async function checkPhone(){
    let phoneInput = document.getElementById("phone");
    let phone = phoneInput.value;
    let phoneNum = parseInt(phone);
    let message="";
    if (isNaN(phoneNum)){
        message = "El teléfono debe ser un número"
    }else if(!Number.isInteger(phoneNum)){
        message = "El teléfono debe ser un número entero"
    }else if(phoneNum < 0){
        message = "El teléfono debe ser un número positivo"
    }else if(phone.length != 9){
        message = "El teléfono debe tener 9 cifras"
    }
    
    const messageDiv = document.getElementById("phoneContent");
    messageDiv.innerHTML = message;
}

async function checkMail(){
    let mailInput = document.getElementById("mail");
    let mail = mailInput.value;

    const response = await fetch(`/availableMail?mail=${mail}`);
    const responseObj = await response.json();
    
    let message = responseObj.available ? "Email disponible" : "Email no disponible";
    let color = responseObj.available ? "green" : "red";

    const messageDiv = document.getElementById("mailContent");
    messageDiv.innerHTML = message;
    messageDiv.style.color = color;
}