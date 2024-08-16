var action;

$( document ).ready(function() {
    $('#menu-organizaciones').addClass('active');
    load();
    action = $('#frmOrganizacion').attr('action');
});

$("#agregar-organizacion").click(function() {
    let rowidAdd = $(this).attr('id').split("-")[1];
    $('#frmOrganizacion').attr('action', '/organizaciones/crear');

});

$(".editar-organizacion").click(function() {
    let rowidEdit = $(this).attr('id').split("-")[1];
    let id_organizacion = $(this).attr('id').split("-")[2];

    $('#organizacion-razon-social').val( $('#organizacion-razon-social-'+rowidEdit).text() );
    $('#organizacion-tipo').val( $('#organizacion-tipo-'+rowidEdit).text() );
    $('#organizacion-clasificacion').val( $('#organizacion-clasificacion-'+rowidEdit).text() );

    $('#frmOrganizacion').attr('action', '/organizaciones/modificar/' + id_organizacion);
    $('#modalOrganizacion').modal('show');
});

$(".borrar-organizacion").click(function() {
    let rowidDel = $(this).attr('id').split("-")[1];
    let id_organizacion = $(this).attr('id').split("-")[2];
    $('#org-razon-social-borrar').text($('#organizacion-razon-social-'+rowidDel).text());
    $('#frmBorrar').attr('action', '/organizaciones/eliminar/'  + id_organizacion);
    $('#modalBorrarOrganizacion').modal('show');
});

