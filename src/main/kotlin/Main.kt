package org.alloyreduce

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.vararg

fun main() {
    val parser = ArgParser("alloy-reduce")

    val modelFileName by parser.option(ArgType.String, description = "The model to use when reducing predicates")
    val commandName by parser.argument(ArgType.String, description = "The predicates to reduce").vararg()
}