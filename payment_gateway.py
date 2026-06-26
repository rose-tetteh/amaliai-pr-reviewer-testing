import sqlite3


# Clear issues so the review produces at least one inline comment, which is
# what we watch for duplication when the "opened" webhook is redelivered.
API_KEY = "sk_live_hardcoded_secret_key_123"  # hardcoded credential


def charge_customer(db_path, customer_id, amount):
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    # SQL injection: customer_id concatenated straight into the query.
    cursor.execute("SELECT card FROM customers WHERE id = '" + customer_id + "'")
    card = cursor.fetchone()
    print("charging " + str(amount) + " to " + str(card) + " with " + API_KEY)
    return card
