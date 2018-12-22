package model

import org.apache.spark.mllib.linalg.{SparseMatrix}

class EncoderDecoder{
  val chars:List[String] = (" ñ!,.?-".toCharArray
    ++ ('a' to 'z')
    //++ ('0' to '9')
    ).map(_.toString).toList

  val maxMessageLength:Int = Configuration.maxMessageLength

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

  def decode(botSays:Array[Double]): String = {
    var decodedAnswer = ""
    for (elem <- botSays.grouped(chars.length)) {
      val valIndexTuple = elem.zipWithIndex.maxBy(f => f._1)
      val letter = chars(valIndexTuple._2)
      decodedAnswer += letter
    }
    decodedAnswer
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

  def encodeTupleWordForWord(inputSentence:String, machineMessage:String):(Array[Array[Double]],Array[Array[Double]]) ={
    val tupleWordForWord = (
      encodeWordForWord(inputSentence),
      encodeWordForWord(machineMessage)
    )
    tupleWordForWord
  }

  def decodeWordForWord(responseArrays:Array[Array[Double]]):String = {
    var response = ""
    for (array <- responseArrays) {
      response += decode(array).trim +" "
    }
    response
  }

  def decodeTupleWordForWord(tuple:(Array[Array[Double]],Array[Array[Double]])):(String,String) = {
    val decodedTuple = (decodeWordForWord(tuple._1),decodeWordForWord(tuple._2))
    decodedTuple
  }

  def synchronizeInputArrays(tuple:(Array[Array[Double]],Array[Array[Double]])):(Array[Array[Double]],Array[Array[Double]]) = {
    val inputArray = tuple._1
    val trainingArray = tuple._2
    val totalLength = inputArray.length + trainingArray.length
    val arrayLength = maxMessageLength * chars.length
    val completeInput = inputArray ++
      Array.fill[Array[Double]](trainingArray.length)(Array.fill[Double](arrayLength)(0))
    val completeTraining = Array.fill[Array[Double]](inputArray.length)(Array.fill[Double](arrayLength)(0)) ++
      trainingArray
    (completeInput,completeTraining)
  }
}
