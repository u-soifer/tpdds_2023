$( document ).ready(function() {
    $('#menu-medios-de-transporte').addClass('active');
    $('.subte').hide();
    $('.tren').hide();
    $('.colectivo').hide();
    load();
});

$( "#transporte_seleccionado" ).change(function() {
    switch ($( "#transporte_seleccionado" ).val()){
        case 'subte':
            $('.subte').show();
            $('.tren').hide();
            $('.colectivo').hide();
            break;
        case 'tren':
            $('.subte').hide();
            $('.tren').show();
            $('.colectivo').hide();
            break;
        default:
            $('.subte').hide();
            $('.tren').hide();
            $('.colectivo').show();
            break;
    }
});
$("#agregar-linea").click(function() {
    let rowidAdd = $(this).attr('id').split("-")[1];
    $('#frmNuevaLinea').attr('action', '/medios-de-transporte/crear');
});




