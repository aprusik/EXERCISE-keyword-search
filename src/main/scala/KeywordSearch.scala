import scala.collection.mutable
import scala.io.{Source, StdIn}

object KeywordSearch {
  val sentence = new StringBuilder()
  var sentenceList: mutable.HashMap[Int, String] = mutable.HashMap()
  val index: mutable.HashMap[String, Set[Int]] = mutable.HashMap()
  var num = 0

  def parseLines(source: Source): Unit = {
    for (line <- source.getLines()) {
      val ln = line.trim + " "
      var end = ln.indexOfSlice(". ")
      if (end == -1) sentence ++= ln
      else {
        var prevEnd = 0
        while (end != -1) {
          sentence ++= ln.slice(prevEnd, end)
          val result = sentence.result() + "."
          indexSentence(result)
          sentenceList += (num -> result)
          num += 1
          sentence.clear()

          prevEnd = end + 2
          end = ln.indexOfSlice(". ", end + 2)
          if (end == -1) sentence ++= ln.slice(prevEnd, ln.length)
        }
      }
    }
  }

  def indexSentence(str: String): Unit = {
    val words = str.split(" ").
      map(_.replaceAll("[\\W]", "")) // doesn't match "a.k.a"

    words.foreach(w => {
      val word = w.toLowerCase // should be configurable
      if (index.contains(word)) index(word) += num
      else index += (word -> Set(num))
    })
  }

  def search(q: String): Set[Int] = {
    val query = q.toLowerCase
    if (index.contains(query)) index(query)
    else Set()
  }

  def printResults(results: Set[Int]): Unit =
    results.foreach(i => println(sentenceList(i)))

  def main(args: Array[String]): Unit = {
    val source = Source.fromFile("input.txt")

    parseLines(source)

    source.close()

    // sentenceList.foreach(println) // debug info

    val results = search(StdIn.readLine())

    printResults(results)
  }
}
