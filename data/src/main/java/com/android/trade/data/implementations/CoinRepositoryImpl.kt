package com.android.trade.data.implementations

import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.CoinDomainModel
import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.data.local.datasource.LocalDataSource
import com.android.trade.data.mapper.CoinDataMapper
import com.android.trade.data.remote.datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val mapper: CoinDataMapper
) : CoinRepository {
    override fun fetchCoin(): Flow<ApiResult<CoinDomainModel>> {
        return mapper.responseToDomainCoin(remoteDataSource.fetchCoin(1))
    }

    override suspend fun localCoin(): CoinDomainModel {
        localDataSource
        return CoinDomainModel(1)
    }
}