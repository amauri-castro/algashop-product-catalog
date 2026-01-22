package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        headers {
            accept 'application/json'
            contentType 'application/json'
        }
        urlPath("/api/v1/products") {
            body([
                    name: "Notebook Legion",
                    brand: "Lenovo",
                    regularPrice: 1500.00,
                    salePrice: 1000.00,
                    enabled: true,
                    categoryId: '019be330-5c35-7ef8-b59b-0cf73765a296',
                    description: "A Gamer Notebook!"
            ])
        }
    }
    response {
        status 201
        headers {
            contentType 'application/json'
        }
        body([
                id: anyUuid(),
                addedAt: anyIso8601WithOffset(),
                name: "Notebook Legion",
                brand: "Lenovo",
                regularPrice: 1500.00,
                salePrice: 1000.00,
                inStock: false,
                enabled: true,
                category: [
                        id: "019be330-5c35-7ef8-b59b-0cf73765a296",
                        name: "Notebook"
                ],
                description: "A Gamer Notebook!"
        ])
    }
}