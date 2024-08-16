$( document ).ready(function() {
    $('#menu-agentes-sectoriales').addClass('active');
    load();
});

$(".editar-agente-sectorial").click(function() {
    let rowidEdit = $(this).attr('id').split("-")[1];
    let id_agenteSectorial = $(this).attr('id').split("-")[2];

    $('#agente-sectorial-nombre-editar').val( $('#agente-sectorial-nombre-editar-'+rowidEdit).text() );
    $('#agente-sectorial-provincia-editar').val( $('#agente-sectorial-provincia-editar-'+rowidEdit).text() );
    $('#agente-sectorial-municipio-editar').val( $('#agente-sectorial-municipio-editar-'+rowidEdit).text() );

    $('#frmEditarAgenteSectorial').attr('action', '/agentes-sectoriales/modificar/' + id_agenteSectorial);
    $('#modalEditarAgenteSectorial').modal('show');
});

$( "#sector_T" ).change(function() {
    switch ($( "#sector_T" ).val()){
        case 'provincia':
            $('.provincia').show();
            $('.municipio').hide();
            break;
        default:
            $('.provincia').show();
            $('.municipio').show();
            break;
    }
});

$(".rechazar-solicitud").click(function() {
    let id_agenteSectorial = $(this).attr('id').split("-")[2];
    $('#frmRechazarSolicitud').attr('action', '/agentes-sectoriales/rechazar-solicitud/agente/' + id_agenteSectorial);
    $('#modalRechazarSolicitud').modal('show');
});

$(".aceptar-solicitud").click(function() {
    let id_agenteSectorial = $(this).attr('id').split("-")[2];
    $('#frmAceptarSolicitud').attr('action', '/agentes-sectoriales/aceptar-solicitud/agente/' + id_agenteSectorial);
    $('#modalAceptarSolicitud').modal('show');
});

$(".editar-sector-territorial").click(function() {
    let rowidEdit = $(this).attr('id').split("-")[1];
    let id_sectorTerritorial = $(this).attr('id').split("-")[2];

    $('#sector-provincia').val( $('#sector-provincia-'+rowidEdit).text() );
    $('#sector-municipio').val( $('#sector-municipio-'+rowidEdit).text() );

    $('#frmSector').attr('action', '/agentes-sectoriales/modificar/' + id_sectorTerritorial);
    $('#modalSector').modal('show');
});

$(".borrar-sector-territorial").click(function() {
    //let rowidDel = $(this).attr('id').split("-")[1];
    let id_sectorTerritorial = $(this).attr('id').split("-")[2];

    //$('#sector-territorial').text($('#sector-territorial-'+rowidDel).text());
    $('#frmBorrarSectorTerritorial').attr('action', '/agentes-sectoriales/eliminar/sector/' + id_sectorTerritorial);
    $('#modalBorrarSectorTerritorial').modal('show');
});