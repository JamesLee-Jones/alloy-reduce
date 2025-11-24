package org.alloyreduce

import edu.mit.csail.sdg.alloy4.A4Reporter
import edu.mit.csail.sdg.alloy4.Err
import edu.mit.csail.sdg.parser.CompModule
import edu.mit.csail.sdg.parser.CompUtil
import edu.mit.csail.sdg.translator.A4Options
import edu.mit.csail.sdg.translator.TranslateAlloyToKodkod
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import kotlinx.cli.vararg
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createTempDirectory
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension

fun reducePreds(
    predsModule: CompModule,
    model: CompModule,
) {
    val tempDir = createTempDirectory()
}

fun loadPredicateWithModel(
    predPath: String,
    cache: MutableMap<String, String>,
    rep: A4Reporter,
): CompModule {
    val module = CompUtil.parseEverything_fromFile(rep, cache, predPath)
    return module
}

fun predOrigPath(predPath: String): String {
    val path = Path(predPath)
    return "${path.nameWithoutExtension}.orig.${path.extension}"
}

fun checkUnsat(
    runModule: CompModule,
    modelModule: CompModule,
    rep: A4Reporter,
) {
    // TODO(JLJ): Options should ultimately be customizable via command line
    for (command in runModule.allCommands) {
        println(command.toString())
        try {
            val sol = TranslateAlloyToKodkod.execute_command(rep, runModule.allReachableSigs, command, A4Options())
            if (sol.satisfiable()) {
                System.err.println("ERROR: command $command is satisfiable")
                kotlin.system.exitProcess(1)
            }
        } catch (err: Err) {
            println(err.message)
            kotlin.system.exitProcess(1)
        }
    }
}

fun main(args: Array<String>) {
    val parser = ArgParser("alloy-reduce")

    val modelFileName by parser
        .option(
            ArgType.String,
            fullName = "model",
            description = "The model to use when reducing predicates",
        ).required()
    val modulePaths by parser
        .argument(
            ArgType.String,
            description = "Alloy files containing unsatisfiable run commands to be reduced",
        ).vararg()

    parser.parse(args)

    val rep = A4Reporter()
    val cache = mutableMapOf<String, String>()
    val modelModule = CompUtil.parseEverything_fromFile(rep, cache, modelFileName)

    for (modulePath in modulePaths) {
        val predsModule = loadPredicateWithModel(modulePath, cache, rep)
        checkUnsat(predsModule, modelModule, rep)
        File(modulePath).copyTo(File(predOrigPath(modulePath)))
        reducePreds(predsModule, modelModule)
    }
}
