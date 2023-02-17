package dev.sihan.pos.controller

import dev.sihan.pos.dto.PaymentOut
import dev.sihan.pos.dto.SalesHistoryOut
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.GraphQlTester

@SpringBootTest
@AutoConfigureGraphQlTester
internal class SalesControllerTest(@Autowired private val graphQlTester: GraphQlTester) {

    @Test
    fun makePayment() {
        // language=graphql
        val document = """
            mutation {
              MakePayment(payment:{
                price:"100.00",
                priceModifier: 0.95,
                paymentMethod:"MASTERCARD",
                dateTime:"2022-09-01T00:00:00Z"
              }){
                finalPrice,
                points
              }
            }
        """.trimIndent()
        graphQlTester.document(document)
            .execute()
            .path("MakePayment")
            .entity(PaymentOut::class.java)
            .satisfies { p ->
                assertEquals(p.finalPrice, "95.0")
                assertEquals(p.points, 3f)
            }
    }

    @Test
    fun getSalesHistory() {
        // language=graphql
        val document = """
            query {
                GetSalesHistory(startDateTime: "2022-09-01T00:00:00Z", endDateTime: "2024-09-01T00:00:00Z"){
                    dateTime,
                    sales,
                    points
                }
            }
        """.trimIndent()

        graphQlTester.document(document)
            .execute()
            .path("GetSalesHistory")
            .entityList(SalesHistoryOut::class.java)
            .hasSizeGreaterThan(0)
    }
}
