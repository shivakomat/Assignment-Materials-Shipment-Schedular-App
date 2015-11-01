package com.garner.tool.parser

import java.io.File

/** File writer interface */
trait Writer[T] {
  
    /** write header line */
    def writeHeader(header:T)
    
    /** write to file */
	def write(data:T)
    
    /** close file handler */
	def close
}


/** CSV File writer implementation of Writer */
object CSVFileWriter{
  def apply(filename:String,append:Boolean):Writer[List[String]]={
    new CSVWriter(new File(filename),append)
  }
  
  def apply(file:File,append:Boolean=false):Writer[List[String]]={
    new CSVWriter(file,append)
  }
}