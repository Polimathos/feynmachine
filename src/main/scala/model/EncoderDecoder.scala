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

    encodedMatrix.toArray
  }

  def parseBot(rawmessage:String,answer:String):String={
    if (rawmessage.tail.isEmpty) {
      return answer
    }
    if (rawmessage.head.equals('(')) {
      val index = rawmessage.tail.substring(0,rawmessage.tail.indexOf(",")).toInt
      parseBot(rawmessage.tail, answer + chars.slice(index, index+1).apply(0))
    }
    else {
      parseBot(rawmessage.tail,answer)
    }
  }

  def decode(botSays:Array[Double]): String ={
    val botSaysMatrix = new DenseMatrix(maxMessageLength,chars.length,botSays)

  }
}
