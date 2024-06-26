package com.sequenceiq.cloudbreak.service.blueprint;

import com.sequenceiq.cloudbreak.cmtemplate.CmTemplateProcessor;
import com.sequenceiq.cloudbreak.common.type.ClusterManagerType;
import com.sequenceiq.cloudbreak.template.BlueprintProcessingException;

public class BlueprintTextProcessorUtil {
    private BlueprintTextProcessorUtil() {
    }

    public static ClusterManagerType getClusterManagerType(String blueprintText) {
        try {
            new CmTemplateProcessor(blueprintText);
            return ClusterManagerType.CLOUDERA_MANAGER;
        } catch (BlueprintProcessingException ex) {
            return ClusterManagerType.AMBARI;
        }
    }
}
