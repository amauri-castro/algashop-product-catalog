package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept 'application/json'
        }
        url("/api/v1/products/019be330-5c35-7ef8-b59b-0cf73765a296")
    }
    response {
        status 200
        headers {
            contentType 'application/json'
        }
        body([
                id: fromRequest().path(3),
                addedAt: anyIso8601WithOffset(),
                name: "Notebook Legion",
                brand: "Lenovo",
                regularPrice: 1500.00,
                salePrice: 1000.00,
                inStock: false,
                enabled: true,
                categoryId: anyUuid(),
                description: "A Gamer Notebook"
        ])
    }
}