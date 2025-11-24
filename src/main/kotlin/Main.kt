package org.alloyreduce

import com.sun.tools.javac.tree.TreeInfo.args
import edu.mit.csail.sdg.alloy4.A4Reporter
import edu.mit.csail.sdg.parser.CompModule
import edu.mit.csail.sdg.parser.CompUtil
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import kotlinx.cli.vararg
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createTempDirectory
import kotlin.io.path.extension
import kotlin.io.path.fileVisitor
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

fun reducePreds(
    predsModule: CompModule,
    model: CompModule,
) {
    val tempDir = createTempDirectory()
}

fun loadPredicateWithModel(
    predPath: String,
    rep: A4Reporter,
): CompModule {
    val module = CompUtil.parseEverything_fromFile(rep, null, predPath)
    return module
}

fun predOrigPath(predPath: String): String {
    val path = Path(predPath)
    return "${path.nameWithoutExtension}.orig.${path.extension}"
}

fun main(args: Array<String>) {
    val parser = ArgParser("alloy-reduce")

    val modelFileName by parser
        .option(
            ArgType.String,
            fullName = "model",
            description = "The model to use when reducing predicates",
        ).required()
    val predicatePaths by parser.argument(ArgType.String, description = "The predicates to reduce").vararg()

    parser.parse(args)

    val rep = A4Reporter()
    val modelModule = CompUtil.parseEverything_fromFile(rep, null, modelFileName)

    for (predicatePath in predicatePaths) {
        File(predicatePath).copyTo(File(predOrigPath(predicatePath)))
        val predsModule = loadPredicateWithModel(predicatePath, rep)
        reducePreds(predsModule, modelModule)
    }
}
