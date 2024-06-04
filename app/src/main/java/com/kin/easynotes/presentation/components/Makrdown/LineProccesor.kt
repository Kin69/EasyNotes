package com.kin.easynotes.presentation.components.Makrdown

import androidx.core.net.toUri

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

        builder.add(CodeBlock(codeBlock.toString(), isEnded))
    }
}



class ImageInsertionProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean {
        return line.trim().startsWith("!(") && line.trim().endsWith(")")
    }

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val photoUri = line.substringAfter("!(", "").substringBefore(")").toUri()
        builder.add(ImageInsertion(photoUri))
    }
}

class CheckboxProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean = line.trim().matches(Regex("\\[[ xX]]( .*)?"))

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val checked = line.contains(Regex("\\[[Xx]]"))
        val text = line.replace(Regex("\\[[ xX]] ?"), "").trim()
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
    override fun canProcessLine(line: String): Boolean = line.startsWith("- ")

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val text = line.removePrefix("- ").trim()
        builder.add(ListItem(text))
    }
}
