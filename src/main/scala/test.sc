import org.apache.spark.mllib.linalg.{SparseMatrix, DenseMatrix}


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
    parseSparse(rawmessage.tail, answer + chars.slice(index, index+1).apply(0))
  }
  else {
    parseSparse(rawmessage.tail,answer)
  }
}

val arr = encode("hello my name is Marcos")

def decode(botSays:Array[Double]): String ={
  //val botSaysMatrix = new DenseMatrix(maxMessageLength,chars.length,botSays)
  var decodedAnswer = ""
  for (elem <- botSays.grouped(maxMessageLength)) {
    val valIndexTuple = elem.zipWithIndex.maxBy(f => f._1)
    val letter = chars.slice(valIndexTuple._2,valIndexTuple._2 + 1)
    decodedAnswer += letter.head
  }
  decodedAnswer
}

val col = decode(arr)

 val a = "Hhello my name is".map(f => chars.indexOf(f.toString.toLowerCase))
val max =140

println(chars.length)

(0 to a.length).toArray++Array.fill(max-a.length)(a.length)

val secu= a.toArray++Array.fill(max-a.length)(0)
secu.length

val ones =Array.fill(chars.length)(1)
ones.length

val sm = new SparseMatrix(max,
  chars.length,
  (0 to a.length).toArray++Array.fill(max-a.length)(a.length),
  a.toArray,
  Array.fill(a.length)(1),
  isTransposed = true)

sm.toDense


val botSaysMatrix = new DenseMatrix(max,chars.length,sm.toArray)



sm.transpose.toArray

for (elem <- sm.transpose.toArray.grouped(140)) {
  println(elem.zipWithIndex.maxBy(f => f._1))
  println(chars.slice(elem.zipWithIndex.maxBy(f => f._1)._2,elem.zipWithIndex.maxBy(f => f._1)._2+1))
}
  //.foreach(arr => arr.zipWithIndex.maxBy(f => f._1))





/*
b.toString()


"(djas(sak".map(f => println(f.equals('(')))

val ind = "8,jas,(s,ak".substring(0,"8,jas,(s,ak".indexOf(",")).toInt

var dummyList = chars.slice(ind,ind+1)
val indxe = 8
var answer = ""


answer + chars.slice(ind, ind+1).apply(0)

println(answer)


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
*/


