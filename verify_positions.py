"""
Ground-truth diff-position verification for the multi-hunk scenario.

Feeds the REAL UserService.java patch (as GitHub's API exposes it, i.e.
starting at the first @@) through:
  * the bot's NEW _build_line_position_map (corrected arithmetic), and
  * a replica of the OLD algorithm (the off-by-one / pre-fix behaviour),
then prints both, so the values can be cross-checked against where the bot's
inline comments actually land on the open PR.

Run with the bot's venv python:
    ../amaliai-pr-reviewer/.venv/Scripts/python.exe verify_positions.py
"""
import os
import re
import sys

BOT_REPO = os.path.join(os.path.dirname(__file__), "..", "amaliai-pr-reviewer")
sys.path.insert(0, os.path.abspath(BOT_REPO))

from services.pr_service import PRReviewService  # noqa: E402

# Exactly what GitHub's File.patch returns: the unified diff body for the file,
# starting at the first @@ header (no diff/index/--- /+++ preamble).
PATCH = (
    "@@ -8,8 +8,9 @@ public class UserService {\n"
    '     private static final String DB_PASSWORD = "admin123";\n'
    " \n"
    "     public Connection connect() throws SQLException {\n"
    '+        String url = System.getenv("DB_URL");\n'
    "         return DriverManager.getConnection(\n"
    '-                "jdbc:mysql://localhost:3306/users",\n'
    "+                url,\n"
    '                 "root",\n'
    "                 DB_PASSWORD\n"
    "         );\n"
    "@@ -36,8 +37,13 @@ public class UserService {\n"
    "     }\n"
    " \n"
    "     public String getUserRole(String userId) {\n"
    '+        if (userId == null) return "guest";\n'
    '         if (userId.equals("1")) return "admin";\n'
    '         if (userId.equals("2")) return "user";\n'
    '         return "guest";\n'
    "     }\n"
    "+\n"
    "+    public void shutdown() {\n"
    '+        System.out.println("UserService shutting down");\n'
    "+    }\n"
    " }"
)

# The added (+) lines a reviewer would comment on, by NEW-FILE line number.
CHANGED_LINES = {
    11: 'String url = System.getenv("DB_URL");',
    13: "url,",
    40: 'if (userId == null) return "guest";',
    45: "(blank line)",
    46: "public void shutdown() {",
    47: 'System.out.println("UserService shutting down");',
    48: "}",
}


def old_algorithm(patch):
    """Replica of the pre-fix _get_diff_position mapping (note the `- 1`)."""
    lines = patch.split("\n")
    current_line = 0
    line_mapping = {}
    for i, line in enumerate(lines):
        if line.startswith("@@"):
            m = re.search(r"\+(\d+)", line)
            if m:
                current_line = int(m.group(1)) - 1
            continue
        if line.startswith("+") or not (line.startswith("-") or line.startswith("\\")):
            line_mapping[current_line + 1] = i
            current_line += 1
    # The old code returned position - 1.
    return {ln: pos - 1 for ln, pos in line_mapping.items()}


def main():
    svc = PRReviewService.__new__(PRReviewService)  # no GitHub auth needed
    new_map = svc._build_line_position_map(PATCH)
    old_map = old_algorithm(PATCH)

    print("=" * 64)
    print("UserService.java  -  two hunks  -  diff position per CHANGED line")
    print("=" * 64)
    print(f"{'new-file line':>13} | {'OLD pos':>7} | {'NEW pos':>7} | code")
    print("-" * 64)
    for ln in sorted(CHANGED_LINES):
        old = old_map.get(ln, "-")
        new = new_map.get(ln, "-")
        flag = "  <-- shifted" if old != new else ""
        print(f"{ln:>13} | {str(old):>7} | {str(new):>7} | {CHANGED_LINES[ln]}{flag}")
    print("-" * 64)
    print("Full NEW map (new-file line -> diff position):")
    print("  " + ", ".join(f"{k}:{v}" for k, v in sorted(new_map.items())))


if __name__ == "__main__":
    main()
