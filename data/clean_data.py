import csv
from itertools import groupby

from more_itertools import quantify

with open('data/order_history.csv', 'r') as f:
    reader = csv.reader(f)
    menu_items = list(reader)

header = menu_items[0]
menu_items = menu_items[1:]

associations = {}
clean_data = []

for key, group in groupby(menu_items, lambda x: x[0]):
    group = list(group)
    ids = [x[2] for x in group]
    quantities = [x[5] for x in group]
    group[0].pop(5)
    group[0].pop(2)
    group[0].pop(0)
    associations[key] = zip(ids, quantities)
    clean_data.append(group[0])
    # print(group[0])
    # print(key, associations[key])

# with open('data/orders_clean.csv', 'w') as f:
#     writer = csv.writer(f)
#     header.pop(2)
#     header.pop(0)
#     writer.writerow(header)
#     writer.writerows(clean_data)

with open('data/order_menu_assoc.csv', 'w') as f:
    writer = csv.writer(f)
    writer.writerow(['order_id', 'menu_item_id', 'quantity'])
    for key, value in associations.items():
        for (ida, quantity) in value:
            writer.writerow([int(key)+1, int(ida)+1, quantity])