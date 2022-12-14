create table tipo(
	id Serial primary key,
	nombre text
);

create table fuerteAnte(
	tipo_id int,
	tipo_id_fuerte int,
	primary key(tipo_id, tipo_id_fuerte)
);

create table debilAnte(
	tipo_id int,
	tipo_id_debil int,
	primary key(tipo_id, tipo_id_debil)
);

create table pokemon(
	id serial primary key,
	nombre text, 
	saludMaxima int,
	saludActual int,
	disponibilidad boolean
);

create table tipoPokemon(
	pokemon_id int, 
	tipo_id int,
	primary key(pokemon_id, tipo_id)
);

create table pokemonSabe(
	pokemon_id int,
	movimiento_id int,
	primary key (pokemon_id, movimiento_id)
);

create table movimientos(
	id serial primary key,
	nombre text,
	descripcion text,
	dañoCuracion int,
	tipo_id int
);

create table entrenador(
	ci text primary key,
	nombre text,
	apellido text
);

create table acciones(
	id serial primary key,
	vidaActual int,
	numeroTurno int,
	tipoAccion text,
	entrenador_ci text,
	pokemon_id int,
	movimiento_id int
);

create table historial(
	acciones_id int,
	combate_id int,
	primary key (acciones_id, combate_id)
);

create table combates(
	id serial primary key,
	fecha date, 
	hora time
);

alter table fuerteAnte
add foreign key (tipo_id) references tipo(id),
add foreign key (tipo_id_fuerte) references tipo(id)
on delete cascade on update cascade;

alter table debilAnte
add foreign key (tipo_id) references tipo(id),
add foreign key (tipo_id_debil) references tipo(id)
on delete cascade on update cascade;

alter table tipoPokemon
add foreign key (pokemon_id) references pokemon(id),
add foreign key (tipo_id) references tipo(id) 
on delete cascade on update cascade;

alter table pokemonSabe
add foreign key (pokemon_id) references pokemon(id),
add foreign key (movimiento_id) references movimientos(id) 
on delete cascade on update cascade;

alter table movimientos
add foreign key (tipo_id) references tipo(id)
on delete cascade on update cascade;

alter table acciones
add foreign key (entrenador_ci) references entrenador(ci),
add foreign key (pokemon_id) references pokemon(id),
add foreign key (movimiento_id) references movimientos(id) 
on delete cascade on update cascade;

alter table historial 
add foreign key (acciones_id) references acciones(id),
add foreign key (combate_id) references combates(id)
on delete cascade on update cascade;

------------------
------Vista-------
------------------

create view tiposDePokemon as (
	select p.nombre, gettipos(t.pokemon_id) from tipopokemon t 
	join pokemon p on p.id = t.pokemon_id
	group by t.pokemon_id, p.nombre
);

select * from tiposdepokemon;

------------------
--Procedemientos--
------------------

create or replace procedure registrarPokemon(
	p_nombre text,
	p_saludMaxima int,
	tipos_id int[]
) 
as 
$$
declare 
	i int;
	pokemon_id int;
begin 
	select nextval(pg_get_serial_sequence('pokemon', 'id')) into pokemon_id;

	insert into pokemon values (pokemon_id, p_nombre, p_saludMaxima, p_saludMaxima, false);
	
	foreach i in array tipos_id 
	loop
		call asignarTipoPokemon(pokemon_id, i);
	end loop;
end
$$
language plpgsql;

create or replace procedure insActTipo(
	p_id int,
	p_nombre text
)
as 
$$
begin 
	if (p_id is null) then 
		insert into tipo values (default, p_nombre);
	else
		
		if not exists (select * from tipo t where id = p_id) then
			raise exception 'El id no existe en la base de datos';
		end if;
		
		update tipo 
		set nombre = p_nombre 
		where id = p_id;
	end if;
end
$$
language plpgsql;

create or replace procedure insActEntrenador(
	p_ci text,
	p_nombre text,
	p_apellido text
)
as 
$$
begin 
	insert into entrenador values (p_ci, p_nombre, p_apellido) 
	on conflict (ci) do
	update set nombre = p_nombre, apellido = p_apellido;
end
$$
language plpgsql;

