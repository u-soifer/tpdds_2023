var idArea;
var idEmpleado;
var actionEmpleado;

$( document ).ready(function() {
    $('#menu-organizacion').addClass('active');
    load();
    action = $('#frmOrganizacion').attr('action');
});

$("#agregar-area").click(function() {
    $(".input-texto").val('');

    $('#frmOrganizacion').attr('action', action + '/crear');

    $('#modalAreas').modal('show');
});

$(".edit-area").click(function() {
    let rowidEdit = $(this).attr('id').split("-")[1];

    $('#nombre').val($('#nombreArea-'+rowidEdit).text());

    let actionEdit = action + '/modificar/' + $('#idArea-'+rowidEdit).text();
    $('#frmOrganizacion').attr('action', actionEdit);

    $('#modalAreas').modal('show');
});

$(".del-area").click(function() {
    let rowidDel = $(this).attr('id').split("-")[1];
    $('#nombre-area').val($('#nombreArea-'+rowidDel).text());
    let action = $('#frmEliminar').attr('action') + $('#idArea-'+rowidDel).text();
    $('#frmEliminar').attr('action', action);
    $('#modalEliminarArea').modal('show');
});

$("#edit-ubicacion").click(function() {
    $('#pais').val($('#ubi-idPais').text());
    cargarProvincias($('#ubi-idPais').text(),       $('#ubi-idProvincia').text());
    cargarMunicipios($('#ubi-idProvincia').text(),  $('#ubi-idMunicipio').text());
    cargarLocalidades($('#ubi-idMunicipio').text(), $('#ubi-idLocalidad').text());
    $('#nombre').val($('#ubi-nombre').text());
    $('#calle').val($('#ubi-calle').text());
    $('#altura').val($('#ubi-altura').text());

    let actionEdit = '/organizacion/modificarUbicacion'
    $('#frmUbicacion').attr('action', actionEdit);

    $('#modalUbicacion').modal('show');
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
        url: "/organizacion/provincias",
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
        url: "/organizacion/municipios",
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
        url: "/organizacion/localidades",
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

//VER Empleados
$( ".ver-empleados" ).click(function() {
    borrarTablaEmpleados();

    let rowidVerEmpleados = $(this).attr('id').split("-")[1];

    $.ajax({
        type:'GET',
        url: "/organizacion/empleados",
        data: {idArea: $('#idArea-'+rowidVerEmpleados).text()},
        dataType: 'html',
        success: function(data) {
            borrarTablaEmpleados();
            $('#tablaEmpleados').append(data);
            iniciarEventosTabla();
        }
    });

    idArea = $(this).attr('id').split("-")[2];
    //let action = $('#frmTramo').attr('action') + idTrayecto;
    //$('#frmTramo').attr('action', action);
});

function borrarTablaEmpleados(){
    $('#tablaEmpleados').find('tr').remove().end();
}

function iniciarEventosTabla() {
    $(".edit-empleado").click(function () {

        actionEmpleado = 'edit';
        idEmpleado = $(this).attr('id').split("-")[2];

        $.ajax({
            type: 'GET',
            url: "/organizacion/empleados/detalle",
            data: {
                idEmpleado: idEmpleado
            },
            dataType: 'json',
            success: function (data) {

                $('#organizacion-responsable').val(data.id_organizacionResponsable);
                $('#medio-transporte').val(data.medioDeTransporteDet.codMedioDeTransporte).change();
                $('#tramo-origen').val(data.id_origen);
                $('#tramo-destino').val(data.id_destino);
            }
        });

        $('#modalVerEmpleados').modal('hide');
    });

    $(".del-empleado").click(function () {
        $.ajax({
            type: 'POST',
            url: "/organizacion/empleados/eliminar",
            data: {
                idArea: idArea,
                idEmpleado: $(this).attr('id').split("-")[2]
            },
            dataType: 'html',
            success: function (data) {
                borrarTablaEmpleados();
                $('#tablaEmpleados').append(data);

                iniciarEventosTabla();
            }
        });
    });
}