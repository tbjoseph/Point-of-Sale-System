"""
A simple python script to generate
orders data
"""

import random
from datetime import datetime
from typing import Optional

ITEM_COUNT = 21


class Order:
    """
    A simple class to represent an order
    """

    order_id: int
    order_date: datetime  # timestamp
    item_ids: list[int]
    price: float
    payment_method: Optional[str]
    quantity: list[int]

    def __init__(self, order_id: int, date: datetime):
        self.order_id = order_id
        self.order_date = date
        self.item_ids = [random.randint(1, ITEM_COUNT) for _ in range(1, 5)]
        self.payment_method = random.choice(
            ["cash", "card", "dining_dollars", "retail_swipe", "meal_plan_both"]
        )
        self.price = self.get_price()
        self.quantity = [random.randint(2, 7) // 2 for _ in self.item_ids]

    def __str__(self):
        out = ""
        price = f"{round(self.price, 2)}"
        for item_id, quantity in zip(self.item_ids, self.quantity):
            out += f"{self.order_id}, {self.order_date}, {item_id}, \
{price}, {self.payment_method}, {quantity}\n"
        return out

    def get_price(self):
        """Returns the price of an order, and sets the item_ids and quantity"""
        price = 0
        # family meals
        if random.randint(1, 100) < 1:
            price += 31.50
            # drinks
            for _ in range(random.randint(1, 3)):
                price += random.randint(17, 21) * 0.10
        else:
            # appetizers
            if random.randint(1, 100) < 3:
                price += random.randint(1, 3) * 1.90

            # entrees
            if random.randint(1, 100) < 95:
                if random.randint(1, 100) < 10:  # kids meal
                    price += 5.00
                else:
                    # normal entrees
                    price += 6.00
                    entree_price = 1.50 if random.randint(1, 100) < 10 else 2.75
                    for _ in range(random.randint(1, 3)):
                        price += entree_price
            # a-la-carte
            else:
                for _ in range(random.randint(0, 3)):
                    price += random.choice([3.70, 4.95, 7.00, 9.50, 9.70, 13.45])

            # drinks
            if random.randint(1, 100) < 50:
                price += random.randint(17, 21) * 0.10

        return price


def get_popularity(month: int, day: int, hour: int) -> int:
    """Returns the maximum number of orders for a given hour based on
    the behavior of a college clientele"""
    game_days = [(9, 1), (10, 5)]

    popularity = 60  # base popularity

    if (month, day) in game_days:
        popularity *= 5

    if hour in (12, 13, 18, 19):  # lunch and dinner
        popularity *= 2

    if month in (6, 7, 8):  # summer
        popularity /= 3

    return int(popularity)


def create_orders():
    """Create a list of orders"""
    order_id_next = 0
    for month in range(1, 13):
        for day in range(1, 31):
            try:
                for hour in range(10, 20):  # 10am - 8pm
                    popularity = get_popularity(month, day, hour)
                    for _ in range(0, random.randint(0, popularity)):
                        minute = random.randint(0, 59)
                        second = random.randint(0, 59)
                        date = datetime(2020, month, day, hour, minute, second)
                        yield Order(order_id_next, date)
                        order_id_next = order_id_next + 1
            except ValueError:
                # Skip invalid dates
                continue


def main():
    """Entry point"""
    total_price = 0
    with open("order_history.csv", "w", encoding="utf-8") as file:
        file.write("order_id, order_date, item_id, price, payment_method, quantity\n")
        for order in create_orders():
            total_price += order.price
            file.write(str(order))

    print(f"Total price: {round(total_price, 2)}")


if __name__ == "__main__":
    main()
