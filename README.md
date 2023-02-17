## POS application backend
### Powered by
<p float="left">
    <img src="https://upload.wikimedia.org/wikipedia/commons/7/74/Kotlin_Icon.png" height="40px"/> 
    <img src="https://logodix.com/logo/1614292.png" height="40px"/>
    <img src="https://avatars.githubusercontent.com/u/4201559?s=280&v=4" height="40px"/>
    <img src="https://wiki.postgresql.org/images/9/9a/PostgreSQL_logo.3colors.540x557.png" height="40px"/>
    <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/1/17/GraphQL_Logo.svg/1024px-GraphQL_Logo.svg.png" height="40px"/>
    <img src="https://upload.wikimedia.org/wikipedia/commons/7/79/Docker_%28container_engine%29_logo.png" height="40px"/>
</p>

<b>A demo API build with Kotlin, Reactive spring boot, PostgreSQL, GraphQL. Deployed with Docker.</b>

### Instructions

1. Make sure ```Docker``` is installed </br>
2. Clone the repository with ```git clone https://github.com/sihan010/reactive-pos.git``` </br>
3. Open terminal in the project directory </br>
4. Run ```docker-compose up --build```

### Control application behavior

Some aspects of the API are controllable via Environment variables.</br>
These values can be modified in ```docker-compose``` file.</br>
Below are the variables and their default value.

<table>
    <tr>
        <td><code><b>Service</b></code></td>
        <td><code><b>Environment Variable</b></code></td>
        <td><code><b>Default value</b></code></td>
    </tr>
    <tr>
        <td><code>postgres</code></td>
        <td><code>POSTGRES_USER</code></td>
        <td><code>postgres</code></td>
    </tr>
    <tr>
        <td><code>postgres</code></td>
        <td><code>POSTGRES_PASSWORD</code></td>
        <td><code>postgres</code></td>
    </tr>
    <tr>
        <td><code>pgadmin</code></td>
        <td><code>PGADMIN_DEFAULT_EMAIL</code></td>
        <td><code>sihan@sihan.dev</code></td>
    </tr>
    <tr>
        <td><code>pgadmin</code></td>
        <td><code>PGADMIN_DEFAULT_PASSWORD</code></td>
        <td><code>abc123.</code></td>
    </tr>
    <tr>
        <td><code>application</code></td>
        <td><code>SPRING_PORT</code></td>
        <td><code>8080</code></td>
    </tr>
    <tr>
        <td><code>application</code></td>
        <td><code>SPRING_DATASOURCE_URL</code></td>
        <td><code>r2dbc:postgresql://postgres:postgres@postgres:5432/postgres?currentSchema=pos_schema</code>
            <br/>
            <br/>
            <i>Connection URI pattern</i>: <code>r2dbc:driver://user:pass@host:port/db_name?currentSchema=schema</code> 
        </td>
    </tr>
    <tr>
        <td><code>application</code></td>
        <td><code>SPRING_LOG_LEVEL</code></td>
        <td><code>INFO</code></td>
    </tr>
    <tr>
        <td><code>application</code></td>
        <td><code>SPRING_GRAPHQL_PATH</code></td>
        <td><code>/graphql</code></td>
    </tr>
    <tr>
        <td><code>application</code></td>
        <td><code>SPRING_GRAPHIQL_ENABLED</code></td>
        <td><code>true</code></td>
    </tr>
    <tr>
        <td><code>application</code></td>
        <td><code>SPRING_GRAPHIQL_URL</code></td>
        <td><code>/graphiql</code></td>
    </tr>

</table>

### Unit test

Controller methods are covered by Unit tests.
Follow instructions below to run unit test.

- Open terminal in the project directory
- Run <code>./gradlew test</code>

### Test endpoints

If all environment variables are kept at their default value, <b>GraphiQL</b> interface can be browsed
at ```http://localhost:8080/graphiql``` to run <b>GraphQL</b> queries.</br>
Some data are seeded to PostgreSQL during initialization, so you can run below queries are test

#### Get all Payment methods

    query {
      GetAllPaymentMethods{
        paymentMethodId
        paymentMethod
        priceModifierFrom
        priceModifierTo
        points
        createdAt
        updatedAt
      }
    }

#### Get single Payment method

    query {
      GetSinglePaymentMethodByType(type:"AMEX"){
        paymentMethodId
        paymentMethod
        priceModifierFrom
        priceModifierTo
        points
        createdAt
        updatedAt
      }
    }

#### Add new Payment method

    mutation {
      AddSinglePaymentMethod(paymentMethod : {
        paymentMethod :"Test",
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

#### Make a payment

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

#### Get sales history between time

    query {
      GetSalesHistory(startDateTime:"2022-09-01T00:00:00Z", endDateTime:"2023-09-01T00:00:00Z"){
        dateTime,
        sales,
        points
      }
    }

<br/>
<br/>
<br/>

```© sihan @ 2023```

