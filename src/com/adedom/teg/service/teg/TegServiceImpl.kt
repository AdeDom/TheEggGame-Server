package com.adedom.teg.service.teg

import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.request.single.ItemCollectionRequest
import com.adedom.teg.response.BackpackResponse
import com.adedom.teg.response.BaseResponse

class TegServiceImpl(private val repository: TegRepository) : TegService {

    override fun fetchItemCollection(playerId: String): BackpackResponse {
        return repository.fetchItemCollection(playerId)
    }

    override fun postItemCollection(playerId: String, itemCollectionRequest: ItemCollectionRequest): BaseResponse {
        return repository.postItemCollection(playerId, itemCollectionRequest)
    }

}
