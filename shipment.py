import csv
import random
import datetime

###### Define the materials and their cost  

materials = [ 
    #meats and grains
    'chicken', 'beef', 'shrimp', 'tofu', 'rice', 'chow mein noodles',
    #vegetables
    'broccoli', 'green beans', 'carrots', 'cabbage', 'bell peppers', 'mushrooms',
    'black pepper' , 'chili peppers',
    #drinks
    'water', 'soda pack',
    #sauce ingredients
    'soy sauce', 'vinegar', 'sugar', 'orange juice', 'garlic', 'ginger', 'pineapple juice'
    ]

materials_cost = [ 
    #meats and grains
    20, 25, 20, 20, 15, 15,
    #vegetables
    10, 12, 10, 15, 10, 12, 8 , 20,
    #drinks
    10, 50,
    #sauce ingredients
    6, 5, 5, 4, 5, 7, 7
    ]


###### Generate random shipment history data

shipment_ID = 476835
date = datetime.datetime.today().date()
shipments = []
day_offset = 0

#52 weeks in a year
for i in range(52):
    for j in range(25):
        randomItem = random.randint(0, 6)
        amount = random.randint(1, 20)

        shipment = {
            'shipment_ID': shipment_ID,
            'Date': date - datetime.timedelta(days=day_offset),
            'item': materials[randomItem],
            'Amount': amount,
            'price': materials_cost[randomItem],
            'total_price': materials_cost[randomItem] * amount,
        }

        shipments.append(shipment)
        shipment_ID -= 1
        
    day_offset += 7 #shipments are a week apart


###### Save the shipment history data to a CSV file

with open('shipment_history.csv', mode='w', newline='') as file:
    writer = csv.DictWriter(file, fieldnames=['shipment_ID', 'Date', 'item', 'Amount', 'price', 'total_price'])
    writer.writeheader()
    for shipment in shipments:
        writer.writerow(shipment)
