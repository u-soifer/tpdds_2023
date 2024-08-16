$( document ).ready(function() {
    $('#menu-mediciones').addClass('active');
    load();
    action = $('#frmMedicion').attr('action');
});

$("#cargar-medicion").click(function() {
    $('#frmMedicion').attr('action', action + '/cargar');

    $('#modalRegistrarMediciones').modal('show');
});

$("#cargar-medicion2").click(function() {
    $(".input-texto").val('');
    $('#frmMedicion2').attr('action', action + '/cargar2');

    $('#modalRegistrarMediciones2').modal('show');
});

$("#limpiar-medicion").click(function() {
    $('#frmLimpiar').attr('action', action + '/limpiar');
    $('#modalLimpiarMediciones').modal('show');
});

$(".del-area").click(function() {
    let action = $('#frmEliminar').attr('action') + $('#organizacion-idArea-'+rowidDel).text();
    $('#frmEliminar').attr('action', action);
    $('#modalEliminarArea').modal('show');
});
