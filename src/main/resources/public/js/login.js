$( document ).ready(function() {
    login();
    agente();
});

$('#login').click(function (e){
    $.ajax({
        type: 'POST',
        url: "/login",
        data: {
            usuario:     $('#login-usuario').val(),
            contrasenia: $('#login-contrasenia').val()
        },
        dataType: 'json',
        success: function (data) {
            if(data.respuesta == 'OK') {
                window.location.replace(data.redirect);
            }
            else{
                if (data.respuesta == 'MAIL')
                    codigo();

                mostrarError("Error", data.msjError);
            }
        }
    });
});

$('#load-signup').click(function (e){
    signup();
});

$('#button-back').click(function (e){
    login();
});

$('#signup').click(function (e){
    $.ajax({
        type: 'POST',
        url: "/login/signup",
        data: {
            tipoUsuario: $('#slicer').text(),
            nombre:      $('#signup-nombre').val(),
            apellido:    $('#signup-apellido').val(),
            tipoDoc:     $('#signup-tDocumento').val(),
            numDoc:      $('#signup-nDocumento').val(),
            mail:        $('#signup-mail').val(),
            usuario:     $('#signup-usuario').val(),
            contrasenia: $('#signup-password').val()
        },
        dataType: 'json',
        success: function (data) {
            if(data.respuesta == 'OK') {
                codigo();
            }
            else{
                mostrarError("Error", "Los errores son:", data.msjError);
            }
        }
    });
});


$('#validate').click(function (e){
    $.ajax({
        type: 'POST',
        url: "/login/validar",
        data: {
            codigo: $('#validate-codigo').val()
        },
        dataType: 'json',
        success: function (data) {
            if(data.respuesta == 'OK')
                window.location.replace(data.redirect);
            else{
                mostrarError("Error", data.msjError);
            }
        }
    });
});

$('#slicer').click(function (e){
    if($('#slicer').text() == 'Empleado')
        agente();
    else
        empleado()
});

function login(){
    $('#button-back').hide();
    $('#log-in').show();
    $('#sign-up').hide();
    $('#validation').hide();
}

function signup(){
    $('#button-back').show();
    $('#log-in').hide();
    $('#sign-up').show();
    $('#validation').hide();
}

function codigo(){
    $('#button-back').hide();
    $('#log-in').hide();
    $('#sign-up').hide();
    $('#validation').show();
}

function empleado(){
    $('#signup-apellido').show();
    $('#signup-tDocumento').show();
    $('#signup-nDocumento').show();
    $('#slicer').text('Empleado');
}

function agente(){
    $('#signup-apellido').hide();
    $('#signup-tDocumento').hide();
    $('#signup-nDocumento').hide();
    $('#slicer').text('Agente');
}

function mostrarError(titulo, mensaje){
    $('#error-titulo').text(titulo);
    $('#error-msj').text(mensaje);
    $('#modalError').modal('show');
}

function mostrarError(titulo, mensaje, lista){
    $('#error-lista').find('li').remove()
    $('#error-titulo').text(titulo);
    $('#error-msj').text(mensaje);
    $('#error-lista').append(lista);
    $('#modalError').modal('show');
}