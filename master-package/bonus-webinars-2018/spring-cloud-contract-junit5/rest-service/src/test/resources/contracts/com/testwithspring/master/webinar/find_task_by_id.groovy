package contracts.com.testwithspring.master.webinar

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should return a task when the id of the requested task is 1'

    request {
        url '/api/task/1'
        method GET()
    }

    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body (
                id: 1,
                title: 'Write our first contract'
        )
    }
}