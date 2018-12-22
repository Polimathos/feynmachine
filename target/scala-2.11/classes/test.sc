import scala.io.Source
import java.io.PrintWriter

import org.apache.spark.mllib.linalg.SparseMatrix

val chars = (" ñ!,.?-".toCharArray
  ++ ('a' to 'z')
  //++ ('0' to '9')
  ).map(_.toString).toList

val maxMessageLength = 25

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

def encodeWordForWord(sentence:String):Array[Array[Double]] = {
  val words = sentence.split(" ")
  var wordsEncoded:Array[Array[Double]]= Array()
  for (word <- words){
    val wordEncoded = encode(word)
    wordsEncoded ++= Array(wordEncoded)
  }
  wordsEncoded
}

def decode(botSays:Array[Double]): String = {
  //val botSaysMatrix = new DenseMatrix(maxMessageLength,chars.length,botSays)

  var decodedAnswer = ""
  for (elem <- botSays.grouped(chars.length)) {
    val valIndexTuple = elem.zipWithIndex.maxBy(f => f._1)
    val letter = chars(valIndexTuple._2)
    decodedAnswer += letter
  }
  decodedAnswer
}

def decodeWordForWord(responseArrays:Array[Array[Double]]):String = {
  var response = ""
  for (array <- responseArrays) {
    response += decode(array).trim + " "
  }
  response
}

def synchronizeInputArrays(inputArray:Array[Array[Double]],
                           trainingArray:Array[Array[Double]]):(Array[Array[Double]],Array[Array[Double]]) = {
  val totalLength = inputArray.length + trainingArray.length
  val arrayLength = maxMessageLength * chars.length
  val completeInput = inputArray ++
    Array.fill[Array[Double]](trainingArray.length)(Array.fill[Double](arrayLength)(0))
  val completeTraining = Array.fill[Array[Double]](inputArray.length)(Array.fill[Double](arrayLength)(0)) ++
    trainingArray
  (completeInput,completeTraining)
}

def agentRespond(inputArray:Array[Double], trainExampleArray:Array[Double]):(String, String) = {
  val (nextInputPredictionArray, machineResponseArray) = (inputArray, trainExampleArray)
  val nextInputPrediction = decode(nextInputPredictionArray)
  val machineResponse = decode(machineResponseArray)
  (nextInputPrediction,machineResponse)
}

def agentRespondWordForWord(messageArrayTuple:(Array[Array[Double]],Array[Array[Double]])):(String, String) ={
  val zippedArrays = messageArrayTuple._1.zip(messageArrayTuple._2)
  var nextInputPrediction = ""
  var machineResponse = ""
  for (tuple <- zippedArrays){
    val (nextInputWord, machineWord) = agentRespond(tuple._1, tuple._2)
    nextInputPrediction += nextInputWord
    machineResponse += machineWord
  }
  (nextInputPrediction,machineResponse)
}

val arr = encodeWordForWord("hello my baby")
val arr2 = encodeWordForWord("hello my honey")

val t = synchronizeInputArrays(arr,arr2)

val rt = agentRespondWordForWord(t)

println(rt._1)
println(rt._2)






    /*    dropRight(1).(x => print(x.drop(1).dropRight(1))).toString
  )
}*/

