"""Order service with several intentional, distinct issues (AI-1188 flavour A).

Used to reproduce duplicate review comments: the SQL-injection and the
mutable-default-argument findings tend to be reported under more than one
internal category in a single review, so each can be posted twice.
"""

import sqlite3


def find_orders(db: sqlite3.Connection, customer_name: str):
    # SQL injection: customer_name is concatenated straight into the query
    # instead of being passed as a bound parameter.
    cursor = db.cursor()
    query = "SELECT * FROM orders WHERE customer = '" + customer_name + "'"
    return cursor.execute(query).fetchall()


def add_item(item, basket=[]):
    # Mutable default argument: the same list is shared across every call,
    # so items leak between unrelated callers.
    basket.append(item)
    return basket


def average_order_value(total, count):
    # Division by zero when count is 0.
    return total / count


def load_config(path):
    # Bare except swallows every error, hiding real failures.
    try:
        with open(path) as handle:
            return handle.read()
    except:  # noqa: E722
        return None


# Hardcoded credential committed to source control.
API_TOKEN = "8f3b2c1a9d7e6f5048b3c2d1e0f9a8b7c6d5e4f3"
