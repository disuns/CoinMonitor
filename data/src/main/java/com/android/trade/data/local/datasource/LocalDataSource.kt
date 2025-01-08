package com.android.trade.data.local.datasource

import com.android.trade.data.local.dao.CoinDao
import javax.inject.Inject

interface LocalDataSource {
}

class LocalDataSourceImpl @Inject constructor(
    private val coinDao: CoinDao
) : LocalDataSource {
}