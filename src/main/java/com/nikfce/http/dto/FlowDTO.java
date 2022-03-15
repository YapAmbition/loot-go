package com.nikfce.http.dto;

import com.nikfce.role.Looter;
import com.nikfce.scene.Flow;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenzhencheng 2022/3/14
 */
@Data
public class FlowDTO {

    private String name;
    private List<LooterDTO> looters;

    public static FlowDTO generateByFlow(Flow flow) {
        if (flow == null) return null;
        FlowDTO flowDTO = new FlowDTO();
        flowDTO.setName(flow.getName());

        List<Looter> looterList = Flow.genFlowLootersByExp(flow.getLooters());
        List<LooterDTO> looterDTOList = new ArrayList<>();
        for (Looter looter : looterList) {
            LooterDTO looterDTO = LooterDTO.generateByLooter(looter);
            looterDTOList.add(looterDTO);
        }
        flowDTO.setLooters(looterDTOList);
        return flowDTO;
    }

}
