import java.io.FileInputStream
import java.util.Properties

rootProject.extra["util"] = object {
    val localProperties: Properties
        get() = Properties().also {
            it.load(FileInputStream(rootProject.file("local.properties")))
        }

    val versionCode: Int
        get() = (project.extra["versionName"] as String).split(".").let {
            1000000 * it[0].toInt() +
                    10000 * it[1].toInt() +
                    100 * it.getOrElse(2) { "0" }.toInt() +
                    it.getOrElse(3) { "0" }.toInt()
        }

    val proguardFiles: Array<out Any>
        get() = project.file("proguard/").listFiles() ?: emptyArray()
}
