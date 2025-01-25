package com.android.trade.coinmonitor.di.usecase

import com.android.trade.domain.repositories.RoomRepository
import com.android.trade.domain.usecase.room.GetRoomAllCoinUseCase
import com.android.trade.domain.usecase.room.RoomDeleteCoinUseCase
import com.android.trade.domain.usecase.room.RoomInsertCoinUseCase
import com.android.trade.domain.usecaseimpl.room.GetRoomAllCoinUseCaseImpl
import com.android.trade.domain.usecaseimpl.room.RoomDeleteCoinUseCaseImpl
import com.android.trade.domain.usecaseimpl.room.RoomInsertCoinUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RoomUseCaseModule {

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
}