create or replace procedure insActMovimiento(
	p_id int,
	p_nombre text,
	p_descripcion text,
	p_dañocuracion int,
	p_tipo_id int
)
as
$$
begin 
	if (p_id is null) then 
		insert into movimientos values (default, p_nombre, p_descripcion, p_dañocuracion, p_tipo_id);
	else
		update movimientos 
		set nombre = p_nombre, descripcion = p_descripcion, dañocuracion = p_dañocuracion, tipo_id = p_tipo_id
		where id = p_id;
	end if;
end
$$
language plpgsql;

create or replace procedure insActCombate(
	p_id int,
	p_fecha date,
	p_hora time
)
as 
$$
begin 
	if (p_id is null) then
		insert into combates values (default, now()::date, now()::time);
	else 
		update combates 
		set fecha = p_fecha, hora = p_hora
		where id = p_id;
	end if;
end
$$
language plpgsql;

create or replace procedure registrarDebilAnte(
	p_tipo_id int,
	p_tipo_id_debil int
)
as 
$$
begin 
	insert into debilante values (p_tipo_id, p_tipo_id_debil)
	on conflict do nothing;
end
$$ 
language plpgsql;

create or replace procedure registrarFuerteAnte(
	p_tipo_id int,
	p_tipo_id_fuerte int
)
as 
$$
begin 
	insert into debilante values (p_tipo_id, p_tipo_id_fuerte)
	on conflict do nothing;
end
$$ 
language plpgsql;

create or replace procedure asignarTipoPokemon(
	p_pokemon_id int,
	p_tipo_id int
)
as 
$$
begin 
	insert into tipopokemon values (p_pokemon_id, p_tipo_id)
	on conflict do nothing;
end
$$
language plpgsql;

create or replace procedure enseñarMovimiento(	
	p_pokemon_id int,
	p_movimiento_id int
)
as 
$$
begin 
	insert into pokemonsabe values (p_pokemon_id, p_movimiento_id) on conflict do nothing;
end
$$
language plpgsql;

create or replace procedure olvidarMovimiento(	
	p_pokemon_id int,
	p_movimiento_id int
)
as 
$$
begin 
	delete from pokemonsabe where pokemon_id = p_pokemon_id and movimiento_id = p_movimiento_id;
end
$$
language plpgsql;

create or replace procedure registrarAccion(
	p_numeroTurno int,
	p_tipoAccion text,
	p_entrenador_ci text,
	p_pokemon_id int,
	p_movimiento_id int,
	p_combate_id int
)
as 
$$
declare 
	v_vidaActual int;
	v_accion_id int;
begin 
	select saludactual into v_vidaActual from pokemon p where id = p_pokemon_id;
	select nextval(pg_get_serial_sequence('acciones', 'id')) into v_accion_id;
	
	insert into acciones values(v_accion_id, v_vidaActual, p_numeroTurno, p_tipoAccion, p_entrenador_ci, p_pokemon_id, p_movimiento_id);
	call agregarHistorial(v_accion_id, p_combate_id);
end
$$
language plpgsql;

create or replace procedure agregarHistorial(
	p_accciones_id int,
	p_combate_id int 
)
as 
$$
begin 
	insert into historial values (p_accciones_id, p_combate_id);
end
$$
language plpgsql;

create or replace procedure realizarMovimiento(
    p_entrenador_id1 text,
    p_entrenador_id2 text,
    p_pokemon_id1 int,
    p_pokemon_id2 int,
    p_movimiento_id int,
    p_numeroTurno int,
    p_combate_id int
) as
$$
declare
    dañoMovimiento int;
    tipoAtaque int;
    tipoPokemonEnemigo int;
  	v_saludMax int;
    v_saludAct int;
    v_saludActPoke2 int;
