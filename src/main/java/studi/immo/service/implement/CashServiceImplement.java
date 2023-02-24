package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.Cash;
import studi.immo.repository.CashRepository;
import studi.immo.service.CashService;

@Service
public class CashServiceImplement implements CashService {

    private CashRepository cashRepository;

    public CashServiceImplement (CashRepository cashRepository){
        super ();
        this.cashRepository = cashRepository;
    }

    @Override
    public Cash saveCash(Cash cash) {
        return cashRepository.save(cash);
    }

    @Override
    public Cash getCashByUserID(Long userId) {
        return cashRepository.getCashByUserId(userId);
    }


}
