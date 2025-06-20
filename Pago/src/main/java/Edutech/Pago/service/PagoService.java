package Edutech.Pago.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Edutech.Pago.model.Pago;
import Edutech.Pago.repository.PagoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PagoService {

     @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> findAll(){
        return pagoRepository.findAll();
    }

    public Pago findById(long id) {
        return pagoRepository.findById(id).get();
    }

    public Pago save(Pago pago){
        return pagoRepository.save(pago);
    }

    public void delete(long id){
        pagoRepository.deleteById(id);
    }
}
