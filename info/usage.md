# Usage Guide

Clide is organized into four main tabs, each serving a different productivity purpose.

## Tab Navigation

Switch between tabs at the top of the window:
- **Tasks**: Main task management with due dates and descriptions
- **Calendar**: Visual month view of tasks by due date
- **TODO Lists**: Quick checklists for organizing work
- **Notes**: Self-chat and journaling

---

## Tasks Tab

### Creating a Task

1. Fill in `Title` (required), optional `Due Date`, and optional `Description`.
2. Click `Save`.

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

### Validation

- `Title` cannot be blank
- Title max length: 120
- Description max length: 2000

Validation errors are shown in a dialog.

---

## Calendar Tab

### Viewing Tasks by Date

- Navigate months using `<` and `>` buttons
- Click `Today` to jump to current month
- Days with tasks show a count (e.g., "2 tasks")
- Click any date to see tasks for that day

### Visual Indicators

- **Blue border**: Today's date
- **Darker blue background**: Selected date
- **Task count label**: Number of tasks due on that date

### Task Details

Tasks for the selected date appear in the right panel showing:
- Status (ACTIVE or COMPLETED)
- Task title

---

## TODO Lists Tab

### Creating a List

1. Enter list name in the text field (left panel)
2. Click `+` button
3. The new list appears in the list view

### Managing Lists

- **Select a list**: Click it in the left panel
- **Delete a list**: Select it, then click `Delete`
- **Add item**: Enter text in the right panel, click `Add`

### Working with Items

- **Check off item**: Click the checkbox
- **Delete item**: Click the `X` button
- **Completed items**: Shown with strikethrough text

---

## Notes Tab

### Creating a Note

1. Type your note in the text area
2. Click `Save Note`
3. The note appears above with a timestamp

### Managing Notes

- Notes are sorted newest first
- Each note shows creation date and time
- Click `Delete` on any note to remove it
- Click `Clear` to reset the input area

### Use Cases

- Quick thoughts and reminders
- Meeting notes with timestamps
- Personal journal entries
- Self-chat for working through ideas

---

## Data Persistence

All data (tasks, TODO lists, notes) is automatically saved to the local H2 database at `data/taskdb.mv.db`. No manual save required - changes persist immediately.
