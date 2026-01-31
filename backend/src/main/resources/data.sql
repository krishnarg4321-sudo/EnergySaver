-- Seed data for appliances
-- This script populates the appliances table with common household appliances

INSERT INTO appliances (name, category, default_rated_watts, description) VALUES
-- Kitchen Appliances
('Refrigerator', 'Kitchen', 150, 'Standard household refrigerator'),
('Microwave Oven', 'Kitchen', 1200, 'Standard microwave oven'),
('Electric Stove', 'Kitchen', 2000, 'Electric cooking stove'),
('Dishwasher', 'Kitchen', 1800, 'Automatic dishwasher'),
('Coffee Maker', 'Kitchen', 1000, 'Electric coffee maker'),
('Toaster', 'Kitchen', 800, 'Electric toaster'),
('Blender', 'Kitchen', 400, 'Kitchen blender'),
('Electric Kettle', 'Kitchen', 1500, 'Electric water kettle'),

-- Entertainment
('Television (LED 50")', 'Entertainment', 100, '50-inch LED television'),
('Television (LCD 32")', 'Entertainment', 60, '32-inch LCD television'),
('Gaming Console', 'Entertainment', 150, 'Video game console'),
('Sound System', 'Entertainment', 80, 'Home audio system'),
('Cable Box', 'Entertainment', 30, 'Cable/satellite receiver'),

-- Climate Control
('Air Conditioner (Window)', 'Climate Control', 1000, 'Window unit AC'),
('Air Conditioner (Central)', 'Climate Control', 3500, 'Central air conditioning'),
('Space Heater', 'Climate Control', 1500, 'Electric space heater'),
('Ceiling Fan', 'Climate Control', 75, 'Ceiling fan'),
('Dehumidifier', 'Climate Control', 300, 'Electric dehumidifier'),

-- Laundry
('Washing Machine', 'Laundry', 500, 'Electric washing machine'),
('Dryer', 'Laundry', 3000, 'Electric clothes dryer'),
('Iron', 'Laundry', 1200, 'Electric iron'),

-- Computing
('Desktop Computer', 'Computing', 200, 'Desktop PC'),
('Laptop Computer', 'Computing', 60, 'Laptop computer'),
('Monitor (24")', 'Computing', 40, 'Computer monitor'),
('Printer', 'Computing', 50, 'Inkjet printer'),
('Router/Modem', 'Computing', 10, 'Internet router'),

-- Lighting
('LED Bulb (10W)', 'Lighting', 10, 'LED light bulb'),
('Incandescent Bulb (60W)', 'Lighting', 60, 'Incandescent light bulb'),
('Halogen Lamp', 'Lighting', 300, 'Halogen floor lamp'),

-- Personal Care
('Hair Dryer', 'Personal Care', 1500, 'Electric hair dryer'),
('Curling Iron', 'Personal Care', 50, 'Hair curling iron'),
('Electric Shaver', 'Personal Care', 15, 'Electric razor'),

-- Miscellaneous
('Vacuum Cleaner', 'Miscellaneous', 1200, 'Electric vacuum cleaner'),
('Water Heater (Electric)', 'Miscellaneous', 4500, 'Electric water heater'),
('Garage Door Opener', 'Miscellaneous', 350, 'Automatic garage door'),
('Aquarium Equipment', 'Miscellaneous', 150, 'Fish tank with pump and heater')
ON CONFLICT DO NOTHING;
