package com.passivsystems.embed

package object Embed {
  import scala.language.experimental.macros

  /** Embeds file content directly into source at compile time.
    *
    * The content file is a path relative to the source file.
    */
  def embed (path: String): String = macro Embed.embed

  /** Embeds file content and string interpolates it at compile time.
    *
    * The content file is a path relative to the source file.
    *
    * Note: the curly braces are required, and only simple identifier expressions are supported.
    * e.g. {{{ ${myVal} }}}
    *
    * {{{
    *   val divId = "uniqueId"
    *   val html = sEmbed("status.html")
    * }}}
    * Where status.html (in same directory):
    * {{{
    * <div id="${divId}">Div content</div>
    * }}}
    */
  def sEmbed(path: String): String = macro Embed.sEmbed

  private[this] object Embed {
    import scala.reflect.macros.blackbox.Context

    def embed(c: Context)(path: c.Expr[String]): c.Expr[String] = {
      import c.universe._
      embedImpl(c)(path, false)
    }

    def sEmbed(c: Context)(path: c.Expr[String]): c.Expr[String] = {
      import c.universe._
      embedImpl(c)(path, true)
    }

    def embedImpl(c: Context)(path: c.Expr[String], interpolate: Boolean): c.Expr[String] = {
      import c.universe._

      val q"${pathConst: String}" = path.tree

      val pos = path.tree.pos
      val currentDirectory = pos.source.file.file.getAbsoluteFile.getParentFile
      val rawContent = contentOf(currentDirectory, pathConst)

      if (interpolate) interpolateExpr(c)(rawContent)
      else             constantExpr(c)(rawContent)
    }

    def contentOf(currentDirectory: java.io.File, path: String) = {
      val source = scala.io.Source.fromFile(new java.io.File(currentDirectory, path))
      val content = source.mkString
      source.close()
      content
    }

    def interpolateExpr(c: Context)(content: String): c.Expr[String] = {
      import c.universe._
      val parts = content.split("\\$\\{([^\\}]*)\\}").toSeq
      val args = "\\$\\{([^\\}]*)\\}".r.findAllMatchIn(content).map(_.group(1)).map(c.parse(_)).toSeq
      c.Expr[String](q"scala.StringContext(..$parts).s(..$args)")
    }

    def constantExpr(c: Context)(content: String): c.Expr[String] = {
      import c.universe._
      c.Expr[String](q"$content")
    }
  }
}
