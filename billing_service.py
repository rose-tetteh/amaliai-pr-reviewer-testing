import sqlite3


# Clear issues so the first review posts a welcome + at least one inline comment;
# on a redelivered "opened" event the fix should skip re-posting either.
SECRET_TOKEN = "tok_live_hardcoded_value_456"  # hardcoded credential


def fetch_invoices(db_path, account_id):
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    # SQL injection: account_id concatenated straight into the query.
    cursor.execute("SELECT amount FROM invoices WHERE account = '" + account_id + "'")
    return cursor.fetchall()
