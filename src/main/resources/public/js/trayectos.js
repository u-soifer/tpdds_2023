var idTrayecto;
var idTramo;
var actionTrayectoCrear;
var actionTramo;

$( document ).ready(function() {
    $('#menu-trayectos').addClass('active');
    load();

    $('.vehiculo-particular').hide();
    $('.transporte-publico').hide();
    $('.servicio-contratado').hide();

    actionTrayectoCrear = $('#frmTrayecto').attr('action');
});

//TRAYECTOS
$("#agregarTrayecto").click(function() {
    $('#frmTrayecto').attr('action', '/trayectos/crear');
});

$(".editar-trayecto").click(function() {
    let rowidEdit = $(this).attr('id').split("-")[1];

    $('#trayecto-origen').val( $('#idOrigen-'+rowidEdit).text() );
    $('#trayecto-destino').val( $('#idDestino-'+rowidEdit).text() );
    $('#trayecto-nombre').val( $('#nombre-'+rowidEdit).text() );

    $('#frmTrayecto').attr('action', '/trayectos/modificar/' + $('#idTrayecto-'+rowidEdit).text());

    $('#modalNuevoTrayecto').modal('show');
});

$(".borrar-trayecto").click(function() {
    let rowidDel = $(this).attr('id').split("-")[1];
    $('#nombre-trayecto-eliminar').text($('#nombre-'+rowidDel).text());
    $('#frmEliminar').attr('action', '/trayectos/eliminar/' + $('#idTrayecto-'+rowidDel).text())
    $('#modalEliminarTrayecto').modal('show');
});

//VER TRAMOS
$( ".ver-tramos" ).click(function() {
    borrarTablaTramos();

    let rowidVerTramos = $(this).attr('id').split("-")[1];

    $.ajax({
        type:'GET',
        url: "/trayectos/tramos",
        data: {idTrayecto: $('#idTrayecto-'+rowidVerTramos).text()},
        dataType: 'html',
        success: function(data) {
            borrarTablaTramos();
            $('#tablaTramos').append(data);
            iniciarEventosTabla();
        }
    });

    idTrayecto = $(this).attr('id').split("-")[2];
    //let action = $('#frmTramo').attr('action') + idTrayecto;
    //$('#frmTramo').attr('action', action);
});

//ABM TRAMOS
$( "#guardarTramo" ).click(function() {
    if(actionTramo == 'add') {

        $.ajax({
            type: 'POST',
            url: "/trayectos/tramos/crear",
            data: {
                idTrayecto: idTrayecto,
                organizacionResponsable: $('#organizacion-responsable').val(),
                tramoOrigen: $('#tramo-origen').val(),
                tramoDestino: $('#tramo-destino').val(),
                medioTransporte: $('#medio-transporte').val(),
                tipoVehiculo: $('#tipo-vehiculo').val(),
                tipoCombustible: $('#tipo-combustible').val(),
                tipoTransporte: $('#tipo-transporte').val(),
                lineaTransporte: $('#linea-transporte').val(),
                paradaOrigen: $('#parada-origen').val(),
                paradaDestino: $('#parada-destino').val(),
                servicioContratado: $('#servicio-contratado').val(),
                capacidadServicioContratado: $('#capacidad-servicio-contratado').val(),
                capacidadVehiculoParticular: $('#capacidad-vehiculo-particular').val()
            },
            dataType: 'html',
            success: function (data) {
                borrarTablaTramos();
                $('#tablaTramos').append(data);
                iniciarEventosTabla();

                $('#modalNuevoTramo').modal('hide');
                $('#modalVerTramos').modal('show');
            }
        });
    }
    else if(actionTramo = 'edit'){
        $.ajax({
            type: 'POST',
            url: "/trayectos/tramos/modificar",
            data: {
                idTrayecto: idTrayecto,
                idTramo: idTramo,
                organizacionResponsable: $('#organizacion-responsable').val(),
                tramoOrigen: $('#tramo-origen').val(),
                tramoDestino: $('#tramo-destino').val(),
                medioTransporte: $('#medio-transporte').val(),
                tipoVehiculo: $('#tipo-vehiculo').val(),
                tipoCombustible: $('#tipo-combustible').val(),
                tipoTransporte: $('#tipo-transporte').val(),
                lineaTransporte: $('#linea-transporte').val(),
                paradaOrigen: $('#parada-origen').val(),
                paradaDestino: $('#parada-destino').val(),
                servicioContratado: $('#servicio-contratado').val(),
                capacidadServicioContratado: $('#capacidad-servicio-contratado').val(),
                capacidadVehiculoParticular: $('#capacidad-vehiculo-particular').val()
            },
            dataType: 'html',
            success: function (data) {
                borrarTablaTramos();
                $('#tablaTramos').append(data);
                iniciarEventosTabla();

                $('#modalNuevoTramo').modal('hide');
                $('#modalVerTramos').modal('show');
            }
        });
    }
});

