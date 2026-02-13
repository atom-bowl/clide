# Usage Guide

## Main Workflow

1. Launch the app.
2. Fill in `Title` (required), optional `Due Date`, and optional `Description`.
3. Click `Save`.

## Editing a Task

1. Click a task row in the table.
2. Update fields in the form.
3. Click `Save`.

## Deleting a Task

1. Select a task row.
2. Click `Delete`.

## Complete / Reopen

1. Select a task row.
2. Click `Toggle Complete`.

## Filtering and Search

- Use `Status` dropdown (`All`, `Active`, `Completed`)
- Use search box for title/description matching
- Search ranking prioritizes title matches

## Save Visibility Behavior

After saving:

- The app refreshes the list.
- It tries to auto-select the saved task.
- If current filters/search hide it, the view resets to `All` and clears search so the saved task is visible.

## Validation

- `Title` cannot be blank
- Title max length: 120
- Description max length: 2000

Validation errors are shown in a dialog.
