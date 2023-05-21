package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.Address;
import studi.immo.repository.AddressRepository;
import studi.immo.service.AddressService;

@Service
public class AddressServiceImplement implements AddressService {

    private AddressRepository addressRepository;

    public AddressServiceImplement (AddressRepository addressRepository){
        super();
        this.addressRepository = addressRepository;
    }

    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address getAddressByID(Long id) {
        return addressRepository.findById(id).get();
    }

    @Override
    public void deleteAddressById(Long id) {
        addressRepository.deleteById(id);
    }
}
