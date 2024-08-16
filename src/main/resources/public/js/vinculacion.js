var action;

$( document ).ready(function() {
    $('#menu-vinculacion').addClass('active');
    load();
    action = $('#frmVinculacion').attr('action');
    iniciarEventos();
});

function iniciarEventos() {
    $(".aceptar-empleado").click(function () {
        let rowidAceptar = $(this).attr('id').split("-")[1];
        let actionAceptar = action + '/aceptar/' + $('#vinculacion-idTrabajo-'+rowidAceptar).text();
        $('#frmVinculacion').attr('action', actionAceptar);
    });

    $(".rechazar-empleado").click(function () {
        let rowidRechazar = $(this).attr('id').split("-")[1];
        let actionRechazar = action + '/rechazar/' + $('#vinculacion-idTrabajo-'+rowidRechazar).text();
        $('#frmVinculacion').attr('action', actionRechazar);
    });
}