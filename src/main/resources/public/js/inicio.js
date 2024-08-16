$( document ).ready(function() {
    $('#menu-inicio').addClass('active');
    load();
});

$('#new-request').click(function(e){
    $('#modal_nueva-solicitud').modal('show');
});