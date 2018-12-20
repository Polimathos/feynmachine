package model

import org.apache.spark.mllib.linalg.{SparseMatrix}

class EncoderDecoder{
  val chars = (" ñ!,.?-".toCharArray
    ++ ('a' to 'z')
    //++ ('0' to '9')
    ).map(_.toString).toList

  val maxMessageLength = Configuration.maxMessageLength

  def encode(message: String): Array[Double] ={
    val messageCut = message.take(maxMessageLength)
    val rowIndices = messageCut.map(f => chars.indexOf(f.toString.toLowerCase) match{
      case -1 => 0
      case x => x
    })

    val encodedMatrix = new SparseMatrix(chars.length,
      maxMessageLength,
      (0 to rowIndices.length).toArray++
        Array.fill(maxMessageLength-rowIndices.length)(rowIndices.length),
      rowIndices.toArray,
      Array.fill(rowIndices.length)(1)
    )
    encodedMatrix.toArray
  }

  def parseSparse(rawmessage:String,answer:String):String={
    /* This method parses a string matrix that has been promotes to a string*/
    if (rawmessage.tail.isEmpty) {
      return answer
    }
    if (rawmessage.head.equals('(')) {
      val index = rawmessage.tail.substring(0,rawmessage.tail.indexOf(",")).toInt
      parseSparse(rawmessage.tail, answer + chars.slice(index, index+1).head)
    }
    else {
      parseSparse(rawmessage.tail,answer)
    }
  }

  def decode(botSays:Array[Double]): String ={
    //val botSaysMatrix = new DenseMatrix(maxMessageLength,chars.length,botSays)

    var decodedAnswer = ""
    for (elem <- botSays.grouped(chars.length)) {
      val valIndexTuple = elem.zipWithIndex.maxBy(f => f._1)
      val letter = chars(valIndexTuple._2)
      decodedAnswer += letter
    }
    decodedAnswer
  }
}
