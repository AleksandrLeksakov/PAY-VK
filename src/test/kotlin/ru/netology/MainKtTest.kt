

import org.junit.Test
import org.junit.Assert.*


class PaymentServiceTest {

    @Test
    fun testMastercardExceedLimits() {
        val transferAmount: Long = 100000
        val previousMonthTransfers: Long = 0
        val result = calculateCommission("Mastercard", previousMonthTransfers, transferAmount, false, false)
        assertEquals(720, result.first)
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
    } // ... (Добавьте другие тесты для различных сценариев)
}