begin
    select dañocuracion into dañoMovimiento from movimientos m where id = p_movimiento_id;
    select tipo_id into tipoAtaque from movimientos m where id = p_movimiento_id;
    select saludmaxima into v_saludMax from pokemon where id = p_pokemon_id1;
    select saludactual into v_saludAct from pokemon where id = p_pokemon_id1;
   	select saludactual into v_saludActPoke2 from pokemon where id = p_pokemon_id2;

  	call registraraccion(p_numeroTurno, 'Movimiento', p_entrenador_id1, p_pokemon_id1, p_movimiento_id, p_combate_id);

  	if (dañoMovimiento > 0) then
        if (v_saludAct + dañoMovimiento < v_saludMax) then
             update pokemon
             set saludactual = saludactual + dañoMovimiento
             where id = p_pokemon_id1;
        elsif (v_saludAct + dañoMovimiento >= v_saludMax) then
             update pokemon
             set saludactual = v_saludMax
             where id = p_pokemon_id1;
       	end if;
    else
        if (select tipoAtaque in (select * from getdebilidades(p_pokemon_id2))) then
            dañoMovimiento := dañoMovimiento * 2;
        elsif (select tipoAtaque in (select * from getresistente(p_pokemon_id2))) then
            dañoMovimiento := dañoMovimiento / 2;
        end if;
       
	    if v_saludActPoke2 + dañoMovimiento < 0 then
	       	update pokemon
	        set saludactual = 0
	        where id = p_pokemon_id2;
	    else
	    	update pokemon
	        set saludactual = saludactual + dañoMovimiento
	        where id = p_pokemon_id2;
	    end if;
    end if;
   
   	if (esDerrotado(p_pokemon_id2)) then
   		call finalizarCombate(p_numeroTurno, p_pokemon_id1, p_pokemon_id2, p_entrenador_id1, p_entrenador_id2, p_combate_id);
   	end if;	
end
$$
language plpgsql;

create or replace procedure iniciarCombate(
	p_pokemon_id1 int,
	p_pokemon_id2 int,
	p_entrenador_id1 text,
	p_entrenador_id2 text,
	p_combate_id int
) as 
$$
begin
	call registraraccion(0, 'Inicio', p_entrenador_id1, p_pokemon_id1, null, p_combate_id); 
	call registraraccion(0, 'Inicio', p_entrenador_id2, p_pokemon_id2, null, p_combate_id); 

	update pokemon 
	set disponibilidad = false
	where id in (p_pokemon_id1, p_pokemon_id2);
end 
$$
language plpgsql;

create or replace procedure finalizarCombate(
	p_numero_turno int,
	p_pokemon_id1 int,
	p_pokemon_id2 int,
	p_entrenador_id1 text,
	p_entrenador_id2 text,
	p_combate_id int
) as 
$$
declare 

begin
	call registraraccion(p_numero_turno, 'Final', p_entrenador_id1, p_pokemon_id1, null, p_combate_id); 
	call registraraccion(p_numero_turno, 'Final', p_entrenador_id2, p_pokemon_id2, null, p_combate_id); 

	if (not esDerrotado(p_pokemon_id1)) then
		update pokemon 
		set disponibilidad = true
		where id = p_pokemon_id1;
	else
		update pokemon 
		set disponibilidad = true
		where id = p_pokemon_id1;
	end if;
	
end 
$$
language plpgsql;

create or replace procedure curarPokemonesDebilitados()
as 
$$
begin 
	update pokemon 
	set saludactual = saludmaxima 
	where saludactual = 0;
end 
$$
language plpgsql;

create or replace procedure curarPokemones()
as 
$$
begin 
	update pokemon 
	set saludactual = saludmaxima 
	where saludactual <> saludmaxima;
end 
$$
language plpgsql;

-------------
--Funciones--
-------------

create or replace function esDebil(
	p_tipo_id int,
	p_tipo_id_debil int
) returns boolean as 
$$
begin 
	if exists (select from debilante d where tipo_id = p_tipo_id and tipo_id_debil = p_tipo_id_debil) then
		return true;
	end if;

	return false;
end
$$
language plpgsql;

create or replace function getDebilidades(
	p_pokemon_id int
)
returns setof int
as 
$$
declare 
	i_fila record;
	j_fila record;
	v_tipo_id int;
begin 

	for i_fila in (select tipo_id from tipopokemon t where pokemon_id = p_pokemon_id) loop 
		v_tipo_id := i_fila.tipo_id;
	
		for j_fila in (select tipo_id_debil from debilante d where tipo_id = v_tipo_id) loop
			return next j_fila.tipo_id_debil;
		end loop;
		
	end loop;

end
$$
language plpgsql;

create or replace function getResistente(
	p_pokemon_id int
)
returns setof int
as 
$$
declare 
	i_fila record;
	j_fila record;
	v_tipo_id int;
begin 

	for i_fila in (select tipo_id from tipopokemon t where pokemon_id = p_pokemon_id) loop 
		v_tipo_id := i_fila.tipo_id;
	
		for j_fila in (select tipo_id_fuerte from fuerteante f where tipo_id = v_tipo_id) loop
			return next j_fila.tipo_id_fuerte;
		end loop;
		
	end loop;

end
$$
language plpgsql;

