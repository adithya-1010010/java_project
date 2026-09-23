# Git Workflow

## Remote

`origin` → https://github.com/adithya-1010010/java_project.git (branch `main`).

## Rules

- One meaningful commit per phase, message exactly as defined in `implementation-plan.md` §11.
- Commit only relevant/intended files; never commit secrets, `target/`, `*.db`, or IDE files (see `.gitignore`).
- Push only after build + tests pass.
- Verify the push succeeded before stopping.
- No phase commits out of order; no next phase until previous is pushed.