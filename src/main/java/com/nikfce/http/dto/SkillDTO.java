package com.nikfce.http.dto;

import com.nikfce.action.Skill;
import lombok.Data;

/**
 * @author shenzhencheng 2022/3/15
 */
@Data
public class SkillDTO {

    private String name;
    private String code;
    private String desc;

    public static SkillDTO generateBySkill(Skill skill) {
        if (skill == null) return null;
        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setCode(skill.code());
        skillDTO.setName(skill.name());
        skillDTO.setDesc(skill.desc());
        return skillDTO;
    }

}