create or replace function getVersus(
	p_combate_id int 
) 
returns text 
as 
$$
declare 
	i_fila record;
	v_nombre text;
	resultado text;
	separador text = 'vs';
begin
	
	for i_fila in (select * from historial h join acciones a on a.id = h.acciones_id where h.combate_id = p_combate_id and a.tipoaccion ilike 'inicio') loop 
		select nombre into v_nombre from pokemon p where id = i_fila.pokemon_id;
		resultado := concat(resultado, v_nombre, separador);
		separador := '';
	end loop;
	
	return resultado;
end 
$$
language plpgsql;

create or replace function getTipos(
	p_pokemon_id int
) returns text 
as 
$$
declare 
	v_fila record;
	separador text = '';
	resultado text;
	v_nombre text;
begin
	for v_fila in (select * from tipopokemon t where pokemon_id = p_pokemon_id) loop 
		select nombre into v_nombre from tipo t where id = v_fila.tipo_id;
		resultado := concat(resultado, separador, v_nombre);
		separador := ', ';
	end loop;

	return resultado;
end 
$$
language plpgsql;

create or replace function esDerrotado(
	p_pokemon_id int
) returns boolean 
as 
$$
declare 
	vida int;
begin
	
	select saludactual into vida from pokemon p where id = p_pokemon_id;
	
	if (vida <= 0) then
		return true;
	end if;
	
	return false;
end
$$
language plpgsql;

create or replace function getSiguienteCombateId()
returns int
as 
$$
declare
	v_id int;
begin 
	select nextval(pg_get_serial_sequence('combates', 'id')) into v_id;
	return v_id;
end 
$$
language plpgsql;

create or replace function getNumMovAprendidos(
	p_pokemon_id int
)
returns int
as 
$$
declare 
	num int;
begin
	select count(*) into num from pokemonsabe p where pokemon_id = p_pokemon_id;
	return num;
end 
$$
language plpgsql;

create or replace function conoceMovimientos(
	p_pokemon_id int,
	p_movimiento_id int
) returns text 
as 
$$
declare 
	num int;
	resultado text;
begin
	
	select count(*) into num from pokemonsabe p where pokemon_id = p_pokemon_id and movimiento_id = p_movimiento_id;
	
	if num = 1 then
		return 'X';
	end if;
	
	return '';
end 
$$
language plpgsql;

------------
--Triggers--
------------

create or replace function validarTipoPokemon()
returns trigger 
as 
$$
declare 
	numTiposAsignados int;
	tipo_idAsignado int;
begin 
	select count(*) into numTiposAsignados from tipopokemon t where pokemon_id = new.pokemon_id;

	if (numTiposAsignados + 1 > 2) then
		raise exception 'Este pokemon ya tiene asignados 2 tipos';
	end if;

	select tipo_id into tipo_idAsignado from tipopokemon t where pokemon_id = new.pokemon_id;

	if (numTiposAsignados = 0) then
		return new;
	elsif (numTiposAsignados = 1 and not esDebil(tipo_idAsignado, new.tipo_id)) then 
		return new;
	else
		raise exception 'No se puede asignar el tipo ya que el pokemon ya tiene un tipo y el nuevo es su debilidad';
	end if;
end
$$
language plpgsql;

create trigger validarTipoPokemon
before insert on tipopokemon
for each row 
execute procedure validarTipoPokemon();

create or replace function validarMismoTipoPokeMov()
returns trigger 
as 
$$
declare 
	v_tipo_movimiento int;
begin 
	select tipo_id into v_tipo_movimiento from movimientos m where id = new.movimiento_id;
	
	if (select v_tipo_movimiento in (select tipo_id from tipopokemon t where pokemon_id = new.pokemon_id)) then
		return new;
	else 
		raise exception 'Un pokemon no puede aprender un movimiento que no es de su(s) tipos';
	end if;
end
$$
language plpgsql;

create trigger validarMismoTipoPokeMov
before insert on pokemonsabe
for each row 
execute procedure validarMismoTipoPokeMov();

create or replace function validarUpdateTipoMov()
returns trigger 
as 
$$
begin 
	if (new.tipo_id <> old.tipo_id) then
		raise exception 'No se puede cambiar el tipo de un movimiento';
	end if;
	
	return new;
end
$$
language plpgsql;

create trigger validarUpdateTipoMov
before update on movimientos
for each row 
execute procedure validarUpdateTipoMov();

