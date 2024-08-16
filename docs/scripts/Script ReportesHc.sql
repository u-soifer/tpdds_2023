/*HC total por sector territorial*/

select st.id_sectorTerritorial, sum(o.huellaCarbon)
from `mi-impacto-ambiental`.organizacion o
join `mi-impacto-ambiental`.ubicacion u on u.id_ubicacion = o.id_ubicacion
join `mi-impacto-ambiental`.sector_territorial st on u.id_provincia = st.id_provincia or u.id_municipio = st.id_municipio
group by st.id_sectorTerritorial order by st.id_sectorTerritorial;


/*HC total por tipo de Organización (según la clasificación de la Organización)*/
select o.tipoOrganizacion, sum(o.huellaCarbon) from `mi-impacto-ambiental`.organizacion o
where o.clasificacion = 3
 group by o.tipoOrganizacion order by o.tipoOrganizacion;


/*Composición de HC total de un determinado sector territorial*/
select m.id_consumo, sum(o.huellaCarbon)
from `mi-impacto-ambiental`.organizacion o
join `mi-impacto-ambiental`.ubicacion u on u.id_ubicacion = o.id_ubicacion
join `mi-impacto-ambiental`.sector_territorial st on u.id_provincia = st.id_provincia or u.id_municipio = st.id_municipio
join `mi-impacto-ambiental`.medicion m on m.id_organizacion = o.id_organizacion
where st.id_sectorTerritorial = 150
group by m.id_consumo
order by m.id_consumo;


/* Composición de HC total a nivel país (discriminando provincias)*/
select prov.nombre, m.id_consumo,  sum(o.huellaCarbon) from `mi-impacto-ambiental`.organizacion o
join `mi-impacto-ambiental`.ubicacion u on o.id_ubicacion = u.id_ubicacion
join `mi-impacto-ambiental`.medicion m on m.id_organizacion = o.id_organizacion
join `mi-impacto-ambiental`.provincia prov on prov.id = u.id_provincia
join `mi-impacto-ambiental`.pais p on p.id = 9
group by prov.nombre, m.id_consumo
order by prov.nombre, m.id_consumo;


/*Composición de HC total de una determinada Organización*/

select m.id_consumo, sum(m.huellaCarbon) from `mi-impacto-ambiental`.organizacion o
join `mi-impacto-ambiental`.medicion m on m.id_organizacion = o.id_organizacion
where o.id_organizacion = 9
group by m.id_consumo
order by m.id_consumo;

/*Evolución de HC total de un determinado sector territorial*/

select m.periodoDeImputacion, sum(m.huellaCarbon) from `mi-impacto-ambiental`.sector_territorial st
join `mi-impacto-ambiental`.ubicacion u on u.id_provincia = st.id_provincia or u.id_municipio = st.id_municipio
join `mi-impacto-ambiental`.organizacion o on o.id_ubicacion = u.id_ubicacion
join `mi-impacto-ambiental`.medicion m on m.id_organizacion = o.id_organizacion
where m.periodicidad = 1 and st.id_sectorTerritorial = 150 and m.periodoDeImputacion between 2020 and 2023
group by m.periodoDeImputacion order by periodoDeImputacion;

/*Evolución de HC total de una determinada Organización*/

select m.periodoDeImputacion, sum(m.huellaCarbon) from `mi-impacto-ambiental`.organizacion o
join `mi-impacto-ambiental`.medicion m on m.id_organizacion = o.id_organizacion
where m.periodicidad = 1 and o.id_organizacion = 9 and m.periodoDeImputacion between 2020 and 2023
group by m.periodoDeImputacion order by m.periodoDeImputacion;
