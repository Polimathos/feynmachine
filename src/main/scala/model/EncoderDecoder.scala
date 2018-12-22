package model

import org.apache.spark.mllib.linalg.{SparseMatrix}

class EncoderDecoder{
  val chars:List[String] = (" ñ!,.?-".toCharArray
    ++ ('a' to 'z')
    //++ ('0' to '9')
    ).map(_.toString).toList

  val maxWordLength:Int = Configuration.maxWordLength

  def encode(message: String): Array[Double] ={
    val messageCut = message.take(maxWordLength)
    val rowIndices = messageCut.map(f => chars.indexOf(f.toString.toLowerCase) match{
      case -1 => 0
      case x => x
    })

    val encodedMatrix = new SparseMatrix(chars.length,
      maxWordLength,
      (0 to rowIndices.length).toArray++
        Array.fill(maxWordLength-rowIndices.length)(rowIndices.length),
      rowIndices.toArray,
      Array.fill(rowIndices.length)(1)
    )
    encodedMatrix.toArray
  }

  def decode(botSays:Array[Double]): String = {
    var decodedAnswer = ""
    for (elem <- botSays.grouped(chars.length)) {
      val valIndexTuple = elem.zipWithIndex.maxBy(f => f._1)
      val letter = chars(valIndexTuple._2)
      decodedAnswer += letter
    }
    decodedAnswer
  }

  def parseSparse(rawmessage:String,answer:String):String= {
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


  def encodeTuple(tuple:(String,String)): (Array[Double],Array[Double])= {
    val encodedTuple = (encode(tuple._1),encode(tuple._2))
    encodedTuple
  }

  def decodeTuple(tuple:(Array[Double],Array[Double])):(String,String) = {
    val decodedTuple = (decode(tuple._1),decode(tuple._2))
    decodedTuple
  }

  def encodeWordForWord(sentence:String):Array[Array[Double]] = {
    val words = sentence.split(" ")
    var wordsEncoded:Array[Array[Double]]= Array()
    for (word <- words) {
      val wordEncoded = encode(word)
      wordsEncoded ++= Array(wordEncoded)
    }
    wordsEncoded
  }

  def decodeWordForWord(responseArrays:Array[Array[Double]]):String = {
    var response = ""
    for (array <- responseArrays) {
      val decoded = decode(array).trim
      if (!decoded.isEmpty){
        response += decoded +" "
      }
    }
    response
  }

  def encodeTupleWordForWord(inputSentence:String, machineMessage:String):(Array[Array[Double]],Array[Array[Double]]) ={
    val tupleWordForWord = (
      encodeWordForWord(inputSentence),
      encodeWordForWord(machineMessage)
    )
    tupleWordForWord
  }

  def decodeTupleWordForWord(tuple:(Array[Array[Double]],Array[Array[Double]])):(String,String) = {
    val decodedTuple = (decodeWordForWord(tuple._1),decodeWordForWord(tuple._2))
    decodedTuple
  }

  def synchronizeInputArrays(tuple:(Array[Array[Double]],Array[Array[Double]])):(Array[Array[Double]],Array[Array[Double]]) = {
    val inputArray = tuple._1
    val trainingArray = tuple._2
    val maxNumberOfWords:Int = Configuration.maxNumberOfWords
    val arrayLength = maxWordLength * chars.length
    val completeInput = inputArray ++
      Array.fill[Array[Double]](maxNumberOfWords)(Array.fill[Double](arrayLength)(0))
    val completeTraining = if (trainingArray.length>=maxNumberOfWords) {
      trainingArray.take(maxNumberOfWords)
    } else {
      Array.fill[Array[Double]](inputArray.length)(Array.fill[Double](arrayLength)(0)) ++
        trainingArray ++
        Array.fill[Array[Double]](
          maxNumberOfWords - trainingArray.length)(Array.fill[Double](arrayLength)(0))
    }
    (completeInput,completeTraining)
  }
}
