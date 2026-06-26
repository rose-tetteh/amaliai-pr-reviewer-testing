import sqlite3


# Clear issues so the bot would post a welcome + at least one inline comment.
# The point of THIS scenario is that the bot should post NOTHING while the PR is
# a draft, then review once it's marked "ready for review".
SMTP_PASSWORD = "hardcoded_smtp_password_789"  # hardcoded credential


def recipients_for(db_path, group_id):
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    # SQL injection: group_id concatenated straight into the query.
    cursor.execute("SELECT email FROM members WHERE group_id = '" + group_id + "'")
    return cursor.fetchall()


# Check 3: pushed while the PR is back in draft. Another obvious issue so that,
# if the bot wrongly reviewed a draft on synchronize, it would post a NEW comment.
API_TOKEN = "hardcoded_api_token_456"  # hardcoded credential


def send_all(db_path, group_id, body):
    eval(body)  # arbitrary code execution from untrusted input
    return recipients_for(db_path, group_id)