-- Trigger que valida que el pokemon tenga un ataque y que no se pase de 4

create or replace function validarPokemonSabe()
returns trigger 
as 
$$
declare 
	numMovimientos int;
begin 
	IF (TG_OP = 'INSERT') THEN 

		select count(*) into numMovimientos from pokemonsabe p where pokemon_id = new.pokemon_id;
	
		if (numMovimientos > 4) then
			raise exception 'El pokemon ya sabe 4 movimientos';
		else
			update pokemon 
			set disponibilidad = true
			where id = new.pokemon_id;
		end if;
	
		return new;
	
	ELSIF (TG_OP = 'DELETE') THEN
		
		select count(*) - 1 into numMovimientos from pokemonsabe p where pokemon_id = old.pokemon_id;
	
		if (numMovimientos <= 0) then
			update pokemon 
			set disponibilidad = false
			where id = old.pokemon_id;
		end if;
		
		return old;
	END IF;
end
$$
language plpgsql;

create trigger validarPokemonSabe
after insert on pokemonsabe
for each row 
execute procedure validarPokemonSabe();

create trigger validarPokemonSabe2
before delete on pokemonsabe
for each row 
execute procedure validarPokemonSabe();

drop trigger validarPokemonSabe on pokemonsabe;

-- Trigger que valida la vida

create or replace function validarSaludActualPokemon()
returns trigger 
as 
$$
begin 
	if (new.saludactual = 0) then
		new.disponibilidad := false;
	end if;
	return new;
end
$$
language plpgsql;

create trigger validarSaludActualPokemon
before update on pokemon
for each row 
execute procedure validarSaludActualPokemon();

-- Trigger que valida que ambos se cumplan

create or replace function validarDisponibilidad()
returns trigger 
as 
$$
begin 
	if (new.saludactual > 0 and getNumMovAprendidos(new.id) > 0) then
		new.disponibilidad = true;
	else 
		new.disponibilidad = false;
	end if;

	return new;	
end
$$
language plpgsql;

create trigger validarDisponibilidad
after update on pokemon 
for each row 
when (new.disponibilidad = true)
execute procedure validarDisponibilidad();

drop trigger validarDisponibilidad on pokemon;

select * from pokemon p 

---------
--Datos--
---------

call insacttipo(null, 'Electrico');
call insacttipo(null, 'Fuego');
call insacttipo(null, 'Agua');
call insacttipo(null, 'Planta');
call insacttipo(null, 'Roca');
call insacttipo(null, 'Volador');
call insacttipo(null, 'Lucha');

call registrarDebilAnte(2, 3);
call registrarDebilAnte(2, 5);
call registrarDebilAnte(6, 7);
call registrarDebilAnte(4, 2);

call registrarpokemon('Charizard', 100);
call asignartipopokemon(1, 2); 
call asignartipopokemon(1, 6); 

call enseñarmoviento(1, 2);
call enseñarmoviento(1, 4);
call enseñarmoviento(5, 1);
call enseñarmoviento(5, 3);

select * from tipopokemon t;
select * from debilante d;
select * from pokemon p;

select * from getdebilidades(1);

SELECT *, gettipos(id) as tipos FROM pokemon

select * from movimientos m 

CREATE EXTENSION tablefunc;

select * from crosstab(
'
	select p2.nombre, m.nombre, conoceMovimientos(p2.id, m.id) from pokemonsabe p 
	right join pokemon p2 on p2.id = p.pokemon_id 
	join movimientos m on m.id = p.movimiento_id 
	order by 1,2;
'
,
'
	select nombre from movimientos m order by 1;
') as (Pokemon TEXT, Ascuas TEXT, Ataque_Ala TEXT, Chispa TEXT, Impactrueno TEXT, Lanzarrocas TEXT, Llamarada TEXT);

select p2.nombre, m.nombre, conoceMovimientos(p2.id, m.id) from pokemonsabe p 
right join pokemon p2 on p2.id = p.pokemon_id 
join movimientos m on m.id = p.movimiento_id 
order by 1,2;

select p2.nombre, m.nombre from pokemonsabe p 
right join pokemon p2 on p2.id = p.pokemon_id 
join movimientos m on m.id = p.movimiento_id 
order by 1,2;

select * from pokemonsabe p where p.pokemon_id = 10 and movimiento_id = 6

select nombre from movimientos m order by 1;





