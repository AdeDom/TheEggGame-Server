package com.adedom.teg.business.report

import com.adedom.teg.models.report.ItemCollectionResponse
import com.adedom.teg.models.report.LogActiveResponse

internal interface ReportService {

    fun itemCollection(): ItemCollectionResponse

    fun logActive(): LogActiveResponse

}
