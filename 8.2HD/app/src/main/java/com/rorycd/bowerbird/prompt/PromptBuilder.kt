package com.rorycd.bowerbird.prompt

object PromptBuilder {
    fun generatePrompt(options: PromptOptions): String {
        val builder = StringBuilder()
        builder.append("Analyze the image. ")

        // If there are conditions, build an "if" statement
        if (!options.conditions.isNullOrEmpty()) {
            builder.append("If the image doesn't/isn't ")
            builder.append(options.conditions.joinToString(" and "))
            builder.append(", then respond with 'false'. Otherwise, ")
        }

        builder.append("Return a JSON object in this format: \n{\n")
        options.promptOutputs.forEachIndexed { idx, outputType ->
            val subject = options.subjectDescriptions?.getOrNull(idx) ?: "the main subject"
            val key = outputType.name.lowercase()

            val result = when (outputType) {
                PromptOutputs.FILENAME -> "A brief (<20 chars) filename-friendly description of the $subject.'\n"
                PromptOutputs.TAG -> "1-10 comma separated tags describing $subject\n"
            }

            builder.append("  \"$key\": \"$result\"")
            if (idx < options.promptOutputs.size - 1) builder.append(",")
            builder.append("\n")
        }

        builder.append("}")
        return builder.toString()
    }
}
