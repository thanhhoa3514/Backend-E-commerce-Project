package contracts.userprofile

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should update user profile"
    
    request {
        method PUT()
        url "/api/v1/profile"
        headers {
            contentType(applicationJson())
            header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9...")
        }
        body([
            "bio": "Updated bio",
            "dateOfBirth": "1990-01-01",
            "profilePicture": "updated-profile.jpg"
        ])
    }
    
    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body([
            "id": $(anyNumber()),
            "userId": $(anyNumber()),
            "bio": "Updated bio",
            "dateOfBirth": "1990-01-01",
            "profilePicture": "updated-profile.jpg"
        ])
    }
}