function iniciarEventosTabla() {
    $(".edit-tramo").click(function () {

        actionTramo = 'edit';
        idTramo = $(this).attr('id').split("-")[2];

        $.ajax({
            type: 'GET',
            url: "/trayectos/tramos/detalle",
            data: {
                idTramo: idTramo
            },
            dataType: 'json',
            success: function (data) {

                $('#organizacion-responsable').val(data.id_organizacionResponsable);
                $('#medio-transporte').val(data.medioDeTransporteDet.codMedioDeTransporte).change();
                $('#tramo-origen').val(data.id_origen);
                $('#tramo-destino').val(data.id_destino);

                switch (data.medioDeTransporteDet.codMedioDeTransporte){
                    case "tp":
                        $('#tipo-transporte').val(data.medioDeTransporteDet.id_tipoTransportePublico).change();
                        $('#linea-transporte').val(data.medioDeTransporteDet.id_linea).change();
                        //('#parada-origen').val();
                        //$('#parada-destino').val();
                        break;
                    case "sc":
                        $('#servicio-contratado').val(data.medioDeTransporteDet.id_servicioContratado);
                        $('#capacidad-servicio-contratado').val(data.medioDeTransporteDet.capacidadServicioContratado);
                        break;
                    case "vp":
                        $('#tipo-vehiculo').val(data.medioDeTransporteDet.tipoVehiculo);
                        $('#tipo-combustible').val(data.medioDeTransporteDet.tipoCombustible);
                        $('#capacidad-vehiculo-particular').val(data.medioDeTransporteDet.capacidadVehiculoParticular);
                        break;
                    case "bp":
                        break;
                }
            }
        });

        $('#modalVerTramos').modal('hide');
        $('#modalNuevoTramo').modal('show');

    });

    $(".del-tramo").click(function () {
        $.ajax({
            type: 'POST',
            url: "/trayectos/tramos/eliminar",
            data: {
                idTrayecto: idTrayecto,
                idTramo: $(this).attr('id').split("-")[2]
            },
            dataType: 'html',
            success: function (data) {
                borrarTablaTramos();
                $('#tablaTramos').append(data);
                iniciarEventosTabla();

                $('#modalNuevoTramo').modal('hide');
                $('#modalVerTramos').modal('show');
            }
        });
    });
}

function borrarTablaTramos(){
    $('#tablaTramos').find('tr').remove().end();
}

//FORM TRAMOS
$('#nuevoTramo').click(function () {
    actionTramo = 'add';
    $('#medio-transporte').val('-1').change();
    $('.select-modal-tramo').val('-1');
    $('.input-texto').val('');
    $('#modalVerTramos').modal('hide');
    $('#modalNuevoTramo').modal('show');
});

