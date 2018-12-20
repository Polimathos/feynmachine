import scala.io.Source
import java.io.PrintWriter
/*


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

*/





    /*    dropRight(1).(x => print(x.drop(1).dropRight(1))).toString
  )
}*/

