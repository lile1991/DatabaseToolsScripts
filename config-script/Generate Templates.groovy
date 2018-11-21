import groovy.text.*
import java.io.*
import com.intellij.database.model.DasTable
import com.intellij.database.model.ObjectKind
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil
import java.io.StringWriter
import org.apache.velocity.VelocityContext
import org.apache.velocity.Template
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.RuntimeConstants
import groovy.json.JsonSlurper

typeMapping = [
        (~/(?i)bigint/)                      : "Long",
        (~/(?i)tinyint/)                     : "Byte",
        (~/(?i)int/)                      : "Integer",
        (~/(?i)float|double|decimal|real/): "java.math.BigDecimal",
        (~/(?i)datetime|timestamp/)       : "java.util.Date",
        (~/(?i)date/)                     : "java.util.Date",
        (~/(?i)time/)                     : "java.sql.Time",
        (~/(?i)/)                         : "String"
]

// 指定配置文件基本路径
basePath = PROJECT.getBaseDir().path + "/generate"

// 读取配置
config = readConfig()


// 初始化模版引擎
VelocityEngine ve = new VelocityEngine()
ve.setProperty(RuntimeConstants.RUNTIME_LOG, config.runtimeLog)
ve.setProperty( RuntimeConstants.FILE_RESOURCE_LOADER_PATH, basePath)
ve.init()

config.modules.each { module ->
    module.templates.each { templateConfig ->
        // 获取模版文件
        Template template = ve.getTemplate(templateConfig.templateFile)

        // 设置变量
        VelocityContext ctx = new VelocityContext()

        ctx.put("module", module)
        ctx.put("packageName", templateConfig.packageName)

        SELECTION.filter { it instanceof DasTable && it.getKind() == ObjectKind.TABLE }.each {
            def table = it
            def oriClassName = javaName(table.getName().substring(table.getName().indexOf("_", 2) + 1), true)
            def className = templateConfig.classPrefix +
                    oriClassName +
                    templateConfig.classSuffix
            // 将类信息放入模板变量
            ctx.put("class", [
                    "oriName": oriClassName,    // 表名转类名
                    "name"   : className,   // 对oriClassName增加前缀/后缀后的类名
                    "comment": table.comment    // 表注释
            ])
            // 将字段信息放入模板变量
            ctx.put("fields", calcFields(table))

            // 输出
            StringWriter sw = new StringWriter()
            template.merge(ctx, sw)
            new File(PROJECT.getBaseDir().path + "/" + templateConfig.targetPath, className + "." + (templateConfig.fileExt != null ? templateConfig.fileExt : "java")).withPrintWriter("UTF-8") { out -> out.print sw }
        }
    }
}




/**
 * 读取配置config.json
 * @return 解析后的config
 */
def readConfig() {
    def configText = new File(basePath + "/config.json").text
    new JsonSlurper().parseText(configText)
}

/**
 * 处理表字段信息
 * @param table 表信息
 * @return
 */
def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())
        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        fields += [[
                           name : javaName(col.getName(), false),
                           type : typeStr,
                           comment: col.comment,
                           annos: ""]]
    }
}

/**
 * 转驼峰
 */
def javaName(str, capitalize) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1? s : Case.LOWER.apply(s[0]) + s[1..-1]
}
