package com.nikfce.scene;

import com.nikfce.role.Looter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 进入场景的上下文
 * @author shenzhencheng 2022/3/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntrudeContext {

    private List<Looter> intruders;

}
