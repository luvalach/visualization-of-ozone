SET client_encoding = 'UTF8';

INSERT INTO sources (description, url) VALUES ('Total ozone column using the DOAS technique. Station overpass data.', 'http://avdc.gsfc.nasa.gov/pub/data/satellite/Aura/OMI/V03/L2OVP/OMDOAO3/');
INSERT INTO sources (description, url) VALUES ('Surface UV irradiances. Station overpass data.', 'http://avdc.gsfc.nasa.gov/pub/data/satellite/Aura/OMI/V03/L2OVP/OMUVB/');

INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (1, 11, 'Total ozone column (DU)', 'Ozone', 'O3', 'Dobson unit', 'DU', 1);