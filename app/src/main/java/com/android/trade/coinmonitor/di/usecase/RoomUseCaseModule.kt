package com.android.trade.coinmonitor.di.usecase

import com.android.trade.domain.repositories.RoomRepository
import com.android.trade.domain.usecase.room.GetCoinListUseCase
import com.android.trade.domain.usecase.room.GetRoomAllCoinUseCase
import com.android.trade.domain.usecase.room.RoomDeleteCoinListUseCase
import com.android.trade.domain.usecase.room.RoomDeleteCoinUseCase
import com.android.trade.domain.usecase.room.RoomInsertCoinListUseCase
import com.android.trade.domain.usecase.room.RoomInsertCoinUseCase
import com.android.trade.domain.usecaseimpl.room.GetCoinListUseCaseImpl
import com.android.trade.domain.usecaseimpl.room.GetRoomAllCoinUseCaseImpl
import com.android.trade.domain.usecaseimpl.room.RoomDeleteCoinListUseCaseImpl
import com.android.trade.domain.usecaseimpl.room.RoomDeleteCoinUseCaseImpl
import com.android.trade.domain.usecaseimpl.room.RoomInsertCoinListUseCaseImpl
import com.android.trade.domain.usecaseimpl.room.RoomInsertCoinUseCaseImpl
import com.android.trade.presentation.usecasegroup.RoomCoinUseCaseGroup
import com.android.trade.presentation.usecasegroup.RoomCoinListUseCaseGroup
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RoomUseCaseModule {

    @Provides
    fun provideRoomCoinUseCaseGroup(
        insertCoin: RoomInsertCoinUseCase,
        getAllCoin: GetRoomAllCoinUseCase,
        deleteCoin: RoomDeleteCoinUseCase
    ): RoomCoinUseCaseGroup {
        return RoomCoinUseCaseGroup(insertCoin, getAllCoin, deleteCoin)
    }

    @Provides
    fun provideRoomCoinListUseCaseGroup(
        insertCoinList: RoomInsertCoinListUseCase,
        getCoinList: GetCoinListUseCase,
        deleteCoinList: RoomDeleteCoinListUseCase
    ): RoomCoinListUseCaseGroup {
        return RoomCoinListUseCaseGroup(insertCoinList, getCoinList, deleteCoinList)
    }

    @Provides
    @ViewModelScoped
    fun provideRoomInsertCoinUseCase(
        roomRepository: RoomRepository
    ): RoomInsertCoinUseCase {
        return RoomInsertCoinUseCaseImpl(roomRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetRoomAllCoinUseCase(
        roomRepository: RoomRepository
    ): GetRoomAllCoinUseCase {
        return GetRoomAllCoinUseCaseImpl(roomRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideRoomDeleteCoinUseCase(
        roomRepository: RoomRepository
    ): RoomDeleteCoinUseCase {
        return RoomDeleteCoinUseCaseImpl(roomRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCoinListUseCase(
        roomRepository: RoomRepository
    ): GetCoinListUseCase {
        return GetCoinListUseCaseImpl(roomRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideRoomInsertCoinListUseCase(
        roomRepository: RoomRepository
    ): RoomInsertCoinListUseCase {
        return RoomInsertCoinListUseCaseImpl(roomRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideRoomDeleteCoinListUseCase(
        roomRepository: RoomRepository
    ): RoomDeleteCoinListUseCase {
        return RoomDeleteCoinListUseCaseImpl(roomRepository)
    }
}