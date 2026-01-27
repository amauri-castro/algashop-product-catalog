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
                    name: value(
                            test("Notebook Legion"),
                            stub(nonBlank())
                    ),
                    brand: value(
                            test("Lenovo"),
                            stub(nonBlank())
                    ),
                    regularPrice: value(
                            test(1500.00),
                            stub(number())
                    ),
                    salePrice: value(
                            test(1000.00),
                            stub(number())
                    ),
                    enabled: value(
                            test(true),
                            stub(anyBoolean())
                    ),
                    categoryId: value(
                            test("019be330-5c35-7ef8-b59b-0cf73765a296"),
                            stub(anyUuid())
                    ),
                    description: value(
                            test("A Gamer Notebook"),
                            stub(optional(nonBlank()))
                    )
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
                name: fromRequest().body('$.name'),
                brand: fromRequest().body('$.brand'),
                regularPrice: fromRequest().body('$.regularPrice'),
                salePrice: fromRequest().body('$.salePrice'),
                inStock: anyBoolean(),
                enabled: fromRequest().body('$.enabled'),
                category: [
                        id: anyUuid(),
                        name: "Notebook"
                ],
                description: fromRequest().body('$.description')
        ])
    }
}