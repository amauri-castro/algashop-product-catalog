package contracts.category

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept "application/json"
        }
        url("/api/v1/categories") {
            queryParameters {
                parameter('page', value(stub(optional(anyNumber())), test(0)))
                parameter('size', value(stub(optional(anyNumber())), test(10)))
            }
        }
    }
    response {
        status 200
        headers {
            contentType "application/json"
        }
        body([
                size: fromRequest().query("size"),
                number: 0,
                totalElements: 2,
                totalPages: 1,
                content: [
                        [
                                id: anyUuid(),
                                name: "Notebooks",
                                enabled: true
                        ],
                        [
                                id: anyUuid(),
                                name: "Desktops",
                                enabled: false
                        ]
                ]
        ])
    }
}