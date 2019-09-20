import scala.collection.mutable
import scala.io.Source

object KeywordSearch extends App {
  val sentence = new StringBuilder()
  var sentenceList: mutable.Queue[String] = mutable.Queue()

  for (line <- Source.stdin.getLines()) {
    val ln = line.trim + " "
    var end = ln.indexOfSlice(". ")
    if (end == -1) sentence ++= ln
    else {
      while (end != -1) {
        sentence ++= ln.slice(0, end)
        sentenceList += sentence.result() + "."
        sentence.clear()

        val prevEnd = end
        end = ln.indexOfSlice(". ", end + 1)
        if (end == -1) sentence ++= ln.slice(prevEnd+2, ln.length)
        else sentence ++= ln.slice(prevEnd + 2, end)
      }
    }
  }

  sentenceList.foreach(println)
}