$( "#medio-transporte" ).change(function() {
    switch ($( "#medio-transporte" ).val()){
        case 'bp':
            $('.tramo-origen').show();
            $('.tramo-destino').show();
            $('.vehiculo-particular').hide();
            $('.transporte-publico').hide();
            $('.servicio-contratado').hide();
            break;
        case 'vp':
            $('.tramo-origen').show();
            $('.tramo-destino').show();
            $('.vehiculo-particular').show();
            $('.transporte-publico').hide();
            $('.servicio-contratado').hide();
            break;
        case 'tp':
            $('.tramo-origen').hide();
            $('.tramo-destino').hide();
            $('.vehiculo-particular').hide();
            $('.transporte-publico').show();
            $('.servicio-contratado').hide();
            break;
        case 'sc':
            $('.tramo-origen').show();
            $('.tramo-destino').show();
            $('.vehiculo-particular').hide();
            $('.transporte-publico').hide();
            $('.servicio-contratado').show();
            break;
        default:
            $('.tramo-origen').hide();
            $('.tramo-destino').hide();
            $('.vehiculo-particular').hide();
            $('.transporte-publico').hide();
            $('.servicio-contratado').hide();
            break;
    }
});

$("#tipo-transporte").change(function() {
    $('#linea-transporte')
        .find('option')
        .remove()
        .end()
        .append('<option disabled selected value="-1">Seleccionar...</option>');

    $.ajax({
        type:'GET',
        url: "/trayectos/lineas",
        data: {
            id_tipoTransporte: $("#tipo-transporte").val()
        },
        dataType: 'json',
        success: function(data) {
            for (let i = 0; i< data.length; i++){
                $('#linea-transporte').append('<option value="' + data[i].id_medioDeTransporte + '" >' + data[i].linea + '</option>' )
            }
        }
    });
});

$("#linea-transporte").change(function() {
    $('#parada-origen')
        .find('option')
        .remove()
        .end()
        .append('<option disabled selected value="-1">Seleccionar ubicación...</option>');

    $('#parada-destino')
        .find('option')
        .remove()
        .end()
        .append('<option disabled selected value="-1">Seleccionar ubicación...</option>');

    $.ajax({
        type:'GET',
        url: "/trayectos/paradas",
        data: {
            id_medioDeTransporte: $("#linea-transporte").val()
        },
        dataType: 'json',
        success: function(data) {
            for (let i = 0; i< data.length; i++){
                $('#parada-origen').append('<option value="' + data[i].id_ubicacion + '" >' + data[i].calle + " " + data[i].altura + '</option>' )
                $('#parada-destino').append('<option value="' + data[i].id_ubicacion + '" >' + data[i].calle + " " + data[i].altura + '</option>' )
            }
        }
    });
});

//TRAMOS COMPARTIDOS
$("#tramosCompartidos").click(function() {
    $.ajax({
        type:'GET',
        url: "/trayectos/tramos/compartidos",
        data: {

        },
        dataType: 'html',
        success: function(data) {
            borrarTablaTramosCompartidos();
            $('#tabla-tramos-compartidos').append(data);
            iniciarEventosTablaTramosCompartidos();
            $('#modalVerTramos').modal('hide');
            $('#modalTramosCompartidos').modal('show');
        }
    });
});

function borrarTablaTramosCompartidos(){
    $('#tabla-tramos-compartidos').find('tr').remove().end();
}

function iniciarEventosTablaTramosCompartidos(){

    $('.compartir-trayecto').click(function (e){
        $.ajax({
            type:'POST',
            url: "/trayectos/tramos/compartir",
            data: {
                idTrayecto: idTrayecto,
                idTramo: $(this).attr('id').split("-")[1]
            },
            dataType: 'html',
            success: function(data) {
                borrarTablaTramos();
                $('#tablaTramos').append(data);
                iniciarEventosTabla();

                $('#modalTramosCompartidos').modal('hide');
                $('#modalVerTramos').modal('show');
            }
        });
    });
}