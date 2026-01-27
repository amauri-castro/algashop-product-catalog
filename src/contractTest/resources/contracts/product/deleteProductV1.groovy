package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method DELETE()
        headers {
            accept 'application/json'
        }
        url("/api/v1/products/019be330-5c35-7ef8-b59b-0cf73765a296")
    }
    response {
        status 204
    }
}