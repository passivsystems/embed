import com.passivsystems.embed.Embed._

import org.scalatest._

class EmbedTest extends FlatSpec with Matchers {

  private def contentOf(path: String) = {
    val source = scala.io.Source.fromFile(path)
    val content = source.mkString
    source.close()
    content
  }

  "Embed" should "include static file content" in {
    val txt = embed("resource.txt")
    val expectedTxt = contentOf("src/test/scala/resource.txt")
    txt should be (expectedTxt)
  }

  it should "string interpolate static file content" in {
    def test(guard: Boolean) = {
      val txt = sEmbed("sResource.txt")
      val expectedTxt = s"the guard is: ${if (guard) "TRUE" else "FALSE"}\n"
      txt should be (expectedTxt)
    }
    test(true)
    test(false)
  }
}
