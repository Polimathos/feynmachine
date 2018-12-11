package model

import org.apache.spark.mllib.linalg.{SparseMatrix, DenseMatrix}

class EncoderDecoder {
  val chars = List(
    " ", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
    "u", "v", "w", "x", "y", "z", "ñ", ",", ".", "?", "!", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
  )
  val maxMessageLength = 140

  def encode(message: String): Array[Double] ={
    val rowIndices = message.map(f => chars.indexOf(f.toString.toLowerCase))

    val encodedMatrix = new SparseMatrix(maxMessageLength,
      chars.length,
      (0 to rowIndices.length).toArray++
        Array.fill(maxMessageLength-rowIndices.length)(rowIndices.length),
      rowIndices.toArray,
      Array.fill(rowIndices.length)(1),
      isTransposed = true)

    return encodedMatrix.toArray
  }

  def decode(botSays:Array[Double]): String ={
    val botSaysMatrix = new DenseMatrix(maxMessageLength,chars.length,botSays)

  }
}
