package dev.sihan.pos.controller

import dev.sihan.pos.model.PaymentMethod
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.GraphQlTester
import java.time.Instant

@SpringBootTest
@AutoConfigureGraphQlTester
internal class PaymentMethodControllerTest(@Autowired private val graphQlTester: GraphQlTester) {

    @Test
    fun getAllPaymentMethods() {
        // language=graphql
        val document = """
            query {
                GetAllPaymentMethods {
                    paymentMethodId
                    paymentMethod
                    priceModifierFrom
                    priceModifierTo
                    points
                    createdAt
                    updatedAt
                }
            }
        """.trimIndent()

        graphQlTester.document(document)
            .execute()
            .path("GetAllPaymentMethods")
            .entityList(PaymentMethod::class.java)
            .hasSizeGreaterThan(5)
    }

    @Test
    fun getSinglePaymentMethodByType() {
        // language=graphql
        val document = """
            query {
              GetSinglePaymentMethodByType(type:"CASH"){
                    paymentMethodId
                    paymentMethod
                    priceModifierFrom
                    priceModifierTo
                    points
                    createdAt
                    updatedAt
                  }
              }
        """.trimIndent()
        graphQlTester.document(document)
            .execute()
            .path("GetSinglePaymentMethodByType")
            .entity(PaymentMethod::class.java)
            .satisfies { p ->
                assertEquals(p.priceModifierFrom, 0.9f)
                assertEquals(p.priceModifierTo, 1.0f)
            }
    }

    @Test
    fun addPaymentMethod() {
        val randomName = Instant.now().toEpochMilli().toString()
        // language=graphql
        val document = """
            mutation {
              AddSinglePaymentMethod(paymentMethod : {
                paymentMethod :"$randomName",
                priceModifierFrom: 0.80,
                priceModifierTo: 0.99,
                points: 0.05
              }){
                    paymentMethodId
                    paymentMethod
                    priceModifierFrom
                    priceModifierTo
                    points
                    createdAt
                    updatedAt
              }
            }
        """.trimIndent()
        graphQlTester.document(document)
            .execute()
            .path("AddSinglePaymentMethod")
            .entity(PaymentMethod::class.java)
            .satisfies { p ->
                assertEquals(p.paymentMethod, randomName)
                assertEquals(p.priceModifierFrom, 0.80f)
                assertEquals(p.priceModifierTo, 0.99f)
            }
    }
}
