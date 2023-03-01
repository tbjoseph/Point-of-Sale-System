import csv
import random
import datetime

###### Define the materials and their cost  

materials = [
    'Breaded Chicken',
    'Orange Chicken Sauce',
    'Angus Steak',
    'Broccoli',
    'Onions',
    'Bell Peppers',
    'Shrimp',
    'Walnuts',
    'Chicken',
    'Teriyaki Sauce',
    'Beef',
    'Peanuts',
    'Chili Peppers',
    'Assorted Vegetalbes',
    'Szechwan Sauce',
    'Honey Sauce',
    'Sweet-Tangy Sauce',
    'Mushrooms',
    'Zucchini',
    'Light Giner Soy Sauce',
    'Pineapples',
    'Sweet Chili Sauce',
    'Greens',
    'Celery',
    'Cabbage',
    'Wheat Noodles',
    'White Rice',
    'Soy Sauce',
    'Eggs',
    'Peas',
    'Carrots',
    'Green Onions',
    'Brown Rice',
    'Chicken Egg Roll',
    'Veggie Spring Roll',
    'Cream Cheese Rangoon',
    'Dr Pepper',
    'Coca Cola',
    'Diet Coke',
    'Barq\'s Root Beer',
    'Fanta Orange',
    'Minute Maid Lemonade',
    'Powerade Mountain Berry Blast',
    'Sprite',
    'Coca Cola Cherry',
    'Fuze Raspberry Iced Tea',
    'Sweat Tea',
    'Powerade Fruit Punch',
    'Dasani',
    'Minute Maid Apple Juice',
    'Napkins',
    'Utensils',
    'Bowl',
    'Plate',
    'Bigger Plate',
    'Lids',
    'Straws'
    ]

# Meats: 20, Vegetables: 15, Miscellaneous: 10
materials_cost = [
    20,
    10,
    20,
    15,
    15,
    15,
    20,
    10,
    20,
    10,
    20,
    20,
    15,
    15,
    10,
    10,
    10,
    15,
    15,
    10,
    10,
    10,
    15,
    15,
    15,
    10,
    10,
    10,
    10,
    15,
    15,
    15,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10,
    10
    ]

materials_ID = range(len(materials))


###### Generate random shipment history data

shipment_ID = 476835
date = datetime.datetime.today().date()
shipments = []
day_offset = 0

#52 weeks in a year
for i in range(26):
    for j in range(10):
        randomItem = random.randint(0, len(materials) - 1)
        amount = random.randint(15, 20)

        shipment = {
            'item_ID': materials_ID[randomItem],
            'shipment_ID': shipment_ID,
            'Date': date - datetime.timedelta(days=day_offset),
            'item': materials[randomItem],
            'Amount': amount,
            'price': materials_cost[randomItem],
            'total_price': materials_cost[randomItem] * amount,
        }

        shipments.append(shipment)
        shipment_ID -= 1
        
    day_offset += 14 #shipments are 2 weeks apart


###### Save the shipment history data to a CSV file

with open('shipment_history.csv', mode='w', newline='') as file:
    writer = csv.DictWriter(file, fieldnames=['item_ID', 'shipment_ID', 'Date', 'item', 'Amount', 'price', 'total_price'])
    writer.writeheader()
    for shipment in shipments:
        writer.writerow(shipment)
