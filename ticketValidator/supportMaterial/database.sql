-- WARNING: This schema is for context only and is not meant to be run.
-- Table order and constraints may not be valid for execution.

CREATE TABLE public.conexion_estacion (
  id_estacion_origen integer NOT NULL,
  id_estacion_destino integer NOT NULL,
  distancia_km numeric NOT NULL,
  CONSTRAINT conexion_estacion_pkey PRIMARY KEY (id_estacion_origen, id_estacion_destino),
  CONSTRAINT conexion_estacion_id_estacion_origen_fkey FOREIGN KEY (id_estacion_origen) REFERENCES public.estacion(id_estacion),
  CONSTRAINT conexion_estacion_id_estacion_destino_fkey FOREIGN KEY (id_estacion_destino) REFERENCES public.estacion(id_estacion)
);
CREATE TABLE public.contacto_emergencia (
  id_contacto integer NOT NULL DEFAULT nextval('contacto_emergencia_id_contacto_seq'::regclass),
  id_usuario character varying NOT NULL,
  nombres character varying NOT NULL,
  apellidos character varying NOT NULL,
  tipo_identificacion character varying,
  identificacion character varying,
  CONSTRAINT contacto_emergencia_pkey PRIMARY KEY (id_contacto),
  CONSTRAINT contacto_emergencia_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(identificacion)
);
CREATE TABLE public.equipaje (
  id_equipaje integer NOT NULL DEFAULT nextval('equipaje_id_equipaje_seq'::regclass),
  id_tiquete integer NOT NULL,
  id_vagon integer,
  peso numeric CHECK (peso <= 80::numeric),
  CONSTRAINT equipaje_pkey PRIMARY KEY (id_equipaje),
  CONSTRAINT equipaje_id_tiquete_fkey FOREIGN KEY (id_tiquete) REFERENCES public.tiquete(id_tiquete),
  CONSTRAINT equipaje_id_vagon_fkey FOREIGN KEY (id_vagon) REFERENCES public.vagon(id_vagon)
);
CREATE TABLE public.estacion (
  id_estacion integer NOT NULL DEFAULT nextval('estacion_id_estacion_seq'::regclass),
  nombre character varying NOT NULL UNIQUE,
  CONSTRAINT estacion_pkey PRIMARY KEY (id_estacion)
);
CREATE TABLE public.ruta (
  id_ruta integer NOT NULL DEFAULT nextval('ruta_id_ruta_seq'::regclass),
  id_tren integer NOT NULL,
  fecha_salida timestamp without time zone NOT NULL,
  fecha_llegada timestamp without time zone NOT NULL,
  CONSTRAINT ruta_pkey PRIMARY KEY (id_ruta),
  CONSTRAINT ruta_id_tren_fkey FOREIGN KEY (id_tren) REFERENCES public.tren(id_tren)
);
CREATE TABLE public.ruta_estacion (
  id_ruta integer NOT NULL,
  id_estacion integer NOT NULL,
  orden integer NOT NULL,
  CONSTRAINT ruta_estacion_pkey PRIMARY KEY (id_ruta, id_estacion),
  CONSTRAINT ruta_estacion_id_ruta_fkey FOREIGN KEY (id_ruta) REFERENCES public.ruta(id_ruta),
  CONSTRAINT ruta_estacion_id_estacion_fkey FOREIGN KEY (id_estacion) REFERENCES public.estacion(id_estacion)
);
CREATE TABLE public.telefono (
  id_telefono integer NOT NULL DEFAULT nextval('telefono_id_telefono_seq'::regclass),
  numero character varying NOT NULL,
  id_usuario character varying,
  id_contacto integer,
  CONSTRAINT telefono_pkey PRIMARY KEY (id_telefono),
  CONSTRAINT telefono_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(identificacion),
  CONSTRAINT telefono_id_contacto_fkey FOREIGN KEY (id_contacto) REFERENCES public.contacto_emergencia(id_contacto)
);
CREATE TABLE public.tiquete (
  id_tiquete integer NOT NULL DEFAULT nextval('tiquete_id_tiquete_seq'::regclass),
  id_usuario character varying NOT NULL,
  id_ruta integer NOT NULL,
  id_vagon integer NOT NULL,
  numero_asiento integer NOT NULL,
  categoria character varying NOT NULL CHECK (categoria::text = ANY (ARRAY['PREMIUM'::character varying, 'EJECUTIVA'::character varying, 'ESTANDAR'::character varying]::text[])),
  valor_pagado numeric NOT NULL,
  estado boolean NOT NULL,
  fecha_compra timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT tiquete_pkey PRIMARY KEY (id_tiquete),
  CONSTRAINT tiquete_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(identificacion),
  CONSTRAINT tiquete_id_ruta_fkey FOREIGN KEY (id_ruta) REFERENCES public.ruta(id_ruta),
  CONSTRAINT tiquete_id_vagon_fkey FOREIGN KEY (id_vagon) REFERENCES public.vagon(id_vagon)
);
CREATE TABLE public.tren (
  id_tren integer NOT NULL DEFAULT nextval('tren_id_tren_seq'::regclass),
  nombre character varying NOT NULL,
  tipo character varying NOT NULL CHECK (tipo::text = ANY (ARRAY['MERCEDES'::character varying, 'ARNOLD'::character varying]::text[])),
  kilometraje numeric DEFAULT 0,
  CONSTRAINT tren_pkey PRIMARY KEY (id_tren)
);
CREATE TABLE public.usuario (
  identificacion character varying NOT NULL,
  nombres character varying NOT NULL,
  apellidos character varying NOT NULL,
  contrasena character varying NOT NULL,
  tipo character varying NOT NULL CHECK (tipo::text = ANY (ARRAY['PASAJERO'::character varying, 'ADMINISTRADOR'::character varying, 'EMPLEADO'::character varying]::text[])),
  tipo_identificacion character varying NOT NULL,
  direccion character varying,
  CONSTRAINT usuario_pkey PRIMARY KEY (identificacion)
);
CREATE TABLE public.vagon (
  id_vagon integer NOT NULL DEFAULT nextval('vagon_id_vagon_seq'::regclass),
  id_tren integer NOT NULL,
  tipo character varying NOT NULL CHECK (tipo::text = ANY (ARRAY['PASAJEROS'::character varying, 'EQUIPAJE'::character varying]::text[])),
  capacidad integer,
  CONSTRAINT vagon_pkey PRIMARY KEY (id_vagon),
  CONSTRAINT vagon_id_tren_fkey FOREIGN KEY (id_tren) REFERENCES public.tren(id_tren)
);