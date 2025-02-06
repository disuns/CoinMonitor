package com.android.trade.presentation

import app.cash.turbine.test
import com.android.trade.common.enum.MarketType
import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.CoinInfo
import com.android.trade.domain.models.Market
import com.android.trade.domain.models.Ticker
import com.android.trade.presentation.mappers.CoinPresentationMapper
import com.android.trade.presentation.usecasegroup.CoinMarketUseCaseGroup
import com.android.trade.presentation.usecasegroup.CoinTickerUseCaseGroup
import com.android.trade.presentation.viewmodels.CoinViewModel
import com.android.trade.presentation.viewmodels.RoomAndWebSocketViewModel
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class CoinViewModelTest {
    @MockK
    private lateinit var coinMarketUseCaseGroup: CoinMarketUseCaseGroup

    @MockK
    private lateinit var coinTickerUseCaseGroup: CoinTickerUseCaseGroup

    @MockK
    private lateinit var mapper: CoinPresentationMapper

    private lateinit var viewModel: CoinViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = spyk(CoinViewModel(coinMarketUseCaseGroup, coinTickerUseCaseGroup, mapper), recordPrivateCalls = true)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `fetchMarket 성공적으로 데이터를 반환하는지 테스트`() = runTest {
        // Given
        val market = MarketType.UPBIT.id
        val coinList = listOf(CoinInfo("BTC", "BTC_KRW", "Bitcoin"))

        coEvery { coinMarketUseCaseGroup.getUpbitMarket() } returns flowOf(ApiResult.Success(Market()))
        coEvery { mapper.domainToCoinInfo(any(), eq(market)) } returns flowOf(ApiResult.Success(coinList))

        // When
        val resultFlow = viewModel.fetchMarket(market)

        // Then
        resultFlow.test {
            val result = awaitItem()
            assert(result is ApiResult.Success)
            assert((result as ApiResult.Success).value == coinList)
            awaitComplete()
        }

        coVerify { coinMarketUseCaseGroup.getUpbitMarket() }
        coVerify { mapper.domainToCoinInfo(any(), eq(market)) }
    }

    @Test
    fun `fetchMarketSequentially 정상작동 테스트`() = runTest {
        //Given
        val markets = listOf(MarketType.UPBIT.id, MarketType.BITHUMB.id, MarketType.BINANCE.id)
        val coinListUpbit = listOf(CoinInfo("BTC", "BTC_KRW", "Bitcoin"))
        val coinListBithumb = listOf(CoinInfo("ETH", "ETH_KRW", "Ethereum"))
        val coinListBinance = listOf(CoinInfo("XRP", "XRP_KRW", "XRP"))

        val mockViewModel: RoomAndWebSocketViewModel = mockk(relaxed = true)

        coEvery { coinMarketUseCaseGroup.getUpbitMarket() } returns flowOf(ApiResult.Success(Market()))
        coEvery { coinMarketUseCaseGroup.getBithumbMarket() } returns flowOf(ApiResult.Success(Market()))
        coEvery { coinMarketUseCaseGroup.getBinanceMarket() } returns flowOf(ApiResult.Success(Market()))
        coEvery { mapper.domainToCoinInfo(any(), eq(MarketType.UPBIT.id)) } returns flowOf(ApiResult.Success(coinListUpbit))
        coEvery { mapper.domainToCoinInfo(any(), eq(MarketType.BITHUMB.id)) } returns flowOf(ApiResult.Success(coinListBithumb))
        coEvery { mapper.domainToCoinInfo(any(), eq(MarketType.BINANCE.id)) } returns flowOf(ApiResult.Success(coinListBinance))

        coEvery { mockViewModel.getAllCoin() } just Runs
        coEvery { mockViewModel.insertCoinList(any()) } just Runs
        coEvery { mockViewModel.sendAllMessage() } just Runs

        //When
        viewModel.fetchMarketSequentially(markets, mockViewModel)

        //Then
        advanceUntilIdle()
        coVerify { mockViewModel.getAllCoin() }

        coVerify(exactly = 1) { mockViewModel.insertCoinList(coinListUpbit) }
        coVerify(exactly = 1) { mockViewModel.insertCoinList(coinListBithumb) }
        coVerify(exactly = 1) { mockViewModel.insertCoinList(coinListBinance) }

        coVerify { mockViewModel.sendAllMessage() }
    }

    @Test
    fun `fetchTicker 정상작동 테스트`() = runTest {
        //Given
        val mockViewModel: RoomAndWebSocketViewModel = mockk(relaxed = true)

        val coinList = mutableListOf(
            CoinInfo(MarketType.UPBIT.id, "BTC", "KRW-BTC", price = "0"),
            CoinInfo(MarketType.BITHUMB.id, "ETH", "KRW-ETH", price = "0")
        )
        val mockApiResultTicker = ApiResult.Success(Ticker().apply {
            add(Ticker.Item("BTC", "1000"))
            add(Ticker.Item("ETH","2000"))
        })

        coEvery { coinTickerUseCaseGroup.getUpbitTicker(coinList[0].code) } returns flowOf(mockApiResultTicker)
        coEvery { coinTickerUseCaseGroup.getBithumbTicker(coinList[1].code) } returns flowOf(mockApiResultTicker)

        coEvery { mockViewModel.updateList(any()) } just Runs

        //when
        viewModel.fetchTicker(coinList, mockViewModel, onComplete = { })

        // Then
        advanceUntilIdle()
        coVerify { mockViewModel.updateList(any()) }

        assertEquals("1000", coinList.first { it.code == "BTC" }.price)
        assertEquals("2000", coinList.first { it.code == "ETH" }.price)

        // 사용된 함수들에 대해 호출 검증
        coVerify { coinTickerUseCaseGroup.getUpbitTicker(any()) }
        coVerify { coinTickerUseCaseGroup.getBithumbTicker(any()) }
    }
}