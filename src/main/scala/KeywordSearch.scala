import java.io.{FileNotFoundException, IOException}

import scala.collection.mutable
import scala.io.{Source, StdIn}

object KeywordSearch {
  var caseSensitive: Boolean = false
  var loopQuery: Boolean = true

  val sentence = new StringBuilder()
  val index: mutable.HashMap[String, Set[Int]] = mutable.HashMap()
  var sentenceList: mutable.HashMap[Int, String] = mutable.HashMap()
  var num: Int = 0

  /** Parses input from the given source file.
    *
    * Indexes sentences as they are parsed so that they are only traversed once.
    *
    * @param source the source to be read.
    */
  def parseLines(source: Source): Unit = {
    for (line <- source.getLines()) {
      // Make sure lines end in a single space for parsing them together
      val ln = line.trim + " "
      // Find end of sentence
      var end = ln.indexOfSlice(". ")

      // Continue looking for the end of a sentence and making sentences until
      // the end of the line is reached.
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

  /** Traverses each word in the sentence and indexes which sentences they are
    * present in.
    *
    * @param str the sentence having it's words indexed.
    */
  def indexSentence(str: String): Unit = {
    // Separate words and get rid of special characters
    val words = str.split(" ").
      map(_.replaceAll("[\\W]", "")) // doesn't match "a.k.a"

    // Manage case sensitivity and index words
    words.foreach(w => {
      val word = if (caseSensitive) w else w.toLowerCase
      if (index.contains(word)) index(word) += num
      else index += (word -> Set(num))
    })
  }

  /** Looks up which sentences contain the queried word.
    *
    * @param q the word to search for.
    * @return a Set of keys to find sentences in the sentenceList
    */
  def search(q: String): Set[Int] = {
    val query = if (caseSensitive) q else q.toLowerCase
    if (index.contains(query)) index(query)
    else Set()
  }

  /** Print search results to StdOut. */
  def printResults(results: Set[Int]): Unit =
    results.foreach(i => println(sentenceList(i)))

  /** Parses arguments and apply configurations. */
  def parseArgs(args: Array[String]): Unit = {
    args.slice(1, args.length).foreach({
      case "-c" => caseSensitive = true
      case "-l" => loopQuery= true
      case x => caseSensitive = {
        println(s"'$x' is an invalid argument. Valid arguments are:\n" +
          s"-c [turn case sensitivity to ON (default: OFF)]\n" +
          s"-l [turn OFF looping so that only one search query is accepted, then the program closes (default: ON)]\n"
        )
        sys.exit(0)
      }
    })
  }

  def main(args: Array[String]): Unit = {
    // Parse program arguments
    if (args.length < 1) {
      println("Usage: java -jar KeywordSearch.jar [file to search] [arguments]")
      sys.exit(0)
    }
    parseArgs(args)

    // Read and parse file
    try {
      val source = Source.fromFile(args(0))
      parseLines(source)
      source.close()
    } catch {
      case _: FileNotFoundException => println("Please input a valid file.")
      case _: IOException => println("Encountered an IO exception.")
    }

    // Search querying
    if (loopQuery) {
      var query = StdIn.readLine()
      while (query != "" || query == null) {
        val results = search(query)
        printResults(results)
        query = StdIn.readLine()
      }
    } else {
      val results = search(StdIn.readLine())
      printResults(results)
    }
  }
}
