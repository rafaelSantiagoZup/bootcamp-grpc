package protobuff.br.com.zup

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("protobuff.br.com.zup")
		.start()
}

