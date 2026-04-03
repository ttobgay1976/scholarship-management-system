/*
 * package com.sprms.system.modelMapper;
 * 
 * import java.util.List;
 * 
 * import org.mapstruct.Mapper; import org.mapstruct.ObjectFactory;
 * 
 * import com.sprms.system.frmbeans.Statusfrmbean;
 * 
 * import ch.qos.logback.core.status.Status;
 * 
 * 
 * @Mapper(componentModel = "spring") public interface StatusFrmBeanMapper {
 * 
 * // entity to frmbean Status toEntity(Statusfrmbean statusfrmbean);
 * 
 * 
 * // frmbean to entity Statusfrmbean toFrmBean(Status entity);
 * 
 * 
 * 
 * default List<Statusfrmbean> toFrmBean(List<Status> entities) { if (entities
 * == null) return null; return entities.stream().map(this::toFrmBean) // calls
 * single-element mapping .toList(); }
 * 
 * default List<Status> toEntity(List<Statusfrmbean> frmBeans) { if (frmBeans ==
 * null) return null; return frmBeans.stream().map(this::toEntity).toList(); }
 * 
 * }
 */