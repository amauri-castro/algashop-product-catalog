package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method DELETE()
        urlPath("/api/v1/products/019bff63-8a93-7884-89aa-cf73116430ed/enable")
    }
    response {
        status 404
        headers {
            contentType"application/problem+json"
        }
        body([
                instance: fromRequest().path(),
                type: "/errors/not-found",
                title: "Not Found"
        ])
    }
}