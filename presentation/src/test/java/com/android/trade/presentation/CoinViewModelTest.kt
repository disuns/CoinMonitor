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
import junit.framework.TestCase.assertTrue
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
            CoinInfo(MarketType.BITHUMB.id, "ETH", "KRW-ETH", price = "0"),
            CoinInfo(MarketType.BINANCE.id, "BTC", "BTCUSDT", price = "0"),
            CoinInfo(MarketType.BYBIT.id, "ETH", "ETHUSDT", price = "0")
        )

        coEvery { coinTickerUseCaseGroup.getUpbitTicker(eq(coinList[0].code)) } returns flowOf(
            ApiResult.Success(
                Ticker().apply { add(Ticker.Item(code = coinList[0].code, price = "10000")) }
            )
        )
        coEvery { coinTickerUseCaseGroup.getBithumbTicker(eq(coinList[1].code)) } returns flowOf(
            ApiResult.Success(
                Ticker().apply { add(Ticker.Item(code = coinList[1].code, price = "20000")) }
            )
        )
        coEvery { coinTickerUseCaseGroup.getBinanceTicker("[\"BTC\"]") } returns flowOf(
            ApiResult.Success(
                Ticker().apply { add(Ticker.Item(code = coinList[2].code, price = "30000")) }
            )
        )
        coEvery { coinTickerUseCaseGroup.getBybitTicker(eq(coinList[3].code)) } returns flowOf(
            ApiResult.Success(
                Ticker().apply { add(Ticker.Item(code = coinList[3].code, price = "40000")) }
            )
        )

        every { mapper.domainToUIPrice(MarketType.UPBIT.id, "10000") } returns "format10000"
        every { mapper.domainToUIPrice(MarketType.BITHUMB.id, "20000") } returns "format20000"
        every { mapper.domainToUIPrice(MarketType.BINANCE.id, "30000") } returns "format30000"
        every { mapper.domainToUIPrice(MarketType.BYBIT.id, "40000") } returns "format40000"

        var onCompleteCalled = false
        val onComplete = {onCompleteCalled = true}

        //when
        viewModel.fetchTicker(coinList, mockViewModel, onComplete)

        // Then
        advanceUntilIdle()

        assertEquals("format10000", coinList.first { it.market == MarketType.UPBIT.id && it.code == "BTC" }.price)
        assertEquals("format20000", coinList.first { it.market == MarketType.BITHUMB.id && it.code == "ETH" }.price)
        assertEquals("format30000", coinList.first { it.market == MarketType.BINANCE.id && it.code == "BTC" }.price)
        assertEquals("format40000", coinList.first { it.market == MarketType.BYBIT.id && it.code == "ETH" }.price)

        assertTrue(onCompleteCalled)

        coVerify { coinTickerUseCaseGroup.getUpbitTicker(any()) }
        coVerify { coinTickerUseCaseGroup.getBithumbTicker(any()) }
    }
}