/*
 * Copyright 2012 Henry Story, http://bblfish.net/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.www.play.rdf

import java.net.URL
import play.api.libs.iteratee.Iteratee
import util.Try

/**
 *
 * @tparam Result The object resulting of the Iteratee application
 * @tparam SyntaxType The Syntax type that this iteratee parses
 */
trait RDFIteratee[Result, +SyntaxType] {

  /**
   * @param loc the location of the document to evaluate relative URLs (this will not make a connection)
   * @return an iteratee to process a streams of bytes that will parse to an RDF#Graph
   *
   */
  def apply(loc: Option[URL] = None): Iteratee[Array[Byte], Try[Result]]

  def map[OtherResult](trans: Result => OtherResult): RDFIteratee[OtherResult,SyntaxType] =
    new RDFIteratee[OtherResult,SyntaxType] {
    def apply(loc: Option[URL]) = RDFIteratee.this.apply(loc).map{ it =>
      it.map{res => trans.apply(res)}
    }
  }
}


