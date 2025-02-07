package com.android.trade.presentation

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.android.trade.common.enum.MarketType
import com.android.trade.domain.WebSocketManager
import com.android.trade.domain.models.CoinInfo
import com.android.trade.domain.models.WebSocketData
import com.android.trade.presentation.mappers.CoinPresentationMapper
import com.android.trade.presentation.models.MarketUiModel
import com.android.trade.presentation.usecasegroup.RoomCoinListUseCaseGroup
import com.android.trade.presentation.usecasegroup.RoomCoinUseCaseGroup
import com.android.trade.presentation.viewmodels.BaseViewModel
import com.android.trade.presentation.viewmodels.RoomAndWebSocketViewModel
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.slot
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class RoomAndWebSocketViewModelTest {
    @MockK
    private lateinit var roomCoinUseCaseGroup: RoomCoinUseCaseGroup

    @MockK
    private lateinit var roomCoinListUseCaseGroup: RoomCoinListUseCaseGroup

    @MockK
    private lateinit var webSocketManager: WebSocketManager

    @MockK
    private lateinit var mapper: CoinPresentationMapper

    private lateinit var viewModel: RoomAndWebSocketViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val webSocketListenerSlot = slot<(WebSocketData?) -> Unit>()

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        // ViewModel 생성 시 WebSocket 리스너 캡처
        every { webSocketManager.setWebSocketListener(capture(webSocketListenerSlot)) } just Runs


        viewModel = spyk(
            RoomAndWebSocketViewModel(
                roomCoinUseCaseGroup,
                roomCoinListUseCaseGroup,
                webSocketManager,
                mapper
            ),
            recordPrivateCalls = true
        )

        // ArchTaskExecutor를 사용하여 LiveData 테스트 가능하게 설정
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean = true
        })

        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()

        // ArchTaskExecutor 설정 복구
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    @Test
    fun `webSocket listener updates messages LiveData`() = runTest {
        // Given
        val dummyWebSocketData = WebSocketData(/* 필요한 필드 값 설정 */)
        val uiWebSocketData = WebSocketData(/* UI 변환된 필드 값 설정 */)

        // WebSocket 리스너를 설정하는 Slot
        val webSocketListenerSlot = slot<(WebSocketData?) -> Unit>()

        // WebSocketManager가 setWebSocketListener를 호출하면 해당 Slot을 캡처하도록 설정
        every { webSocketManager.setWebSocketListener(capture(webSocketListenerSlot)) } just Runs

        // Mapper가 WebSocketData를 UI 모델로 변환하도록 설정
        every { mapper.domainToUIPrice(dummyWebSocketData) } returns uiWebSocketData

        // When: 캡처한 WebSocket 리스너를 직접 호출하여 데이터를 전달
        webSocketListenerSlot.captured.invoke(dummyWebSocketData)
        advanceUntilIdle()  // 비동기 작업 완료 대기

        // Then: LiveData가 올바르게 업데이트되었는지 확인
        assertEquals(uiWebSocketData, viewModel.messages.value)

        // Mapper가 변환을 수행했는지 검증
        coVerify { mapper.domainToUIPrice(dummyWebSocketData) }
    }

    @Test
    fun `connectWebSocket 테스트`() = runTest {
        //Given
        val market = MarketType.UPBIT.id
        coEvery { webSocketManager.connect(eq(market)) } returns true

        //When
        viewModel.connectWebSocket(market)

        //Then
        advanceUntilIdle()

        coVerify { webSocketManager.connect(market) }
    }
    @Test
    fun `disconnectWebSocket 테스트`() = runTest {
        //Given
        val market = "Upbit"
        coEvery { webSocketManager.disconnect(eq(market)) } just Runs

        //When
        viewModel.disconnectWebSocket(market)

        //Then
        advanceUntilIdle()

        coVerify { webSocketManager.disconnect(market) }
    }

    @Test
    fun `sendAllMessage 테스트`() = runTest {
        // Given
        val coinList = mutableListOf(
            CoinInfo(MarketType.UPBIT.id, "KRW-BTC", "Bitcoin", price = "0"),
            CoinInfo(MarketType.BITHUMB.id, "KRW-ETH", "Ethereum", price = "0"),
            CoinInfo(MarketType.BYBIT.id, "ETHUSDT", "Ethereum", price = "0")
        )
        coEvery { webSocketManager.sendMessage(any(), any()) } just Runs

        viewModel.updateList(coinList)

        // When
        viewModel.sendAllMessage()

        // Then
        advanceUntilIdle()

        coVerify { webSocketManager.sendMessage(MarketType.UPBIT.id, mutableListOf("KRW-BTC")) }
        coVerify { webSocketManager.sendMessage(MarketType.BITHUMB.id, mutableListOf("KRW-ETH")) }
        coVerify { webSocketManager.sendMessage(MarketType.BYBIT.id, mutableListOf("ETHUSDT")) }
    }

    @Test
    fun `sendMessage 테스트`() = runTest {
        //Given
        val market = MarketType.UPBIT.id
        val codes = mutableListOf("BTC")

        coEvery { webSocketManager.sendMessage(eq(market), eq(codes)) } just Runs

        //When
        viewModel.sendMessage(market, codes)

        //Then
        advanceUntilIdle()

        coVerify { webSocketManager.sendMessage(market, codes) }
    }

    @Test
    fun `disconnectAll 테스트`() = runTest {
        //Given
        coEvery { webSocketManager.disconnectAll() } just Runs

        //When
        viewModel.disconnectAll()

        //Then
        advanceUntilIdle()

        coVerify { webSocketManager.disconnectAll() }
    }

    @Test
    fun `insertCoin 테스트`() = runTest {
        //Given
        viewModel.updateList(emptyList())
        advanceUntilIdle()
        val coin = CoinInfo(market = MarketType.UPBIT.id, code = "BTC", coinName = "Bitcoin")

        coEvery { roomCoinUseCaseGroup.insertCoin(eq(coin)) } returns "성공"
        coEvery { webSocketManager.sendMessage(MarketType.UPBIT.id, any()) } just Runs

        //When
        viewModel.insertCoin(coin)

        //Then
        advanceUntilIdle()

        coVerify { roomCoinUseCaseGroup.insertCoin(coin) }
        coVerify { webSocketManager.sendMessage(MarketType.UPBIT.id, mutableListOf("BTC")) }

        val result = viewModel.state.coinsListState.value
        assertEquals(1, result.size)
        assertTrue(result.contains(coin))
    }

    @Test
    fun `getAllCoin 테스트`() = runTest {
        //Given
        val currentList = listOf(
            CoinInfo(MarketType.UPBIT.id, "BTC", "KRW-BTC", price = "10000"),
            CoinInfo(MarketType.BITHUMB.id, "ETH", "KRW-ETH", price = "30000")
        )
        val newList = mutableListOf(
            CoinInfo(MarketType.UPBIT.id, "BTC", "KRW-BTC", price = "10000"),
            CoinInfo(MarketType.BITHUMB.id, "ETH", "KRW-ETH", price = "30000"),
            CoinInfo(MarketType.BYBIT.id, "BTC", "BTCUSDT", price = "20000")
        )
        viewModel.updateList(currentList)
        advanceUntilIdle()

        coEvery { roomCoinUseCaseGroup.getAllCoin() } returns newList

        //When
        viewModel.getAllCoin()

        //Then
        advanceUntilIdle()

        val result = viewModel.state.coinsListState.value
        assertEquals(newList.size, result.size)
        assertTrue(result.containsAll(newList))

        coVerify { roomCoinUseCaseGroup.getAllCoin() }
    }

    @Test
    fun `deleteCoin 테스트 - 마켓에 남은 코인이 없을 때 disconnect 호출`() = runTest {
        //Given
        val coin = CoinInfo(market = MarketType.UPBIT.id, code = "BTC", coinName = "KRW-BTC")
        viewModel.updateList(listOf(coin))
        advanceUntilIdle()

        coEvery { roomCoinUseCaseGroup.deleteCoin(MarketType.UPBIT.id, "BTC") } just Runs
        coEvery { webSocketManager.disconnect(MarketType.UPBIT.id) } just Runs

        //When
        viewModel.deleteCoin(coin)

        //Then
        advanceUntilIdle()

        coVerify { roomCoinUseCaseGroup.deleteCoin(MarketType.UPBIT.id, "BTC") }
        coVerify { webSocketManager.disconnect(MarketType.UPBIT.id) }

        val result = viewModel.state.coinsListState.value
        assertTrue(result.isEmpty())
    }

    @Test
    fun `deleteCoin 테스트 - 마켓에 다른 코인이 남아 있을 때 sendMessage 호출`() = runTest {
        //Given
        val coin1 = CoinInfo(market = MarketType.UPBIT.id, code = "KRW-BTC", coinName = "Bitcoin")
        val coin2 = CoinInfo(market = MarketType.UPBIT.id, code = "KRW-ETH", coinName = "Ethereum")

        viewModel.updateList(listOf(coin1, coin2))
        advanceUntilIdle()

        coEvery { roomCoinUseCaseGroup.deleteCoin(MarketType.UPBIT.id, "KRW-BTC") } just Runs
        coEvery { webSocketManager.sendMessage(MarketType.UPBIT.id, mutableListOf("KRW-ETH")) } just Runs

        // When
        viewModel.deleteCoin(coin1)

        // Then
        advanceUntilIdle()
        coVerify { roomCoinUseCaseGroup.deleteCoin(MarketType.UPBIT.id, "KRW-BTC") }
        coVerify { webSocketManager.sendMessage(MarketType.UPBIT.id, mutableListOf("KRW-ETH")) }

        val result = viewModel.state.coinsListState.value
        assertEquals(1, result.size)
        assertTrue(result.contains(coin2))
    }

    @Test
    fun `insertCoinList 테스트코드`() = runTest {
        // given:
        val coinList = listOf(CoinInfo(market = MarketType.UPBIT.id, code = "KRW-BTC", coinName = "Bitcoin"))

        coEvery { roomCoinListUseCaseGroup.insertCoinList(coinList) } just Runs

        //When
        viewModel.insertCoinList(coinList)

        //Then
        advanceUntilIdle()
        coVerify { roomCoinListUseCaseGroup.insertCoinList(coinList) }
    }

    @Test
    fun `getCoinList 테스트`() = runTest {
        //Given
        val market = MarketType.UPBIT.id
        val domainList = listOf(
            CoinInfo(market = market, code = "BTC", coinName = "Bitcoin"),
            CoinInfo(market = market, code = "ETH", coinName = "Ethereum")
        )
        val uiList = MarketUiModel(
            market = market,
            items = mutableListOf(
                MarketUiModel.Item(code = "BTC", name = "Bitcoin"),
                MarketUiModel.Item(code = "ETH", name = "Ethereum")
            )
        )

        // Mock 설정
        coEvery { roomCoinListUseCaseGroup.getCoinList(market) } returns domainList
        every { mapper.domainToUIMarket(eq(domainList)) } returns uiList

        // When
        val result = viewModel.getCoinList(market)

        // Then
        advanceUntilIdle()
        assertEquals(uiList, result)

        coVerify { roomCoinListUseCaseGroup.getCoinList(market) }
        coVerify { mapper.domainToUIMarket(domainList) }
    }

    @Test
    fun `deleteCoinList 테스트`() = runTest {
        //Given
        coEvery { roomCoinListUseCaseGroup.deleteCoinList() } just Runs

        //When
        viewModel.deleteCoinList()

        //Then
        advanceUntilIdle()
        coVerify { roomCoinListUseCaseGroup.deleteCoinList() }
    }

    @Test
    fun `updateList 테스트`() = runTest {
        //Given
        val coinList = listOf(CoinInfo(market = MarketType.UPBIT.id, code = "BTC", coinName = "Bitcoin"))

        //When
        viewModel.updateList(coinList)

        //Then
        advanceUntilIdle()
        val result = viewModel.state.coinsListState.value
        assertEquals(coinList.size, result.size)
        assertTrue(result.containsAll(coinList))
    }
}