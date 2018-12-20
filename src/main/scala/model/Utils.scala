package model

object Utils {
/*  import scala.io.Source
  import java.io.PrintWriter

  val lines = Source.fromFile("/home/mgonzalo/Escritorio/research/dialogue datasets/" +
    "cornell_movie_dialogs_corpus/cornell movie-dialogs corpus/movie_conversations.txt").getLines.toList

  var conversations:List[String]= List()

  val s = new PrintWriter("/home/mgonzalo/Escritorio/research/dialogue datasets/" +
    "cornell_movie_dialogs_corpus/cornell movie-dialogs corpus/conversationsIndices.txt")

  for (l <- lines){
    println(l.replaceAll("""\+\+\+\$\+\+\+""",";").
      split(" ; ").
      last.
      drop(2).
      dropRight(2).
      toString.
      split("', '").sliding(2).foreach(x => {
      s.println(x.head)
      s.println(x.last)
    })
    )
  }
  s.close()

  val movieLines = Source.fromFile("/home/mgonzalo/Escritorio/research/dialogue datasets/" +
    "cornell_movie_dialogs_corpus/cornell movie-dialogs corpus/movie_lines.txt").getLines.toList

  val movieIndices = Source.fromFile("/home/mgonzalo/Escritorio/research/dialogue datasets/" +
    "cornell_movie_dialogs_corpus/cornell movie-dialogs corpus/conversationsIndices.txt").getLines.toList

  val s = new PrintWriter("/home/mgonzalo/Escritorio/research/dialogue datasets/" +
    "cornell_movie_dialogs_corpus/cornell movie-dialogs corpus/conversationsTraining.txt")

  val movieDict = movieLines.map(x => x.split(""" \+\+\+\$\+\+\+ """)).map(x => (x.head,x.last)).toMap


  movieIndices.foreach(ind => {
    s.println(movieDict(ind))
  })


  s.close()*/
}
