$( document ).ready(function() {
   $('#menu-reportes').addClass('active');
   $('.sector-territorial').hide();
   $('.organizacion').hide();
   $('.pais').hide();
   $('.fecha-inicio').hide();
   $('.fecha-fin').hide();
   $('.reporte_id').hide();

   switch (document.getElementById("nombre_reporte").textContent) {
   case 'st':
       $('.reporte_st').show();
       $('.reporte_to').hide();
       $('.reporte_pe').hide();
       $('.reporte_pr').hide();
       $('.reporte_m').hide();
       break;
   case 'to':
       $('.reporte_st').hide();
       $('.reporte_to').show();
       $('.reporte_pe').hide();
       $('.reporte_pr').hide();
       $('.reporte_m').hide();
       break;
   case 'cst':
       $('.reporte_st').hide();
       $('.reporte_to').hide();
       $('.reporte_pe').hide();
       $('.reporte_pr').hide();
       $('.reporte_m').show();
       break;
   case 'cnp':
       $('.reporte_st').hide();
       $('.reporte_to').hide();
       $('.reporte_pe').hide();
       $('.reporte_pr').show();
       $('.reporte_m').hide();
       break;
   case 'co':
       $('.reporte_st').hide();
       $('.reporte_to').hide();
       $('.reporte_pe').hide();
       $('.reporte_pr').hide();
       $('.reporte_m').show();
       break;
   case 'est':
       $('.reporte_st').hide();
       $('.reporte_to').hide();
       $('.reporte_pe').show();
       $('.reporte_pr').hide();
       $('.reporte_m').hide();
       break;
   case 'eo':
       $('.reporte_st').hide();
       $('.reporte_to').hide();
       $('.reporte_pe').show();
       $('.reporte_pr').hide();
       $('.reporte_m').hide();
       break;
   default: break;
   }

   load();
});

function obtenerReporte(){
    let miCanvas=document.getElementById("grafico").getContext("2d");
    var objetos = Array.from(document.getElementsByClassName("objetos"));

    var valores = Array.from(document.getElementsByClassName("valores"));

    var chart = new Chart(miCanvas, {
        type:"bar",
        data:{
            labels:objetos.map(e => e.textContent),
            datasets:[
                {
                    label:"Grafico de Barras",
                    backgroundColor:"rgb(0,187,45)",
                    borderColor:"rgb(0,147,57)",
                    data:valores.map(v => v.textContent)
                }
            ]
        }
    })
}

$( "#reporte_seleccionado" ).change(function() {
    switch ($( "#reporte_seleccionado" ).val()){
        case 'st':
            $('.sector-territorial').hide();
            $('.organizacion').hide();
            $('.pais').hide();
            $('.fecha-inicio').hide();
            $('.fecha-fin').hide();
            break;
        case 'to':
            $('.sector-territorial').hide();
            $('.organizacion').hide();
            $('.pais').hide();
            $('.fecha-inicio').hide();
            $('.fecha-fin').hide();
            break;
        case 'cst':
            $('.sector-territorial').show();
            $('.organizacion').hide();
            $('.pais').hide();
            $('.fecha-inicio').hide();
            $('.fecha-fin').hide();
            break;
        case 'cnp':
            $('.sector-territorial').hide();
            $('.organizacion').hide();
            $('.pais').show();
            $('.fecha-inicio').hide();
            $('.fecha-fin').hide();
            break;
        case 'co':
            $('.sector-territorial').hide();
            $('.organizacion').show();
            $('.pais').hide();
            $('.fecha-inicio').hide();
            $('.fecha-fin').hide();
            break;
        case 'est':
            $('.sector-territorial').show();
            $('.organizacion').hide();
            $('.pais').hide();
            $('.fecha-inicio').show();
            $('.fecha-fin').show();
            break;
        default:
            $('.sector-territorial ').hide();
            $('.organizacion').show();
            $('.pais').hide();
            $('.fecha-inicio').show();
            $('.fecha-fin').show();
            break;
    }
});
