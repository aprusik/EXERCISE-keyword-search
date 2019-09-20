import scala.collection.mutable
import scala.io.Source

object KeywordSearch extends App {
  val sentence = new StringBuilder()
  var sentenceList: mutable.HashMap[Int, String] = mutable.HashMap()
  val index: mutable.HashMap[String, mutable.Queue[Int]] = mutable.HashMap()

  var num = 0
  for (line <- Source.stdin.getLines()) {
    val ln = line.trim + " "
    var end = ln.indexOfSlice(". ")
    if (end == -1) sentence ++= ln
    else {
      while (end != -1) {
        sentence ++= ln.slice(0, end)
        val result = sentence.result() + "."
        indexSentence(result)
        sentenceList += (num -> result)
        num += 1
        sentence.clear()

        val prevEnd = end
        end = ln.indexOfSlice(". ", end + 1)
        if (end == -1) sentence ++= ln.slice(prevEnd+2, ln.length)
        else sentence ++= ln.slice(prevEnd + 2, end)
      }
    }
  }

  sentenceList.foreach(println)

  def indexSentence(str: String): Unit = {
    val words = str.split(" ").
      map(_.replaceAll("[\\W]", "")) // doesn't match "a.k.a"

    words.foreach(w => {
      if (index.contains(w)) index(w) += num
      else index += (w -> mutable.Queue(num))
    })
  }
}
