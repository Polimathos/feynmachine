
import java.io.File


import com.ogmacorp.ogmaneo._

import org.apache.spark.mllib.linalg.{DenseMatrix, SparseMatrix}

val chars = List(
  " ","a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
  "u", "v", "w", "x", "y", "z", "ñ", ",", ".", "?", "!", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
)
val maxMessageLength = 140


val message = "aa"

val rowIndices = message.map(f => chars.indexOf(f.toString.toLowerCase))

val encodedMatrix = new SparseMatrix(chars.length,
  maxMessageLength,
  (0 to rowIndices.length).toArray++
    Array.fill(maxMessageLength-rowIndices.length)(rowIndices.length),
  rowIndices.toArray,
  Array.fill(rowIndices.length)(1),
  isTransposed = false
)


val a =(('a' to 'z') ++ ('0' to '9') ++ ("ñ!,.? ").toCharArray).map(_.toString).toList

a.dropRight(1)
encodedMatrix.toArray.slice(39,44)

"hello my name is hello".contains("hell o")


