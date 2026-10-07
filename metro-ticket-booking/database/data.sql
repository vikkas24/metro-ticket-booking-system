INSERT INTO stations (name, code, line, station_order) VALUES
('Bhayandar','BHY','Western Line',1),('Mira Road','MRD','Western Line',2),('Dahisar','DHS','Western Line',3),
('Borivali','BVI','Western Line',4),('Kandivali','KDV','Western Line',5),('Malad','MLD','Western Line',6),
('Goregaon','GRG','Western Line',7),('Andheri','AND','Western Line',8),('Jogeshwari','JOG','Western Line',9),
('Ram Mandir','RMD','Western Line',10),('Bandra','BND','Western Line',11),('Khar Road','KHR','Western Line',12),
('Santacruz','SNZ','Western Line',13),('Vile Parle','VLP','Western Line',14),('Dadar','DDR','Western Line',15)
ON CONFLICT (code) DO NOTHING;
