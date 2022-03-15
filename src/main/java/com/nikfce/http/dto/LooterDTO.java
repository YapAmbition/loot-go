package com.nikfce.http.dto;

import com.nikfce.action.Skill;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenzhencheng 2022/3/15
 */
@Data
public class LooterDTO {

    private String name;
    private String code;
    private Properties basicProperties;
    private List<SkillDTO> skillList;

    public static LooterDTO generateByLooter(Looter looter) {
        if (looter == null) return null;
        LooterDTO looterDTO = new LooterDTO();
        looterDTO.setName(looter.getName());
        looterDTO.setCode(looter.getCode());
        looterDTO.setBasicProperties(looter.copyBasicProperties());
        List<SkillDTO> skillDTOList = new ArrayList<>();
        for (Skill skill : looter.getSkillList()) {
            SkillDTO skillDTO = SkillDTO.generateBySkill(skill);
            skillDTOList.add(skillDTO);
        }
        looterDTO.setSkillList(skillDTOList);
        return looterDTO;
    }

}
