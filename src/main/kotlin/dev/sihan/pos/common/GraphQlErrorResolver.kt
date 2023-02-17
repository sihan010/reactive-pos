package dev.sihan.pos.common

import graphql.ErrorType
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.stereotype.Component

data class PosApplicationError(
    val errorType: ErrorType,
    override val message: String
) : Throwable()

data class PosApplicationMultiErrors(
    val posErrors: List<PosApplicationError>
) : Throwable()

@Component
class GraphQLExceptionHandler : DataFetcherExceptionResolverAdapter() {
    override fun resolveToSingleError(t: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        return when (t) {
            is PosApplicationError -> toGraphQLError(t)
            else -> super.resolveToSingleError(t, env)
        }
    }

    override fun resolveToMultipleErrors(t: Throwable, env: DataFetchingEnvironment): MutableList<GraphQLError>? {
        return when (t) {
            is PosApplicationMultiErrors -> toGraphQLErrors(t)
            else -> super.resolveToMultipleErrors(t, env)
        }
    }

    private fun toGraphQLError(error: PosApplicationError): GraphQLError {
        return GraphqlErrorBuilder
            .newError()
            .message(error.message)
            .errorType(error.errorType)
            .build()
    }

    private fun toGraphQLErrors(errors: PosApplicationMultiErrors): MutableList<GraphQLError> {
        return errors.posErrors.map { e ->
            GraphqlErrorBuilder
                .newError()
                .message(e.message)
                .errorType(e.errorType)
                .build()
        }.toMutableList()
    }
}
