package com.kin.easynotes.presentation.components.markdown

interface MarkdownLineProcessor {
    fun canProcessLine(line: String): Boolean
    fun processLine(line: String, builder: MarkdownBuilder)
}

class CodeBlockProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean {
        return line.startsWith("```")
    }

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val codeBlock = StringBuilder()
        var index = builder.lineIndex + 1
        var isEnded = false

        while (index < builder.lines.size) {
            val nextLine = builder.lines[index]
            if (nextLine == "```") {
                builder.lineIndex = index
                isEnded = true
                break
            }
            codeBlock.appendLine(nextLine)
            index++
        }

        builder.add(CodeBlock(codeBlock.toString(), isEnded, line))
    }
}

class CheckboxProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean = line.matches(Regex("^\\[[ xX]]( .*)?"))

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val checked = line.contains(Regex("^\\[[Xx]]"))
        val text = line.replace(Regex("^\\[[ xX]] ?"), "").trim()
        builder.add(CheckboxItem(text, checked, builder.lineIndex))
    }
}


class HeadingProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean = line.startsWith("#")

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val level = line.takeWhile { it == '#' }.length
        val text = line.drop(level).trim()
        builder.add(Heading(level, text))
    }
}

class QuoteProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean = line.trim().startsWith(">")

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val level = line.takeWhile { it == '>' }.length
        val text = line.drop(level).trim()
        builder.add(Quote(level, text))
    }
}

class ListItemProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean {
        val trimmed = line.trim()
        return trimmed.startsWith("- ") || 
               trimmed.startsWith("+ ") || 
               trimmed.startsWith("* ")
    }

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val trimmed = line.trim()
        val text = when {
            trimmed.startsWith("- ") -> trimmed.removePrefix("- ").trim()
            trimmed.startsWith("+ ") -> trimmed.removePrefix("+ ").trim()
            trimmed.startsWith("* ") -> trimmed.removePrefix("* ").trim()
            else -> trimmed // Shouldn't happen due to canProcessLine check
        }
        builder.add(ListItem(text))
    }
}

class ImageInsertionProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean {
        return line.trim().startsWith("!(") && line.trim().endsWith(")")
    }

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val photoUri = line.substringAfter("!(", "").substringBefore(")")
        builder.add(ImageInsertion(photoUri))
    }
}

class LinkProcessor : MarkdownLineProcessor {
    private val urlPattern = Regex("(https?://[\\w-]+(\\.[\\w-]+)+[\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])")
    
    override fun canProcessLine(line: String): Boolean {
        return urlPattern.containsMatchIn(line)
    }

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val matches = urlPattern.findAll(line)
        val urlRanges = mutableListOf<Pair<String, IntRange>>()
        
        for (match in matches) {
            val url = match.value
            val range = match.range
            urlRanges.add(Pair(url, range))
        }
        
        if (urlRanges.isNotEmpty()) {
            builder.add(Link(line, urlRanges))
        } else {
            builder.add(NormalText(line))
        }
    }
}

class HorizontalRuleProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean {
        return line.trim() == "---"
    }

    override fun processLine(line: String, builder: MarkdownBuilder) {
        builder.add(HorizontalRule(line))
    }
}
