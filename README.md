# Embed

Scala macro for embedding file content directly into a scala source file at compile time.

## Usage

first import the macro:
```scala
import com.passivsystems.embed.Embed._
```

then can reference any file with a path relative to the current source file:
```scala
val text: String = embed("resource.txt")
```

## String interpolation

By using the ```sEmbed```, we can have similar semantics to ```s""``` String interpolation. The embedded file can refer to any variable in scope at the point of call.

Note, that the curly braces are required for all embedded expressions. e.g. ```${myVal}``` not ```$myVal```

e.g.
```scala
val guard = true
val txt = sEmbed("template.txt")
```
where template.txt:
```
the guard is: ${if (guard) "TRUE" else "FALSE"}
```

## Scala js

One possible usecase is in Scala js projects to move html into their own file.
e.g.

```scala
import com.passivsystems.embed.Embed._
import org.scalajs.dom
def el[T <: dom.raw.Element](id: String) =
    dom.document.getElementById(id).asInstanceOf[T]

val btnId = "uniqueId"
// el[dom.raw.HTMLElement]("div").innerHTML = s"""
// <button id="${btnId}">Click me</button>
// """
el[dom.raw.HTMLElement]("div").innerHTML = sEmbed("status.html")
el[dom.raw.HTMLButtonElement](btnId).onclick = { event: MouseEvent => println("Clicked!") }
```
where status.html:
```html
<button id="${btnId}">Click me</button>
```
