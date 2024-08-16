var action;

$( document ).ready(function() {
    $('#menu-contactos').addClass('active');
    load();
    action = $('#frmContacto').attr('action');
});

$("#agregar-contacto").click(function() {
    $(".input-texto").val('');

    $('#frmContacto').attr('action', action + '/crear');

    $('#modalContactos').modal('show');
});

$(".edit-contacto").click(function() {
    let rowidEdit = $(this).attr('id').split("-")[1];

    $('#nombre').val($('#contacto-nombre-'+rowidEdit).text());
    $('#mail').val($('#contacto-mail-'+rowidEdit).text());
    $('#telefono').val($('#contacto-telefono-'+rowidEdit).text());


    let actionEdit = action + '/modificar/' + $('#contacto-idContacto-'+rowidEdit).text();
    $('#frmContacto').attr('action', actionEdit);

    $('#modalContactos').modal('show');
});

$(".del-contacto").click(function() {
    let rowidDel = $(this).attr('id').split("-")[1];
    $('#nombre-contacto').text($('#contacto-nombre-'+rowidDel).text());
    let action = $('#frmEliminar').attr('action') + $('#contacto-idContacto-'+rowidDel).text();
    $('#frmEliminar').attr('action', action);
    $('#modalEliminarContacto').modal('show');
});
