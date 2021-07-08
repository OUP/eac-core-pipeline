package com.oup.eac.data;

import java.util.List;

import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;

public interface ExternalSystemIdTypeDao extends BaseDao<ExternalSystemIdType, String>{

    public List<ExternalSystemIdType> findByExternalSystem(ExternalSystem exSys);
    public ExternalSystemIdType findByExternalSystemAndName(ExternalSystem exSys, String name);
    ExternalSystemIdType findByExternalSystemNameAndTypeName(String externalSystemName, String typeName);
    List<ExternalSystemIdType> findByExternalSystemOrderedByName(ExternalSystem exSys);
}
