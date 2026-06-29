"""Request processor with three intentional issues (AI-1188 flavour B).

Reproduces an unchanged finding being re-posted after an unrelated fix:
  1. hardcoded secret  (this is what we will fix)
  2. divide-by-zero    (left untouched)
  3. eval on input     (left untouched)

After fixing only the secret, lines 2 and 3 shift down. On re-review the
divide-by-zero is correctly skipped as already-posted, but the eval finding is
re-posted as new because its de-dup fingerprint depends on an unstable category.
"""

API_SECRET = "3c9f1b7a2e8d4f60a1b5c8d2e7f3a9b48c1d6e02"


def divide(a, b):
    # Divide-by-zero: no check that b is non-zero.
    return a / b


def run(expr):
    # eval on caller-supplied input: arbitrary code execution.
    return eval(expr)
