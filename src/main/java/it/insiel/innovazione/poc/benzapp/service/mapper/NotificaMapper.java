package it.insiel.innovazione.poc.benzapp.service.mapper;

import it.insiel.innovazione.poc.benzapp.domain.*;
import it.insiel.innovazione.poc.benzapp.service.dto.NotificaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notifica} and its DTO {@link NotificaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NotificaMapper extends EntityMapper<NotificaDTO, Notifica> {}
