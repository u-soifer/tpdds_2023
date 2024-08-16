$( document ).ready(function() {
    $('#menu-ubicaciones').addClass('active');
    load();
});

$("#agregar-ubicacion").click(function() {
    $(".input-texto").val('');
    borrarOpcionesProvincia();
    borrarOpcionesMunicipio();
    borrarOpcionesLocalidad();

    $('#frmUbicacion').attr('action', '/ubicaciones/crear');

    $('#modalUbicacion').modal('show');
});

$(".edit-ubicacion").click(function() {
    let rowidEdit = $(this).attr('id').split("-")[1];

    $('#pais').val($('#ubi-idPais-'+rowidEdit).text());
    cargarProvincias($('#ubi-idPais-'+rowidEdit).text(),       $('#ubi-idProvincia-'+rowidEdit).text());
    cargarMunicipios($('#ubi-idProvincia-'+rowidEdit).text(),  $('#ubi-idMunicipio-'+rowidEdit).text());
    cargarLocalidades($('#ubi-idMunicipio-'+rowidEdit).text(), $('#ubi-idLocalidad-'+rowidEdit).text());
    $('#nombre').val($('#ubi-nombre-'+rowidEdit).text());
    $('#calle').val($('#ubi-calle-'+rowidEdit).text());
    $('#altura').val($('#ubi-altura-'+rowidEdit).text());

    let actionEdit = '/ubicaciones/modificar/' + $('#ubi-idUbicacion-'+rowidEdit).text();
    $('#frmUbicacion').attr('action', actionEdit);

    $('#modalUbicacion').modal('show');
});

$(".del-ubicacion").click(function() {
    let rowidDel = $(this).attr('id').split("-")[1];
    $('#nombre-ubicacion').text($('#ubi-nombre-'+rowidDel).text());
    let actionDel = '/ubicaciones/eliminar/' + $('#ubi-idUbicacion-'+rowidDel).text();
    $('#frmEliminar').attr('action', actionDel);
    $('#modalEliminarUbicacion').modal('show');
});

$("#pais").change(function() {
    cargarProvincias($("#pais").val());
    borrarOpcionesMunicipio();
    borrarOpcionesLocalidad();
});

$("#provincia").change(function() {
    borrarOpcionesLocalidad();
    cargarMunicipios($("#provincia").val())
});

$("#municipio").change(function() {
    cargarLocalidades($("#municipio").val())
});



function cargarProvincias(idPais, sel){
    borrarOpcionesProvincia();

    $.ajax({
        type:'GET',
        url: "/ubicaciones/provincias",
        data: {id_pais: idPais},
        dataType: 'json',
        success: function( data ) {
            for (let i = 0; i< data.length; i++){
                $('#provincia').append('<option value="' + data[i].id + '" >' + data[i].nombre + '</option>' )
                if(sel != null)
                    $('#provincia').val(sel);
            }
        }
    });
}

function cargarMunicipios(idProvincia, sel){
    borrarOpcionesMunicipio();

    $.ajax({
        type:'GET',
        url: "/ubicaciones/municipios",
        data: {id_provincia: idProvincia},
        dataType: 'json',
        success: function( data ) {
            for (let i = 0; i< data.length; i++){
                $('#municipio').append('<option value="' + data[i].id + '" >' + data[i].nombre + '</option>' )
                if(sel != null)
                    $('#municipio').val(sel);
            }
        }
    });
}

function cargarLocalidades(idMunicipio, sel){
    borrarOpcionesLocalidad();

    $.ajax({
        type:'GET',
        url: "/ubicaciones/localidades",
        data: {id_municipio: idMunicipio},
        dataType: 'json',
        success: function( data ) {
            for (let i = 0; i< data.length; i++){
                $('#localidad').append('<option value="' + data[i].id + '" >' + data[i].nombre + '</option>' );
                if(sel != null)
                    $('#localidad').val(sel);
            }
        }
    });
}

function borrarOpcionesProvincia(){
    $('#provincia')
        .find('option')
        .remove()
        .end()
        .append('<option disabled selected>Provincia</option>');
}

function borrarOpcionesMunicipio(){
    $('#municipio')
        .find('option')
        .remove()
        .end()
        .append('<option disabled selected>Municipio</option>');
}

function borrarOpcionesLocalidad(){
    $('#localidad')
        .find('option')
        .remove()
        .end()
        .append('<option disabled selected>Localidad</option>');
}