package com.example.taskmanager.search

import com.example.taskmanager.domain.Task
import java.time.LocalDate

object TaskSearch {
    @JvmStatic
    fun filterAndRank(tasks: List<Task>, rawSearch: String?): List<Task> {
        val terms = rawSearch
            ?.trim()
            ?.lowercase()
            ?.split(Regex("\\s+"))
            ?.filter { it.isNotBlank() }
            .orEmpty()

        if (terms.isEmpty()) {
            return tasks
        }

        return tasks
            .mapNotNull { task ->
                val score = score(task, terms)
                if (score <= 0) null else task to score
            }
            .sortedWith(
                compareByDescending<Pair<Task, Int>> { it.second }
                    .thenBy { it.first.dueDate ?: LocalDate.MAX }
                    .thenByDescending { it.first.createdAt }
            )
            .map { it.first }
    }

    private fun score(task: Task, terms: List<String>): Int {
        val title = task.title.orEmpty().lowercase()
        val desc = task.description.orEmpty().lowercase()
        val combined = "$title $desc"

        if (terms.any { !combined.contains(it) }) {
            return 0
        }

        return terms.sumOf { term ->
            var value = 0
            if (title.contains(term)) {
                value += 3
            }
            if (title.startsWith(term)) {
                value += 2
            }
            if (desc.contains(term)) {
                value += 1
            }
            value
        }
    }
}
