package ru.netology

const val DAILY_LIMIT = 150_000L
const val MONTHLY_LIMIT = 600_000L
const val MASTERCARD_MAESTRO_FREE_TRANSFER_LIMIT = 75_000L
const val MASTERCARD_MAESTRO_COMMISSION_PERCENT = 0.006
const val MASTERCARD_MAESTRO_MIN_COMMISSION = 20L
const val VISA_MIR_COMMISSION_PERCENT = 0.0075
const val VISA_MIR_MIN_COMMISSION = 35L
const val VK_PAY_DAILY_LIMIT = 15_000L
const val VK_PAY_MONTHLY_LIMIT = 40_000L

fun calculateCommission(
    cardType: String = "Мир",
    previousMonthTransfers: Long = 0,
    transferAmount: Long,
    isToVKPay: Boolean = false,
    isFromVKPay: Boolean = false
): Pair<Long, Boolean> {
    var commission: Long = 0
    var isSuccessful = true

    // Проверка лимитов
    if (isFromVKPay) {
        if (transferAmount > VK_PAY_DAILY_LIMIT || previousMonthTransfers + transferAmount > VK_PAY_MONTHLY_LIMIT) {
            isSuccessful = false
            return Pair(commission, isSuccessful)
        }
    } else {
        if (transferAmount > DAILY_LIMIT || previousMonthTransfers + transferAmount > MONTHLY_LIMIT) {
            isSuccessful = false
            return Pair(commission, isSuccessful)
        }
    }

    // Комиссия не взимается за переводы на VK Pay
    if (isToVKPay) {
        return Pair(commission, isSuccessful)
    }

    // Комиссия не взимается за переводы с Mastercard/Maestro в рамках акции
    if (cardType == "Mastercard" || cardType == "Maestro") {
        if (transferAmount in 300..MASTERCARD_MAESTRO_FREE_TRANSFER_LIMIT) {
            return Pair(commission, isSuccessful)
        }
    }

    // Расчет комиссии
    when (cardType) {
        "Mastercard", "Maestro" -> {
            commission = (transferAmount * MASTERCARD_MAESTRO_COMMISSION_PERCENT + MASTERCARD_MAESTRO_MIN_COMMISSION).toLong()
        }
        "Visa", "Мир" -> {
            commission = maxOf((transferAmount * VISA_MIR_COMMISSION_PERCENT).toLong(), VISA_MIR_MIN_COMMISSION)
        }
        else -> {
            throw IllegalArgumentException("Invalid card type")
        }
    }

    return Pair(commission, isSuccessful)
}

fun main() {
    // Примеры использования
    println("Пример 1: Перевод с Mastercard на счёт, 5000 рублей, в рамках акции")
    val result1 = calculateCommission(cardType = "Mastercard", transferAmount = 5000)
    println("Комиссия: ${result1.first}")
    println("Успешно: ${result1.second}")

    println("\nПример 2: Перевод с Mastercard на счёт, 100000 рублей, вне рамок акции")
    val result2 = calculateCommission(cardType = "Mastercard", transferAmount = 100_000)
    println("Комиссия: ${result2.first}")
    println("Успешно: ${result2.second}")

    println("\nПример 3: Перевод с Visa на счёт, 100000 рублей")
    val result3 = calculateCommission(cardType = "Visa", transferAmount = 100_000)
    println("Комиссия: ${result3.first}")
    println("Успешно: ${result3.second}")

    println("\nПример 4: Перевод на VK Pay, 10000 рублей")
    val result4 = calculateCommission(cardType = "Visa", transferAmount = 10_000, isToVKPay = true)
    println("Комиссия: ${result4.first}")
    println("Успешно: ${result4.second}")

    println("\nПример 5: Перевод с VK Pay, 10000 рублей")
    val result5 = calculateCommission(cardType = "Visa", transferAmount = 10_000, isFromVKPay = true)
    println("Комиссия: ${result5.first}")
    println("Успешно: ${result5.second}")
}