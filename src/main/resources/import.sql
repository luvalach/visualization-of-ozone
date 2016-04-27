SET client_encoding = 'UTF8';

INSERT INTO sources (description, url, tableheaderpattern) VALUES ('Total ozone column using the DOAS technique. Station overpass data.', 'http://avdc.gsfc.nasa.gov/pub/data/satellite/Aura/OMI/V03/L2OVP/OMDOAO3/', '.*Datetime.*Ozone.*');
INSERT INTO sources (description, url, tableheaderpattern) VALUES ('Surface UV irradiances. Station overpass data.', 'http://avdc.gsfc.nasa.gov/pub/data/satellite/Aura/OMI/V03/L2OVP/OMUVB/', '.*Datetime.*Dis.*OMTO3_O3.*');

INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (1, 11, 'Total ozone column (DU)', 'Ozone', 'O3', 'Dobson unit', 'DU', 1);
INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (7, 35, 'Local Noon Time Clear Sky UV Index (dimensionless)', 'CSUVindex', 'CSUVindex', 'dimensionless', '', 2);
INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (4, 15, 'Cloud Fraction (0-100, dimensionless)', 'Cloud Fraction', 'Cld. F.', '(0-100, dimensionless)', '0-100', 1);
INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (5, 16, 'Cloud Pressure (hPa)', 'Cloud Pressure', 'Cld. P.', 'Hectopascal', 'hPa', 1);
INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (3, 14, 'Effective surface reflectivity at 360 nm (%)', 'Reflectivity', 'Ref.', '%', '%', 1);
INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (2, 9, 'Distance between the station and the CTP (km)', 'Distance', 'Dist.', 'Kilometers', 'Km', 1);
INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (12, 9, 'Distance between the station and the CTP (km)', 'Distance (OMUVB)', 'Dis', 'Kilometers', 'Km', 2);
INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (6, 24, 'Erythemal Daily Dose (J/m^2)', 'EDDOse', 'EDDOse', 'J/m^2', 'J/m^2', 2);
INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (8, 36, 'Satellite Measured Overpass UV Index (dimensionless)', 'OPUVindex', 'OPUVindex', 'dimensionless', NULL, 2);
INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (9, 39, 'Surface Albedo at 360 nm (dimensionless)', 'SufAlbedo', 'SufAlbedo', 'dimensionless', NULL, 2);
INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (10, 16, 'Total column ozone (DU)', 'OMTO3_O3', 'OMTO3_O3', 'Dobson unit', 'DU', 2);
INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (11, 17, 'Clear Sky Erythemal Daily Dose (J/m^2)', 'CSEDDose', 'CSEDDose', 'J/m^2', 'J/m^2', 2);
INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (13, 10, 'Solar Zenith Angle (degree)', 'SZA', 'SZA', 'degree', '°', 2);
INSERT INTO phenomenon_types (id, columnno, description, name, nameshortcut, unit, unitshortcut, source_id) VALUES (14, 23, 'Cloud Optical Thickness (dimensionless)', 'CldOpt', 'CldOpt', 'dimensionless', NULL, 2);
