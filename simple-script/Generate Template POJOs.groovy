import com.intellij.database.model.DasTable
import com.intellij.database.model.ObjectKind
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

import java.io.StringWriter
import java.util.HashSet

import org.apache.velocity.VelocityContext
import org.apache.velocity.Template
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.app.Velocity
import org.apache.velocity.exception.ResourceNotFoundException
import org.apache.velocity.exception.ParseErrorException
import org.apache.velocity.exception.MethodInvocationException
import org.apache.velocity.runtime.RuntimeConstants

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */

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

FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
  initTemplate()
  SELECTION.filter { it instanceof DasTable && it.getKind() == ObjectKind.TABLE }.each { generate(it, dir) }
}

Template template

def initTemplate() {
  FILE_RESOURCE_LOADER_PATH = PROJECT.getBaseDir().path
  POJO_TEMPLATE = "POJOTemplate.vm"
  RUNTIME_LOG = "D:/dbTools.log"

  // 初始化模版引擎
  VelocityEngine ve = new VelocityEngine()
  ve.setProperty(RuntimeConstants.RUNTIME_LOG, RUNTIME_LOG)
  ve.setProperty( RuntimeConstants.FILE_RESOURCE_LOADER_PATH, FILE_RESOURCE_LOADER_PATH)
  ve.init()

  // 获取模版文件
  template = ve.getTemplate(POJO_TEMPLATE)
}

def generate(table, dir) {
  def className = javaName(table.getName(), true)

  VelocityContext ctx = new VelocityContext()
  // 设置变量
  setContextProperty(ctx, table, className, dir)

  // 输出
  StringWriter sw = new StringWriter()
  template.merge(ctx, sw)

  new File(dir, className + ".java").withPrintWriter("UTF-8") { out -> out.print sw }
}

def setContextProperty(ctx, table, className, dir) {
  // 将类信息放入模板变量
  ctx.put("class", [
          "name"   : className,   // 类名
          "comment": table.comment    // 表注释
  ])

  def cmbFields = calcFields(table)
  // 将字段信息放入模板变量
  ctx.put("imports", cmbFields.imports)
  ctx.put("fields", cmbFields.fields)
}

def calcFields(table) {
  def imports = [] as HashSet
  def fields = DasUtil.getColumns(table).reduce([]) { fields, col ->
    def spec = Case.LOWER.apply(col.getDataType().getSpecification())
    def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value

    if(typeStr.contains(".")) {
      imports << typeStr
      typeStr = typeStr.substring(typeStr.lastIndexOf(".") + 1)
    }

    fields += [[
                 name : javaName(col.getName(), false),
                 type : typeStr,
                 comment: col.comment,
                 annos: ""]]

  }
  ["fields": fields, "imports": imports]
}

def javaName(str, capitalize) {
  def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
    .collect { Case.LOWER.apply(it).capitalize() }
    .join("")
    .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
  capitalize || s.length() == 1? s : Case.LOWER.apply(s[0]) + s[1..-1]
}
