package com.example.demo.service;

import com.example.demo.entity.Solicitud;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    public List<Solicitud> obtenerTodasLasSolicitudes() {
        return solicitudRepository.findAll();
    }

    public Solicitud obtenerSolicitudPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));
    }

    public Solicitud crearSolicitud(Solicitud solicitud) {
        validarSolicitud(solicitud);
        return solicitudRepository.save(solicitud);
    }

    public Solicitud actualizarSolicitud(Long id, Solicitud solicitudActualizada) {
        Solicitud solicitud = obtenerSolicitudPorId(id);
        solicitud.setFechaIngreso(solicitudActualizada.getFechaIngreso());
        solicitud.setMonto(solicitudActualizada.getMonto());
        solicitud.setCliente(solicitudActualizada.getCliente()); // Cambia `idCliente` por `cliente`
        validarSolicitud(solicitud);
        return solicitudRepository.save(solicitud);
    }

    public void eliminarSolicitud(Long id) {
        Solicitud solicitud = obtenerSolicitudPorId(id);
        solicitudRepository.delete(solicitud);
    }

    private void validarSolicitud(Solicitud solicitud) {
        if (solicitud.getMonto().compareTo(BigDecimal.valueOf(1000000)) <= 0) {
            throw new BusinessException("El monto debe ser mayor a 1 millón");
        }
        if (solicitud.getFechaIngreso().isBefore(LocalDate.now())) {
            throw new BusinessException("La fecha de ingreso no puede ser inferior a la fecha actual");
        }
       
    }
    
}
