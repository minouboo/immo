package studi.immo.service;

import studi.immo.entity.Cash;

public interface CashService {

    Cash saveCash (Cash cash);

    Cash getCashByUserID (Long userId);

    Cash getCashById (Long id);



}
