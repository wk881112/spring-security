/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bigbank;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.Assert;

public class BankServiceImpl implements BankService {
	private BankDao bankDao;

	// Not used unless you declare a <protect-pointcut>
	@Pointcut("execution(* bigbank.BankServiceImpl.*(..))")
	public void myPointcut() {}

	public BankServiceImpl(BankDao bankDao) {
		Assert.notNull(bankDao);
		this.bankDao = bankDao;
	}

	public Account[] findAccounts() {
		return this.bankDao.findAccounts();
	}

	public Account post(Account account, double amount) {
		Assert.notNull(account);
		Assert.notNull(account.getId());

		// We read account bank from DAO so it reflects the latest balance
		Account a = bankDao.readAccount(account.getId());
		if (account == null) {
			throw new IllegalArgumentException("Couldn't find requested account");
		}

		a.setBalance(a.getBalance() + amount);
		bankDao.createOrUpdateAccount(a);
		return a;
	}

	public Account readAccount(Long id) {
		return bankDao.readAccount(id);
	}
}
