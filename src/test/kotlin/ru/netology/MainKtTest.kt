import org.junit.Test
import org.junit.Assert.*

class PaymentServiceTest {

    // Тесты для Mastercard:

    @Test
    fun `test should return commission for transfer from Mastercard within limits`() {
        val transferAmount: Long = 10_000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Mastercard", previousMonthTransfers, transferAmount, false, false)
        assertEquals(0, result.first) // Ожидаемая комиссия 80 рублей
        assertTrue(result.second) // Перевод должен быть успешным
    }



    @Test
    fun testMastercardExceedLimits() {
        val transferAmount: Long = 100000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Mastercard", previousMonthTransfers, transferAmount, false, false)
        assertEquals(620, result.first) // Ожидаемая комиссия 620 рублей
        assertTrue(result.second) // Перевод должен быть успешным
    }

    @Test
    fun testMastercardWithinLimits() {
        val transferAmount: Long = 5000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Mastercard", previousMonthTransfers, transferAmount, false, false)
        assertEquals(0, result.first) // Комиссия не должна взиматься
        assertTrue(result.second) // Перевод должен быть успешным
    }

    @Test
    fun testMastercardMaestroFreeTransfer() {
        val transferAmount: Long = 50000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Mastercard", previousMonthTransfers, transferAmount, false, false)
        assertEquals(0, result.first) // Комиссия не должна взиматься
        assertTrue(result.second) // Перевод должен быть успешным
    }

    @Test
    fun testMastercardMaestroFreeTransferLimit() {
        val transferAmount: Long = 75000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Mastercard", previousMonthTransfers, transferAmount, false, false)
        assertEquals(0, result.first) // Комиссия не должна взиматься
        assertTrue(result.second) // Перевод должен быть успешным
    }

    @Test
    fun testMastercardMaestroCommission() {
        val transferAmount: Long = 80000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Mastercard", previousMonthTransfers, transferAmount, false, false)
        assertEquals(500, result.first) // Ожидаемая комиссия 480 рублей
        assertTrue(result.second) // Перевод должен быть успешным
    }

    // Тесты для Visa:

    @Test
    fun `test should return commission for transfer from Visa within limits`() {
        val transferAmount: Long = 10_000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, false)
        assertEquals(75, result.first) // Ожидаемая комиссия 75 рублей
        assertTrue(result.second) // Перевод должен быть успешным
    }



    @Test
    fun testVisaWithinLimits() {
        val transferAmount: Long = 100000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, false)
        assertEquals(750, result.first) // Ожидаемая комиссия 750 рублей
        assertTrue(result.second) // Перевод должен быть успешным
    }

    @Test
    fun testVisaMirCommission() {
        val transferAmount: Long = 80000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, false)
        assertEquals(600, result.first) // Ожидаемая комиссия 600 рублей
        assertTrue(result.second) // Перевод должен быть успешным
    }

    @Test
    fun testDailyLimitExceeded() {
        val transferAmount: Long = 160000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, false)
        assertEquals(0, result.first) // Комиссия не должна взиматься
        assertFalse(result.second) // Перевод не должен быть успешным
    }

    @Test
    fun testMonthlyLimitExceeded() {
        val transferAmount: Long = 550000
        val previousMonthTransfers: Long = 50000
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, false)
        assertEquals(0, result.first) // Комиссия не должна взиматься
        assertFalse(result.second) // Перевод не должен быть успешным
    }

    // Тесты для Mir:

    @Test
    fun `test should return commission for transfer from Mir within limits`() {
        val transferAmount: Long = 10_000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Мир", previousMonthTransfers, transferAmount, false, false)
        assertEquals(75, result.first) // Ожидаемая комиссия 75 рублей
        assertTrue(result.second) // Перевод должен быть успешным
    }



    // Тесты для VK Pay:

    @Test
    fun testTransferToVkPay() {
        val transferAmount: Long = 10000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, true, false)
        assertEquals(0, result.first) // Комиссия не должна взиматься
        assertTrue(result.second) // Перевод должен быть успешным
    }

    @Test
    fun testTransferFromVkPayWithinLimits() {
        val transferAmount: Long = 10000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, true)
        assertEquals(75, result.first) // Ожидаемая комиссия 75 рублей
        assertTrue(result.second) // Перевод должен быть успешным
    }

    @Test
    fun testTransferFromVkPayExceedLimits() {
        val transferAmount: Long = 20000
        val previousMonthTransfers: Long = 30000
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, true)
        assertEquals(0, result.first) // Комиссия не должна взиматься
        assertFalse(result.second) // Перевод не должен быть успешным
    }

    @Test
    fun testVkPayDailyLimitExceeded() {
        val transferAmount: Long = 16000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, true)
        assertEquals(0, result.first) // Комиссия не должна взиматься
        assertFalse(result.second) // Перевод не должен быть успешным
    }

    @Test
    fun testVkPayMonthlyLimitExceeded() {
        val transferAmount: Long = 35000
        val previousMonthTransfers: Long = 5000
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, true)
        assertEquals(0, result.first) // Комиссия не должна взиматься
        assertFalse(result.second) // Перевод не должен быть успешным
    }

    // Тесты для некорректного типа карты:

    @Test
    fun `test should throw IllegalArgumentException for invalid card type`() {
        val transferAmount: Long = 10_000
        val previousMonthTransfers: Long = 0
        try {
            calculateCommission("Unknown", previousMonthTransfers, transferAmount, false, false)
            fail("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            // Ожидаемое исключение
        }
    }

    // Тесты для перевода на VK Pay:

    @Test
    fun `test should return commission for transfer from VkPay within limits`() {
        val transferAmount: Long = 10_000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, true)
        assertEquals(75, result.first) // Ожидаемая комиссия 75 рублей
        assertTrue(result.second) // Перевод должен быть успешным
    }

    @Test
    fun `test should return commission for transfer from VkPay exceed limits`() {
        val transferAmount: Long = 20_000
        val previousMonthTransfers: Long = 30_000
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, true)
        assertEquals(0, result.first) // Комиссия не должна взиматься
        assertFalse(result.second) // Перевод не должен быть успешным
    }



}