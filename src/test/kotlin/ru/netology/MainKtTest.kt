import org.junit.Test
import org.junit.Assert.*

class PaymentServiceTest {

    @Test
    fun testMastercardExceedLimits() {
        val transferAmount: Long = 100000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Mastercard", previousMonthTransfers, transferAmount, false, false)
        assertEquals(620, result.first)
        assertTrue(result.second)
    }

    @Test
    fun testVisaWithinLimits() {
        val transferAmount: Long = 100000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, false)
        assertEquals(750, result.first)
        assertTrue(result.second)
    }

    @Test
    fun testTransferToVkPay() {
        val transferAmount: Long = 10000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, true, false)
        assertEquals(0, result.first)
        assertTrue(result.second)
    }

    @Test
    fun testTransferFromVkPayWithinLimits() {
        val transferAmount: Long = 10000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, true)
        assertEquals(75, result.first)
        assertTrue(result.second)
    }

    @Test
    fun testTransferFromVkPayExceedLimits() {
        val transferAmount: Long = 20000
        val previousMonthTransfers: Long = 30000
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, true)
        assertEquals(0, result.first)
        assertFalse(result.second)
    }

    @Test
    fun testInvalidCardType() {
        val transferAmount: Long = 100000
        val previousMonthTransfers: Long = 0
        try {
            calculateCommission("Unknown", previousMonthTransfers, transferAmount, false, false)
            fail("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            // Expected exception
        }
    }

    @Test
    fun testMastercardWithinLimits() {
        val transferAmount: Long = 5000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Mastercard", previousMonthTransfers, transferAmount, false, false)
        assertEquals(0, result.first)
        assertTrue(result.second)
    }

    @Test
    fun testMastercardMaestroFreeTransfer() {
        val transferAmount: Long = 50000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Mastercard", previousMonthTransfers, transferAmount, false, false)
        assertEquals(0, result.first)
        assertTrue(result.second)
    }

    @Test
    fun testMastercardMaestroFreeTransferLimit() {
        val transferAmount: Long = 75000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Mastercard", previousMonthTransfers, transferAmount, false, false)
        assertEquals(0, result.first)
        assertTrue(result.second)
    }

    @Test
    fun testMastercardMaestroCommission() {
        val transferAmount: Long = 80000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Mastercard", previousMonthTransfers, transferAmount, false, false)
        assertEquals(480, result.first)
        assertTrue(result.second)
    }

    @Test
    fun testVisaMirCommission() {
        val transferAmount: Long = 80000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, false)
        assertEquals(600, result.first)
        assertTrue(result.second)
    }

    @Test
    fun testDailyLimitExceeded() {
        val transferAmount: Long = 160000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, false)
        assertEquals(0, result.first)
        assertFalse(result.second)
    }

    @Test
    fun testMonthlyLimitExceeded() {
        val transferAmount: Long = 550000
        val previousMonthTransfers: Long = 50000
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, false)
        assertEquals(0, result.first)
        assertFalse(result.second)
    }

    @Test
    fun testVkPayDailyLimitExceeded() {
        val transferAmount: Long = 16000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, true)
        assertEquals(0, result.first)
        assertFalse(result.second)
    }

    @Test
    fun testVkPayMonthlyLimitExceeded() {
        val transferAmount: Long = 35000
        val previousMonthTransfers: Long = 5000
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, true)
        assertEquals(0, result.first)
        assertFalse(result.second)
    }

    @Test
    fun `test should return commission for transfer from VkPay within limits`() {
        val transferAmount: Long = 10_000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, true)
        assertEquals(75, result.first)
        assertTrue(result.second)
    }

    @Test
    fun `test should return commission for transfer from VkPay exceed limits`() {
        val transferAmount: Long = 20_000
        val previousMonthTransfers: Long = 30_000
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, true)
        assertEquals(0, result.first)
        assertFalse(result.second)
    }

    @Test
    fun `test should return commission for transfer from Mastercard within limits`() {
        val transferAmount: Long = 10_000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Mastercard", previousMonthTransfers, transferAmount, false, false)
        assertEquals(80, result.first)
        assertTrue(result.second)
    }

    @Test
    fun `test should return commission for transfer from Mastercard exceed limits`() {
        val transferAmount: Long = 160_000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Mastercard", previousMonthTransfers, transferAmount, false, false)
        assertEquals(970, result.first)
        assertTrue(result.second)
    }

    @Test
    fun `test should return commission for transfer from Visa within limits`() {
        val transferAmount: Long = 10_000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, false)
        assertEquals(75, result.first)
        assertTrue(result.second)
    }

    @Test
    fun `test should return commission for transfer from Visa exceed limits`() {
        val transferAmount: Long = 160_000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Visa", previousMonthTransfers, transferAmount, false, false)
        assertEquals(1200, result.first)
        assertTrue(result.second)
    }

    @Test
    fun `test should return commission for transfer from Mir within limits`() {
        val transferAmount: Long = 10_000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Мир", previousMonthTransfers, transferAmount, false, false)
        assertEquals(75, result.first)
        assertTrue(result.second)
    }

    @Test
    fun `test should return commission for transfer from Mir exceed limits`() {
        val transferAmount: Long = 160_000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Мир", previousMonthTransfers, transferAmount, false, false)
        assertEquals(1200, result.first)
        assertTrue(result.second)
    }

}