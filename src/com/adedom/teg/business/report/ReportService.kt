package com.adedom.teg.business.report

import com.adedom.teg.models.report.ItemCollectionResponse

internal interface ReportService {

    fun itemCollection(): ItemCollectionResponse

}
