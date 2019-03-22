package com.sometest.processor


import com.sometest.model.TransactionStatus
import com.sometest.model.entity.BankAccount
import com.sometest.model.entity.Transaction
import com.sometest.repository.BankAccountRepository
import com.sometest.repository.TransactionRepository
import spock.lang.Specification

class IncomingTransactionProcessorShould extends Specification {

	def "catch transaction and process"() {

		given:
		def transactionRepositoryMock = Mock(TransactionRepository)
		def bankAccountRepository = Mock(BankAccountRepository)
		def processor = new IncomingTransactionProcessor(transactionRepositoryMock, bankAccountRepository)
		def transaction = Mock(Transaction)
		def addresseMock = Mock(BankAccount)

		transaction.getTo() >> addresseMock
		addresseMock.getBalance() >> new BigDecimal("1000.00")
		transaction.getAmount() >> new BigDecimal("2000.00")

		when: "calling process method"
		processor.process(transaction)

		then: "make transaction on process"
		1 * transaction.setStatus(TransactionStatus.IN_PROGRESS)
		1 * transactionRepositoryMock.update(transaction)

		and: "process bank account balance and adds right amount"
		1 * addresseMock.setBalance(new BigDecimal("3000.00"))
		1 * bankAccountRepository.update(addresseMock)

		and: "if all success make transaction success"
		1 * transaction.setStatus(TransactionStatus.SUCCESS)
		1 * transactionRepositoryMock.update(transaction)

	}

}
