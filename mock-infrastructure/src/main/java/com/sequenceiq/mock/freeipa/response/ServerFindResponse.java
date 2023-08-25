package com.sequenceiq.mock.freeipa.response;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.model.CloudVmMetaDataStatus;
import com.sequenceiq.freeipa.client.model.IpaServer;

@Component
public class ServerFindResponse extends AbstractFreeIpaResponse<Set<IpaServer>> {
    @Override
    public String method() {
        return "server_find";
    }

    @Override
    protected Set<IpaServer> handleInternal(List<CloudVmMetaDataStatus> metadatas, String body) {
        return metadatas.stream().map(metadata -> {
            IpaServer ipaServer = new IpaServer();
            ipaServer.setCn("ipaserver" + metadata.getCloudVmInstanceStatus().getCloudInstance().getTemplate().getPrivateId() + ".ipatest.local");
            return ipaServer;
        }).collect(Collectors.toSet());
    }
}
