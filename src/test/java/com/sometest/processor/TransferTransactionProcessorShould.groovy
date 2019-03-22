package com.sometest.processor

import com.sometest.exception.TransactionFailException
import com.sometest.model.TransactionStatus
import com.sometest.model.entity.BankAccount
import com.sometest.model.entity.Transaction
import com.sometest.repository.BankAccountRepository
import com.sometest.repository.TransactionRepository
import spock.lang.Specification

class TransferTransactionProcessorShould extends Specification {
	def "process transaction correctly"() {
		given:
		def transactionRepositoryMock = Mock(TransactionRepository)
		def bankAccountRepository = Mock(BankAccountRepository)
		def processor = new TransferTransactionProcessor(transactionRepositoryMock, bankAccountRepository)
		def transaction = Mock(Transaction)
		def senderMock = Mock(BankAccount)
		def addresseeMock = Mock(BankAccount)

		transaction.getFrom() >> senderMock
		transaction.getTo() >> addresseeMock
		senderMock.getBalance() >> new BigDecimal("3000.00")
		transaction.getAmount() >> new BigDecimal("2000.00")

		when: "calling process method"
		processor.process(transaction)

		then: "make transaction on process"
		1 * transaction.setStatus(TransactionStatus.IN_PROGRESS)
		1 * transactionRepositoryMock.update(transaction)

		and: "call transfer method from repository"
		1 * bankAccountRepository.makeTransfer(senderMock, addresseeMock, transaction.getAmount())

		and: "if all success make transaction success"
		1 * transaction.setStatus(TransactionStatus.SUCCESS)
		1 * transactionRepositoryMock.update(transaction)
	}

	def "throw exception when client balance is not enough"() {
		given:
		def transactionRepositoryMock = Mock(TransactionRepository)
		def bankAccountRepository = Mock(BankAccountRepository)
		def processor = new OutgoingTransactionProcessor(transactionRepositoryMock, bankAccountRepository)
		def transaction = Mock(Transaction)
		def senderMock = Mock(BankAccount)
		def addresseeMock = Mock(BankAccount)

		transaction.getFrom() >> senderMock
		transaction.getTo() >> addresseeMock
		senderMock.getBalance() >> new BigDecimal("1000.00")
		transaction.getAmount() >> new BigDecimal("2000.00")

		when: "calling process method"
		processor.process(transaction)

		then: "throw an exception"
		def e = thrown(TransactionFailException)
		e.getMessage() == "Client does not have enough funds"

	}
}
